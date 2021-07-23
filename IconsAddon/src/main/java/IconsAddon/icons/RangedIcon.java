package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class RangedIcon extends AbstractDamageTypeIcon {

    private static RangedIcon singleton;

    public static RangedIcon get()
    {
        if (singleton == null) {
            singleton = new RangedIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Ranged";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Ranged.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
