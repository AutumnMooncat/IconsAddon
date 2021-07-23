package IconsAddon.damageModifiers;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public abstract class AbstractDamageModifier {
    public boolean ignoresBlock() {
        return false;
    }

    public boolean ignoresThorns() {
        return false;
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
        return damage;
    }

    public float atDamageFinalGive(float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
        return damage;
    }

    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {}

    public int onAttackToChangeDamage(DamageInfo info, int damageAmount, AbstractCreature target) {
        return damageAmount;
    }

    public void onDamageModifiedByBlock(DamageInfo info, int unblockedAmount, int blockedAmount, AbstractCreature target) {}

    public String getCardDescriptor() {
        return null;
    }

    public TooltipInfo getCustomTooltip() {
        return null;
    }

    public boolean removeWhenActivated() {
        return false;
    }
}
