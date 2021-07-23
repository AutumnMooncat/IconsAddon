package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class LightIcon extends AbstractDamageTypeIcon {

    private static LightIcon singleton;

    public static LightIcon get()
    {
        if (singleton == null) {
            singleton = new LightIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Light";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Light.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
