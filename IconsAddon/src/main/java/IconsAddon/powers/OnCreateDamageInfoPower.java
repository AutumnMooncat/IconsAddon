package IconsAddon.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;

public interface OnCreateDamageInfoPower {
    void onCreateDamageInfo(DamageInfo info, AbstractCard instigatorCard);
}
