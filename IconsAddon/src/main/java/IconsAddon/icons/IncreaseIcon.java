package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class IncreaseIcon extends AbstractDamageTypeIcon {

    private static IncreaseIcon singleton;

    public static IncreaseIcon get()
    {
        if (singleton == null) {
            singleton = new IncreaseIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Increase";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Increase.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
