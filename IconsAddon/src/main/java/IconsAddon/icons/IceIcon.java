package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class IceIcon extends AbstractDamageTypeIcon {

    private static IceIcon singleton;

    public static IceIcon get()
    {
        if (singleton == null) {
            singleton = new IceIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Ice";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Ice.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
