package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class RunningIcon extends AbstractDamageTypeIcon {

    private static RunningIcon singleton;

    public static RunningIcon get()
    {
        if (singleton == null) {
            singleton = new RunningIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Running";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Running.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
