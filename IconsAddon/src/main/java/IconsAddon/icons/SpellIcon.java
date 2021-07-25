package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class SpellIcon extends AbstractCustomIcon {

    private static SpellIcon singleton;

    public static SpellIcon get()
    {
        if (singleton == null) {
            singleton = new SpellIcon();
        }
        return singleton;
    }


    @Override
    public String name() {
        return "Spell";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("IconsAddonResources/images/icons/Spell.png");
    }
}
