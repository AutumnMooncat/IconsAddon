package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class RepeatIcon extends AbstractCustomIcon {

    private static RepeatIcon singleton;

    public static RepeatIcon get()
    {
        if (singleton == null) {
            singleton = new RepeatIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Repeat";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Repeat.png");
    }
}
