package IconsAddon.relics;

import IconsAddon.blockModifiers.AbstractBlockModifier;

import java.util.HashSet;

public interface OnCreateBlockInstanceRelic {
    void onCreateBlockInstance(HashSet<AbstractBlockModifier> blockTypes, Object instigator);
}
