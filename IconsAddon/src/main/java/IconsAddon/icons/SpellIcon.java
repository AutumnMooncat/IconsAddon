package IconsAddon.icons;

import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class SpellIcon extends AbstractDamageTypeIcon {

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
    public TextureAtlas.AtlasRegion getTexture() {
        Texture tex = TextureLoader.getTexture("IconsAddonResources/images/icons/Spell.png");
        return new TextureAtlas.AtlasRegion(tex, 0, 0, IMG_SIZE, IMG_SIZE);
    }
}
