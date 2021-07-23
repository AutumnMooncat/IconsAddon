package IconsAddon.patches;

import IconsAddon.icons.AbstractDamageTypeIcon;
import IconsAddon.util.DamageTypeIconHelper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Affine2;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import javassist.CtBehavior;

public class TipBoxIconsPatches {

    @SpirePatch(clz= FontHelper.class, method="renderSmartText", paramtypez = {SpriteBatch.class, BitmapFont.class, String.class, float.class, float.class, float.class, float.class, Color.class})
    public static class FontHelpFixes
    {
        private static final float CARD_ENERGY_IMG_WIDTH = 24.0f * Settings.scale;

        @SpireInsertPatch(locator = Locator.class, localvars = {"word"})
        public static void DrawIconsPls(SpriteBatch sb, BitmapFont font, String msg, float x, float y, float lineWidth, float lineSpacing, Color baseColor, @ByRef float[] ___curWidth, float ___curHeight, @ByRef String[] word)
        {
            if (word[0].length() > 0 && word[0].charAt(0) == '[') {
                String key = word[0].trim();
                if (key.endsWith(AbstractDamageTypeIcon.CODE_ENDING)) {
                    key = key.replace("*d", "D").replace("*b", "B").replace("*m", "M");
                }
                AbstractDamageTypeIcon icon = DamageTypeIconHelper.getIcon(key);
                if (icon != null) {
                    float width = CARD_ENERGY_IMG_WIDTH * Settings.scale;
                    renderSmallIcon(sb, icon, x+___curWidth[0], y+___curHeight);
                    ___curWidth[0] += width;
                    word[0] = "";
                }
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception
            {
                Matcher matcher = new Matcher.MethodCallMatcher(FontHelper.class, "identifyOrb");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }

    public static void renderSmallIcon(SpriteBatch sb, AbstractDamageTypeIcon icon, float offsetX, float offsetY)
    {
        TextureAtlas.AtlasRegion region = icon.getTexture();
        float renderScale = icon.getRenderScale();
        Affine2 aff = new Affine2();
        aff.setToTrnRotScl(
                (offsetX) * Settings.scale,
                (offsetY - 2f - region.getRegionHeight()/2f) * Settings.scale,
                0,
                Settings.scale * renderScale,
                Settings.scale * renderScale
        );
        sb.draw(
                region,
                region.packedWidth,
                region.packedHeight,
                aff
        );
    }
}
