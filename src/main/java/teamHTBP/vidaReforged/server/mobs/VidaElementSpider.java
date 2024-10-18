package teamHTBP.vidaReforged.server.mobs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.mobs.IVidaShieldMob;

public class VidaElementSpider extends Spider implements IVidaShieldMob {
    public static final EntityDataAccessor<Integer> SHIED = SynchedEntityData.defineId(VidaElementSpider.class, EntityDataSerializers.INT);
    public VidaElementSpider(EntityType<? extends Spider> mobType, Level level) {
        super(mobType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHIED, 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(SHIED, tag.getInt("Shield"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Shield", this.entityData.get(SHIED));
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 12.0D);
    }


    @Override
    public boolean hasShield() {
        return this.entityData.get(SHIED) > 0;
    }

    @Override
    public int getShield() {
        return this.entityData.get(SHIED);
    }

    @Override
    public VidaElement getShieldType() {
        return VidaElement.FIRE;
    }

    @Override
    public int decreaseShield(int hurt) {
        int sheid = this.getShield();
        this.entityData.set(SHIED, Math.max(0, sheid - hurt));
        return sheid - hurt;
    }

    public void setShied(int shied){
        this.entityData.set(SHIED, shied);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType type, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        this.setShied(20);
        return super.finalizeSpawn(levelAccessor, difficultyInstance, type, groupData, tag);
    }
}
