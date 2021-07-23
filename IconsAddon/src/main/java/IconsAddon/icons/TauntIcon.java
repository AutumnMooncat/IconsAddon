package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class TauntIcon extends AbstractDamageTypeIcon {

    private static TauntIcon singleton;

    public static TauntIcon get()
    {
        if (singleton == null) {
            singleton = new TauntIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Taunt";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Taunt.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
