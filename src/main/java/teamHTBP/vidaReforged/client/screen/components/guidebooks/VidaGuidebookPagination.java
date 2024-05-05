package teamHTBP.vidaReforged.client.screen.components.guidebooks;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.client.screen.components.common.IconButton;
import teamHTBP.vidaReforged.client.screen.components.common.VidaWidget;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaGuidebookDetailPageViewModel;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaScreenEventChannelViewModel;
import teamHTBP.vidaReforged.core.common.system.guidebook.VidaScreenEvent;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;

import java.util.List;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class VidaGuidebookPagination extends VidaWidget implements IVidaGuidebookComponent {
    public static final ResourceLocation GUIDEBOOK_RESOURCE = new ResourceLocation(MOD_ID, "textures/gui/book.png");
    public static final TextureSection RIGHT_ARROW_NON_ACTIVE = new TextureSection(GUIDEBOOK_RESOURCE, 0, 243, 15, 10, 512, 512);
    public static final TextureSection RIGHT_ARROW_ACTIVE = new TextureSection(GUIDEBOOK_RESOURCE, 0, 259, 15, 10, 512, 512);
    public static final TextureSection LEFT_ARROW_NON_ACTIVE = new TextureSection(GUIDEBOOK_RESOURCE, 117, 243, 15, 10, 512, 512);
    public static final TextureSection LEFT_ARROW_ACTIVE = new TextureSection(GUIDEBOOK_RESOURCE, 117, 259, 15, 10, 512, 512);
    private IconButton rightButton;
    private IconButton leftButton;
    private IconButton page;
    private FrameLayout frameLayout;
    private GridLayout gridLayout;
    private VidaScreenEventChannelViewModel channel;
    private VidaGuidebookDetailPageViewModel pageModel;
    private ResourceLocation target;
    private VidaScreenEvent leftHandler;
    private VidaScreenEvent rightHandler;


    public VidaGuidebookPagination(int x, int y, int width, int height, Component component) {
        super(x, y, width, height, component);
        this.leftHandler = new VidaScreenEvent("prevPage", null);
        this.rightHandler = new VidaScreenEvent("nextPage", null);
        this.pageModel = new ViewModelProvider(requireParent()).get(VidaGuidebookDetailPageViewModel.class);
        this.channel = new ViewModelProvider(requireParent()).get(VidaScreenEventChannelViewModel.class);
        this.target = pageModel.getCurrent();
        init();
    }


    public VidaGuidebookPagination(int x, int y, int width, int height, Component component, VidaScreenEvent leftHandler, VidaScreenEvent rightHandler) {
        super(x, y, width, height, component);
        this.leftHandler = leftHandler;
        this.rightHandler = rightHandler;
        this.pageModel = new ViewModelProvider(requireParent()).get(VidaGuidebookDetailPageViewModel.class);
        this.channel = new ViewModelProvider(requireParent()).get(VidaScreenEventChannelViewModel.class);
        this.target = pageModel.getCurrent();
        init();
    }

    @Override
    public void init() {
        this.frameLayout = new FrameLayout(0, 0, getWidth(), 50);
        this.gridLayout = new GridLayout(0, 0);
        IconButton.Builder builder = new IconButton.Builder().width(15).height(10).isBackground(false);
        rightButton = builder.rev(512).imageTex(RIGHT_ARROW_NON_ACTIVE).imageActiveTex(RIGHT_ARROW_ACTIVE).message(Component.empty()).build(0,0).setListener(this::onRight);
        leftButton = builder.rev(512).imageTex(LEFT_ARROW_NON_ACTIVE).imageActiveTex(LEFT_ARROW_ACTIVE).message(Component.empty()).build(0,0).setListener(this::onLeft);
        page = builder.width(50).height(10).rev(512).imageTex(null).imageActiveTex(null).isBackground(false).message(Component.literal("1")).build(0, 0);
        // 排版
        gridLayout.addChild(rightButton, 0 , 3);
        gridLayout.addChild(page, 0, 2);
        gridLayout.addChild(leftButton, 0 , 1);
        frameLayout.addChild(gridLayout, frameLayout.newChildLayoutSettings().alignHorizontallyCenter().alignVerticallyMiddle());
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        frameLayout.setX(getX());
        frameLayout.setY(getY());
        frameLayout.arrangeElements();

        page.setText(Component.literal(String.format("%d / %d", this.pageModel.getPage(target), target == null ? 0 : pageModel.getMaxPage(target))));
        this.rightButton.render(graphics, mouseX, mouseY, partialTicks);
        this.page.render(graphics, mouseX, mouseY, partialTicks);
        this.leftButton.render(graphics, mouseX, mouseY, partialTicks);

        this.handleVisibility();
    }
    
    public void onLeft(){
        this.channel.pushMessage(leftHandler);
    }

    public void onRight(){
        this.channel.pushMessage(rightHandler);
    }

    public void handleVisibility(){
        int page = this.pageModel.getPage(target);
        int maxPage = this.pageModel.getMaxPage(target);

        this.rightButton.setVisible(true);
        this.leftButton.setVisible(true);
        if(page >= maxPage){
            this.rightButton.setVisible(false);
        }
        if(page <= 1){
            this.leftButton.setVisible(false);
        }
    }

    @Override
    public List<GuiEventListener> children() {
        return List.of(leftButton, rightButton);
    }
}
