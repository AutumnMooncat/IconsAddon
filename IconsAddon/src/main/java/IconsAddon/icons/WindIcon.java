package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class WindIcon extends AbstractCustomIcon {

    private static WindIcon singleton;

    public static WindIcon get()
    {
        if (singleton == null) {
            singleton = new WindIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Wind";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Wind.png");
    }
}
