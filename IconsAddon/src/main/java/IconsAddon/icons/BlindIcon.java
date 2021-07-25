package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class BlindIcon extends AbstractCustomIcon {

    private static BlindIcon singleton;

    public static BlindIcon get()
    {
        if (singleton == null) {
            singleton = new BlindIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Blind";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Blind.png");
    }
}
