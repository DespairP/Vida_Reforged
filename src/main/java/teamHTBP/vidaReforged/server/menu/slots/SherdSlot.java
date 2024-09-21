package teamHTBP.vidaReforged.server.menu.slots;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class SherdSlot extends ItemPredicateSlot{
    public final String ID_POINTS = "points";
    public final String ID_GENERATE_RANDOM_POINT = "isGeneratePoints";
    public final int BORDER_SIZE = 380;
    public final int MIN_POINT_SIZE = 120;
    public final int MAX_POINT_SIZE = 200;

    public SherdSlot(Container inventoryIn, int index, int xPosition, int yPosition, Predicate<ItemStack> tester) {
        super(inventoryIn, index, xPosition, yPosition, tester);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void set(ItemStack sherdStack) {
        CompoundTag sherdTag = sherdStack.getOrCreateTag();
        boolean isGenerateRandomPoint = sherdTag.getBoolean(ID_GENERATE_RANDOM_POINT);
        if(!isGenerateRandomPoint){
            List<Vector2i> randomPoints = new ArrayList<>();
            RandomSource random = RandomSource.create();
            ListTag listtag = new ListTag();
            for (int i = 0; i < random.nextInt(MIN_POINT_SIZE, MAX_POINT_SIZE); i++) {
                Vector2i point = new Vector2i(random.nextInt(BORDER_SIZE), random.nextInt(BORDER_SIZE));
                CompoundTag pointTag = new CompoundTag();
                pointTag.putByte("Point", (byte) i);
                pointTag.putInt("x", point.x());
                pointTag.putInt("y", point.y());
                randomPoints.add(point);
                listtag.add(pointTag);
            }
            sherdTag.putBoolean(ID_GENERATE_RANDOM_POINT, true);
            sherdTag.put(ID_POINTS, listtag);
            sherdTag.putInt("size", randomPoints.size());
        }
        super.set(sherdStack);
    }

    public List<Vector2i> getRandomPoints(){
        List<Vector2i> points = new ArrayList<>();
        ItemStack stack = getItem();
        if(stack.isEmpty()){
            return points;
        }
        CompoundTag tag = stack.getOrCreateTag();
        boolean isGeneratePoints = tag.getBoolean(ID_GENERATE_RANDOM_POINT);
        if(!isGeneratePoints){
            return points;
        }
        ListTag listTag = tag.getList(ID_POINTS, 10);

        for(int i = 0; i < listTag.size(); i++){
            CompoundTag pointTag = listTag.getCompound(i);
            points.add(new Vector2i(pointTag.getInt("x"), pointTag.getInt("y")));
        }
        return points;
    }
}
