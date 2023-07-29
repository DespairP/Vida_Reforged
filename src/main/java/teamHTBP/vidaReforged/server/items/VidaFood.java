package teamHTBP.vidaReforged.server.items;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class VidaFood extends Item {
    public VidaFood(FoodProperties properties) {
        super(new Item.Properties().food(properties));
    }
}
