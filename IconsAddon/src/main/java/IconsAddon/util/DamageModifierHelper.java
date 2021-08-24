package IconsAddon.util;

import IconsAddon.damageModifiers.AbstractDamageModifier;
import IconsAddon.patches.BindingPatches;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.defect.DamageAllButOneEnemyAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.util.List;

public class DamageModifierHelper {

    public static DamageInfo makeBoundDamageInfo(Object objectWithDamageModifier, AbstractCreature damageSource, int base, DamageInfo.DamageType type) {
        BindingPatches.directlyBoundInstigator = objectWithDamageModifier;
        DamageInfo di = makeBoundDamageInfoFromArray(DamageModifierManager.modifiers(objectWithDamageModifier), damageSource, base, type);
        BindingPatches.directlyBoundInstigator = null;
        return di;
    }

    public static DamageInfo makeBoundDamageInfo(Object objectWithDamageModifier, AbstractCreature damageSource, int base) {
        return makeBoundDamageInfo(objectWithDamageModifier, damageSource, base, DamageInfo.DamageType.NORMAL);
    }

    public static DamageInfo makeBoundDamageInfoFromArray(List<AbstractDamageModifier> mods, AbstractCreature damageSource, int base, DamageInfo.DamageType type) {
        BindingPatches.directlyBoundDamageMods.addAll(mods);
        DamageInfo di = new DamageInfo(damageSource, base, type);
        BindingPatches.directlyBoundDamageMods.clear();
        return di;
    }

    public static DamageInfo makeBoundDamageInfoFromArray(List<AbstractDamageModifier> mods, AbstractCreature damageSource, int base) {
        return makeBoundDamageInfoFromArray(mods, damageSource, base, DamageInfo.DamageType.NORMAL);
    }

    public static void bindAction(Object objectWithDamageModifier, AbstractGameAction action) {
        BindingPatches.BoundGameAction.actionDelayedDirectlyBoundInstigator.set(action, objectWithDamageModifier);
        bindActionFromArray(DamageModifierManager.modifiers(objectWithDamageModifier), action);
    }

    public static void bindActionFromArray(List<AbstractDamageModifier> mods, AbstractGameAction action) {
        BindingPatches.BoundGameAction.actionDelayedDamageMods.get(action).addAll(mods);
    }

    public static DamageAllEnemiesAction makeModifiedDamageAllEnemiesAction(Object objectWithDamageModifier, AbstractCreature source, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect, boolean isFast) {
        DamageAllEnemiesAction action = new DamageAllEnemiesAction(source, amount, type, effect, isFast);
        bindAction(objectWithDamageModifier, action);
        return action;

    }

    public static DamageAllEnemiesAction makeModifiedDamageAllEnemiesAction(Object objectWithDamageModifier, AbstractCreature source, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect) {
        DamageAllEnemiesAction action = new DamageAllEnemiesAction(source, amount, type, effect);
        bindAction(objectWithDamageModifier, action);
        return action;
    }

    public static DamageAllEnemiesAction makeModifiedDamageAllEnemiesAction(Object objectWithDamageModifier, AbstractPlayer player, int baseDamage, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect) {
        DamageAllEnemiesAction action = new DamageAllEnemiesAction(player, baseDamage, type, effect);
        bindAction(objectWithDamageModifier, action);
        return action;
    }

    public static DamageAllButOneEnemyAction makeModifiedDamageAllButOneEnemyAction(Object objectWithDamageModifier, AbstractCreature source, AbstractCreature target, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect, boolean isFast) {
        DamageAllButOneEnemyAction action = new DamageAllButOneEnemyAction(source, target, amount, type, effect, isFast);
        bindAction(objectWithDamageModifier, action);
        return action;
    }

    public static DamageAllButOneEnemyAction makeModifiedDamageAllButOneEnemyAction(Object objectWithDamageModifier, AbstractCreature source, AbstractCreature target, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect) {
        DamageAllButOneEnemyAction action = new DamageAllButOneEnemyAction(source, target, amount, type, effect);
        bindAction(objectWithDamageModifier, action);
        return action;
    }

    public static VampireDamageAllEnemiesAction makeModifiedVampireDamageAllEnemiesAction(Object objectWithDamageModifier, AbstractCreature source, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect) {
        VampireDamageAllEnemiesAction action = new VampireDamageAllEnemiesAction(source, amount, type, effect);
        bindAction(objectWithDamageModifier, action);
        return action;
    }

}
