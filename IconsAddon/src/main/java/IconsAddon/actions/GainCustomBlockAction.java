package IconsAddon.actions;

import IconsAddon.blockModifiers.AbstractBlockModifier;
import IconsAddon.util.BlockModContainer;
import IconsAddon.util.BlockModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.ArrayList;
import java.util.List;

public class GainCustomBlockAction extends AbstractGameAction {

    private final List<AbstractBlockModifier> mods;

    public GainCustomBlockAction(AbstractCard card, AbstractCreature target, int amount) {
        this.mods = BlockModifierManager.modifiers(card);
        this.target = target;
        this.amount = amount;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.startDuration = Settings.ACTION_DUR_XFAST;
    }

    public GainCustomBlockAction(BlockModContainer container, AbstractCreature target, int amount) {
        this.mods = container.modifiers();
        this.target = target;
        this.amount = amount;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.startDuration = Settings.ACTION_DUR_XFAST;
    }

    @Override
    public void update() {
        if (!this.target.isDying && !this.target.isDead && this.duration == this.startDuration) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.SHIELD));
            BlockModifierManager.addCustomBlock(target, mods, amount);

            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                c.applyPowers();
            }
        }
        tickDuration();
    }
}
