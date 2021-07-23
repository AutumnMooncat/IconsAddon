package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class WaterIcon extends AbstractDamageTypeIcon {

    private static WaterIcon singleton;

    public static WaterIcon get()
    {
        if (singleton == null) {
            singleton = new WaterIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Water";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Water.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
