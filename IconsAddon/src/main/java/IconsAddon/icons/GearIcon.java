package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class GearIcon extends AbstractCustomIcon {

    private static GearIcon singleton;

    public static GearIcon get()
    {
        if (singleton == null) {
            singleton = new GearIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Gear";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Gear.png");
    }
}
