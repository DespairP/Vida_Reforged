package teamHTBP.vidaReforged.server.commands.arguments;

import com.google.gson.JsonObject;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;

public class MagicArgumentInfo implements ArgumentTypeInfo<MagicArgument, MagicArgumentInfo.Template> {

    @Override
    public void serializeToNetwork(Template p_235375_, FriendlyByteBuf p_235376_) {

    }

    @Override
    public Template deserializeFromNetwork(FriendlyByteBuf p_235377_) {
        return new Template();
    }

    @Override
    public void serializeToJson(Template p_235373_, JsonObject p_235374_) {

    }

    @Override
    public Template unpack(MagicArgument p_235372_) {
        return new Template();
    }


    public final class Template implements ArgumentTypeInfo.Template<MagicArgument> {

        @Override
        public MagicArgument instantiate(CommandBuildContext p_235378_) {
            return MagicArgument.arg();
        }

        @Override
        public ArgumentTypeInfo<MagicArgument, ?> type() {
            return MagicArgumentInfo.this;
        }
    }
}
