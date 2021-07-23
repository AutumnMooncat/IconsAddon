package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class WindIcon extends AbstractDamageTypeIcon {

    private static WindIcon singleton;

    public static WindIcon get()
    {
        if (singleton == null) {
            singleton = new WindIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Wind";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Wind.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
