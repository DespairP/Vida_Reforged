package teamHTBP.vidaReforged.client.screen.screens.guidebook;


import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.events.ShadersHandler;
import teamHTBP.vidaReforged.client.screen.screens.common.VidaScreen;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaGuidebookDetailPageViewModel;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaGuidebookListPageViewModel;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaScreenEventChannelViewModel;
import teamHTBP.vidaReforged.core.common.system.guidebook.VidaScreenEvent;
import teamHTBP.vidaReforged.core.common.system.guidebook.VidaPageList;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;
import teamHTBP.vidaReforged.core.common.ui.lifecycle.IDefaultLifeCycleObserver;
import teamHTBP.vidaReforged.core.common.ui.lifecycle.ILifeCycleOwner;
import teamHTBP.vidaReforged.core.utils.anim.SecondOrderDynamics;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;
import teamHTBP.vidaReforged.server.providers.VidaGuidebookManager;

import java.util.ArrayList;
import java.util.List;

import static teamHTBP.vidaReforged.core.common.VidaConstant.EVENT_NEXT_PAGE;
import static teamHTBP.vidaReforged.core.common.VidaConstant.EVENT_PREV_PAGE;

public class VidaGuidebookListScreen extends VidaScreen {
    /**现在正在显示的list数据体*/
    public VidaPageList pageList;
    /**渲染材质使用*/
    public static final ResourceLocation GUIDEBOOK_RESOURCE = new ResourceLocation(VidaReforged.MOD_ID, "textures/gui/book.png");
    public static final TextureSection GUIDEBOOK_SECTION = new TextureSection(GUIDEBOOK_RESOURCE, 0, 0, 187, 230, 512, 512);
    public static final int SECTION_SIZE = 512;
    /**右边书本组件容器*/
    VidaGuidebookPageContainer page;
    /**左边进度条组件容器*/
    VidaGuidebookPageProgress progress;
    /**总透明度*/
    public FloatRange alpha = new FloatRange(0, 0, 1);
    /**动画使用，确认书本容器位置*/
    public SecondOrderDynamics guidebookPosition;
    /**书本容器偏移位置*/
    private int guidebookLeftOffset = 0;
    /**中间位置*/
    protected int leftPos;
    protected int topPos;
    /**边距*/
    protected int marginSize = 16;
    /**帧数计时器，用于记录帧数*/
    private VidaGuiHelper.TickHelper ticker;
    private VidaGuidebookDetailPageViewModel detailModel;
    private Minecraft mc;

    /**
     * 打开页面
     */
    public VidaGuidebookListScreen(Player player) {
        super(Component.literal("vida_guidebook:list"));
        this.mc = Minecraft.getInstance();
    }

    @Override
    public void added() {
        super.added();
        final int screenScaledWidth = mc.getWindow().getGuiScaledWidth();
        final int screenScaledHeight = mc.getWindow().getGuiScaledHeight();
        final VidaPageList list = VidaGuidebookManager.LIST_MAP.values().stream().findFirst().get();

        VidaGuidebookListPageViewModel listModel = new ViewModelProvider(this).get(VidaGuidebookListPageViewModel.class);
        listModel.loadPageList(list);

        VidaScreenEventChannelViewModel channel = new ViewModelProvider(this).get(VidaScreenEventChannelViewModel.class);
        this.detailModel = new ViewModelProvider(this).get(VidaGuidebookDetailPageViewModel.class);
        this.detailModel.setCurrent(list, listModel.maxPage);

        // 初始化位置
        this.ticker = new VidaGuiHelper.TickHelper();
        this.leftPos = (screenScaledWidth - GUIDEBOOK_SECTION.width()) / 2;
        this.topPos = (screenScaledHeight - GUIDEBOOK_SECTION.height()) / 2;
        guidebookPosition = new SecondOrderDynamics(0.15f, 1, 0, new Vector3f(leftPos, topPos, 0));

        // 初始化容器
        this.page = new VidaGuidebookPageContainer(this.leftPos, this.topPos, GUIDEBOOK_SECTION.width(), GUIDEBOOK_SECTION.height(), Component.literal("page"));
        this.page.setPage(new VidaGuidebookList(0, 0, GUIDEBOOK_SECTION.width(), GUIDEBOOK_SECTION.height(), Component.literal("")));


        // 初始化进度
        this.progress = new VidaGuidebookPageProgress(marginSize, marginSize, screenScaledWidth - page.getWidth() - marginSize * 2, screenScaledHeight - marginSize * 2, Component.literal(""));


        channel.listenMessage(this, this::handleEvent);

        getLifeCycle().addObserver(new IDefaultLifeCycleObserver() {
            @Override
            public void onCreate(ILifeCycleOwner owner) {
                System.out.println("create");
            }

            @Override
            public void onStart(ILifeCycleOwner owner) {
                System.out.println("start");
            }

            @Override
            public void onResume(ILifeCycleOwner owner) {
                System.out.println("resume");
            }

            @Override
            public void onPause(ILifeCycleOwner owner) {
                System.out.println("pause");
            }

            @Override
            public void onHide(ILifeCycleOwner owner) {
                System.out.println("hide");
            }

            @Override
            public void onDestroy(ILifeCycleOwner owner) {
                System.out.println("destroy");
            }
        });

    }

