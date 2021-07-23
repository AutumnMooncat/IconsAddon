package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class FireIcon extends AbstractDamageTypeIcon {

    private static FireIcon singleton;

    public static FireIcon get()
    {
        if (singleton == null) {
            singleton = new FireIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Fire";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Fire.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
