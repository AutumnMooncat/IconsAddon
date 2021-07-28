package IconsAddon.util;

import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
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

import java.util.function.Consumer;

public class DamageModifierHelper {

    public static DamageInfo makeBoundDamageInfo(Object objectWithDamageModifier, AbstractCreature damageSource, int base, DamageInfo.DamageType type) {
        DamageInfo di = new DamageInfo(damageSource, base, type);
        DamageModifierManager.spliceBoundObject(di, objectWithDamageModifier);
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

    public static DamageAction makeModifiedDamageAction(Object objectWithDamageModifier, AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect) {
        DamageModifierManager.spliceBoundObject(info, objectWithDamageModifier);
        return new DamageAction(target, info, effect);
    }

    public static DamageAction makeModifiedDamageAction(Object objectWithDamageModifier, AbstractCreature target, DamageInfo info, int stealGoldAmount) {
        DamageModifierManager.spliceBoundObject(info, objectWithDamageModifier);
        return new DamageAction(target, info, stealGoldAmount);
    }

    public static DamageAction makeModifiedDamageAction(Object objectWithDamageModifier, AbstractCreature target, DamageInfo info) {
        DamageModifierManager.spliceBoundObject(info, objectWithDamageModifier);
        return new DamageAction(target, info);
    }

    public static DamageAction makeModifiedDamageAction(Object objectWithDamageModifier, AbstractCreature target, DamageInfo info, boolean superFast) {
        DamageModifierManager.spliceBoundObject(info, objectWithDamageModifier);
        return new DamageAction(target, info, superFast);
    }

    public static DamageAction makeModifiedDamageAction(Object objectWithDamageModifier, AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect, boolean superFast) {
        DamageModifierManager.spliceBoundObject(info, objectWithDamageModifier);
        return new DamageAction(target, info, effect, superFast);
    }

    public static DamageAction makeModifiedDamageAction(Object objectWithDamageModifier, AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect, boolean superFast, boolean muteSfx) {
        DamageModifierManager.spliceBoundObject(info, objectWithDamageModifier);
        return new DamageAction(target, info, effect, superFast, muteSfx);
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

    public static DamageRandomEnemyAction makeModifiedDamageRandomEnemyAction(Object objectWithDamageModifier, DamageInfo info, AbstractGameAction.AttackEffect effect) {
        DamageModifierManager.spliceBoundObject(info, objectWithDamageModifier);
        return new DamageRandomEnemyAction(info, effect);
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

    public static DamagePerAttackPlayedAction makeModifiedDamagePerAttackPlayedAction(Object objectWithDamageModifier, AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect) {
        DamageModifierManager.spliceBoundObject(info, objectWithDamageModifier);
        return new DamagePerAttackPlayedAction(target, info, effect);
    }

    public static DamagePerAttackPlayedAction makeModifiedDamagePerAttackPlayedAction(Object objectWithDamageModifier, AbstractCreature target, DamageInfo info) {
        DamageModifierManager.spliceBoundObject(info, objectWithDamageModifier);
        return new DamagePerAttackPlayedAction(target, info);
    }

    public static DamageCallbackAction makeModifiedDamageCallbackAction(Object objectWithDamageModifier, AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect, Consumer<Integer> callback) {
        DamageModifierManager.spliceBoundObject(info, objectWithDamageModifier);
        return new DamageCallbackAction(target, info, effect, callback);
    }

    public static VampireDamageAction makeModifiedVampireDamageAction(Object objectWithDamageModifier, AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect) {
        DamageModifierManager.spliceBoundObject(info, objectWithDamageModifier);
        return new VampireDamageAction(target, info, effect);
    }

    public static VampireDamageAllEnemiesAction makeModifiedVampireDamageAllEnemiesAction(Object objectWithDamageModifier, AbstractCreature source, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect) {
        VampireDamageAllEnemiesAction action = new VampireDamageAllEnemiesAction(source, amount, type, effect);
        DamageModifierManager.BoundGameAction.boundObject.set(action, objectWithDamageModifier);
        return action;
    }

}
