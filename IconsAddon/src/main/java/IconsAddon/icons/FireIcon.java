package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class FireIcon extends AbstractCustomIcon {

    private static FireIcon singleton;

    public static FireIcon get()
    {
        if (singleton == null) {
            singleton = new FireIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Fire";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Fire.png");
    }
}
