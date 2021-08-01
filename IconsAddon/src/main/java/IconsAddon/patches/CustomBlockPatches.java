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
    public static DamageInfo workingInfo;

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
                    tmp -= removedAmount;
                    if (b.currentAmount <= 0) {
                        tmp = b.onRemove(true, workingInfo, tmp);
                    }
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
        public static void pls(AbstractCreature __instance, @ByRef int[] amount) {
            int tmp = amount[0];
            int removedAmount;
            boolean isStartTurnLostBlock = OnPlayerLoseBlockToggle.isEnabled;
            int backupIndex = -1;
            int reduction = 0;
            int effectiveAmount;
            if (specificBlockToReduce != null) {
                backupIndex = CustomBlockManager.blockTypes(__instance).indexOf(specificBlockToReduce);
                CustomBlockManager.blockTypes(__instance).remove(backupIndex);
                CustomBlockManager.blockTypes(__instance).add(0, specificBlockToReduce);
            }
            if (!isStartTurnLostBlock) {
                for (AbstractCustomBlockType b : CustomBlockManager.blockTypes(__instance)) {
                    effectiveAmount = Math.max(1, b.damageReducedPerBlockUsed());
                    removedAmount = Math.min((int)Math.ceil((double)tmp/effectiveAmount), b.currentAmount);
                    b.currentAmount -= removedAmount;
                    if (b != specificBlockToReduce) {
                        b.onThisBlockDamaged(workingInfo, removedAmount);
                    }
                    tmp -= removedAmount*effectiveAmount;
                    reduction += removedAmount*(1-effectiveAmount);
                    if (b.currentAmount <= 0) {
                        int delta = b.onRemove(false, workingInfo, tmp);
                        tmp -= delta;
                        reduction += delta;
                        if (reduction > tmp) {
                            reduction = tmp;
                        }
                    }
                    if (tmp <= 0 || reduction >= tmp) {
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
            amount[0] -= reduction;
            if (amount[0] < 0) {
                amount[0] = 0;
            }
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
        public static void OnAttackedAndSaveInfo(AbstractCreature __instance, DamageInfo info, int damageAmount) {
            workingInfo = info;
            CustomBlockManager.onAttacked(__instance, info, damageAmount);
        }

        @SpirePrefixPatch()
        public static void removeInfo(AbstractCreature __instance, DamageInfo info, int damageAmount) {
            workingInfo = null;
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class PlayerDamageGivePatches {
        @SpireInsertPatch(locator = PlayerDamageGiveLocator.class, localvars = "tmp")
        public static void singleGive(AbstractCard __instance, AbstractMonster mo, @ByRef float[] tmp) {
            tmp[0] = CustomBlockManager.atDamageGive(AbstractDungeon.player, tmp[0], __instance.damageTypeForTurn, mo, __instance);
        }

        @SpireInsertPatch(locator = PlayerMultiDamageGiveLocator.class, localvars = {"tmp","i"})
        public static void multiGive(AbstractCard __instance, AbstractMonster mo, float[] tmp, int i) {
            tmp[i] = CustomBlockManager.atDamageGive(AbstractDungeon.player, tmp[i], __instance.damageTypeForTurn, AbstractDungeon.getMonsters().monsters.get(i), __instance);
        }

        @SpireInsertPatch(locator = PlayerDamageFinalGiveLocator.class, localvars = "tmp")
        public static void singleFinalGive(AbstractCard __instance, AbstractMonster mo, @ByRef float[] tmp) {
            tmp[0] = CustomBlockManager.atDamageFinalGive(AbstractDungeon.player, tmp[0], __instance.damageTypeForTurn, mo, __instance);
        }

        @SpireInsertPatch(locator = PlayerMultiDamageFinalGiveLocator.class, localvars = {"tmp","i"})
        public static void multiFinalGive(AbstractCard __instance, AbstractMonster mo, float[] tmp, int i) {
            tmp[i] = CustomBlockManager.atDamageFinalGive(AbstractDungeon.player, tmp[i], __instance.damageTypeForTurn, AbstractDungeon.getMonsters().monsters.get(i), __instance);
        }

        @SpireInsertPatch(locator = MonsterDamageReceiveLocator.class, localvars = "tmp")
        public static void singleReceive(AbstractCard __instance, AbstractMonster mo, @ByRef float[] tmp) {
            tmp[0] = CustomBlockManager.atDamageReceive(mo, tmp[0], __instance.damageTypeForTurn, AbstractDungeon.player);
        }

        @SpireInsertPatch(locator = MonsterMultiDamageReceiveLocator.class, localvars = {"tmp","i"})
        public static void multiReceive(AbstractCard __instance, AbstractMonster mo, float[] tmp, int i) {
            tmp[i] = CustomBlockManager.atDamageReceive(AbstractDungeon.getMonsters().monsters.get(i), tmp[i], __instance.damageTypeForTurn, AbstractDungeon.player);
        }

        @SpireInsertPatch(locator = MonsterDamageFinalReceiveLocator.class, localvars = "tmp")
        public static void singleFinalReceive(AbstractCard __instance, AbstractMonster mo, @ByRef float[] tmp) {
            tmp[0] = CustomBlockManager.atDamageFinalReceive(mo, tmp[0], __instance.damageTypeForTurn, AbstractDungeon.player);
        }

        @SpireInsertPatch(locator = MonsterMultiDamageFinalReceiveLocator.class, localvars = {"tmp","i"})
        public static void multiFinalReceive(AbstractCard __instance, AbstractMonster mo, float[] tmp, int i) {
            tmp[i] = CustomBlockManager.atDamageFinalReceive(AbstractDungeon.getMonsters().monsters.get(i), tmp[i], __instance.damageTypeForTurn, AbstractDungeon.player);
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

    @SpirePatch(clz = AbstractMonster.class, method = "calculateDamage")
    public static class PlayerDamageReceivePatches {
        @SpireInsertPatch(locator = PlayerDamageReceiveLocator.class, localvars = {"tmp"})
        public static void receive(AbstractMonster __instance, @ByRef float[] tmp) {
            tmp[0] = CustomBlockManager.atDamageReceive(AbstractDungeon.player, tmp[0], DamageInfo.DamageType.NORMAL, __instance);
        }

        @SpireInsertPatch(locator = PlayerDamageFinalReceiveLocator.class, localvars = {"tmp"})
        public static void finalReceive(AbstractMonster __instance, @ByRef float[] tmp) {
            tmp[0] = CustomBlockManager.atDamageFinalReceive(AbstractDungeon.player, tmp[0], DamageInfo.DamageType.NORMAL, __instance);
        }

        @SpireInsertPatch(locator = MonsterDamageGiveLocator.class, localvars = {"tmp"})
        public static void give(AbstractMonster __instance, @ByRef float[] tmp) {
            tmp[0] = CustomBlockManager.atDamageGive(__instance, tmp[0], DamageInfo.DamageType.NORMAL, AbstractDungeon.player, null);
        }

        @SpireInsertPatch(locator = MonsterDamageFinalGiveLocator.class, localvars = {"tmp"})
        public static void finalGive(AbstractMonster __instance, @ByRef float[] tmp) {
            tmp[0] = CustomBlockManager.atDamageFinalGive(__instance, tmp[0], DamageInfo.DamageType.NORMAL, AbstractDungeon.player, null);
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

    private static class CreatureAddBlockLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    private static class PlayerDamageGiveLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
            int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{tmp[0]};
        }
    }

    private static class PlayerDamageFinalGiveLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
            int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{tmp[1]};
        }
    }

    private static class PlayerMultiDamageGiveLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
            int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{tmp[2]};
        }
    }

    private static class PlayerMultiDamageFinalGiveLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
            int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{tmp[3]};
        }
    }

    private static class PlayerDamageReceiveLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
            int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{tmp[0]};
        }
    }

    private static class PlayerDamageFinalReceiveLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
            int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{tmp[1]};
        }
    }

    private static class MonsterDamageGiveLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractMonster.class, "powers");
            int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{tmp[0]};
        }
    }

    private static class MonsterDamageFinalGiveLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractMonster.class, "powers");
            int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{tmp[1]};
        }
    }

    private static class MonsterDamageReceiveLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractMonster.class, "powers");
            int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{tmp[0]};
        }
    }

    private static class MonsterDamageFinalReceiveLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractMonster.class, "powers");
            int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{tmp[1]};
        }
    }

    private static class MonsterMultiDamageReceiveLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractMonster.class, "powers");
            int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{tmp[2]};
        }
    }

    private static class MonsterMultiDamageFinalReceiveLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractMonster.class, "powers");
            int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{tmp[3]};
        }
    }
}
