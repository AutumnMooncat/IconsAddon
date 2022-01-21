package IconsAddon.powers;

import IconsAddon.blockModifiers.AbstractBlockModifier;

import java.util.HashSet;

public interface OnCreateBlockInstancePower {
    void onCreateBlockInstance(HashSet<AbstractBlockModifier> blockTypes, Object instigator);
}
