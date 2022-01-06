package IconsAddon.util;

import IconsAddon.damageModifiers.AbstractDamageModifier;

import java.util.*;

public class DamageModContainer {
    private final List<AbstractDamageModifier> damageModifiers;

    public DamageModContainer() {
        this(new ArrayList<>());
    }

    public DamageModContainer(AbstractDamageModifier mod) {
        this(new ArrayList<>(Collections.singletonList(mod)));
    }

    public DamageModContainer(List<AbstractDamageModifier> mods) {
        damageModifiers = mods;
        Collections.sort(damageModifiers);
    }

    public List<AbstractDamageModifier> modifiers() {
        return damageModifiers;
    }

    public void addModifier(AbstractDamageModifier damageMod) {
        damageModifiers.add(damageMod);
        Collections.sort(damageModifiers);
    }

    public void addModifiers(List<AbstractDamageModifier> damageMods) {
        damageModifiers.addAll(damageMods);
        Collections.sort(damageModifiers);
    }

    public void removeModifier(AbstractDamageModifier damageMod) {
        damageModifiers.remove(damageMod);
    }

    public void removeModifiers(ArrayList<AbstractDamageModifier> damageMods) {
        damageModifiers.removeAll(damageMods);
    }

    public void clearModifiers() {
        damageModifiers.clear();
    }
}
