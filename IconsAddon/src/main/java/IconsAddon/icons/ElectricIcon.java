package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ElectricIcon extends AbstractCustomIcon {

    private static ElectricIcon singleton;

    public static ElectricIcon get()
    {
        if (singleton == null) {
            singleton = new ElectricIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Electric";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Electric.png");
    }
}
