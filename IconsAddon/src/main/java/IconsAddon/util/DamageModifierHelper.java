package IconsAddon.util;

import IconsAddon.powers.OnCreateDamageInfoPower;
import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.defect.DamageAllButOneEnemyAction;
import com.megacrit.cardcrawl.actions.unique.DamagePerAttackPlayedAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.function.Consumer;

public class DamageModifierHelper {

    private static Object bindingObject;

    public static DamageInfo makeBoundDamageInfo(Object objectWithDamageModifier, AbstractCreature damageSource, int base, DamageInfo.DamageType type) {
        bindingObject = objectWithDamageModifier;
        DamageInfo di = new DamageInfo(damageSource, base, type);
        bindingObject = null;
        return di;
    }

    public static DamageInfo makeBoundDamageInfo(Object objectWithDamageModifier, AbstractCreature damageSource, int base) {
        return makeBoundDamageInfo(objectWithDamageModifier, damageSource, base, DamageInfo.DamageType.NORMAL);
    }

    public static void bindDamageInfo(Object objectWithDamageModifier, DamageInfo info) {
        DamageModifierManager.spliceBoundObject(info, objectWithDamageModifier);
    }

    public static void bindAction(Object objectWithDamageModifier, AbstractGameAction action) {
        DamageModifierManager.BoundGameAction.boundObject.set(action, objectWithDamageModifier);
    }

    public static DamageAllEnemiesAction makeModifiedDamageAllEnemiesAction(Object objectWithDamageModifier, AbstractCreature source, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect, boolean isFast) {
        DamageAllEnemiesAction action = new DamageAllEnemiesAction(source, amount, type, effect, isFast);
        DamageModifierManager.BoundGameAction.boundObject.set(action, objectWithDamageModifier);
        return action;

    }

    public static DamageAllEnemiesAction makeModifiedDamageAllEnemiesAction(Object objectWithDamageModifier, AbstractCreature source, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect) {
        DamageAllEnemiesAction action = new DamageAllEnemiesAction(source, amount, type, effect);
        DamageModifierManager.BoundGameAction.boundObject.set(action, objectWithDamageModifier);
        return action;
    }

    public static DamageAllEnemiesAction makeModifiedDamageAllEnemiesAction(Object objectWithDamageModifier, AbstractPlayer player, int baseDamage, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect) {
        DamageAllEnemiesAction action = new DamageAllEnemiesAction(player, baseDamage, type, effect);
        DamageModifierManager.BoundGameAction.boundObject.set(action, objectWithDamageModifier);
        return action;
    }

    public static DamageAllButOneEnemyAction makeModifiedDamageAllButOneEnemyAction(Object objectWithDamageModifier, AbstractCreature source, AbstractCreature target, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect, boolean isFast) {
        DamageAllButOneEnemyAction action = new DamageAllButOneEnemyAction(source, target, amount, type, effect, isFast);
        DamageModifierManager.BoundGameAction.boundObject.set(action, objectWithDamageModifier);
        return action;
    }

    public static DamageAllButOneEnemyAction makeModifiedDamageAllButOneEnemyAction(Object objectWithDamageModifier, AbstractCreature source, AbstractCreature target, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect) {
        DamageAllButOneEnemyAction action = new DamageAllButOneEnemyAction(source, target, amount, type, effect);
        DamageModifierManager.BoundGameAction.boundObject.set(action, objectWithDamageModifier);
        return action;
    }

    public static VampireDamageAllEnemiesAction makeModifiedVampireDamageAllEnemiesAction(Object objectWithDamageModifier, AbstractCreature source, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect) {
        VampireDamageAllEnemiesAction action = new VampireDamageAllEnemiesAction(source, amount, type, effect);
        DamageModifierManager.BoundGameAction.boundObject.set(action, objectWithDamageModifier);
        return action;
    }

    @SpirePatch(clz = DamageInfo.class, method = "<ctor>", paramtypez = {AbstractCreature.class, int.class, DamageInfo.DamageType.class})
    private static class BindObjectToDamageInfo {
        @SpirePostfixPatch()
        public static void PostfixMeToPiggybackBinding(DamageInfo __instance, AbstractCreature damageSource, int base, DamageInfo.DamageType type) {
            //Grab the action currently running, as this is what was processing when our damage info was created
            AbstractGameAction a = AbstractDungeon.actionManager.currentAction;
            if (a != null) {
                //If the action is not null, see if it has an instigator object
                Object o = DamageModifierManager.BoundGameAction.boundObject.get(a);
                if (o != null) {
                    //If so, this is our bound object to grab DamageMods off
                    DamageModifierManager.spliceBoundObject(__instance, o);
                }
            }
            if (bindingObject != null) {
                DamageModifierManager.spliceBoundObject(__instance, bindingObject);
            }
            for (AbstractPower p : damageSource.powers) {
                if (p instanceof OnCreateDamageInfoPower) {
                    ((OnCreateDamageInfoPower) p).onCreateDamageInfo(__instance);
                }
            }
        }
    }

}
