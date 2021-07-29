package IconsAddon.util;

import IconsAddon.blockModifiers.AbstractCustomBlockType;
import IconsAddon.patches.CustomBlockPatches;
import IconsAddon.powers.BlockTipPower;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class CustomBlockManager {
    @SpirePatch(clz = AbstractCreature.class, method = SpirePatch.CLASS)
    private static class BlockTypes {
        public static SpireField<ArrayList<AbstractCustomBlockType>> blockTypes = new SpireField<>(ArrayList::new);
    }

    public static AbstractCustomBlockType getTopBlockType(AbstractCreature owner) {
        return BlockTypes.blockTypes.get(owner).get(0);
    }

    public static boolean hasCustomBlockType(AbstractCreature owner) {
        return !BlockTypes.blockTypes.get(owner).isEmpty();
    }

    public static void addCustomBlock(AbstractCustomBlockType blockType) {
        //Grab the owner
        AbstractCreature owner = blockType.owner;
        //We haven't stacked by default
        boolean successfullyStacked = false;

        //If we actually want to stack...
        if (blockType.shouldStack()) {
            //Try looking for one to stack with
            for (AbstractCustomBlockType b : blockTypes(owner)){
                //IF we find it, set the flag and stack
                if (b.getClass().equals(blockType.getClass())) {
                    //Increase the amounts and call onStack
                    b.currentAmount += blockType.currentAmount;
                    b.onStack(blockType.currentAmount);
                    //Set the flag
                    successfullyStacked = true;
                    break;
                }
            }
        }

        //If we failed to stack of if we weren't supposed to stack in the first place...
        if (!successfullyStacked) {
            //Check if this is the first custom block being added
            if (!hasCustomBlockType(owner)) {
                //If it is, we need to add a placeholder for Normal Block if we already had block
                if (owner.currentBlock > 0) {
                    BlockTypes.blockTypes.get(owner).add(0, makeBlockPlaceholder(owner, owner.currentBlock));
                }
                //We also need to set up our power to show the custom info
                addToTop(new ApplyPowerAction(owner, owner, new BlockTipPower(owner)));
            }
            //Add to the first index, we loop first -> last, so this uses a First In - Last Out paradigm
            BlockTypes.blockTypes.get(owner).add(0, blockType);
            //Call on application code
            blockType.onApplication();
        }

        //Flag the block we are about to add is not a Normal Block
        CustomBlockPatches.isNormalBlock = false;
        owner.addBlock(blockType.currentAmount);

        //Update the power
        updatePowerDescription(owner);
    }

    public static ArrayList<AbstractCustomBlockType> blockTypes(AbstractCreature owner) {
        return BlockTypes.blockTypes.get(owner);
    }

    public static void reduceSpecificBlockType(AbstractCustomBlockType blockType, int amount) {
        int toRemove = Math.min(blockType.currentAmount, amount);
        CustomBlockPatches.specificBlockToReduce = blockType;
        blockType.owner.loseBlock(toRemove);
    }

    public static void removeSpecificBlockType(AbstractCustomBlockType blockType) {
        CustomBlockPatches.specificBlockToReduce = blockType;
        blockType.owner.loseBlock(blockType.currentAmount);
    }

    public static void removeEmptyBlockTypes(AbstractCreature owner) {
        CustomBlockManager.blockTypes(owner).removeIf(b -> b.currentAmount <= 0);
        if (BlockTypes.blockTypes.get(owner).stream().allMatch(b -> b instanceof BlockPlaceholder)) {
            removeAllCustomBlocks(owner);
        }
        updatePowerDescription(owner);
    }

    public static void removeAllCustomBlocks(AbstractCreature owner) {
        BlockTypes.blockTypes.get(owner).clear();
        updatePowerDescription(owner);
    }

    public static void updatePowerDescription(AbstractCreature owner) {
        if (owner.hasPower(BlockTipPower.POWER_ID)) {
            if (!hasCustomBlockType(owner)) {
                addToTop(new RemoveSpecificPowerAction(owner, owner, owner.getPower(BlockTipPower.POWER_ID)));
            } else {
                owner.getPower(BlockTipPower.POWER_ID).updateDescription();
            }
        }
    }

    public static int getBlockRetValBasedOnRemainingAmounts(AbstractCreature owner) {
        int ret = 0;
        for (AbstractCustomBlockType b : blockTypes(owner)) {
            ret += b.currentAmount;
        }
        return owner.currentBlock - ret;
    }

    public static void addBlockMakePlaceHolderIfNeeded(AbstractCreature owner, float amount) {
        //if we have a custom block up then we need to either add or stack a Block Placeholder when adding Normal Block
        if (hasCustomBlockType(owner)) {
            //Floor the amount
            int amt = MathUtils.floor(amount);
            //Set a flag so we don't stack AND add a new one
            boolean successfullyStacked = false;

            //Try looking for existing one to stack
            for (AbstractCustomBlockType b : blockTypes(owner)) {
                //If we find it, set the flag and stack
                if (b.getClass().equals(BlockPlaceholder.class)) {
                    //Increase the amount
                    b.currentAmount += amt;
                    //Set the flag
                    successfullyStacked = true;
                    break;
                }
            }

            //If we failed to stack we need a new PlaceHolder
            if (!successfullyStacked) {
                BlockTypes.blockTypes.get(owner).add(0, makeBlockPlaceholder(owner, owner.currentBlock));
            }

            //Update description
            updatePowerDescription(owner);
        }
    }

    public static BlockPlaceholder makeBlockPlaceholder(AbstractCreature owner, int amount) {
        return new BlockPlaceholder(owner, amount);
    }

    public static String makeTipDescription(AbstractCreature owner) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (AbstractCustomBlockType b : BlockTypes.blockTypes.get(owner)) {
            sb.append(b.currentAmount).append(" ").append(b.getName()).append(" - ").append(b.getDescription());
            if (++i < BlockTypes.blockTypes.get(owner).size()) {
                sb.append(" NL ");
            }
        }
        return sb.toString();
    }

    private static class BlockPlaceholder extends AbstractCustomBlockType {
        public BlockPlaceholder(AbstractCreature owner, int amount) {
            super(owner, amount);
        }

        @Override
        public String getName() {
            return TipHelper.capitalize(GameDictionary.BLOCK.NAMES[0]);
        }

        @Override
        public String getDescription() {
            return GameDictionary.keywords.get(GameDictionary.BLOCK.NAMES[0]);
        }
    }

    private static void addToTop(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToTop(action);
    }

    private static void addToBot(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }

    public static void atEndOfRound(AbstractCreature owner) {
        for (AbstractCustomBlockType b : blockTypes(owner)) {
            b.atEndOfRound();
        }
    }

    public static float atDamageReceive(AbstractCreature owner, float damage, DamageInfo.DamageType type) {
        for (AbstractCustomBlockType b : blockTypes(owner)) {
            damage = b.atDamageReceive(damage, type);
        }
        return damage;
    }

    public static float atDamageFinalReceive(AbstractCreature owner, float damage, DamageInfo.DamageType type) {
        for (AbstractCustomBlockType b : blockTypes(owner)) {
            damage = b.atDamageFinalReceive(damage, type);
        }
        return damage;
    }

    public static int onHeal(AbstractCreature owner, int healAmount) {
        for (AbstractCustomBlockType b : blockTypes(owner)) {
            healAmount = b.onHeal(healAmount);
        }
        return healAmount;
    }

    public static void onAttack(AbstractCreature owner, DamageInfo info, int damageAmount, AbstractCreature target) {
        for (AbstractCustomBlockType b : blockTypes(owner)) {
            b.onAttack(info, damageAmount, target);
        }
    }

    //TODO not manipulating damage taken. This is a design choice, but revisit later.
    public static void onAttacked(AbstractCreature owner, DamageInfo info, int damageAmount) {
        for (AbstractCustomBlockType b : blockTypes(owner)) {
            b.onAttacked(info, damageAmount);
        }
    }

    //TODO not manipulating damage taken. This is a design choice, but revisit later.
    public static int onAttackedPostBlockReductions(AbstractCreature owner, DamageInfo info, int damageAmount) {
        for (AbstractCustomBlockType b : blockTypes(owner)) {
            b.onAttackedPostBlockReductions(info, damageAmount);
        }
        return damageAmount;
    }

    public static void onCardDraw(AbstractCreature owner, AbstractCard card) {
        for (AbstractCustomBlockType b : blockTypes(owner)) {
            b.onCardDraw(card);
        }
    }

    public static void onPlayCard(AbstractCreature owner, AbstractCard card, AbstractMonster m) {
        for (AbstractCustomBlockType b : blockTypes(owner)) {
            b.onPlayCard(card, m);
        }
    }
}
