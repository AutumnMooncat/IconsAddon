package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class DazedIcon extends AbstractCustomIcon {

    private static DazedIcon singleton;

    public static DazedIcon get()
    {
        if (singleton == null) {
            singleton = new DazedIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Dazed";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Dazed.png");
    }
}
