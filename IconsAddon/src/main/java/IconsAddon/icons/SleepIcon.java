package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class SleepIcon extends AbstractCustomIcon {

    private static SleepIcon singleton;

    public static SleepIcon get()
    {
        if (singleton == null) {
            singleton = new SleepIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Sleep";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Sleep.png");
    }
}
