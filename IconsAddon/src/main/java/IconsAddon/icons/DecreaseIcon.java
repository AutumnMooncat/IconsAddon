package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class DecreaseIcon extends AbstractCustomIcon {

    private static DecreaseIcon singleton;

    public static DecreaseIcon get()
    {
        if (singleton == null) {
            singleton = new DecreaseIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Decrease";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Decrease.png");
    }
}
