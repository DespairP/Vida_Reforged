package teamHTBP.vidaReforged.server.providers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.profiling.ProfilerFiller;
import teamHTBP.vidaReforged.core.common.system.guidebook.*;

import java.util.*;

public class VidaGuidebookManager<T> extends AbstractVidaManager{
    public static final Codec<VidaScreenEvent> VIDA_PAGE_EVENT_CODEC = RecordCodecBuilder.create(ins -> ins.group(
            Codec.STRING.fieldOf("type").orElse("").forGetter(VidaScreenEvent::getType),
            ExtraCodecs.JSON.fieldOf("data").orElse(new JsonObject()).forGetter(VidaScreenEvent::getData)
    ).apply(ins, VidaScreenEvent::new));

    public static final Codec<VidaPageSection> VIDA_PAGE_SECTION_CODEC = RecordCodecBuilder.create( ins -> ins.group(
            Codec.STRING.fieldOf("name").forGetter(VidaPageSection::getName),
            ResourceLocation.CODEC.fieldOf("icon").forGetter(VidaPageSection::getIcon),
            VIDA_PAGE_EVENT_CODEC.fieldOf("click").forGetter(VidaPageSection::getClick)
    ).apply(ins, VidaPageSection::new));

    public static final Codec<VidaPageList> VIDA_PAGE_LIST_CODEC = RecordCodecBuilder.create(ins -> ins.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(VidaPageList::getId),
            ExtraCodecs.COMPONENT.fieldOf("description").forGetter(VidaPageList::getDescription),
            Codec.BOOL.fieldOf("isSearchEnabled").orElse(false).forGetter(VidaPageList::isSearchEnabled),
            Codec.BOOL.fieldOf("isBackgroundEnabled").orElse(false).forGetter(VidaPageList::isBackgroundEnabled)
    ).apply(ins, VidaPageList::new));

    public static final Codec<VidaPageListItem> VIDA_PAGE_LIST_ITEM_CODEC = RecordCodecBuilder.create(ins -> ins.group(
            ResourceLocation.CODEC.fieldOf("id").orElse(new ResourceLocation("")).forGetter(VidaPageListItem::getId),
            ResourceLocation.CODEC.listOf().fieldOf("parentIds").orElseGet(LinkedList::new).forGetter(VidaPageListItem::getParentIds),
            Codec.INT.fieldOf("priority").orElseGet(() -> 999).forGetter(VidaPageListItem::getPriority),
            ResourceLocation.CODEC.fieldOf("list").forGetter(VidaPageListItem::getList),
            ExtraCodecs.COMPONENT.fieldOf("description").forGetter(VidaPageListItem::getDescription),
            DisplayInfo.CODES.fieldOf("info").forGetter(VidaPageListItem::getInfo),
            VidaGuidebookManager.VIDA_PAGE_EVENT_CODEC.optionalFieldOf("event").forGetter(VidaPageListItem::getEvent)
    ).apply(ins, VidaPageListItem::new));

    public static final Codec<VidaPageComponents> VIDA_PAGE_COMPONENTS_CODEC = RecordCodecBuilder.create(ins -> ins.group(
            Codec.INT.fieldOf("page").orElse(0).forGetter(VidaPageComponents::getPage),
            VidaPageComponentSerializer.CODEC.listOf().fieldOf("components").orElse(new ArrayList<>()).forGetter(VidaPageComponents::getComponents)
    ).apply(ins, VidaPageComponents::new));

    public static final Codec<VidaPageDetail> VIDA_PAGE_DETAIL_CODEC = RecordCodecBuilder.create(ins -> ins.group(
            ResourceLocation.CODEC.fieldOf("id").orElse(new ResourceLocation("")).forGetter(VidaPageDetail::getId),
            ExtraCodecs.COMPONENT.fieldOf("title").orElse(Component.empty()).forGetter(VidaPageDetail::getTitle),
            VIDA_PAGE_COMPONENTS_CODEC.listOf().fieldOf("pages").orElse(new ArrayList<>()).forGetter(VidaPageDetail::getPages),
            VidaPageComponentSerializer.CODEC.listOf().fieldOf("detailComponents").orElse(new ArrayList<>()).forGetter(VidaPageDetail::getDetailComponents)
    ).apply(ins, VidaPageDetail::new));

    public static Map<ResourceLocation, VidaPageSection> SECTION_MAP = new HashMap<>();
    public static Map<ResourceLocation, VidaPageList> LIST_MAP = new HashMap<>();
    public static Map<ResourceLocation, VidaPageListItem> LIST_ITEM_MAP = new HashMap<>();
    public static Map<ResourceLocation, VidaPageDetail> DETAIL_PAGE_MAP = new HashMap<>();

    private Codec<T> codec;
    private Map<ResourceLocation, T> storeMap;
    private AfterHandler<T> handler;

    public VidaGuidebookManager(String path, Codec<T> codec, Map<ResourceLocation, T> storeMap, AfterHandler<T> handler) {
        super(path);
        this.codec = codec;
        this.storeMap = storeMap;
        this.handler = handler;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonElementMap, ResourceManager manager, ProfilerFiller profilerFiller) throws JsonParseException{
        storeMap.clear();

        jsonElementMap.forEach( ((location, element) -> {
            T data = codec.parse(JsonOps.INSTANCE, element).getOrThrow(false, (errorMessage) -> {throw new JsonParseException(errorMessage);});
            storeMap.put(location, data);
        }) );
        if(handler != null){
            handler.handle(storeMap);
        }
    }

    /**处理parent*/
    public static void handleListItem(Map<ResourceLocation, VidaPageListItem> processMap){
        processMap.forEach((location, item) -> {
            item.setId(location);
            if(item.getParentIds() == null || item.getParentIds().isEmpty()){
                return;
            }
            List<VidaPageListItem> parents = new LinkedList<>();
            for(ResourceLocation parentRes : item.getParentIds()){
                //
                VidaPageListItem parentItem = processMap.getOrDefault(parentRes, null);
                if(parentItem == null){
                    LOGGER.error("Couldn't load PageListItem's parent {}: {}", location, parentRes);
                    continue;
                }
                parents.add(parentItem);
            }
            item.setParents(parents);
        });
    }


    public static void handleDetail(Map<ResourceLocation, VidaPageDetail> processMap){
        processMap.forEach((location, detail) -> {
            detail.setId(location);
        });
    }

    public interface AfterHandler<T>{
        public void handle(Map<ResourceLocation, T> processMap);
    }
}
