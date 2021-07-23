package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class DazedIcon extends AbstractDamageTypeIcon {

    private static DazedIcon singleton;

    public static DazedIcon get()
    {
        if (singleton == null) {
            singleton = new DazedIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Dazed";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Dazed.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
