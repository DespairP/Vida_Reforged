package teamHTBP.vidaReforged.core.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.registries.RegistryObject;

public record DecoBlockProperties(WoodType woodType, BlockSetType blockSetType, RegistryObject<Block> baseBlock) {
    public void check() throws NullPointerException{
        if(woodType == null && blockSetType == null && baseBlock == null){
            throw new NullPointerException("if you want the register the advancedBlock,please fill the deco properties");
        }
    }
}
