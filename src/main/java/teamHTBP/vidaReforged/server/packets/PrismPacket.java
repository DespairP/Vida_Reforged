package teamHTBP.vidaReforged.server.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.server.blockEntities.MagicWordCraftingTableBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.PrismBlockEntity;

import java.util.function.Supplier;

public class PrismPacket {
    BlockPos pos = BlockPos.ZERO;

    double rad0 = 0;

    double rad1 = 0;
    /*Logger*/
    public static final Logger LOGGER = LogManager.getLogger();

    public PrismPacket() {
    }

    public PrismPacket(BlockPos pos,double rad0, double rad1) {
        this.pos = pos;
        this.rad0 = rad0;
        this.rad1 = rad1;
    }

    public static PrismPacket fromBytes(FriendlyByteBuf buffer){
        BlockPos pos = buffer.readBlockPos();
        double rad0 = buffer.readDouble();
        double rad1 = buffer.readDouble();
        return new PrismPacket(pos, rad0, rad1);
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeDouble(rad0);
        buffer.writeDouble(rad1);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() == null) return;
            ServerLevel level = ctx.get().getSender().serverLevel();
            if(!level.isLoaded(pos)){
                return;
            }
            if(level.getBlockEntity(pos) instanceof PrismBlockEntity entity){
                entity.setProcess(true);
                entity.setClientRad$0((int)rad0);
                entity.setClientRad$1((int)rad1);
                level.sendBlockUpdated(pos, entity.getBlockState(), entity.getBlockState(), 1 | 2);
            }
        });
    }
}
