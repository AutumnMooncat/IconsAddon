package IconsAddon.patches;

import IconsAddon.blockModifiers.AbstractBlockModifier;
import IconsAddon.powers.OnCreateBlockContainerPower;
import IconsAddon.util.BlockContainer;
import IconsAddon.util.BlockModifierManager;
import basemod.patches.com.megacrit.cardcrawl.actions.GameActionManager.OnPlayerLoseBlockToggle;
import basemod.patches.com.megacrit.cardcrawl.core.AbstractCreature.ModifyPlayerLoseBlock;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.Collections;

public class BlockModifierPatches {

    public static BlockContainer specificContainerToReduce = null;
    public static DamageInfo workingInfo;
    public static Object boundObject;

    @SpirePatch(clz = ModifyPlayerLoseBlock.class, method = "Prefix")
    public static class ModifyStartOfTurnBlockLossPatch {
        @SpirePostfixPatch
        public static void pls(AbstractCreature __instance, int[] amount, boolean noAnimation) {
            if (OnPlayerLoseBlockToggle.isEnabled) {
                int tmp = amount[0];
                int removedAmount;
                //Specifically retain the block types that are not fully removed
                for (BlockContainer b : BlockModifierManager.blockContainers(__instance)) {
                    removedAmount = Math.min(b.getBlockAmount(), Math.min(b.computeStartTurnBlockLoss(), tmp));
                    for (AbstractBlockModifier m : b.getBlockTypes()) {
                        m.onStartOfTurnBlockLoss(removedAmount);
                    }

                    b.setBlockAmount(b.getBlockAmount() - removedAmount);
                    tmp -= removedAmount;
                    if (b.getBlockAmount() <= 0) {
                        for (AbstractBlockModifier m : b.getBlockTypes()) {
                            tmp = m.onRemove(true, workingInfo, tmp);
                        }
                    }
                    if (tmp <= 0) {
                        break;
                    }
                }
                BlockModifierManager.removeEmptyBlockContainers(__instance);
                amount[0] = BlockModifierManager.getBlockRetValBasedOnRemainingAmounts(__instance);
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
            if (specificContainerToReduce != null) {
                backupIndex = BlockModifierManager.blockContainers(__instance).indexOf(specificContainerToReduce);
                BlockModifierManager.blockContainers(__instance).remove(backupIndex);
                BlockModifierManager.blockContainers(__instance).add(0, specificContainerToReduce);
            }
            if (!isStartTurnLostBlock && !RetainMonsterBlockPatches.monsterStartOfTurn) {
                for (BlockContainer b : BlockModifierManager.blockContainers(__instance)) {
                    //effectiveAmount = Math.max(1, b.damageReducedPerBlockUsed());
                    //removedAmount = Math.min((int)Math.ceil((double)tmp/effectiveAmount), b.currentAmount);
                    removedAmount = Math.min(tmp, b.getBlockAmount());
                    b.setBlockAmount(b.getBlockAmount() - removedAmount);
                    if (b != specificContainerToReduce) {
                        for (AbstractBlockModifier m : b.getBlockTypes()) {
                            m.onThisBlockDamaged(workingInfo, removedAmount);
                        }
                    }
                    tmp -= removedAmount;
                    //reduction += removedAmount*(1-effectiveAmount);
                    if (b.getBlockAmount() <= 0) {
                        int d = tmp;
                        for (AbstractBlockModifier m : b.getBlockTypes()) {
                            d = m.onRemove(false, workingInfo, d);
                        }
                        reduction += tmp - d;
                        tmp = d;
                        if (reduction > tmp) {
                            reduction = tmp;
                        }
                    }
                    if (tmp <= 0 || reduction >= tmp) {
                        break;
                    }
                }
            }
            if (specificContainerToReduce != null) {
                BlockModifierManager.blockContainers(__instance).remove(0);
                BlockModifierManager.blockContainers(__instance).add(backupIndex, specificContainerToReduce);
                specificContainerToReduce = null;
            }
            BlockModifierManager.removeEmptyBlockContainers(__instance);
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
            //Define a new arraylist
            ArrayList<AbstractBlockModifier> blockTypes = new ArrayList<>();
            //Grab the action currently running, as this is what was processing when our block method was called
            AbstractGameAction a = AbstractDungeon.actionManager.currentAction;
            if (a != null) {
                //If the action is not null, see if it has an instigator object
                Object o = BlockModifierManager.BoundGameAction.boundBlockObject.get(a);
                if (o != null) {
                    //If so, this is our bound object to grab DamageMods off
                    for (AbstractBlockModifier m : BlockModifierManager.modifiers(o)) {
                        blockTypes.add(m.makeCopy());
                    }
                }
            }
            if (boundObject != null) {
                for (AbstractBlockModifier m : BlockModifierManager.modifiers(boundObject)) {
                    blockTypes.add(m.makeCopy());
                }
                boundObject = null;
            }
            for (AbstractPower p : __instance.powers) {
                if (p instanceof OnCreateBlockContainerPower) {
                    ((OnCreateBlockContainerPower) p).onCreateBlockContainer(blockTypes);
                }
            }
            Collections.sort(blockTypes);
            BlockContainer b = new BlockContainer(__instance, (int)tmp, blockTypes);
            BlockModifierManager.addBlockContainer(__instance, b);
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "decrementBlock")
    public static class OnAttackPreBlockDamaged {
        @SpirePrefixPatch()
        public static void OnAttackedAndSaveInfo(AbstractCreature __instance, DamageInfo info, int damageAmount) {
            workingInfo = info;
            BlockModifierManager.onAttacked(__instance, info, damageAmount);
        }

        @SpirePostfixPatch()
        public static void removeInfo(AbstractCreature __instance, DamageInfo info, int damageAmount) {
            workingInfo = null;
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class PlayerDamageGivePatches {
        @SpireInsertPatch(locator = PlayerDamageGiveLocator.class, localvars = "tmp")
        public static void singleGive(AbstractCard __instance, AbstractMonster mo, @ByRef float[] tmp) {
            tmp[0] = BlockModifierManager.atDamageGive(AbstractDungeon.player, tmp[0], __instance.damageTypeForTurn, mo, __instance);
        }

        @SpireInsertPatch(locator = PlayerMultiDamageGiveLocator.class, localvars = {"tmp","i"})
        public static void multiGive(AbstractCard __instance, AbstractMonster mo, float[] tmp, int i) {
            tmp[i] = BlockModifierManager.atDamageGive(AbstractDungeon.player, tmp[i], __instance.damageTypeForTurn, AbstractDungeon.getMonsters().monsters.get(i), __instance);
        }

        @SpireInsertPatch(locator = PlayerDamageFinalGiveLocator.class, localvars = "tmp")
        public static void singleFinalGive(AbstractCard __instance, AbstractMonster mo, @ByRef float[] tmp) {
            tmp[0] = BlockModifierManager.atDamageFinalGive(AbstractDungeon.player, tmp[0], __instance.damageTypeForTurn, mo, __instance);
        }

        @SpireInsertPatch(locator = PlayerMultiDamageFinalGiveLocator.class, localvars = {"tmp","i"})
        public static void multiFinalGive(AbstractCard __instance, AbstractMonster mo, float[] tmp, int i) {
            tmp[i] = BlockModifierManager.atDamageFinalGive(AbstractDungeon.player, tmp[i], __instance.damageTypeForTurn, AbstractDungeon.getMonsters().monsters.get(i), __instance);
        }

        @SpireInsertPatch(locator = MonsterDamageReceiveLocator.class, localvars = "tmp")
        public static void singleReceive(AbstractCard __instance, AbstractMonster mo, @ByRef float[] tmp) {
            tmp[0] = BlockModifierManager.atDamageReceive(mo, tmp[0], __instance.damageTypeForTurn, AbstractDungeon.player);
        }

        @SpireInsertPatch(locator = MonsterMultiDamageReceiveLocator.class, localvars = {"tmp","i"})
        public static void multiReceive(AbstractCard __instance, AbstractMonster mo, float[] tmp, int i) {
            tmp[i] = BlockModifierManager.atDamageReceive(AbstractDungeon.getMonsters().monsters.get(i), tmp[i], __instance.damageTypeForTurn, AbstractDungeon.player);
        }

        @SpireInsertPatch(locator = MonsterDamageFinalReceiveLocator.class, localvars = "tmp")
        public static void singleFinalReceive(AbstractCard __instance, AbstractMonster mo, @ByRef float[] tmp) {
            tmp[0] = BlockModifierManager.atDamageFinalReceive(mo, tmp[0], __instance.damageTypeForTurn, AbstractDungeon.player);
        }

        @SpireInsertPatch(locator = MonsterMultiDamageFinalReceiveLocator.class, localvars = {"tmp","i"})
        public static void multiFinalReceive(AbstractCard __instance, AbstractMonster mo, float[] tmp, int i) {
            tmp[i] = BlockModifierManager.atDamageFinalReceive(AbstractDungeon.getMonsters().monsters.get(i), tmp[i], __instance.damageTypeForTurn, AbstractDungeon.player);
        }


    }

    @SpirePatch(clz = AbstractCard.class, method = "applyPowersToBlock")
    public static class ApplyPowersToBlock {
        @SpireInsertPatch(locator = BlockLocator.class, localvars = {"tmp"})
        public static void blockInsert(AbstractCard __instance, @ByRef float[] tmp) {
            for (BlockContainer b : BlockModifierManager.blockContainers(AbstractDungeon.player)) {
                for (AbstractBlockModifier m : b.getBlockTypes()) {
                    tmp[0] = m.onModifyBlock(tmp[0], __instance);
                }
            }
        }

        @SpireInsertPatch(locator = BlockFinalLocator.class, localvars = {"tmp"})
        public static void blockFinalInsert(AbstractCard __instance, @ByRef float[] tmp) {
            for (BlockContainer b : BlockModifierManager.blockContainers(AbstractDungeon.player)) {
                for (AbstractBlockModifier m : b.getBlockTypes()) {
                    tmp[0] = m.onModifyBlockFinal(tmp[0], __instance);
                }
            }
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "calculateDamage")
    public static class PlayerDamageReceivePatches {
        @SpireInsertPatch(locator = PlayerDamageReceiveLocator.class, localvars = {"tmp"})
        public static void receive(AbstractMonster __instance, @ByRef float[] tmp) {
            tmp[0] = BlockModifierManager.atDamageReceive(AbstractDungeon.player, tmp[0], DamageInfo.DamageType.NORMAL, __instance);
        }

        @SpireInsertPatch(locator = PlayerDamageFinalReceiveLocator.class, localvars = {"tmp"})
        public static void finalReceive(AbstractMonster __instance, @ByRef float[] tmp) {
            tmp[0] = BlockModifierManager.atDamageFinalReceive(AbstractDungeon.player, tmp[0], DamageInfo.DamageType.NORMAL, __instance);
        }

        @SpireInsertPatch(locator = MonsterDamageGiveLocator.class, localvars = {"tmp"})
        public static void give(AbstractMonster __instance, @ByRef float[] tmp) {
            tmp[0] = BlockModifierManager.atDamageGive(__instance, tmp[0], DamageInfo.DamageType.NORMAL, AbstractDungeon.player, null);
        }

        @SpireInsertPatch(locator = MonsterDamageFinalGiveLocator.class, localvars = {"tmp"})
        public static void finalGive(AbstractMonster __instance, @ByRef float[] tmp) {
            tmp[0] = BlockModifierManager.atDamageFinalGive(__instance, tmp[0], DamageInfo.DamageType.NORMAL, AbstractDungeon.player, null);
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class MakeStatEquivalentCopy {
        public static AbstractCard Postfix(AbstractCard result, AbstractCard self) {
            for (AbstractBlockModifier mod : BlockModifierManager.modifiers(self)) {
                if (!mod.isInnate()) {
                    BlockModifierManager.addModifier(result, mod);
                }
            }
            return result;
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
