package IconsAddon.powers;

import IconsAddon.damageModifiers.AbstractDamageModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;

import java.util.ArrayList;
import java.util.List;

public interface DamageModApplyingPower {
    /**
     * Called after this power contributes to binding damage to some damage info
     * @param info The damage into this power contributed to
     * @param cardMayBeNull The instigator card behind the creation of the damage info, if it was created by a card. May be null if the damage info was created by something other than a card.
     */
    void onAddedDamageModsToDamageInfo(DamageInfo info, AbstractCard cardMayBeNull);

    /**
     * Called before adding any mods to the final binding of damage, to see if it should render mod icons on the card, and to see if it should add the mods to card damage calculation
     * @param infoMayBeNull The damage info this may be contributing to. Will be null during the checks for rendering icons and card damage calculation.
     * @param cardMayBeNull The card this info may be adding mods to for card damage calculation. Will be null when binding damage if the damage was not created by a card.
     * @param activeDamageModifiers What modifiers already exist at this point. Can be used to ensure you don't accidentally add a modifier that already exists by performing a class check. Can not be null.
     * @return Whether you want to actually add any mods.
     */
    boolean shouldPushMods(DamageInfo infoMayBeNull, AbstractCard cardMayBeNull, List<AbstractDamageModifier> activeDamageModifiers);

    /**
     * Does not get called if shouldPushMods returns false
     * @param infoMayBeNull The damage info this may be contributing to. Will be null during the checks for rendering icons and card damage calculation.
     * @param cardMayBeNull The card this info may be adding mods to for card damage calculation. Will be null when binding damage if the damage was not created by a card.
     * @param activeDamageModifiers What modifiers already exist at this point. Can be used to ensure you don't accidentally add a modifier that already exists by performing a class check. Can not be null.
     * @return Returns a list of mods to add for damage binding, icon rendering, and damage calculation.
     */
    ArrayList<AbstractDamageModifier> modsToPush(DamageInfo infoMayBeNull, AbstractCard cardMayBeNull, List<AbstractDamageModifier> activeDamageModifiers);
}
