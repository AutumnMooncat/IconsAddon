package IconsAddon.util;

import IconsAddon.IconsAddonMod;
import IconsAddon.blockModifiers.AbstractBlockModifier;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

public class BlockContainer implements Comparable<BlockContainer> {
    private static final String ID = IconsAddonMod.makeID("BlockContainer");
    private static final UIStrings strings = CardCrawlGame.languagePack.getUIString(ID);
    private final ArrayList<AbstractBlockModifier> containedBlockTypes;
    private int blockAmount;
    private final AbstractCreature owner;
    private Color blockColor;
    private Color textColor;
    private Texture blockImage = ImageMaster.BLOCK_ICON;

    public BlockContainer(AbstractCreature owner, int blockAmount, ArrayList<AbstractBlockModifier> blockTypes) {
        this.owner = owner;
        this.blockAmount = blockAmount;
        this.containedBlockTypes = blockTypes;
        for (AbstractBlockModifier m : containedBlockTypes) {
            m.setContainer(this);
            if (m.blockImageColor() != null) {
                blockColor = m.blockImageColor();
            }
            if (m.blockTextColor() != null) {
                textColor = m.blockTextColor();
            }
            if (m.customBlockImage() != null) {
                blockImage = m.customBlockImage();
            }
        }
    }

    public AbstractCreature getOwner() {
        return owner;
    }

    public int getBlockAmount() {
        return blockAmount;
    }

    public void setBlockAmount(int blockAmount) {
        this.blockAmount = blockAmount;
    }

    public ArrayList<AbstractBlockModifier> getBlockTypes() {
        return containedBlockTypes;
    }

    public boolean defaultBlock() {
        return containedBlockTypes.size() == 0;
    }

    public int computeStartTurnBlockLoss() {
        int ret = blockAmount;
        for (AbstractBlockModifier t : containedBlockTypes) {
            if (t.amountLostAtStartOfTurn() < ret) {
                ret = t.amountLostAtStartOfTurn();
            }
        }
        return ret;
    }

    public String makeName() {
        StringBuilder sb = new StringBuilder();
        sb.append(blockAmount).append(" ");
        if (defaultBlock()) {
            sb.append(TipHelper.capitalize(GameDictionary.BLOCK.NAMES[0]));
        } else if (containedBlockTypes.size() == 1) {
            sb.append(containedBlockTypes.get(0).getName());
        } else
        {
            sb.append(strings.TEXT[0]);
        }
        return sb.toString();
    }

    public String makeDescription() {
        StringBuilder sb = new StringBuilder();
        if (defaultBlock()) {
            sb.append(GameDictionary.BLOCK.DESCRIPTION);
        } else if (containedBlockTypes.size() == 1) {
            sb.append(containedBlockTypes.get(0).getDescription());
        } else {
            int i = 0;
            for (AbstractBlockModifier b : containedBlockTypes) {
                sb.append(b.getName()).append(" - ").append(b.getDescription());
                if (++i < containedBlockTypes.size()) {
                    sb.append(" NL ");
                }
            }
        }
        return sb.toString();
    }

    public int computePriority() {
        int ret = 0;
        for (AbstractBlockModifier t : containedBlockTypes) {
            if (t.priority() == AbstractBlockModifier.Priority.TOP) {
                ret--;
            } else if (t.priority() == AbstractBlockModifier.Priority.BOTTOM) {
                ret++;
            }
        }
        return ret;
    }

    public boolean containsSameBlockTypes(BlockContainer b) {
        ArrayList<Class<?>> comp = new ArrayList<>();
        for (AbstractBlockModifier t : containedBlockTypes) {
            comp.add(t.getClass());
        }
        for (AbstractBlockModifier t : b.containedBlockTypes) {
            if (!comp.remove(t.getClass())) {
                return false;
            }
        }
        return comp.isEmpty();
    }

    public boolean shouldStack() {
        boolean ret = true;
        for (AbstractBlockModifier t : containedBlockTypes) {
            ret &= t.shouldStack();
        }
        return ret;
    }

    public Texture getBlockImage() {
        return blockImage;
    }

    public Color getBlockColor() {
        return blockColor;
    }

    public Color getTextColor() {
        return textColor;
    }

    @Override
    public int compareTo(BlockContainer o) {
        return this.computePriority() - o.computePriority();
    }
}
