package IconsAddon.patches;

import IconsAddon.blockModifiers.AbstractCustomBlockType;
import IconsAddon.util.CustomBlockManager;
import basemod.patches.com.megacrit.cardcrawl.actions.GameActionManager.OnPlayerLoseBlockToggle;
import basemod.patches.com.megacrit.cardcrawl.core.AbstractCreature.ModifyPlayerLoseBlock;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

public class CustomBlockPatches {

    public static AbstractCustomBlockType specificBlockToReduce = null;
    public static boolean isNormalBlock = true;

    @SpirePatch(clz = ModifyPlayerLoseBlock.class, method = "Prefix")
    public static class ModifyStartOfTurnBlockLossPatch {
        @SpirePostfixPatch
        public static void pls(AbstractCreature __instance, int[] amount, boolean noAnimation) {
            if (OnPlayerLoseBlockToggle.isEnabled) {
                int tmp = amount[0];
                int removedAmount;
                //Specifically retain the block types that are not fully removed
                for (AbstractCustomBlockType b : CustomBlockManager.blockTypes(__instance)) {
                    removedAmount = Math.min(b.currentAmount, Math.min(b.amountLostAtStartOfTurn(), tmp));
                    b.onStartOfTurnBlockLoss(removedAmount);
                    b.currentAmount -= removedAmount;
                    if (b.currentAmount <= 0) {
                        b.onRemove();
                    }
                    tmp -= removedAmount;
                    if (tmp <= 0) {
                        break;
                    }
                }
                CustomBlockManager.removeEmptyBlockTypes(__instance);
                amount[0] = CustomBlockManager.getBlockRetValBasedOnRemainingAmounts(__instance);
            }
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "loseBlock", paramtypez = {int.class, boolean.class})
    public static class DecrementCustomBlockAmounts {
        @SpirePrefixPatch
        public static void pls(AbstractCreature __instance, int amount) {
            int tmp = amount;
            int removedAmount;
            boolean isStartTurnLostBlock = OnPlayerLoseBlockToggle.isEnabled;
            int backupIndex = -1;
            if (specificBlockToReduce != null) {
                backupIndex = CustomBlockManager.blockTypes(__instance).indexOf(specificBlockToReduce);
                CustomBlockManager.blockTypes(__instance).remove(backupIndex);
                CustomBlockManager.blockTypes(__instance).add(0, specificBlockToReduce);
            }
            if (!isStartTurnLostBlock) {
                for (AbstractCustomBlockType b : CustomBlockManager.blockTypes(__instance)) {
                    removedAmount = Math.min(tmp, b.currentAmount);
                    b.currentAmount -= removedAmount;
                    if (b != specificBlockToReduce) {
                        b.onThisBlockDamaged(removedAmount);
                    }
                    if (b.currentAmount <= 0) {
                        b.onRemove();
                    }
                    tmp -= removedAmount;
                    if (tmp <= 0) {
                        break;
                    }
                }
            }
            if (specificBlockToReduce != null) {
                CustomBlockManager.blockTypes(__instance).remove(0);
                CustomBlockManager.blockTypes(__instance).add(backupIndex, specificBlockToReduce);
                specificBlockToReduce = null;
            }
            CustomBlockManager.removeEmptyBlockTypes(__instance);
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "addBlock")
    public static class AddBlockMakePlaceHolderIfNeeded {
        @SpireInsertPatch(locator = CreatureAddBlockLocator.class, localvars = "tmp")
        public static void pls(AbstractCreature __instance, int amount, float tmp) {
            if (isNormalBlock) {
                CustomBlockManager.addBlockMakePlaceHolderIfNeeded(__instance, tmp);
            } else {
                isNormalBlock = true;
            }
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "decrementBlock")
    public static class OnAttackPreBlockDamaged {
        @SpirePrefixPatch()
        public static void pls(AbstractCreature __instance, DamageInfo info, int damageAmount) {
            CustomBlockManager.onAttacked(__instance, info, damageAmount);
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class ModifyDamage {
        @SpireInsertPatch(locator = DamageLocator.class, localvars = "tmp")
        public static void single(AbstractCard __instance, AbstractMonster mo, @ByRef float[] tmp) {
            for (AbstractCustomBlockType b : CustomBlockManager.blockTypes(AbstractDungeon.player)) {
                tmp[0] = b.atDamageGive(tmp[0], __instance.damageTypeForTurn, mo, __instance);
            }
        }

        @SpireInsertPatch(locator = MultiDamageLocator.class, localvars = {"tmp","i"})
        public static void multi(AbstractCard __instance, AbstractMonster mo, float[] tmp, int i) {
            for (AbstractCustomBlockType b : CustomBlockManager.blockTypes(AbstractDungeon.player)) {
                tmp[i] = b.atDamageGive(tmp[i], __instance.damageTypeForTurn, AbstractDungeon.getMonsters().monsters.get(i), __instance);
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class ModifyDamageFinal {
        @SpireInsertPatch(locator = DamageFinalLocator.class, localvars = "tmp")
        public static void single(AbstractCard __instance, AbstractMonster mo, @ByRef float[] tmp) {
            for (AbstractCustomBlockType b : CustomBlockManager.blockTypes(AbstractDungeon.player)) {
                tmp[0] = b.atDamageFinalGive(tmp[0], __instance.damageTypeForTurn, mo, __instance);
            }
        }

        @SpireInsertPatch(locator = MultiDamageFinalLocator.class, localvars = {"tmp","i"})
        public static void multi(AbstractCard __instance, AbstractMonster mo, float[] tmp, int i) {
            for (AbstractCustomBlockType b : CustomBlockManager.blockTypes(AbstractDungeon.player)) {
                tmp[i] = b.atDamageFinalGive(tmp[i], __instance.damageTypeForTurn, AbstractDungeon.getMonsters().monsters.get(i), __instance);
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "applyPowersToBlock")
    public static class ApplyPowersToBlock {
        @SpireInsertPatch(locator = BlockLocator.class, localvars = {"tmp"})
        public static void blockInsert(AbstractCard __instance, @ByRef float[] tmp) {
            for (AbstractCustomBlockType b : CustomBlockManager.blockTypes(AbstractDungeon.player)) {
                tmp[0] = b.onModifyBlock(tmp[0], __instance);
            }
        }

        @SpireInsertPatch(locator = BlockFinalLocator.class, localvars = {"tmp"})
        public static void blockFinalInsert(AbstractCard __instance, @ByRef float[] tmp) {
            for (AbstractCustomBlockType b : CustomBlockManager.blockTypes(AbstractDungeon.player)) {
                tmp[0] = b.onModifyBlockFinal(tmp[0], __instance);
            }
        }
    }

    /*
    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class onAttackMonster {
        @SpireInsertPatch(rlocs = {44}, localvars = "damageAmount")
        public static void onAttack(AbstractMonster __instance, DamageInfo info, int damageAmount) {
            if (info.owner != null) {
                for (AbstractCustomBlockType b : CustomBlockManager.blockTypes(info.owner)) {
                    b.onAttack(info, damageAmount, __instance);
                }
            }
        }

        @SpireInsertPatch(locator = MonsterOnAttackedLocator.class, localvars = "damageAmount")
        public static void onAttacked(AbstractMonster __instance, DamageInfo info, int damageAmount) {
            for (AbstractCustomBlockType b : CustomBlockManager.blockTypes(info.owner)) {
                b.onAttacked(info, damageAmount);
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class onAttackPlayer {
        @SpireInsertPatch(rlocs = {55}, localvars = "damageAmount")
        public static void onAttack(AbstractPlayer __instance, DamageInfo info, int damageAmount) {
            if (info.owner != null) {
                for (AbstractCustomBlockType b : CustomBlockManager.blockTypes(info.owner)) {
                    b.onAttack(info, damageAmount, __instance);
                }
            }
        }

        @SpireInsertPatch(locator = PlayerOnAttackedLocator.class, localvars = "damageAmount")
        public static void onAttacked(AbstractPlayer __instance, DamageInfo info, int damageAmount) {
            for (AbstractCustomBlockType b : CustomBlockManager.blockTypes(__instance)) {
                b.onAttacked(info, damageAmount);
            }
        }
    }*/

    private static class BlockFinalLocator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    private static class BlockLocator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
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

    private static class PlayerOnAttackedLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
            int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{tmp[1]};
        }
    }

    private static class MonsterOnAttackedLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractMonster.class, "powers");
            int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{tmp[2]};
        }
    }

    private static class CreatureAddBlockLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
