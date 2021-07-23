package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class InfatuatedIcon extends AbstractDamageTypeIcon {

    private static InfatuatedIcon singleton;

    public static InfatuatedIcon get()
    {
        if (singleton == null) {
            singleton = new InfatuatedIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Infatuated";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Infatuated.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
