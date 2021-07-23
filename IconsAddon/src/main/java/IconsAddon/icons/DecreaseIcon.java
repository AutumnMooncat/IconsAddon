package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class DecreaseIcon extends AbstractDamageTypeIcon {

    private static DecreaseIcon singleton;

    public static DecreaseIcon get()
    {
        if (singleton == null) {
            singleton = new DecreaseIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Decrease";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Decrease.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
