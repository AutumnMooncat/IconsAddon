package IconsAddon.damageModifiers;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public abstract class AbstractDamageModifier implements Comparable<AbstractDamageModifier> {
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

    public ArrayList<TooltipInfo> getCustomTooltips() {
        return new ArrayList<>();
    }

    public boolean removeWhenActivated() {
        return false;
    }

    public int priority() {
        return 0;
    }

    protected void addToTop(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToTop(action);
    }

    protected void addToBot(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }

    @Override
    public int compareTo(AbstractDamageModifier other) {
        return this.priority() - other.priority();
    }
}
