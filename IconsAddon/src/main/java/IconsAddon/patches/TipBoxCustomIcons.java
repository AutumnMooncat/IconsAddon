package IconsAddon.patches;

import IconsAddon.icons.AbstractCustomIcon;
import IconsAddon.util.CustomIconHelper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Affine2;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import javassist.CtBehavior;

import java.util.ArrayList;

public class TipBoxCustomIcons {

    @SpirePatch(clz= TipHelper.class, method="renderPowerTips")
    public static class DontYeetPowerIcon
    {
        private static final float CARD_ENERGY_IMG_WIDTH = 24.0f * Settings.scale;

        @SpireInsertPatch(locator = Locator.class, localvars = {"tip"})
        public static void StopThat(float x, float y, SpriteBatch sb, ArrayList<PowerTip> powerTips, @ByRef PowerTip[] tip)
        {
            String[] words = tip[0].header.split(" ");
            boolean modified = false;
            for (int i = 0 ; i < words.length ; i++)
            if (words[i].length() > 0 && words[i].charAt(0) == '[') {
                String key = words[i].trim();
                if (key.endsWith(AbstractCustomIcon.CODE_ENDING)) {
                    key = key.replace("*d", "D").replace("*b", "B").replace("*m", "M");
                }
                AbstractCustomIcon icon = CustomIconHelper.getIcon(key);
                if (icon != null) {
                    words[i] = "   ";
                    modified = true;
                }
            }
            if (modified) {
                tip[0].header = String.join(" ", words);
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception
            {
                Matcher matcher = new Matcher.MethodCallMatcher(GlyphLayout.class, "setText");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }

    @SpirePatch(clz= FontHelper.class, method="getSmartHeight")
    public static class DontMakeNewLine
    {
        private static final float CARD_ENERGY_IMG_WIDTH = 24.0f * Settings.scale;

        @SpireInsertPatch(locator = Locator.class, localvars = {"word"})
        public static void IfThereIsAnIcon(BitmapFont font, String msg, float lineWidth, float lineSpacing, @ByRef float[] ___curWidth, float ___curHeight, @ByRef String[] word)
        {
            if (word[0].length() > 0 && word[0].charAt(0) == '[') {
                String key = word[0].trim();
                if (key.endsWith(AbstractCustomIcon.CODE_ENDING)) {
                    key = key.replace("*d", "D").replace("*b", "B").replace("*m", "M");
                }
                AbstractCustomIcon icon = CustomIconHelper.getIcon(key);
                if (icon != null) {
                    float width = CARD_ENERGY_IMG_WIDTH * Settings.scale;
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

    //TODO need to fix relics
    /*@SpirePatch2(clz= FontHelper.class, method="renderWrappedText", paramtypez = {SpriteBatch.class, BitmapFont.class, String.class, float.class, float.class, float.class, Color.class, float.class})
    public static class MakeRelicsWork
    {
        private static final float CARD_ENERGY_IMG_WIDTH = 24.0f * Settings.scale;

        @SpireInsertPatch(locator = Locator.class)
        public static void IfThereIsAnIcon(SpriteBatch sb, BitmapFont font, @ByRef String[] msg, float x, float y, float width, Color c, float scale)
        {
            String[] words = msg[0].split(" ");
            boolean modified = false;
            StringBuilder stringThusFar = new StringBuilder();
            float drawScale = font.getCapHeight()/24f;
            for (int i = 0 ; i < words.length ; i++) {
                if (words[i].length() > 0 && words[i].charAt(0) == '[') {
                    String key = words[i].trim();
                    if (key.endsWith(AbstractDamageTypeIcon.CODE_ENDING)) {
                        key = key.replace("*d", "D").replace("*b", "B").replace("*m", "M");
                    }
                    AbstractDamageTypeIcon icon = DamageTypeIconHelper.getIcon(key);
                    if (icon != null) {
                        FontHelper.layout.setText(font, stringThusFar);
                        float w = CARD_ENERGY_IMG_WIDTH + FontHelper.layout.width * Settings.scale;
                        renderSmallIcon(sb, icon, x+w, y, drawScale);
                        words[i] = "   ";
                        modified = true;
                    }
                }
                stringThusFar.append(words[i]).append(i < words.length - 1 ? " " : "");
                if (modified) {
                    msg[0] = stringThusFar.toString();
                }
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception
            {
                Matcher matcher = new Matcher.MethodCallMatcher(GlyphLayout.class, "setText");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }*/

    @SpirePatch(clz= FontHelper.class, method="renderSmartText", paramtypez = {SpriteBatch.class, BitmapFont.class, String.class, float.class, float.class, float.class, float.class, Color.class})
    public static class FontHelpFixes
    {
        private static final float CARD_ENERGY_IMG_WIDTH = 24.0f * Settings.scale;

        @SpireInsertPatch(locator = Locator.class, localvars = {"word"})
        public static void DrawIconsPls(SpriteBatch sb, BitmapFont font, String msg, float x, float y, float lineWidth, float lineSpacing, Color baseColor, @ByRef float[] ___curWidth, float ___curHeight, @ByRef String[] word)
        {
            if (word[0].length() > 0 && word[0].charAt(0) == '[') {
                String key = word[0].trim();
                if (key.endsWith(AbstractCustomIcon.CODE_ENDING)) {
                    key = key.replace("*d", "D").replace("*b", "B").replace("*m", "M");
                }
                AbstractCustomIcon icon = CustomIconHelper.getIcon(key);
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

    public static void renderSmallIcon(SpriteBatch sb, AbstractCustomIcon icon, float offsetX, float offsetY)
    {
        renderSmallIcon(sb, icon, offsetX, offsetY, 1f);
    }

    public static void renderSmallIcon(SpriteBatch sb, AbstractCustomIcon icon, float offsetX, float offsetY, float textScale)
    {
        TextureAtlas.AtlasRegion region = icon.getAtlasTexture();
        float renderScale = icon.getRenderScale() * textScale;
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
