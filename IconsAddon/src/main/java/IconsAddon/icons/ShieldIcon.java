package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ShieldIcon extends AbstractCustomIcon {

    private static ShieldIcon singleton;

    public static ShieldIcon get()
    {
        if (singleton == null) {
            singleton = new ShieldIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Shield";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Shield.png");
    }
}
