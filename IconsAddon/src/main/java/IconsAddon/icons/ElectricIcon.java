package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ElectricIcon extends AbstractDamageTypeIcon {

    private static ElectricIcon singleton;

    public static ElectricIcon get()
    {
        if (singleton == null) {
            singleton = new ElectricIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Electric";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Electric.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
