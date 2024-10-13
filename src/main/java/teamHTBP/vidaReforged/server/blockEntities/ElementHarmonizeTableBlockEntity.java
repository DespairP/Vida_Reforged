package teamHTBP.vidaReforged.server.blockEntities;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3d;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.client.events.ClientTickHandler;
import teamHTBP.vidaReforged.client.particles.VidaParticleTypeLoader;
import teamHTBP.vidaReforged.client.particles.options.BaseParticleType;
import teamHTBP.vidaReforged.client.particles.particles.VidaParticleAttributes;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.color.ColorTheme;
import teamHTBP.vidaReforged.server.blocks.ElementHarmonizeTable;
import teamHTBP.vidaReforged.server.entity.FakeHarmonizeTableItemEntity;
import teamHTBP.vidaReforged.server.recipe.VidaRecipeManager;
import teamHTBP.vidaReforged.server.recipe.records.ElementHarmonizeRecipe;

import java.util.*;

public class ElementHarmonizeTableBlockEntity extends VidaBlockEntity implements IVidaTickableBlockEntity {
    /**
     * 旋转
     */
    @OnlyIn(Dist.CLIENT)
    public float spin = 0;
    /**
     * 祭坛元素
     */
    private final VidaElement element;
    /**
     * 虚拟物品合成
     */
    protected ElementHarmonizeRecipe activeRecipe = null;
    /**
     * 正在使用的物品
     */
    protected SimpleContainer container = new SimpleContainer(1);
    /**
     * 提示物品
     */
    protected SimpleContainer tipContainer = new SimpleContainer(1);
    /**
     * 随机函数
     */
    protected final RandomSource random;
    /**
     * 是否正在合成
     */
    private boolean isProcessing = false;
    /**
     * 合成时刻
     */
    protected int processTick = 0;
    private static final int MAX_PROCESS_TICK = 5000;
    /**
     * 临时储存的表
     */
    private Map<VidaElement, ElementHarmonizeTableBlockEntity> otherTables = new HashMap<>();
    /**
     * 需要检查的方位
     */
    static final VidaElement[] positionElements = new VidaElement[]{VidaElement.GOLD, VidaElement.WOOD, VidaElement.AQUA, VidaElement.FIRE};

