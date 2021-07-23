package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class SlashIcon extends AbstractDamageTypeIcon {

    private static SlashIcon singleton;

    public static SlashIcon get()
    {
        if (singleton == null) {
            singleton = new SlashIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Slash";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Slash.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
