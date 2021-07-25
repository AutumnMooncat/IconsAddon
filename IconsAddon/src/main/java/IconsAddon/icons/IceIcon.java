package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class IceIcon extends AbstractCustomIcon {

    private static IceIcon singleton;

    public static IceIcon get()
    {
        if (singleton == null) {
            singleton = new IceIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Ice";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Ice.png");
    }
}
