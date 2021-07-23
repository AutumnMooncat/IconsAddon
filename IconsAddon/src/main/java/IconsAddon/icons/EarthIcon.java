package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class EarthIcon extends AbstractDamageTypeIcon {

    private static EarthIcon singleton;

    public static EarthIcon get()
    {
        if (singleton == null) {
            singleton = new EarthIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Earth";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Earth.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
