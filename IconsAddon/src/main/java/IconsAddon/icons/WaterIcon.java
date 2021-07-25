package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class WaterIcon extends AbstractCustomIcon {

    private static WaterIcon singleton;

    public static WaterIcon get()
    {
        if (singleton == null) {
            singleton = new WaterIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Water";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Water.png");
    }
}
