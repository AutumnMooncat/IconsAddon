package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class SlashIcon extends AbstractCustomIcon {

    private static SlashIcon singleton;

    public static SlashIcon get()
    {
        if (singleton == null) {
            singleton = new SlashIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Slash";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Slash.png");
    }
}
