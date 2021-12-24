package IconsAddon.patches;

import IconsAddon.damageModifiers.AbstractDamageModifier;
import IconsAddon.powers.DamageModApplyingPower;
import IconsAddon.util.DamageModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

public class RenderElementsOnCardPatches {

    //Don't bother rendering if it isn't in one of 4 immediately viewable locations. We also don't want to render in master deck
    public static boolean validLocation(AbstractCard c) {
        return AbstractDungeon.player.hand.contains(c) ||
                AbstractDungeon.player.drawPile.contains(c) ||
                AbstractDungeon.player.discardPile.contains(c) ||
                AbstractDungeon.player.exhaustPile.contains(c);
    }

    @SpirePatch(clz = AbstractCard.class, method = "renderEnergy")
    public static class RenderOnCardPatch
    {
        @SpirePostfixPatch
        public static void RenderOnCard(AbstractCard __instance, SpriteBatch sb) {
            if (AbstractDungeon.player != null && validLocation(__instance)) {
                renderHelper(sb, __instance.current_x, __instance.current_y, __instance);
            }
        }

        private static void renderHelper(SpriteBatch sb, float drawX, float drawY, AbstractCard card) {
            ArrayList<AbstractDamageModifier> mods = new ArrayList<>();
            for (AbstractDamageModifier mod : DamageModifierManager.modifiers(card)) {
                if (mod.shouldPushIconToCard(card) && mod.getAccompanyingIcon() != null) {
                    mods.add(mod);
                }
            }
            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof DamageModApplyingPower && ((DamageModApplyingPower) p).shouldPushMods(null, card, DamageModifierManager.modifiers(card))) {
                    for (AbstractDamageModifier mod : ((DamageModApplyingPower) p).modsToPush(null, card, DamageModifierManager.modifiers(card))) {
                        if (mod.shouldPushIconToCard(card) && mod.getAccompanyingIcon() != null) {
                            mods.add(mod);
                        }
                    }
                }
            }
            if (!mods.isEmpty()) {
                sb.setColor(Color.WHITE.cpy());
                float dx = -(mods.size()-1)*16f;
                float dy = 210f;
                for (AbstractDamageModifier mod : mods) {
                    TextureAtlas.AtlasRegion img = mod.getAccompanyingIcon().getAtlasTexture();
                    sb.draw(img, drawX + dx + img.offsetX - (float) img.originalWidth / 2.0F, drawY + dy + img.offsetY - (float) img.originalHeight / 2.0F,
                            (float) img.originalWidth / 2.0F - img.offsetX - dx, (float) img.originalHeight / 2.0F - img.offsetY - dy,
                            (float) img.packedWidth, (float) img.packedHeight,
                            card.drawScale * Settings.scale, card.drawScale * Settings.scale, card.angle);
                    dx += 32f;
                }
            }
        }
    }
}
