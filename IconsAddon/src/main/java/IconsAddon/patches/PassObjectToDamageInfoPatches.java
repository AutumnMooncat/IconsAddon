package IconsAddon.patches;

import IconsAddon.util.DamageModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

public class PassObjectToDamageInfoPatches {

    /*
    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class RememberCardPreUseCall {
        @SpireInsertPatch(locator = Locator.class)
        public static void removePowerListener(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse) {
            objectInUse = c;
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
            objectInUse = null;
        }
        private static class Locator2 extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(CardGroup.class, "removeCard");
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
            if (objectInUse != null) {
                //If so, this is our instigator object
                DamageModifierManager.BoundGameAction.boundObject.set(action, objectInUse);
            } else {
                //Grab the current action, as a different action could have added this one to queue
                AbstractGameAction a = AbstractDungeon.actionManager.currentAction;
                if (a != null) {
                    //If the current action is not null, see if it has an instigator object
                    Object o = DamageModifierManager.BoundGameAction.boundObject.get(a);
                    if (o != null) {
                        //If so, that instigator is our instigator for the new action
                        DamageModifierManager.BoundGameAction.boundObject.set(action, o);
                    }
                }
            }
        }
    }*/

    @SpirePatch(clz = DamageInfo.class, method = "<ctor>", paramtypez = {AbstractCreature.class, int.class, DamageInfo.DamageType.class})
    public static class BindObjectToDamageInfo {
        @SpirePrefixPatch()
        public static void Pls(DamageInfo __instance, AbstractCreature damageSource, int base, DamageInfo.DamageType type) {
            //Grab the action currently running, as this is what was processing when our damage info was created
            AbstractGameAction a = AbstractDungeon.actionManager.currentAction;
            if (a != null) {
                //If the action is not null, see if it has an instigator object
                Object o = DamageModifierManager.BoundGameAction.boundObject.get(a);
                if (o != null) {
                    //If so, this is our bound object to grab DamageMods off
                    DamageModifierManager.BoundDamageInfo.splice(__instance, o);
                }
            }
        }
    }
}
