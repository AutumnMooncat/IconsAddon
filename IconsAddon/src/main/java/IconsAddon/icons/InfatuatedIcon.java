package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class InfatuatedIcon extends AbstractCustomIcon {

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
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Infatuated.png");
    }
}
