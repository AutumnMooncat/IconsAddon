package IconsAddon.patches;

import IconsAddon.icons.AbstractDamageTypeIcon;
import IconsAddon.util.DamageTypeIconHelper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CtBehavior;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CardDescriptionDamageTypeIcons {
    private static boolean overrideRenderScale;
    private static float renderScale;

    @SpirePatch(clz= AbstractCard.class, method="renderDescription")
    @SpirePatch(clz=AbstractCard.class, method="renderDescriptionCN")
    public static class RenderSmallIcon
    {
        private static final float CARD_ENERGY_IMG_WIDTH = 26.0f * Settings.scale;

        @SpireInsertPatch(
                locator=Locator.class,
                localvars={"spacing", "i", "start_x", "draw_y", "font", "textColor", "tmp", "gl"}
        )
        public static void Insert(AbstractCard __instance, SpriteBatch sb,
                                  float spacing, int i, @ByRef float[] start_x, float draw_y,
                                  BitmapFont font, Color textColor, @ByRef String[] tmp, GlyphLayout gl)
        {
            if (tmp[0].length() > 0 && tmp[0].charAt(0) == '[') {
                String key = tmp[0].trim();
                if (key.endsWith(AbstractDamageTypeIcon.CODE_ENDING)) {
                    key = key.replace("*d", "D").replace("*b", "B").replace("*m", "M");
                }
                AbstractDamageTypeIcon icon = DamageTypeIconHelper.getIcon(key);
                if (icon != null) {
                    gl.width = CARD_ENERGY_IMG_WIDTH * __instance.drawScale;
                    renderSmallIcon(__instance, sb, icon,
                            (start_x[0] - __instance.current_x) / Settings.scale / __instance.drawScale,
                            (-98.0f - ((__instance.description.size() - 4.0f) / 2.0f - i + 1.0f) * spacing));
                    start_x[0] += gl.width;
                    tmp[0] = "";
                }
            }
        }

        public static void renderSmallIcon(AbstractCard card, SpriteBatch sb, AbstractDamageTypeIcon icon, float offsetX, float offsetY)
        {
            TextureAtlas.AtlasRegion region = icon.getTexture();
            float renderScale = icon.getRenderScale();
            Affine2 aff = new Affine2();
            aff.setToTrnRotScl(
                    card.current_x + offsetX * card.drawScale * Settings.scale,
                    card.current_y + (offsetY) * card.drawScale * Settings.scale,
                    MathUtils.degreesToRadians * card.angle,
                    card.drawScale * Settings.scale * renderScale,
                    card.drawScale * Settings.scale * renderScale
            );
            sb.draw(
                    region,
                    region.packedWidth,
                    region.packedHeight,
                    aff
            );
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception
            {
                Matcher matcher = new Matcher.MethodCallMatcher(GlyphLayout.class, "setText");
                int[] lines = LineFinder.findAllInOrder(ctBehavior, matcher);
                return new int[]{lines[lines.length-1]}; // Only last occurrence
            }
        }
    }

    @SpirePatch(clz= SingleCardViewPopup.class, method="renderDescription")
    @SpirePatch(clz=SingleCardViewPopup.class, method="renderDescriptionCN")
    public static class RenderSmallIconSingleCardView
    {
        @SpireInsertPatch(
                locator=Locator.class,
                localvars={
                        "spacing", "i", "start_x", "tmp", "gl",
                        "card_energy_w", "drawScale", "current_x", "card"
                }
        )
        public static void Insert(SingleCardViewPopup __instance, SpriteBatch sb,
                                  float spacing, int i, @ByRef float[] start_x,
                                  @ByRef String[] tmp, GlyphLayout gl,
                                  float card_energy_w, float drawScale, float current_x, AbstractCard card)
        {
            if (tmp[0].length() > 0 && tmp[0].charAt(0) == '[') {
                String key = tmp[0].trim();
                if (key.endsWith(AbstractDamageTypeIcon.CODE_ENDING)) {
                    key = key.replace("*d", "D").replace("*b", "B").replace("*m", "M");
                }
                AbstractDamageTypeIcon element = DamageTypeIconHelper.getIcon(key);
                if (element != null) {
                    gl.width = card_energy_w * drawScale;
                    try {
                        Method renderSmallEnergy = SingleCardViewPopup.class.getDeclaredMethod("renderSmallEnergy", SpriteBatch.class, TextureAtlas.AtlasRegion.class, float.class, float.class);
                        renderSmallEnergy.setAccessible(true);
                        overrideRenderScale = true;
                        renderScale = element.getRenderScale();
                        renderSmallEnergy.invoke(__instance, sb, element.getTexture(),
                                (start_x[0] - current_x) / Settings.scale / drawScale,
                                -86.0f - ((card.description.size() - 4.0f) / 2.0f - i + 1.0f) * spacing);
                        overrideRenderScale = false;
                        renderScale = 1f;
                        sb.flush();
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    start_x[0] += gl.width;
                    tmp[0] = "";
                }
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception
            {
                Matcher matcher = new Matcher.MethodCallMatcher(GlyphLayout.class, "setText");
                int[] lines = LineFinder.findAllInOrder(ctBehavior, matcher);
                return new int[]{lines[lines.length-1]}; // Only last occurrence
            }
        }
    }

    @SpirePatch(clz=SingleCardViewPopup.class, method="renderSmallEnergy")
    public static class ScaleProperlyPls {
        @SpirePrefixPatch
        public static SpireReturn<?> pls(SingleCardViewPopup __instance, SpriteBatch sb, TextureAtlas.AtlasRegion region, float x, float y, float ___current_x, float ___current_y, float ___drawScale) {
            if (overrideRenderScale) {
                sb.setColor(Color.WHITE);
                sb.draw(region.getTexture(), ___current_x + x * Settings.scale * ___drawScale + region.offsetX * Settings.scale - 4.0F * Settings.scale, ___current_y + y * Settings.scale * ___drawScale + 280.0F * Settings.scale, 0.0F, 0.0F, (float)region.packedWidth, (float)region.packedHeight, ___drawScale * Settings.scale * renderScale, ___drawScale * Settings.scale * renderScale, 0.0F, region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), false, false);
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz=AbstractCard.class,
            method="initializeDescription"
    )
    public static class AlterIconDescriptionSize
    {
        private static final float CARD_ENERGY_IMG_WIDTH = 16.0f * Settings.scale;

        @SpireInsertPatch(
                locator=Locator.class,
                localvars={"gl", "word"}
        )
        public static void Insert(AbstractCard __instance,  @ByRef GlyphLayout[] gl, String word)
        {
            if (word.length() > 0 && word.charAt(0) == '[') {
                AbstractDamageTypeIcon icon = DamageTypeIconHelper.getIcon(word.trim());
                if (icon != null) {
                    gl[0].setText(FontHelper.cardDescFont_N, " ");
                    gl[0].width = CARD_ENERGY_IMG_WIDTH;
                }
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception
            {
                Matcher matcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "DESC_BOX_WIDTH");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }
}
