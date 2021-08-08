package IconsAddon.powers;

import IconsAddon.blockModifiers.AbstractBlockModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.HashSet;

public interface OnCreateBlockContainerPower {
    void onCreateBlockContainer(HashSet<AbstractBlockModifier> blockTypes, AbstractCard instigatorCard);
}