    private void handleEvent(VidaScreenEvent message){
        //
        if(message.getType() == null){
            return;
        }

        if(message.getType().equals("open")){
            ResourceLocation detailId = new ResourceLocation(message.getData().getAsJsonObject().get("detail").getAsString());
            VidaGuidebookPageDetail detail = new VidaGuidebookPageDetail(0, 0, GUIDEBOOK_SECTION.width(), GUIDEBOOK_SECTION.height(), Component.literal(""), VidaGuidebookManager.DETAIL_PAGE_MAP.get(detailId));

            this.guidebookLeftOffset -= 10;
            this.page.setPage(detail);
        }
        if(message.getType().equals("close")){
            this.page.removePage();
            this.guidebookLeftOffset += 10;
        }

        ResourceLocation current = detailModel.getCurrent();
        if(EVENT_NEXT_PAGE.equals(message.getType())){
            detailModel.nextPage(current);
        }
        if(EVENT_PREV_PAGE.equals(message.getType())){
            detailModel.prevPage(current);
        }

    }



    @Override
    protected void init() {
        super.init();
        initCenter();
        guidebookLeftOffset = this.width - this.leftPos - GUIDEBOOK_SECTION.width() - 20;
    }

    protected void initCenter(){
        this.leftPos = (this.width - GUIDEBOOK_SECTION.width()) / 2;
        this.topPos = (this.height - GUIDEBOOK_SECTION.height()) / 2;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderShader(graphics, mouseX, mouseY, partialTicks);
        this.renderIcons(graphics, mouseX, mouseY, partialTicks);
        this.renderPage(graphics, mouseX, mouseY, partialTicks);
        this.ticker.tick(partialTicks);
    }

    public void renderPage(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        PoseStack poseStack = graphics.pose();
        Vector3f toPos = guidebookPosition.update(Minecraft.getInstance().getDeltaFrameTime(), new Vector3f(this.leftPos + this.guidebookLeftOffset, this.topPos, 0),null);
        this.page.setX((int)toPos.x);
        this.page.setY((int)toPos.y);
        poseStack.pushPose();
        poseStack.translate(0, 0, 200);
        this.page.render(graphics, mouseX, mouseY, partialTicks);
        poseStack.popPose();
    }

    public void renderIcons(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        // 重新计算组件大小
        final int screenScaledWidth = mc.getWindow().getGuiScaledWidth();
        final int screenScaledHeight = mc.getWindow().getGuiScaledHeight();

        int width = screenScaledWidth - this.page.getX() - marginSize  * 2;
        int height = screenScaledHeight - marginSize * 2;

        progress.setWidth(width);
        progress.setHeight(height);


        progress.renderWidget(graphics, mouseX, mouseY, partialTicks);
    }

    public int getTickLength(){
        return (int) (ticker.currentTick - ticker.startTick);
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {}


    public void renderShader(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        super.renderBackground(graphics);
        this.alpha.increase(0.1f * Minecraft.getInstance().getDeltaFrameTime());

        RenderTarget tar = Minecraft.getInstance().getMainRenderTarget();
        float aWidth = tar.width;
        float aHeight = tar.height;

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1, 1, 1, this.alpha.get());
        ShaderInstance shader = ShadersHandler.simplicity;
        RenderSystem.setShader(() -> shader);
        ShadersHandler.setUniforms(shader, new ShadersHandler.Point2f(aWidth, aHeight), new ShadersHandler.Point2f(0, 0), getTickLength(), partialTicks);

        Matrix4f matrix4f = graphics.pose().last().pose();
        BufferBuilder builder = Tesselator.getInstance().getBuilder();

        pos(matrix4f, builder);

        BufferUploader.drawWithShader(builder.end());
        RenderSystem.disableBlend();
    }


    void pos(Matrix4f m4, BufferBuilder builder) {
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

        float sX = (0) / 2F;
        float sY = (0) / 2F;
        float eX = sX + Minecraft.getInstance().getWindow().getGuiScaledWidth();
        float eY = sY + Minecraft.getInstance().getWindow().getGuiScaledHeight();

        builder.vertex(m4, sX, eY, 0).endVertex();
        builder.vertex(m4, eX, eY, 0).endVertex();
        builder.vertex(m4, eX, sY, 0).endVertex();
        builder.vertex(m4, sX, sY, 0).endVertex();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double dragX, double dragY) {
        progress.mouseDragged(mouseX, mouseY, mouseButton, dragX, dragY);
        return true;
    }

    @Override
    public List<? extends GuiEventListener> children() {
        List<GuiEventListener> listeners = new ArrayList<>();
        listeners.add(progress);
        listeners.addAll(page.children());
        return listeners;
    }
}
