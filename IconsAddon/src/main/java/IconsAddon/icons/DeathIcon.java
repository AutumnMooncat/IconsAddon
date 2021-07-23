package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class DeathIcon extends AbstractDamageTypeIcon {

    private static DeathIcon singleton;

    public static DeathIcon get()
    {
        if (singleton == null) {
            singleton = new DeathIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Death";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Death.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
