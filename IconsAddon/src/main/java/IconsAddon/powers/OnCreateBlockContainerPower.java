package IconsAddon.powers;

import IconsAddon.blockModifiers.AbstractBlockModifier;

import java.util.ArrayList;

public interface OnCreateBlockContainerPower {
    void onCreateBlockContainer(ArrayList<AbstractBlockModifier> blockTypes);
}
