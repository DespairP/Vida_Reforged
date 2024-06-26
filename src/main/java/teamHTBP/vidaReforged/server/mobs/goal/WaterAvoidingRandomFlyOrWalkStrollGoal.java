package teamHTBP.vidaReforged.server.mobs.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;
import teamHTBP.vidaReforged.server.mobs.OrangeSpottedSparrow;

import javax.annotation.Nullable;

public class WaterAvoidingRandomFlyOrWalkStrollGoal {
    public static class Walk extends WaterAvoidingRandomStrollGoal{
        public Walk(PathfinderMob mob, double speedModifier) {
            super(mob, speedModifier);
        }

        public Walk(PathfinderMob mob, double speedModifier, float probability) {
            super(mob, speedModifier, probability);
        }

        @Override
        public void start() {
            if(this.mob instanceof OrangeSpottedSparrow animal){
                animal.setWalk(true);
            }
            super.start();
        }


    }

    public static class Fly extends WaterAvoidingRandomStrollGoal {
        public Fly(PathfinderMob mob, double speedModifier, float probability) {
            super(mob, speedModifier, probability);
        }

        public Fly(PathfinderMob mob, double speedModifier) {
            super(mob, speedModifier);
        }

        @Override
        public void start() {
            if(this.mob instanceof OrangeSpottedSparrow animal){
                animal.setWalk(false);
            }
            super.start();
        }


        @Nullable
        protected Vec3 getPosition() {
            Vec3 finalPos = null;
            if (this.mob.isInWater()) {
                finalPos = LandRandomPos.getPos(this.mob, 15, 15);
                return finalPos;
            }

            if (this.mob.getRandom().nextBoolean()) {
                finalPos = this.getTreePos();
            }
            if (finalPos == null){
                Vec3 vec3 = this.mob.getViewVector(0.0F);
                Vec3 vec31 = HoverRandomPos.getPos(this.mob, 32, 28, vec3.x, vec3.z, ((float)Math.PI / 2F), 60, 20);
                finalPos = vec31 != null ? vec31 : AirAndWaterRandomPos.getPos(this.mob, 8, 4, -2, vec3.x, vec3.z, (double)((float)Math.PI / 2F));
            }

            return finalPos == null ? super.getPosition() : finalPos;
        }

        @Override
        public void stop() {
            super.stop();
        }

        @Nullable
        private Vec3 getTreePos() {
            BlockPos blockpos = this.mob.blockPosition();
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();

            for(BlockPos blockpos1 : BlockPos.betweenClosed(Mth.floor(this.mob.getX() - 3.0D), Mth.floor(this.mob.getY() - 12.0D), Mth.floor(this.mob.getZ() - 3.0D), Mth.floor(this.mob.getX() + 3.0D), Mth.floor(this.mob.getY() + 6.0D), Mth.floor(this.mob.getZ() + 3.0D))) {
                if (!blockpos.equals(blockpos1)) {
                    BlockState blockstate = this.mob.level().getBlockState(blockpos$mutableblockpos1.setWithOffset(blockpos1, Direction.DOWN));
                    boolean flag = blockstate.getBlock() instanceof LeavesBlock || blockstate.is(VidaBlockLoader.VIDA_LEAVES.get()) || blockstate.is(VidaBlockLoader.VIDA_BLUE_LEAVES.get());
                    if (flag && this.mob.level().isEmptyBlock(blockpos1) && this.mob.level().isEmptyBlock(blockpos$mutableblockpos.setWithOffset(blockpos1, Direction.UP))) {
                        return Vec3.atBottomCenterOf(blockpos1);
                    }
                }
            }

            return null;
        }
    }
}
