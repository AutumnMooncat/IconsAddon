package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ParalysisIcon extends AbstractCustomIcon {

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
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Paralysis.png");
    }
}
