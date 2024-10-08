package teamHTBP.vidaReforged.server.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Random;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.client.particles.VidaParticleTypeLoader;
import teamHTBP.vidaReforged.client.particles.options.BaseParticleType;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;
import teamHTBP.vidaReforged.core.utils.color.ColorTheme;
import teamHTBP.vidaReforged.server.blocks.ElementHarmonizeTable;
import teamHTBP.vidaReforged.server.recipe.VidaRecipeManager;
import teamHTBP.vidaReforged.server.recipe.records.ElementHarmonizeRecipe;

import java.util.*;

public class ElementHarmonizeTableBlockEntity extends VidaBlockEntity implements IVidaTickableBlockEntity {
    /** 旋转 */
    @OnlyIn(Dist.CLIENT)
    public float spin = 0;
    /** 祭坛元素 */
    private final VidaElement element;
    /**虚拟物品合成*/
    protected ElementHarmonizeRecipe activeRecipe = null;
    /** 正在使用的物品 */
    protected SimpleContainer container = new SimpleContainer(1);
    /** 提示物品 */
    protected SimpleContainer tipContainer = new SimpleContainer(1);
    /** 随机函数 */
    protected final RandomSource random;
    /** 是否正在合成 */
    private boolean isProcessing = false;
    /**合成时刻*/
    private int processTick = 0;
    private static final int maxProcessTick = 5000;
    private Map<VidaElement, ElementHarmonizeTableBlockEntity> otherTables = new HashMap<>();
    /**需要检查的方位*/
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
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("container", container.createTag());
        tag.putString("activeRecipe", activeRecipe == null ? "empty" : activeRecipe.getId().toString());
    }

    @Override
    public void doServerTick(Level level, BlockPos pos, BlockState state, BlockEntity entity) {
        if(level.isClientSide){
            spawnParticle(level);
            this.spin = (this.spin + 0.05f) % 360;
        }
        if(!level.isClientSide){
            boolean isStructureComplete = false;
            if(element == VidaElement.EARTH){
                isStructureComplete = checkStructureComplete();
            }
            // 如果结构完整，根据实际来开始
            if(element == VidaElement.EARTH && isStructureComplete){
                // 如果还未在仪式
                if(!isProcessing) { setVirtualDisplayItem(); }
                if(isProcessing) { continueCrafting(); }
            }
            // 如果结构不完整，停止任何逻辑
            if(element == VidaElement.EARTH && !isStructureComplete){
                abort();
            }

        }
    }

    private boolean checkStructureComplete(){
        // 如果计算过了，就不重新计算
        if(otherTables.size() >= 4 && checkAllTableIsActive()){
            return true;
        }
        // 重新计算
        otherTables.clear();
        Map<VidaElement, ElementHarmonizeTableBlockEntity> blockEntities = new HashMap<>();
        BlockPos originPos = getBlockPos();
        for(VidaElement positionElement : positionElements){
            Optional<ElementHarmonizeTableBlockEntity> tableOptional = level.getBlockEntity(originPos.offset(positionElement.getDirection().getNormal().multiply(2)), VidaBlockEntityLoader.ELEMENT_HARMONIZE_TABLE.get());
            tableOptional.ifPresent(table -> {
                if(table.getElement().equals(positionElement)){
                    otherTables.put(positionElement, table);
                }
            });
        }
        return otherTables.size() >= 4;
    }

    public boolean checkAllTableIsActive(){
        for(ElementHarmonizeTableBlockEntity entity : otherTables.values()){
            if(entity.isRemoved()){
                return false;
            }
        }
        return true;
    }

    private void setVirtualDisplayItem(){
        if(getItem().isEmpty()){
            boardcastRecipe(null);
            return;
        }
        List<ElementHarmonizeRecipe> recipes = VidaRecipeManager.getHarmonizeRecipeByEarthItem(getLevel(), this.getItem());
        if (recipes != null && recipes.size() > 0){
            this.activeRecipe = recipes.get(0);
            boardcastRecipe(recipes.get(0));
        }
    }

    private void boardcastRecipe(ElementHarmonizeRecipe recipe){
        for (ElementHarmonizeTableBlockEntity entity : otherTables.values()) {
            entity.setActiveRecipe(recipe);
        }
        setActiveRecipe(recipe);
    }

    private void continueCrafting(){
        this.processTick += 1;
        if(processTick >= maxProcessTick){
            complete();
        }
    }

    private void complete(){

    }

    private void abort(){
        // 重置所有设置
        if(this.isProcessing){
            this.setProcessing(false);
            this.processTick = 0;
        }
        // 重置合成
        this.boardcastRecipe(null);
    }


    private boolean shouldSpawnParticle(){
        return !getItemWithCopy().isEmpty();
    }

    public boolean isProcessing(){
        return isProcessing;
    }

    public void setProcessing(boolean isProcessing){
        this.isProcessing = isProcessing;
    }

    public ItemStack getItemWithCopy(){
        return this.container.getItem(0).copy();
    }

    public ItemStack getItem(){
        return this.container.getItem(0);
    }

    public boolean setItem(ItemStack stack){
        if(!stack.isEmpty() && getItem().isEmpty() && stack.getCount() >= 1){
            this.container.setItem(0, stack.copyWithCount(1));
            return true;
        }
        return false;
    }

    public void spawnParticle(Level level){
        if(!shouldSpawnParticle()){
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

    public VidaElement getElement(){
        return this.element;
    }

    public ParticleOptions getParticle() {
        return new BaseParticleType(VidaParticleTypeLoader.ORB_PARTICLE.get(), ColorTheme.getColorThemeByElement(element).baseColor().toARGB(), new Vector3f(), 0.2F, 30);
    }

    public void setActiveRecipe(ElementHarmonizeRecipe activeRecipe) {
        if(activeRecipe == null || !activeRecipe.equals(this.activeRecipe)){
            this.setUpdated();
        }
        this.activeRecipe = activeRecipe;
    }

    public ItemStack getVirtualItem(){
        return activeRecipe == null ? ItemStack.EMPTY : activeRecipe.getIngredientFromElement(element).getItems()[0];
    }
}
