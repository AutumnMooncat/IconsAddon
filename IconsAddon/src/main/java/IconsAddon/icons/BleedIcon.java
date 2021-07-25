package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;

public class BleedIcon extends AbstractCustomIcon {

    private static BleedIcon singleton;

    public static BleedIcon get()
    {
        if (singleton == null) {
            singleton = new BleedIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Bleed";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Bleed.png");
    }
}
