package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class MuteIcon extends AbstractCustomIcon {

    private static MuteIcon singleton;

    public static MuteIcon get()
    {
        if (singleton == null) {
            singleton = new MuteIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Mute";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Mute.png");
    }
}
