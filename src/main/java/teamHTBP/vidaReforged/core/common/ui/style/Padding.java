package teamHTBP.vidaReforged.core.common.ui.style;

import lombok.Getter;
import lombok.experimental.Accessors;

/**间距*/
@Getter
@Accessors(fluent = true)
public class Padding {
    int top;
    int right;
    int bottom;
    int left;


    public Padding(int top, int right, int bottom, int left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }


    public static Padding of(int top, int right, int bottom, int left){
        return new Padding(top, right, bottom, left);
    }

    public static Padding empty(){
        return new Padding(0, 0, 0, 0);
    }

}
