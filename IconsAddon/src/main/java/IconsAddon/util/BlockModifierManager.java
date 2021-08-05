package IconsAddon.util;

import IconsAddon.blockModifiers.AbstractBlockModifier;
import IconsAddon.patches.BlockModifierPatches;
import IconsAddon.powers.BlockTipPower;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class BlockModifierManager {
    private static final HashMap<Object, ArrayList<AbstractBlockModifier>> boundBlockObjects = new HashMap<>();

    @SpirePatch(clz = AbstractCreature.class, method = SpirePatch.CLASS)
    private static class BlockTypes {
        public static SpireField<ArrayList<BlockContainer>> blockContainers = new SpireField<>(ArrayList::new);
    }

    @SpirePatch(clz = AbstractGameAction.class, method = SpirePatch.CLASS)
    public static class BoundGameAction {
        public static SpireField<Object> boundBlockObject = new SpireField<>(() -> null);
    }

    public static void addBlockContainer(AbstractCreature owner, BlockContainer container) {
        boolean stacked = false;
        for (BlockContainer b : BlockTypes.blockContainers.get(owner)) {
            if (b.containsSameBlockTypes(container) && b.shouldStack() && container.shouldStack()) {
                b.setBlockAmount(b.getBlockAmount()+container.getBlockAmount());
                for (AbstractBlockModifier t : b.getBlockTypes()) {
                    t.onStack(container.getBlockAmount());
                }
                stacked = true;
            }
        }
        if (!stacked) {
            for (AbstractBlockModifier t : container.getBlockTypes()) {
                t.setOwner(owner);
                t.onApplication();
            }
            BlockTypes.blockContainers.get(owner).add(0, container);
            Collections.sort(BlockTypes.blockContainers.get(owner));
        }
    }

    public static void addModifier(Object object, AbstractBlockModifier blockMod) {
        if (!boundBlockObjects.containsKey(object)) {
            boundBlockObjects.put(object, new ArrayList<>());
        }
        boundBlockObjects.get(object).add(blockMod);
        Collections.sort(boundBlockObjects.get(object));
    }

    public static void addModifiers(Object object, ArrayList<AbstractBlockModifier> blockMods) {
        if (!boundBlockObjects.containsKey(object)) {
            boundBlockObjects.put(object, new ArrayList<>());
        }
        boundBlockObjects.get(object).addAll(blockMods);
        Collections.sort(boundBlockObjects.get(object));
    }

    public static ArrayList<AbstractBlockModifier> modifiers(Object object) {
        return boundBlockObjects.getOrDefault(object, new ArrayList<>());
    }

    public static void removeModifier(Object object, AbstractBlockModifier blockMod) {
        if (boundBlockObjects.containsKey(object)) {
            boundBlockObjects.get(object).remove(blockMod);
            Collections.sort(boundBlockObjects.get(object));
        }
    }

    public static void removeModifiers(Object object, ArrayList<AbstractBlockModifier> blockMods) {
        if (boundBlockObjects.containsKey(object)) {
            boundBlockObjects.get(object).removeAll(blockMods);
            Collections.sort(boundBlockObjects.get(object));
        }
    }

    public static void removeAllModifiers(Object object) {
        boundBlockObjects.remove(object);
    }

    public static BlockContainer getTopBlockContainer(AbstractCreature owner) {
        return BlockTypes.blockContainers.get(owner).get(0);
    }

    public static boolean hasCustomBlockType(AbstractCreature owner) {
        return !BlockTypes.blockContainers.get(owner).isEmpty();
    }

    public static void addCustomBlock(AbstractCreature owner, Object objectWithBlockMods, int amount) {
        BlockModifierPatches.boundObject = objectWithBlockMods;
        owner.addBlock(amount);
    }

    public static ArrayList<BlockContainer> blockContainers(AbstractCreature owner) {
        return BlockTypes.blockContainers.get(owner);
    }

    public static void reduceSpecificBlockType(BlockContainer container, int amount) {
        int toRemove = Math.min(container.getBlockAmount(), amount);
        BlockModifierPatches.specificContainerToReduce = container;
        container.getOwner().loseBlock(toRemove);
    }

    public static void removeSpecificBlockType(BlockContainer container) {
        BlockModifierPatches.specificContainerToReduce = container;
        container.getOwner().loseBlock(container.getBlockAmount());
    }

    public static void removeEmptyBlockContainers(AbstractCreature owner) {
        BlockModifierManager.blockContainers(owner).removeIf(b -> b.getBlockAmount() <= 0);
    }

    public static void removeAllBlockContainers(AbstractCreature owner) {
        BlockTypes.blockContainers.get(owner).clear();
    }

    public static int getBlockRetValBasedOnRemainingAmounts(AbstractCreature owner) {
        int ret = 0;
        for (BlockContainer b : blockContainers(owner)) {
            ret += b.getBlockAmount();
        }
        return owner.currentBlock - ret;
    }

    private static void addToTop(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToTop(action);
    }

    private static void addToBot(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }

    public static void atEndOfRound(AbstractCreature owner) {
        for (BlockContainer b : blockContainers(owner)) {
            for (AbstractBlockModifier t : b.getBlockTypes()) {
                t.atEndOfRound();
            }
        }
    }

    public static float atDamageReceive(AbstractCreature owner, float damage, DamageInfo.DamageType type, AbstractCreature source) {
        for (BlockContainer b : blockContainers(owner)) {
            for (AbstractBlockModifier t : b.getBlockTypes()) {
                damage = t.atDamageReceive(damage, type, source);
            }
        }
        return damage;
    }

    public static float atDamageFinalReceive(AbstractCreature owner, float damage, DamageInfo.DamageType type, AbstractCreature source) {
        for (BlockContainer b : blockContainers(owner)) {
            for (AbstractBlockModifier t : b.getBlockTypes()) {
                damage = t.atDamageFinalReceive(damage, type, source);
            }
        }
        return damage;
    }

    public static float atDamageGive(AbstractCreature owner, float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
        for (BlockContainer b : blockContainers(owner)) {
            for (AbstractBlockModifier t : b.getBlockTypes()) {
                damage = t.atDamageGive(damage, type, target, card);
            }
        }
        return damage;
    }

    public static float atDamageFinalGive(AbstractCreature owner, float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
        for (BlockContainer b : blockContainers(owner)) {
            for (AbstractBlockModifier t : b.getBlockTypes()) {
                damage = t.atDamageFinalGive(damage, type, target, card);
            }
        }
        return damage;
    }

    public static int onHeal(AbstractCreature owner, int healAmount) {
        for (BlockContainer b : blockContainers(owner)) {
            for (AbstractBlockModifier t : b.getBlockTypes()) {
                healAmount = t.onHeal(healAmount);
            }
        }
        return healAmount;
    }

    public static void onAttack(AbstractCreature owner, DamageInfo info, int damageAmount, AbstractCreature target) {
        for (BlockContainer b : blockContainers(owner)) {
            for (AbstractBlockModifier t : b.getBlockTypes()) {
                t.onAttack(info, damageAmount, target);
            }
        }
    }

    //TODO not manipulating damage taken. This is a design choice, but revisit later.
    public static void onAttacked(AbstractCreature owner, DamageInfo info, int damageAmount) {
        for (BlockContainer b : blockContainers(owner)) {
            for (AbstractBlockModifier t : b.getBlockTypes()) {
                t.onAttacked(info, damageAmount);
            }
        }
    }

    //TODO not manipulating damage taken. This is a design choice, but revisit later.
    public static int onAttackedPostBlockReductions(AbstractCreature owner, DamageInfo info, int damageAmount) {
        for (BlockContainer b : blockContainers(owner)) {
            for (AbstractBlockModifier t : b.getBlockTypes()) {
                t.onAttackedPostBlockReductions(info, damageAmount);
            }
        }
        return damageAmount;
    }

    public static void onCardDraw(AbstractCreature owner, AbstractCard card) {
        for (BlockContainer b : blockContainers(owner)) {
            for (AbstractBlockModifier t : b.getBlockTypes()) {
                t.onCardDraw(card);
            }
        }
    }

    public static void onPlayCard(AbstractCreature owner, AbstractCard card, AbstractMonster m) {
        for (BlockContainer b : blockContainers(owner)) {
            for (AbstractBlockModifier t : b.getBlockTypes()) {
                t.onPlayCard(card, m);
            }
        }
    }

    public static boolean onApplyPower(AbstractCreature owner, AbstractPower power, AbstractCreature target, AbstractCreature source) {
        boolean retVal = true;
        for (BlockContainer b : blockContainers(owner)) {
            for (AbstractBlockModifier t : b.getBlockTypes()) {
                retVal &= t.onApplyPower(power, target, source);
            }
        }
        return retVal;
    }

    public static int onApplyPowerStacks(AbstractCreature owner, AbstractPower power, AbstractCreature target, AbstractCreature source, int stackAmount) {
        for (BlockContainer b : blockContainers(owner)) {
            for (AbstractBlockModifier t : b.getBlockTypes()) {
                stackAmount = t.onApplyPowerStacks(power, target, source, stackAmount);
            }
        }
        return stackAmount;
    }
}
