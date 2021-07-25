package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class DeathIcon extends AbstractCustomIcon {

    private static DeathIcon singleton;

    public static DeathIcon get()
    {
        if (singleton == null) {
            singleton = new DeathIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Death";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Death.png");
    }
}
