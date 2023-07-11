package teamHTBP.vidaReforged.server.providers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.VidaConstant;
import teamHTBP.vidaReforged.core.utils.json.JsonUtils;
import teamHTBP.vidaReforged.server.providers.records.ElementPotential;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * DataPack Loader:ElementPotential {@link ElementPotential}
 * @author DustW,DespairP
 * */
public class ElementPotentialManager extends AbstractVidaManager {
    /**PotentialMap*/
    private static Map<String, ElementPotential> itemPotentialMap = new LinkedHashMap<>();

    public ElementPotentialManager() {
        super(VidaConstant.DATA_ELEMENT_POTENTIAL);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        // 初始化potential
        itemPotentialMap.clear();
        // 首先区分每个ElementKey
        pObject.forEach(((location, jsonElement) -> handlePotentialFile(location,jsonElement)));
    }

    /**处理每一个json文件*/
    protected void handlePotentialFile(ResourceLocation location,JsonElement jsonElement){
        if(!jsonElement.isJsonObject()){
            LOGGER.warn(String.format("%s is not a valid element potential json file", location.toString()));
            return;
        }
        JsonObject fileObject = jsonElement.getAsJsonObject();
        //处理每种元素
        for(String key : fileObject.keySet()){
            VidaElement element = VidaElement.valueOf(key.toUpperCase());
            if(fileObject.get(key) == null || !fileObject.get(key).isJsonArray()){
                LOGGER.warn(String.format("%s in %s element_potential json file is not an potential array,please have a check", key, location.toString()));
                continue;
            }
            //处理每种元素的Potential
            handleEachElement(element,fileObject.getAsJsonArray(key));
        }
    }

    /**处理每个json文件中每种元素的Potential*/
    protected void handleEachElement(VidaElement element, JsonArray array){
        for(JsonElement object : array.asList()){
            JsonObject potentialItem = object.getAsJsonObject();
            ElementPotential potential = gson.fromJson(potentialItem, ElementPotential.class);
            potential.setElement(element);
            itemPotentialMap.put(potential.getItem().toString(), potential);
        }
    }

    /**根据Item获取Potential*/
    public ElementPotential getPotential(Item item){
        return itemPotentialMap.get(item.toString());
    }

    /**根据ItemStack获取Potential*/
    public static ElementPotential getPotential(ItemStack itemStack){
        return itemPotentialMap.get(itemStack.getItem().toString());
    }

    /**根据ItemStack获取Potential*/
    public static List<ElementPotential> getPotential(Ingredient ingredient){
        NonNullList<ElementPotential> potentials = NonNullList.create();
        for(ItemStack stack : ingredient.getItems()){
            potentials.add( getPotential(stack) );
        }
        return potentials;
    }


}
