package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ImpactIcon extends AbstractCustomIcon {

    private static ImpactIcon singleton;

    public static ImpactIcon get()
    {
        if (singleton == null) {
            singleton = new ImpactIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Impact";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Impact.png");
    }
}
