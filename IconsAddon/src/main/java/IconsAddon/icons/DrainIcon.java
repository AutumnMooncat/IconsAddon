package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class DrainIcon extends AbstractCustomIcon {

    private static DrainIcon singleton;

    public static DrainIcon get()
    {
        if (singleton == null) {
            singleton = new DrainIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Drain";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Drain.png");
    }
}
