package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class RangedIcon extends AbstractCustomIcon {

    private static RangedIcon singleton;

    public static RangedIcon get()
    {
        if (singleton == null) {
            singleton = new RangedIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Ranged";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Ranged.png");
    }
}
