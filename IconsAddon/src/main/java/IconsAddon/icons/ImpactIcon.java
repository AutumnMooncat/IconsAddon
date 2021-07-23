package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ImpactIcon extends AbstractDamageTypeIcon {

    private static ImpactIcon singleton;

    public static ImpactIcon get()
    {
        if (singleton == null) {
            singleton = new ImpactIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Impact";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Impact.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
