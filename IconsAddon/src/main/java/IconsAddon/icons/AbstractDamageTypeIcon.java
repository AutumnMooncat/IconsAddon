package IconsAddon.icons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.core.Settings;

public abstract class AbstractDamageTypeIcon {
    protected static final int IMG_SIZE = 32;
    protected static final int RENDER_SIZE = 24;
    public static final String CODE_ENDING = "Icon]";

    public String cardCode() {
        return "[" + name() + CODE_ENDING;
    }

    public abstract String name();

    public abstract AtlasRegion getTexture();

    public int getImgSize() {
        return IMG_SIZE;
    }

    public float getRenderScale() {
        return (float)RENDER_SIZE/getImgSize();
    }

    public AtlasRegion getQueuedTexture() {
        return getTexture();
    }

    public final void renderExact(SpriteBatch sb, float x, float y, float rotation) {
        AtlasRegion tex = getQueuedTexture();
        sb.draw(tex, x, y, 0, 0, tex.getRegionWidth(), tex.getRegionHeight(), Settings.scale*2f*getRenderScale(), Settings.scale*2f*getRenderScale(), rotation);
    }

    public void render(SpriteBatch sb, float x, float y) {
        renderExact(sb, x, y, 0);
    }
}
