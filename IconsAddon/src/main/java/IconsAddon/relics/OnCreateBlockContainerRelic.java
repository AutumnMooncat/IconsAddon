package IconsAddon.relics;

import IconsAddon.blockModifiers.AbstractBlockModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.HashSet;

public interface OnCreateBlockContainerRelic {
    void onCreateBlockContainer(HashSet<AbstractBlockModifier> blockTypes, AbstractCard instigatorCard);
}
