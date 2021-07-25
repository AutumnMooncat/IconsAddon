package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class TauntIcon extends AbstractCustomIcon {

    private static TauntIcon singleton;

    public static TauntIcon get()
    {
        if (singleton == null) {
            singleton = new TauntIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Taunt";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Taunt.png");
    }
}
