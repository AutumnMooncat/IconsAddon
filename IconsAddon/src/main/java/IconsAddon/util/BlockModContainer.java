package IconsAddon.util;

import IconsAddon.blockModifiers.AbstractBlockModifier;
import IconsAddon.damageModifiers.AbstractDamageModifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockModContainer {
    private final List<AbstractBlockModifier> damageModifiers;

    public BlockModContainer() {
        this(new ArrayList<>());
    }

    public BlockModContainer(AbstractBlockModifier mod) {
        this(new ArrayList<>(Collections.singletonList(mod)));
    }

    public BlockModContainer(List<AbstractBlockModifier> mods) {
        damageModifiers = mods;
        Collections.sort(damageModifiers);
    }

    public List<AbstractBlockModifier> modifiers() {
        return damageModifiers;
    }

    public void addModifier(AbstractBlockModifier damageMod) {
        damageModifiers.add(damageMod);
        Collections.sort(damageModifiers);
    }

    public void addModifiers(List<AbstractBlockModifier> damageMods) {
        damageModifiers.addAll(damageMods);
        Collections.sort(damageModifiers);
    }

    public void removeModifier(AbstractBlockModifier damageMod) {
        damageModifiers.remove(damageMod);
    }

    public void removeModifiers(ArrayList<AbstractBlockModifier> damageMods) {
        damageModifiers.removeAll(damageMods);
    }

    public void clearModifiers() {
        damageModifiers.clear();
    }
}
