package IconsAddon.util;

import IconsAddon.damageModifiers.AbstractDamageModifier;
import IconsAddon.patches.PassTempModsViaActionPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.defect.DamageAllButOneEnemyAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.util.List;

public class DamageModifierHelper {

    public static Object bindingObject;

    public static DamageInfo makeBoundDamageInfo(Object objectWithDamageModifier, AbstractCreature damageSource, int base, DamageInfo.DamageType type) {
        bindingObject = objectWithDamageModifier;
        DamageInfo di = new DamageInfo(damageSource, base, type);
        bindingObject = null;
        return di;
    }

    public static DamageInfo makeBoundDamageInfo(Object objectWithDamageModifier, AbstractCreature damageSource, int base) {
        return makeBoundDamageInfo(objectWithDamageModifier, damageSource, base, DamageInfo.DamageType.NORMAL);
    }

    public static DamageInfo makeBoundDamageInfoFromArray(List<AbstractDamageModifier> mods, AbstractCreature damageSource, int base, DamageInfo.DamageType type) {
        bindingObject = new Object();
        DamageModifierManager.addModifiers(bindingObject, mods);
        DamageInfo di = new DamageInfo(damageSource, base, type);
        DamageModifierManager.removeAllModifiers(bindingObject);
        bindingObject = null;
        return di;
    }

    public static DamageInfo makeBoundDamageInfoFromArray(List<AbstractDamageModifier> mods, AbstractCreature damageSource, int base) {
        return makeBoundDamageInfoFromArray(mods, damageSource, base, DamageInfo.DamageType.NORMAL);
    }

    public static void bindAction(Object objectWithDamageModifier, AbstractGameAction action) {
        PassTempModsViaActionPatch.BoundGameAction.boundDamageObject.set(action, objectWithDamageModifier);
    }

    public static DamageAllEnemiesAction makeModifiedDamageAllEnemiesAction(Object objectWithDamageModifier, AbstractCreature source, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect, boolean isFast) {
        DamageAllEnemiesAction action = new DamageAllEnemiesAction(source, amount, type, effect, isFast);
        PassTempModsViaActionPatch.BoundGameAction.boundDamageObject.set(action, objectWithDamageModifier);
        return action;

    }

    public static DamageAllEnemiesAction makeModifiedDamageAllEnemiesAction(Object objectWithDamageModifier, AbstractCreature source, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect) {
        DamageAllEnemiesAction action = new DamageAllEnemiesAction(source, amount, type, effect);
        PassTempModsViaActionPatch.BoundGameAction.boundDamageObject.set(action, objectWithDamageModifier);
        return action;
    }

    public static DamageAllEnemiesAction makeModifiedDamageAllEnemiesAction(Object objectWithDamageModifier, AbstractPlayer player, int baseDamage, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect) {
        DamageAllEnemiesAction action = new DamageAllEnemiesAction(player, baseDamage, type, effect);
        PassTempModsViaActionPatch.BoundGameAction.boundDamageObject.set(action, objectWithDamageModifier);
        return action;
    }

    public static DamageAllButOneEnemyAction makeModifiedDamageAllButOneEnemyAction(Object objectWithDamageModifier, AbstractCreature source, AbstractCreature target, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect, boolean isFast) {
        DamageAllButOneEnemyAction action = new DamageAllButOneEnemyAction(source, target, amount, type, effect, isFast);
        PassTempModsViaActionPatch.BoundGameAction.boundDamageObject.set(action, objectWithDamageModifier);
        return action;
    }

    public static DamageAllButOneEnemyAction makeModifiedDamageAllButOneEnemyAction(Object objectWithDamageModifier, AbstractCreature source, AbstractCreature target, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect) {
        DamageAllButOneEnemyAction action = new DamageAllButOneEnemyAction(source, target, amount, type, effect);
        PassTempModsViaActionPatch.BoundGameAction.boundDamageObject.set(action, objectWithDamageModifier);
        return action;
    }

    public static VampireDamageAllEnemiesAction makeModifiedVampireDamageAllEnemiesAction(Object objectWithDamageModifier, AbstractCreature source, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect) {
        VampireDamageAllEnemiesAction action = new VampireDamageAllEnemiesAction(source, amount, type, effect);
        PassTempModsViaActionPatch.BoundGameAction.boundDamageObject.set(action, objectWithDamageModifier);
        return action;
    }

}
