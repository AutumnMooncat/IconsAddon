package IconsAddon.patches;

import IconsAddon.blockModifiers.AbstractBlockModifier;
import IconsAddon.damageModifiers.AbstractDamageModifier;
import IconsAddon.powers.OnCreateBlockContainerPower;
import IconsAddon.powers.DamageModApplyingPower;
import IconsAddon.util.BlockContainer;
import IconsAddon.util.BlockModifierManager;
import IconsAddon.util.DamageModifierManager;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
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
import java.util.HashSet;
import java.util.stream.Collectors;

public class BindingPatches {

    public static final ArrayList<AbstractDamageModifier> directlyBoundDamageMods = new ArrayList<>();
    public static final ArrayList<AbstractBlockModifier> directlyBoundBlockMods = new ArrayList<>();
    public static Object directlyBoundInstigator;
    private static AbstractCard cardInUse;

    private static boolean canPassInstigator = true;

    @SpirePatch(clz = AbstractGameAction.class, method = SpirePatch.CLASS)
    public static class BoundGameAction {
        public static SpireField<Object> actionDelayedDirectlyBoundInstigator = new SpireField<>(() -> null);
        public static SpireField<AbstractCard> actionDelayedCardInUse = new SpireField<>(() -> null);
        public static SpireField<ArrayList<AbstractDamageModifier>> actionDelayedDamageMods = new SpireField<>(ArrayList::new);
        public static SpireField<ArrayList<AbstractBlockModifier>> actionDelayedBlockMods = new SpireField<>(ArrayList::new);
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class GrabCardInUse {
        @SpireInsertPatch(locator = Locator.class)
        public static void RememberCardPreUseCall(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse) {
            //Right before you call card.use, set it as the object in use
            cardInUse = c;
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "use");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
        @SpireInsertPatch(locator = Locator2.class)
        public static void ForgetCardPostUseCall(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse) {
            //Once you call card.use, set the object back to null, as any actions were already added to the queue
            cardInUse = null;
        }
        private static class Locator2 extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(GameActionManager.class, "addToBottom");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "addToTop")
    @SpirePatch(clz = GameActionManager.class, method = "addToBottom")
    public static class BindObjectToAction {
        @SpirePrefixPatch
        public static void WithoutCrashingHopefully(GameActionManager __instance, AbstractGameAction action) {
            //When our action is added to the queue, see if there is an active object in use that caused this to happen
            if (cardInUse != null && !(action instanceof ApplyPowerAction)) {
                //If so, this is our instigator object, we need to add any non-innate card mods
                BoundGameAction.actionDelayedCardInUse.set(action, cardInUse);
            }
            //Daisy chain our actions if we can
            AbstractGameAction a = AbstractDungeon.actionManager.currentAction;
            if (a != null && BoundGameAction.actionDelayedCardInUse.get(a) != null && canPassInstigator) {
                BoundGameAction.actionDelayedCardInUse.set(action, BoundGameAction.actionDelayedCardInUse.get(a));
            }
        }
    }

    @SpirePatch(clz = DamageInfo.class, method = "<ctor>", paramtypez = {AbstractCreature.class, int.class, DamageInfo.DamageType.class})
    private static class BindObjectToDamageInfo {

        private static final ArrayList<AbstractDamageModifier> boundMods = new ArrayList<>();

        @SpirePostfixPatch()
        public static void PostfixMeToPiggybackBinding(DamageInfo __instance, AbstractCreature damageSource, int base, DamageInfo.DamageType type) {
            AbstractCard instigatorCard = null;
            //Grab the action currently running, as this is what was processing when our damage info was created
            AbstractGameAction a = AbstractDungeon.actionManager.currentAction;
            if (a != null && canPassInstigator) {
                if (!BoundGameAction.actionDelayedDamageMods.get(a).isEmpty()) {
                    boundMods.addAll(BoundGameAction.actionDelayedDamageMods.get(a).stream().filter(m -> m.affectsDamageType(type)).collect(Collectors.toList()));
                    if (BoundGameAction.actionDelayedDirectlyBoundInstigator.get(a) instanceof AbstractCard) {
                        instigatorCard = BoundGameAction.actionDelayedCardInUse.get(a);
                    }
                }
                if (BoundGameAction.actionDelayedCardInUse.get(a) != null && a.source == damageSource) {
                    boundMods.addAll(DamageModifierManager.modifiers(BoundGameAction.actionDelayedCardInUse.get(a)).stream().filter(m -> m.automaticBindingForCards && m.affectsDamageType(type)).collect(Collectors.toList()));
                    instigatorCard = BoundGameAction.actionDelayedCardInUse.get(a);
                }
            }
            if (!directlyBoundDamageMods.isEmpty()) {
                boundMods.addAll(directlyBoundDamageMods.stream().filter(m -> m.affectsDamageType(type)).collect(Collectors.toList()));
                if (directlyBoundInstigator instanceof AbstractCard) {
                    instigatorCard = (AbstractCard) directlyBoundInstigator;
                }
            }
            if (cardInUse != null) {
                boundMods.addAll(DamageModifierManager.modifiers(cardInUse).stream().filter(m -> m.automaticBindingForCards && m.affectsDamageType(type)).collect(Collectors.toList()));
                instigatorCard = cardInUse;
            }
            if (damageSource != null) {
                for (AbstractPower p : damageSource.powers) {
                    if (p instanceof DamageModApplyingPower && ((DamageModApplyingPower) p).shouldPushMods(__instance, instigatorCard, boundMods)) {
                        boundMods.addAll(((DamageModApplyingPower) p).modsToPush(__instance, instigatorCard, boundMods));
                        ((DamageModApplyingPower) p).onAddedDamageModsToDamageInfo(__instance, instigatorCard);
                    }
                }
            }
            DamageModifierManager.bindDamageMods(__instance, boundMods);
            DamageModifierManager.bindInstigatorCard(__instance, instigatorCard);
            boundMods.clear();
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class AddTempModifiers {

        private static final ArrayList<AbstractDamageModifier> pushedMods = new ArrayList<>();
        private static final ArrayList<AbstractDamageModifier> inherentMods = new ArrayList<>();

        @SpirePrefixPatch()
        public static void addMods(AbstractCard __instance, AbstractMonster mo) {
            inherentMods.addAll(DamageModifierManager.modifiers(__instance));
            pushedMods.addAll(inherentMods);
            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof DamageModApplyingPower && ((DamageModApplyingPower) p).shouldPushMods(null, __instance, pushedMods)) {
                    pushedMods.addAll(((DamageModApplyingPower) p).modsToPush(null, __instance, pushedMods));
                }
            }
            pushedMods.removeAll(inherentMods);
            inherentMods.clear();
            DamageModifierManager.addModifiers(__instance, pushedMods);
        }

