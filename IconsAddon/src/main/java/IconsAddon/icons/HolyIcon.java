package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class HolyIcon extends AbstractCustomIcon {

    private static HolyIcon singleton;

    public static HolyIcon get()
    {
        if (singleton == null) {
            singleton = new HolyIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Holy";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Holy.png");
    }
}
