package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class HeartIcon extends AbstractDamageTypeIcon {

    private static HeartIcon singleton;

    public static HeartIcon get()
    {
        if (singleton == null) {
            singleton = new HeartIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Heart";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Heart.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
