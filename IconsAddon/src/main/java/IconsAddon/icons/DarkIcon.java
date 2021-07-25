package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class DarkIcon extends AbstractCustomIcon {

    private static DarkIcon singleton;

    public static DarkIcon get()
    {
        if (singleton == null) {
            singleton = new DarkIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Dark";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Dark.png");
    }
}
