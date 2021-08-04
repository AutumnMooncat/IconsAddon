package IconsAddon.cardmods;

import IconsAddon.icons.AbstractCustomIcon;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class AddIconToDescriptionMod extends AbstractCardModifier {
    public static final String DAMAGE = "!D!";
    public static final String BLOCK = "!B!";
    public static final String MAGIC = "!M!";

    final String searchString;
    final AbstractCustomIcon icon;

    public AddIconToDescriptionMod(String searchString, AbstractCustomIcon icon) {
        this.searchString = searchString;
        this.icon = icon;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription.replace(searchString, searchString+" "+icon.cardCode());
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new AddIconToDescriptionMod(searchString, icon);
    }
}
