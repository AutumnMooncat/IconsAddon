package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class DarkIcon extends AbstractDamageTypeIcon {

    private static DarkIcon singleton;

    public static DarkIcon get()
    {
        if (singleton == null) {
            singleton = new DarkIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Dark";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Dark.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
