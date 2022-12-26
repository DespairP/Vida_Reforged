package teamHTBP.vidaReforged.core.api.debug;

import java.util.Map;

/**
 * 当启用debug模式后，玩家的指针指向的物品如果继承了此接口，可以显示相关信息
 *
 * */
public interface IDebugObj {

    /**获取*/
    public Map<String,String> getDebugAttributes();

    public default Map<String,String> getTips(){return null;}

}
