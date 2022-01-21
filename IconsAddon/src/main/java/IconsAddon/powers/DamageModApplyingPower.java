package IconsAddon.powers;

import IconsAddon.damageModifiers.AbstractDamageModifier;
import com.megacrit.cardcrawl.cards.DamageInfo;

import java.util.ArrayList;
import java.util.List;

public interface DamageModApplyingPower {

    void onAddedDamageModsToDamageInfo(DamageInfo info, Object instigator);

    boolean shouldPushMods(DamageInfo infoMayBeNull, Object instigator, List<AbstractDamageModifier> activeDamageModifiers);

    ArrayList<AbstractDamageModifier> modsToPush(DamageInfo infoMayBeNull, Object instigator, List<AbstractDamageModifier> activeDamageModifiers);
}
