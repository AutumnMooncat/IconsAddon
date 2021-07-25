package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class PoisonIcon extends AbstractCustomIcon {

    private static PoisonIcon singleton;

    public static PoisonIcon get()
    {
        if (singleton == null) {
            singleton = new PoisonIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Poison";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Poison.png");
    }
}
