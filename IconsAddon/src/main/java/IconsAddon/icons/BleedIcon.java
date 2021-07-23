package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class BleedIcon extends AbstractDamageTypeIcon {

    private static BleedIcon singleton;

    public static BleedIcon get()
    {
        if (singleton == null) {
            singleton = new BleedIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Bleed";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Bleed.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
