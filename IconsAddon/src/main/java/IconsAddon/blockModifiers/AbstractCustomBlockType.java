package IconsAddon.blockModifiers;

import IconsAddon.util.CustomBlockManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class AbstractCustomBlockType {
    public AbstractCreature owner;
    public int currentAmount;

    public AbstractCustomBlockType(AbstractCreature owner, int amount) {
        this.owner = owner;
        this.currentAmount = amount;
    }

    public int amountLostAtStartOfTurn() {
        return currentAmount;
    }

    public float onModifyBlock(float block, AbstractCard card) {
        return block;
    }

    public float onModifyBlockFinal(float block, AbstractCard card) {
        return block;
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
        return damage;
    }

    public float atDamageFinalGive(float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
        return damage;
    }

    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        return damage;
    }

    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        return damage;
    }

    public int onHeal(int healAmount) {
        return healAmount;
    }

    public void onStartOfTurnBlockLoss(int blockLost) {}

    public void onApplication() {}

    public void onStack(int amount) {}

    public void onRemove() {}

    public void onThisBlockDamaged(int lostAmount) {}

    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {}
    
    public void onAttacked(DamageInfo info, int damageAmount) {}

    public void onAttackedPostBlockReductions(DamageInfo info, int damageAmount) {}

    public void atEndOfRound() {}

    public void onCardDraw(AbstractCard card) {}

    public void onPlayCard(AbstractCard card, AbstractMonster m) {}

    public boolean shouldStack() {
        return true;
    }

    abstract public String getName();

    abstract public String getDescription();

    protected void addToTop(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToTop(action);
    }

    protected void addToBot(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }

    protected void reduceThisBlock(int amount) {
        CustomBlockManager.reduceSpecificBlockType(this, amount);
    }

    protected void removeThisBlock() {
        CustomBlockManager.removeSpecificBlockType(this);
    }

}
