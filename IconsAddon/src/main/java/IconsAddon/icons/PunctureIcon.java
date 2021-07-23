package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class PunctureIcon extends AbstractDamageTypeIcon {

    private static PunctureIcon singleton;

    public static PunctureIcon get()
    {
        if (singleton == null) {
            singleton = new PunctureIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Puncture";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Puncture.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
