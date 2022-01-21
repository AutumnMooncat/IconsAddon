package IconsAddon.powers;

import IconsAddon.blockModifiers.AbstractBlockModifier;

import java.util.HashSet;

public interface OnCreateBlockContainerPower {
    void onCreateBlockContainer(HashSet<AbstractBlockModifier> blockTypes, Object instigator);
}
