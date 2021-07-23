package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class MuteIcon extends AbstractDamageTypeIcon {

    private static MuteIcon singleton;

    public static MuteIcon get()
    {
        if (singleton == null) {
            singleton = new MuteIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Mute";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Mute.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
