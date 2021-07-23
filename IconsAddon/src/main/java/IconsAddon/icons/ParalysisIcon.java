package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ParalysisIcon extends AbstractDamageTypeIcon {

    private static ParalysisIcon singleton;

    public static ParalysisIcon get()
    {
        if (singleton == null) {
            singleton = new ParalysisIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Paralysis";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Paralysis.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
