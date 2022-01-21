package IconsAddon.relics;

import IconsAddon.blockModifiers.AbstractBlockModifier;

import java.util.HashSet;

public interface OnCreateBlockContainerRelic {
    void onCreateBlockContainer(HashSet<AbstractBlockModifier> blockTypes, Object instigator);
}
