package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class MusicIcon extends AbstractCustomIcon {

    private static MusicIcon singleton;

    public static MusicIcon get()
    {
        if (singleton == null) {
            singleton = new MusicIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Music";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Music.png");
    }
}
