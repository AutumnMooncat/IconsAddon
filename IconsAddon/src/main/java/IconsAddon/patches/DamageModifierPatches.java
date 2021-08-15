package IconsAddon.patches;

import IconsAddon.damageModifiers.AbstractDamageModifier;
import IconsAddon.util.DamageModifierManager;
import com.evacipated.cardcrawl.mod.stslib.patches.tempHp.PlayerDamage;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;
import javassist.CtBehavior;

public class DamageModifierPatches {

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class ModifyDamage {
        @SpireInsertPatch(locator = DamageLocator.class, localvars = "tmp")
        public static void single(AbstractCard __instance, AbstractMonster mo, @ByRef float[] tmp) {
            for (AbstractDamageModifier mod : DamageModifierManager.modifiers(__instance)) {
                tmp[0] = mod.atDamageGive(tmp[0], __instance.damageTypeForTurn, mo, __instance);
            }
        }

        @SpireInsertPatch(locator = MultiDamageLocator.class, localvars = {"tmp","i"})
        public static void multi(AbstractCard __instance, AbstractMonster mo, float[] tmp, int i) {
            for (AbstractDamageModifier mod : DamageModifierManager.modifiers(__instance)) {
                tmp[i] = mod.atDamageGive(tmp[i], __instance.damageTypeForTurn, AbstractDungeon.getMonsters().monsters.get(i), __instance);
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class ModifyDamageFinal {
        @SpireInsertPatch(locator = DamageFinalLocator.class, localvars = "tmp")
        public static void single(AbstractCard __instance, AbstractMonster mo, @ByRef float[] tmp) {
            for (AbstractDamageModifier mod : DamageModifierManager.modifiers(__instance)) {
                tmp[0] = mod.atDamageFinalGive(tmp[0], __instance.damageTypeForTurn, mo, __instance);
            }
        }

        @SpireInsertPatch(locator = MultiDamageFinalLocator.class, localvars = {"tmp","i"})
        public static void multi(AbstractCard __instance, AbstractMonster mo, float[] tmp, int i) {
            for (AbstractDamageModifier mod : DamageModifierManager.modifiers(__instance)) {
                tmp[i] = mod.atDamageFinalGive(tmp[i], __instance.damageTypeForTurn, AbstractDungeon.getMonsters().monsters.get(i), __instance);
            }
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class onAttackMonster {
        @SpireInsertPatch(rlocs = {34}, localvars = "damageAmount")
        public static void toChangeDamage(AbstractMonster __instance, DamageInfo info, @ByRef int[] damageAmount) {
            for (AbstractDamageModifier mod : DamageModifierManager.getDamageMods(info)) {
                damageAmount[0] = mod.onAttackToChangeDamage(info, damageAmount[0], __instance);
            }
        }

        @SpireInsertPatch(rlocs = {44}, localvars = "damageAmount")
        public static void onAttack(AbstractMonster __instance, DamageInfo info, int damageAmount) {
            for (AbstractDamageModifier mod : DamageModifierManager.getDamageMods(info)) {
                mod.onAttack(info, damageAmount, __instance);
            }
        }

        @SpirePostfixPatch()
        public static void removeModsAfterUse(AbstractMonster __instance, DamageInfo info) {
            Object obj = DamageModifierManager.getDamageMods(info);
            if (obj != null) {
                DamageModifierManager.modifiers(obj).removeIf(AbstractDamageModifier::removeWhenActivated);
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class onAttackPlayer {
        @SpireInsertPatch(rlocs = {29}, localvars = "damageAmount")
        public static void toChangeDamage(AbstractPlayer __instance, DamageInfo info, @ByRef int[] damageAmount) {
            for (AbstractDamageModifier mod : DamageModifierManager.getDamageMods(info)) {
                damageAmount[0] = mod.onAttackToChangeDamage(info, damageAmount[0], __instance);
            }
        }
        @SpireInsertPatch(rlocs = {55}, localvars = "damageAmount")
        public static void onAttack(AbstractPlayer __instance, DamageInfo info, int damageAmount) {
            for (AbstractDamageModifier mod : DamageModifierManager.getDamageMods(info)) {
                mod.onAttack(info, damageAmount, __instance);
            }
        }
        @SpirePostfixPatch()
        public static void removeModsAfterUse(AbstractPlayer __instance, DamageInfo info) {
            if (DamageModifierManager.getInstigatorCard(info) != null) {
                DamageModifierManager.modifiers(DamageModifierManager.getInstigatorCard(info)).removeIf(AbstractDamageModifier::removeWhenActivated);
            }
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "decrementBlock")
    public static class BlockStuff {
        @SpirePrefixPatch
        public static SpireReturn<?> block(AbstractCreature __instance, DamageInfo info, int damageAmount) {
            boolean bypass = false;
            for (AbstractDamageModifier mod : DamageModifierManager.getDamageMods(info)) {
                mod.onDamageModifiedByBlock(info, Math.max(0, damageAmount-__instance.currentBlock), Math.min(damageAmount, __instance.currentBlock), __instance);
                if (mod.ignoresBlock(__instance)) {
                    bypass = true;
                }
            }
            if (bypass) {
                return SpireReturn.Return(damageAmount);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = PlayerDamage.class, method = "Insert", paramtypez = {AbstractCreature.class, DamageInfo.class, int[].class, boolean[].class})
    public static class TempHPBypass {
        @SpirePrefixPatch
        public static SpireReturn<?> noDamagePls(AbstractCreature __instance, DamageInfo info, int[] damageAmount, boolean[] hadBlock) {
            for (AbstractDamageModifier mod : DamageModifierManager.getDamageMods(info)) {
                if (mod.ignoresTempHP(__instance)) {
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
            for (AbstractDamageModifier mod : DamageModifierManager.getDamageMods(info)) {
                if (mod.ignoresThorns()) {
                    return SpireReturn.Return(damageAmount);
                }
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class MakeStatEquivalentCopy {
        public static AbstractCard Postfix(AbstractCard result, AbstractCard self) {
            for (AbstractDamageModifier mod : DamageModifierManager.modifiers(self)) {
                if (!mod.isInherent()) {
                    DamageModifierManager.addModifier(result, mod);
                }
            }
            //DamageModifierManager.addModifiers(result, DamageModifierManager.modifiers(self).stream().filter(m -> !m.inInnate()).collect(Collectors.toCollection(ArrayList::new)));
            return result;
        }
    }

    private static class MultiDamageFinalLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
            int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{tmp[3]};
        }
    }

    private static class DamageFinalLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
            int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{tmp[1]};
        }
    }

    private static class MultiDamageLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
            int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{tmp[2]};
        }
    }

    private static class DamageLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
            int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{tmp[0]};
        }
    }
}