        @SpirePostfixPatch()
        public static void removeMods(AbstractCard __instance, AbstractMonster mo) {
            DamageModifierManager.removeModifiers(__instance, pushedMods);
            pushedMods.clear();
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "addBlock")
    public static class AddBlockMakePlaceHolderIfNeeded {
        static final HashSet<AbstractBlockModifier> blockSet = new HashSet<>();
        @SpireInsertPatch(locator = CreatureAddBlockLocator.class, localvars = "tmp")
        public static void pls(AbstractCreature __instance, int amount, float tmp) {
            AbstractCard instigatorCard = null;
            //Grab the action currently running, as this is what was processing when our block method was called
            AbstractGameAction a = AbstractDungeon.actionManager.currentAction;
            if (a != null) {
                //If the action is not null, see if it has an instigator object
                if (!BoundGameAction.actionDelayedBlockMods.get(a).isEmpty()) {
                    blockSet.addAll(BoundGameAction.actionDelayedBlockMods.get(a));
                }
                if (BoundGameAction.actionDelayedCardInUse.get(a) != null) {
                    for (AbstractBlockModifier m : BlockModifierManager.modifiers(BoundGameAction.actionDelayedCardInUse.get(a))) {
                        if (m.automaticBindingForCards) {
                            blockSet.add(m);
                        }
                    }
                    instigatorCard = BoundGameAction.actionDelayedCardInUse.get(a);
                }
            }
            if (!directlyBoundBlockMods.isEmpty()) {
                blockSet.addAll(directlyBoundBlockMods);
            }
            if (cardInUse != null) {
                for (AbstractBlockModifier m : BlockModifierManager.modifiers(cardInUse)) {
                    if (m.automaticBindingForCards) {
                        blockSet.add(m);
                    }
                }
                instigatorCard = cardInUse;
            }
            for (AbstractPower p : __instance.powers) {
                if (p instanceof OnCreateBlockContainerPower) {
                    ((OnCreateBlockContainerPower) p).onCreateBlockContainer(blockSet, instigatorCard);
                }
            }
            ArrayList<AbstractBlockModifier> blockTypes = new ArrayList<>();
            for (AbstractBlockModifier m : blockSet) {
                blockTypes.add(m.makeCopy());
            }
            blockSet.clear();
            Collections.sort(blockTypes);
            BlockContainer b = new BlockContainer(__instance, (int)tmp, blockTypes);
            BlockModifierManager.addBlockContainer(__instance, b);
        }
    }

    private static class CreatureAddBlockLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    @SpirePatch(clz = AbstractPlayer.class, method = "onCardDrawOrDiscard")
    @SpirePatch(clz = AbstractPlayer.class, method = "draw", paramtypez = int.class)
    @SpirePatch(clz = AbstractPlayer.class, method = "onVictory")
    @SpirePatch(clz = AbstractPlayer.class, method = "channelOrb")
    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    @SpirePatch(clz = AbstractMonster.class, method = "heal")
    @SpirePatch(clz = AbstractMonster.class, method = "die", paramtypez = boolean.class)
    @SpirePatch(clz = AbstractCreature.class, method = "heal", paramtypez = {int.class, boolean.class})
    @SpirePatch(clz = AbstractCreature.class, method = "addBlock")
    @SpirePatch(clz = AbstractCreature.class, method = "addPower")
    @SpirePatch(clz = AbstractCreature.class, method = "applyStartOfTurnPowers")
    @SpirePatch(clz = AbstractCreature.class, method = "applyTurnPowers")
    @SpirePatch(clz = AbstractCreature.class, method = "applyStartOfTurnPostDrawPowers")
    public static class DisableReactionaryActionBinding {
        @SpirePrefixPatch
        public static void disableBefore(AbstractCreature __instance) {
            canPassInstigator = false;
        }
        @SpirePostfixPatch
        public static void enableAfter(AbstractCreature __instance) {
            canPassInstigator = true;
        }
    }

}
