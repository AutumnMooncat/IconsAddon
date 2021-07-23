package IconsAddon.patches;

import IconsAddon.damageModifiers.AbstractDamageModifier;
import IconsAddon.util.DamageModifierManager;
import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.RenderCardDescriptors;
import basemod.patches.com.megacrit.cardcrawl.screens.SingleCardViewPopup.RenderCardDescriptorsSCV;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.List;

public class DamageModifierPatches {

    @SpirePatch2(clz = basemod.patches.com.megacrit.cardcrawl.helpers.TipHelper.FakeKeywords.class, method = "Prefix")
    public static class AddTooltipTop {
        @SpireInsertPatch(locator = Locator1.class, localvars = "tooltips")
        public static void part1(AbstractCard ___card, @ByRef List<TooltipInfo>[] tooltips) {
            if (DamageModifierManager.modifiers(___card).size() > 0 && tooltips[0] == null) {
                tooltips[0] = new ArrayList<>();
            }
            for (AbstractDamageModifier mod : DamageModifierManager.modifiers(___card)) {
                if (mod.getCustomTooltip() != null) {
                    tooltips[0].add(mod.getCustomTooltip());
                }
            }
        }
        private static class Locator1 extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(CustomCard.class, "getCustomTooltipsTop");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }

        @SpireInsertPatch(locator = Locator2.class, localvars = "tooltips")
        public static void part2(AbstractCard ___card, @ByRef List<TooltipInfo>[] tooltips) {
            if (DamageModifierManager.modifiers(___card).size() > 0 && tooltips[0] == null) {
                tooltips[0] = new ArrayList<>();
            }
            for (AbstractDamageModifier mod : DamageModifierManager.modifiers(___card)) {
                tooltips[0].add(mod.getCustomTooltip());
            }
        }
        private static class Locator2 extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(List.class, "iterator");
                int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                int[] ret = new int[1];
                for (int value : tmp) {
                    ret[0] = value-1;
                }
                return ret;
            }
        }
    }

    @SpirePatch2(clz = basemod.patches.com.megacrit.cardcrawl.screens.SingleCardViewPopup.FakeKeywords.class, method = "InsertBefore")
    public static class AddTooltipTopSCV {
        @SpirePostfixPatch()
        public static void pls(AbstractCard acard, ArrayList<PowerTip>[] t) {
            for (AbstractDamageModifier mod : DamageModifierManager.modifiers(acard)) {
                if (mod.getCustomTooltip() != null) {
                    t[0].add(mod.getCustomTooltip().toPowerTip());
                }
            }
        }
    }

    @SpirePatch(clz = RenderCardDescriptors.Frame.class, method = "Insert")
    public static class AddDescriptorFrame {
        @SpireInsertPatch(locator = Locator.class, localvars = "descriptors")
        public static void pls(AbstractCard __instance, SpriteBatch sb, float x, float y, float[] tOffset, float[] tWidth, @ByRef List<String>[] descriptors) {
            for (AbstractDamageModifier mod : DamageModifierManager.modifiers(__instance)) {
                if (mod.getCardDescriptor() != null) {
                    descriptors[0].add(mod.getCardDescriptor());
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(List.class, "size");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = RenderCardDescriptors.Text.class, method = "Insert")
    public static class AddDescriptorText {
        @SpireInsertPatch(locator = Locator.class, localvars = "descriptors")
        public static void pls(AbstractCard __instance, SpriteBatch sb, String[] text, @ByRef List<String>[] descriptors) {
            for (AbstractDamageModifier mod : DamageModifierManager.modifiers(__instance)) {
                if (mod.getCardDescriptor() != null) {
                    descriptors[0].add(mod.getCardDescriptor());
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(List.class, "size");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch2(clz = RenderCardDescriptorsSCV.Frame.class, method = "Insert")
    public static class AddDescriptorSCVFrame {
        @SpireInsertPatch(locator = Locator.class, localvars = "descriptors")
        public static void pls(AbstractCard ___card, @ByRef List<String>[] descriptors) {
            for (AbstractDamageModifier mod : DamageModifierManager.modifiers(___card)) {
                if (mod.getCardDescriptor() != null) {
                    descriptors[0].add(mod.getCardDescriptor());
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(List.class, "size");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class ModifyDamage {
        @SpireInsertPatch(rlocs = {22}, localvars = "tmp")
        public static void single(AbstractCard __instance, AbstractMonster mo, @ByRef float[] tmp) {
            for (AbstractDamageModifier mod : DamageModifierManager.modifiers(__instance)) {
                tmp[0] = mod.atDamageGive(tmp[0], __instance.damageTypeForTurn, mo, __instance);
            }
        }

        @SpireInsertPatch(rlocs = {72}, localvars = {"tmp","i"})
        public static void multi(AbstractCard __instance, AbstractMonster mo, float[] tmp, int i) {
            for (AbstractDamageModifier mod : DamageModifierManager.modifiers(__instance)) {
                tmp[0] = mod.atDamageGive(tmp[i], __instance.damageTypeForTurn, AbstractDungeon.getMonsters().monsters.get(i), __instance);
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class ModifyDamageFinal {
        @SpireInsertPatch(rlocs = {34}, localvars = "tmp")
        public static void single(AbstractCard __instance, AbstractMonster mo, @ByRef float[] tmp) {
            for (AbstractDamageModifier mod : DamageModifierManager.modifiers(__instance)) {
                tmp[0] = mod.atDamageFinalGive(tmp[0], __instance.damageTypeForTurn, mo, __instance);
            }
        }

        @SpireInsertPatch(rlocs = {95}, localvars = "tmp")
        public static void multi(AbstractCard __instance, float[] tmp) {
            for (AbstractDamageModifier mod : DamageModifierManager.modifiers(__instance)) {
                int i = 0;
                for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
                    tmp[i] = mod.atDamageFinalGive(tmp[i], __instance.damageTypeForTurn, mon, __instance);
                    i++;
                }
            }
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class onAttackMonster {
        @SpireInsertPatch(rlocs = {34}, localvars = "damageAmount")
        public static void toChangeDamage(AbstractMonster __instance, DamageInfo info, @ByRef int[] damageAmount) {
            Object obj = DamageModifierManager.BoundDamageInfo.boundObject.get(info);
            if (obj != null) {
                for (AbstractDamageModifier mod : DamageModifierManager.modifiers(obj)) {
                    damageAmount[0] = mod.onAttackToChangeDamage(info, damageAmount[0], __instance);
                }
            }
        }

        @SpireInsertPatch(rlocs = {44}, localvars = "damageAmount")
        public static void onAttack(AbstractMonster __instance, DamageInfo info, int damageAmount) {
            Object obj = DamageModifierManager.BoundDamageInfo.boundObject.get(info);
            if (obj != null) {
                for (AbstractDamageModifier mod : DamageModifierManager.modifiers(obj)) {
                    mod.onAttack(info, damageAmount, __instance);
                }
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class onAttackPlayer {
        @SpireInsertPatch(rlocs = {29}, localvars = "damageAmount")
        public static void toChangeDamage(AbstractPlayer __instance, DamageInfo info, @ByRef int[] damageAmount) {
            Object obj = DamageModifierManager.BoundDamageInfo.boundObject.get(info);
            if (obj != null) {
                for (AbstractDamageModifier mod : DamageModifierManager.modifiers(obj)) {
                    damageAmount[0] = mod.onAttackToChangeDamage(info, damageAmount[0], __instance);
                }
            }
        }
        @SpireInsertPatch(rlocs = {55}, localvars = "damageAmount")
        public static void onAttack(AbstractPlayer __instance, DamageInfo info, int damageAmount) {
            Object obj = DamageModifierManager.BoundDamageInfo.boundObject.get(info);
            if (obj != null) {
                for (AbstractDamageModifier mod : DamageModifierManager.modifiers(obj)) {
                    mod.onAttack(info, damageAmount, __instance);
                }
            }
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "decrementBlock")
    public static class BlockStuff {
        @SpirePrefixPatch
        public static SpireReturn<?> block(AbstractCreature __instance, DamageInfo info, int damageAmount) {
            Object obj = DamageModifierManager.BoundDamageInfo.boundObject.get(info);
            if (obj != null) {
                boolean bypass = false;
                for (AbstractDamageModifier mod : DamageModifierManager.modifiers(obj)) {
                    mod.onDamageModifiedByBlock(info, Math.max(0, damageAmount-__instance.currentBlock), Math.min(damageAmount, __instance.currentBlock), __instance);
                    if (mod.ignoresBlock()) {
                        bypass = true;
                    }
                }
                if (bypass) {
                    return SpireReturn.Return(damageAmount);
                }
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ThornsPower.class, method = "onAttacked")
    public static class ThornsBypass {
        @SpirePrefixPatch
        public static SpireReturn<?> noDamagePls(ThornsPower __instance, DamageInfo info, int damageAmount) {
            Object obj = DamageModifierManager.BoundDamageInfo.boundObject.get(info);
            if (obj != null) {
                for (AbstractDamageModifier mod : DamageModifierManager.modifiers(obj)) {
                    if (mod.ignoresThorns()) {
                        return SpireReturn.Return(damageAmount);
                    }
                }
            }
            return SpireReturn.Continue();
        }
    }
}
