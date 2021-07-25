package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class LightIcon extends AbstractCustomIcon {

    private static LightIcon singleton;

    public static LightIcon get()
    {
        if (singleton == null) {
            singleton = new LightIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Light";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Light.png");
    }
}
