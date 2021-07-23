package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class GearIcon extends AbstractDamageTypeIcon {

    private static GearIcon singleton;

    public static GearIcon get()
    {
        if (singleton == null) {
            singleton = new GearIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Gear";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Gear.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
