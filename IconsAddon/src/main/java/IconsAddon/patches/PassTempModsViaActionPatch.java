package IconsAddon.patches;

import IconsAddon.blockModifiers.AbstractBlockModifier;
import IconsAddon.damageModifiers.AbstractDamageModifier;
import IconsAddon.powers.OnCreateDamageInfoPower;
import IconsAddon.util.BlockModifierManager;
import IconsAddon.util.DamageModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

import java.util.stream.Collectors;

import static IconsAddon.util.DamageModifierHelper.bindingObject;

public class PassTempModsViaActionPatch {

    private static Object cardInUse;

    @SpirePatch(clz = AbstractGameAction.class, method = SpirePatch.CLASS)
    public static class BoundGameAction {
        public static SpireField<Object> boundDamageObject = new SpireField<>(() -> null);
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
            if (cardInUse != null) {
                //If so, this is our instigator object, we need to add any non-innate card mods
                Object o = new Object();
                for (AbstractDamageModifier mod : DamageModifierManager.modifiers(cardInUse)) {
                    if (mod.automaticBindingForCards) {
                        DamageModifierManager.addModifier(o, mod);
                    }
                }
                for (AbstractBlockModifier mod : BlockModifierManager.modifiers(cardInUse)) {
                    if (!mod.isInnate()) {
                        BlockModifierManager.addModifier(o, mod);
                    }
                }
                BoundGameAction.boundDamageObject.set(action, o);
                BlockModifierManager.BoundGameAction.boundBlockObject.set(action, o);
            }
        }
    }

    @SpirePatch(clz = DamageInfo.class, method = "<ctor>", paramtypez = {AbstractCreature.class, int.class, DamageInfo.DamageType.class})
    private static class BindObjectToDamageInfo {
        @SpirePostfixPatch()
        public static void PostfixMeToPiggybackBinding(DamageInfo __instance, AbstractCreature damageSource, int base, DamageInfo.DamageType type) {
            AbstractCard instigatorCard = null;
            //Grab the action currently running, as this is what was processing when our damage info was created
            AbstractGameAction a = AbstractDungeon.actionManager.currentAction;
            if (a != null) {
                if (BoundGameAction.boundDamageObject.get(a) != null) {
                    DamageModifierManager.bindDamageMods(__instance, DamageModifierManager.modifiers(BoundGameAction.boundDamageObject.get(a)));
                    if (BoundGameAction.boundDamageObject.get(a) instanceof AbstractCard) {
                        instigatorCard = (AbstractCard) BoundGameAction.boundDamageObject.get(a);
                    }
                }
            }
            if (bindingObject != null) {
                DamageModifierManager.bindDamageMods(__instance, DamageModifierManager.modifiers(bindingObject));
                if (bindingObject instanceof AbstractCard) {
                    instigatorCard = (AbstractCard) bindingObject;
                }
            }
            if (cardInUse != null) {
                DamageModifierManager.bindDamageMods(__instance, DamageModifierManager.modifiers(cardInUse).stream().filter(m -> m.automaticBindingForCards).collect(Collectors.toList()));
                if (cardInUse instanceof AbstractCard) {
                    instigatorCard = (AbstractCard) cardInUse;
                }
            }
            if (damageSource != null) {
                for (AbstractPower p : damageSource.powers) {
                    if (p instanceof OnCreateDamageInfoPower) {
                        ((OnCreateDamageInfoPower) p).onCreateDamageInfo(__instance, instigatorCard);
                    }
                }
            }
            DamageModifierManager.bindInstigatorCard(__instance, instigatorCard);
        }
    }
}
