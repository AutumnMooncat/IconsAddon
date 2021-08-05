package IconsAddon.patches;

import IconsAddon.blockModifiers.AbstractBlockModifier;
import IconsAddon.damageModifiers.AbstractDamageModifier;
import IconsAddon.util.BlockModifierManager;
import IconsAddon.util.DamageModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

public class PassTempModsViaActionPatch {

    private static Object cardInUse;

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
                    if (!mod.isInnate()) {
                        DamageModifierManager.addModifier(o, mod);
                    }
                }
                for (AbstractBlockModifier mod : BlockModifierManager.modifiers(cardInUse)) {
                    if (!mod.isInnate()) {
                        BlockModifierManager.addModifier(o, mod);
                    }
                }
                DamageModifierManager.BoundGameAction.boundDamageObject.set(action, o);
                BlockModifierManager.BoundGameAction.boundBlockObject.set(action, o);
            }
        }
    }
}