    public ElementHarmonizeTableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(VidaBlockEntityLoader.ELEMENT_HARMONIZE_TABLE.get(), pPos, pBlockState);
        this.element = ((ElementHarmonizeTable) pBlockState.getBlock()).getElement();
        this.random = RandomSource.create();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        Optional<ElementHarmonizeRecipe> recipeOptional = VidaRecipeManager.getHarmonizeRecipeById(getLevel(), new ResourceLocation(tag.getString("activeRecipe")));
        container.fromTag((ListTag) tag.get("container"));
        this.activeRecipe = recipeOptional.orElse(null);
        this.isProcessing = tag.getBoolean("Process");
        this.processTick = tag.getInt("ProcessTick");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("container", container.createTag());
        tag.putString("activeRecipe", activeRecipe == null ? "empty" : activeRecipe.getId().toString());
        tag.putBoolean("Process", this.isProcessing);
        tag.putInt("ProcessTick",  this.processTick);
    }

    @Override
    public void doServerTick(Level level, BlockPos pos, BlockState state, BlockEntity entity) {
        if (level.isClientSide) {
            spawnParticle(level);
            this.spin = (this.spin + 0.05f) % 360;
        }
        if (!level.isClientSide) {
            boolean isStructureComplete = false;
            if (element == VidaElement.EARTH) {
                isStructureComplete = checkStructureComplete();
            }
            // 如果结构完整，根据实际来开始
            if (element == VidaElement.EARTH && isStructureComplete) {
                // 如果还未在仪式
                if (!isProcessing) {
                    setVirtualDisplayItem();
                }
                // 如果还在仪式
                if (isProcessing) {
                    continueCrafting();
                }
            }
            // 如果结构不完整，停止任何逻辑
            if (element == VidaElement.EARTH && !isStructureComplete) {
                abort();
            }

            if (shouldUpdated) {
                super.setUpdated();
                this.shouldUpdated = false;
            }
        }
    }

    private boolean checkStructureComplete() {
        // 如果计算过了，就不重新计算
        if (otherTables.size() >= 4 && checkAllTableIsActive()) {
            return true;
        }
        // 重新计算
        otherTables.clear();
        Map<VidaElement, ElementHarmonizeTableBlockEntity> blockEntities = new HashMap<>();
        BlockPos originPos = getBlockPos();
        for (VidaElement positionElement : positionElements) {
            Optional<ElementHarmonizeTableBlockEntity> tableOptional = level.getBlockEntity(originPos.offset(positionElement.getDirection().getNormal().multiply(2)), VidaBlockEntityLoader.ELEMENT_HARMONIZE_TABLE.get());
            tableOptional.ifPresent(table -> {
                if (table.getElement().equals(positionElement)) {
                    otherTables.put(positionElement, table);
                }
            });
        }
        return otherTables.size() >= 4;
    }

    public boolean checkAllTableIsActive() {
        for (ElementHarmonizeTableBlockEntity entity : otherTables.values()) {
            if (entity.isRemoved()) {
                return false;
            }
        }
        return true;
    }

    private void setVirtualDisplayItem() {
        if (getItem().isEmpty()) {
            broadcastRecipe(null);
            return;
        }
        List<ElementHarmonizeRecipe> recipes = VidaRecipeManager.getHarmonizeRecipeByEarthItem(getLevel(), this.getItem());
        if (recipes != null && recipes.size() > 0) {
            this.activeRecipe = recipes.get(0);
            broadcastRecipe(recipes.get(0));
        }
    }

    private void broadcastRecipe(ElementHarmonizeRecipe recipe) {
        for (ElementHarmonizeTableBlockEntity entity : otherTables.values()) {
            entity.setActiveRecipe(recipe);
        }
        setActiveRecipe(recipe);
    }

    private void broadcastProcess(boolean isProcessing) {
        for (ElementHarmonizeTableBlockEntity entity : otherTables.values()) {
            entity.setProcessing(isProcessing);
        }
        setProcessing(isProcessing);
    }

    private void broadcastProcessTick(int ticks) {
        for (ElementHarmonizeTableBlockEntity entity : otherTables.values()) {
            entity.setProcessTick(ticks);
        }
        setUpdated();
        setProcessTick(ticks);
    }

    private void broadcastItemStack(ItemStack stack) {
        for (ElementHarmonizeTableBlockEntity entity : otherTables.values()) {
            entity.setResult(stack);
        }
    }

    private void continueCrafting() {
        this.processTick += 1;
        if (processTick >= 800) {
            complete();
        } else {
            doParticleProgress();
            broadcastProcessTick(processTick);
        }
    }

    protected void doParticleProgress(){
        if(level == null || level.isClientSide){
            return;
        }
        BlockPos pos = getBlockPos();
        if(random.nextFloat() > 0.2f){
            ((ServerLevel)level).sendParticles(
                    new BaseParticleType(
                            VidaParticleTypeLoader.CUBE_PARTICLE_TYPE,
                            new VidaParticleAttributes(120, 0.025f, new ARGBColor(0, 255, 255, 255), new ARGBColor(255, 255, 255, 255), BlockPos.ZERO.getCenter().toVector3f())
                    ),
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    6,
                    1.2f,
                    2,
                    1.2f,
                    0F
            );
        }
        if(ClientTickHandler.ticks % 2 == 0){
            for (Map.Entry<VidaElement, ElementHarmonizeTableBlockEntity> blockEntry: otherTables.entrySet()) {
                ((ServerLevel)level).sendParticles(
                        new BaseParticleType(
                                VidaParticleTypeLoader.BEZ_PARTICLE,
                                new VidaParticleAttributes(
                                        120,
                                        0.3f,
                                        new ARGBColor(255, 255, 255, 255),
                                        new ARGBColor(255, 255, 255, 255),
                                        getBlockPos().getCenter().add(0,2, 0).toVector3f(),
                                        getBlockPos().getCenter().add(0,2, 0).toVector3f()
                                )
                        ),
                        blockEntry.getValue().getBlockPos().getCenter().add(0,1.5, 0).x(),
                        blockEntry.getValue().getBlockPos().getCenter().add(0,1.5, 0).y(),
                        blockEntry.getValue().getBlockPos().getCenter().add(0,1.5, 0).z(),
                        1,
                        0,
                        0,
                        0,
                        0F
                );
            }

        }
    }

    private void complete() {
        // 最后再检验合成是否成功
        if (!checkRecipeIsComplete()) {
            abort();
            return;
        }
        if(activeRecipe == null){
            abort();
            return;
        }
        // 设置结果
        broadcastItemStack(ItemStack.EMPTY);
        setResult(this.activeRecipe.getResultItem());
        // 重置进度
        resetProcess();
    }

    protected boolean checkRecipeIsComplete() {
        Map<VidaElement, ItemStack> stackMap = new HashMap<>();
        for (Map.Entry<VidaElement, ElementHarmonizeTableBlockEntity> entityEntry : this.otherTables.entrySet()) {
            if (!entityEntry.getValue().isRemoved()) {
                stackMap.put(entityEntry.getKey(), entityEntry.getValue().getItemWithCopy());
            } else {
                return false;
            }
        }
        stackMap.put(this.element, getItemWithCopy());
        if (activeRecipe == null) {
            return false;
        }
        return this.activeRecipe.matches(stackMap, level);
    }

    protected void start(){
        if(level == null || level.isClientSide){
            return;
        }
        Map<VidaElement, ElementHarmonizeTableBlockEntity> allTables = new HashMap<>(otherTables);
        allTables.put(VidaElement.EARTH, this);
        for(Map.Entry<VidaElement, ElementHarmonizeTableBlockEntity> blockEntity : allTables.entrySet()){
            Vec3i vec3i = blockEntity.getKey().getDirection() == null ? new Vec3i(0,0,0)  : blockEntity.getKey().getDirection().getNormal().above();
            FakeHarmonizeTableItemEntity itemEntity = new FakeHarmonizeTableItemEntity(blockEntity.getValue(), level, new Vector3d(vec3i.getX(), vec3i.getY(), vec3i.getZ()).mul(0.005f));
            itemEntity.init(blockEntity.getValue().getItemWithCopy(), this.getBlockPos().above(2).getCenter(), new ARGBColor(255, 102, 102, 102));
            level.addFreshEntity(itemEntity);
        }
    }

    private void abort() {
        // 如果正在处理，重置所有设置
        if (this.isProcessing) {
            resetProcess();
        }
        // 如果不在处理，只重置合成
        if (!this.isProcessing) {
            this.broadcastRecipe(null);
        }
    }

    private void resetProcess() {
        broadcastProcess(false);
        broadcastRecipe(null);
        broadcastProcessTick(0);
    }

    private boolean shouldSpawnParticle() {
        return !getItemWithCopy().isEmpty();
    }

    public boolean isProcessing() {
        return isProcessing;
    }

    public void setProcessing(boolean isProcessing) {
        if (isProcessing != this.isProcessing) {
            this.setUpdated();
        }
        this.isProcessing = isProcessing;
    }

    public void setActiveRecipe(ElementHarmonizeRecipe activeRecipe) {
        if (activeRecipe == null || !activeRecipe.equals(this.activeRecipe)) {
            this.setUpdated();
        }
        this.activeRecipe = activeRecipe;
    }

    public void setProcessTick(int processTick) {
        if (this.processTick != processTick) {
            this.setUpdated();
        }
        this.processTick = processTick;
    }

    public ItemStack getItemWithCopy() {
        return this.container.getItem(0).copy();
    }

    public ItemStack getItem() {
        return this.container.getItem(0);
    }

    public boolean setItem(ItemStack stack) {
        if (!stack.isEmpty() && getItem().isEmpty() && stack.getCount() >= 1) {
            this.container.setItem(0, stack.copyWithCount(1));
            return true;
        }
        return false;
    }

    public void setResult(ItemStack stack){
        this.container.setItem(0, stack.copy());
        this.setUpdated();
    }

    /**尝试开始*/
    public void tryStart(){
        if(element == VidaElement.EARTH && !isProcessing && checkStructureComplete() && checkRecipeIsComplete()){
            broadcastProcess(true);
            start();
        }
    }

    public void spawnParticle(Level level) {
        if (!shouldSpawnParticle()) {
            return;
        }
        double x = getBlockPos().getX() + (random.nextFloat() * 0.001F) + 0.5F;
        double y = getBlockPos().getY() + (random.nextFloat() * 0.001F) + 1.3F;
        double z = getBlockPos().getZ() + (random.nextFloat() * 0.001F) + 0.5F;
        level.addParticle(
                getParticle(),
                x,
                y,
                z,
                0.015F * (random.nextFloat() - 0.5f),
                0.015F * (random.nextFloat() - 0.5f),
                0.015F * (random.nextFloat() - 0.5f));
    }

    public VidaElement getElement() {
        return this.element;
    }

    public int getProcessTick() {
        return processTick;
    }

    public ParticleOptions getParticle() {
        return new BaseParticleType(VidaParticleTypeLoader.ORB_PARTICLE.get(), ColorTheme.getColorThemeByElement(element).baseColor().toARGB(), new Vector3f(), 0.2F, 30, true);
    }

    public ItemStack getVirtualItem() {
        return activeRecipe == null ? ItemStack.EMPTY : activeRecipe.getIngredientFromElement(element).getItems()[0];
    }

    boolean shouldUpdated = false;

    @Override
    public void setUpdated() {
        this.shouldUpdated = true;
    }
}
