package IconsAddon.actions;

import IconsAddon.util.BlockModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class GainCustomBlockAction extends AbstractGameAction {

    private final Object o;

    public GainCustomBlockAction(Object objectWithBlockMods, AbstractCreature target, int amount) {
        this.o = objectWithBlockMods;
        this.target = target;
        this.amount = amount;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.startDuration = Settings.ACTION_DUR_XFAST;
    }

    @Override
    public void update() {
        if (!this.target.isDying && !this.target.isDead && this.duration == this.startDuration) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.SHIELD));
            BlockModifierManager.addCustomBlock(target, o, amount);
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                c.applyPowers();
            }
        }
        tickDuration();
    }
}
