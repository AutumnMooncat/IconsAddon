package IconsAddon.util;

import IconsAddon.damageModifiers.AbstractDamageModifier;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DamageModifierManager {

    private static final HashMap<Object, ArrayList<AbstractDamageModifier>> boundObjects = new HashMap<>();

    @SpirePatch(clz = DamageInfo.class, method = SpirePatch.CLASS)
    private static class BoundDamageInfo {
        public static final SpireField<Object> boundObject = new SpireField<>(() -> null);
    }

    /**
     * Gets the bound object for the provided DamageInfo
     * @param info The DamageInfo to lookup the bound object for
     * @return The object bound to this DamageInfo. Will be null if no object is bound
     */
    public static Object getBoundObject(DamageInfo info) {
        return BoundDamageInfo.boundObject.get(info);
    }

    /**
     * Binds the object to the DamageInfo is no object is bound. If an object is already bound, adds the modifiers of the new object to the bound object. Should be used in most cases in case multiple mods attempt to bind different damages via patches.
     * @param info The DamageInfo to bind the object to
     * @param object The object to bind to the DamageInfo. This associates all DamageModifiers on the object to the DamageInfo
     */
    public static void spliceBoundObject(DamageInfo info, Object object) {
        if (BoundDamageInfo.boundObject.get(info) == null) {
            BoundDamageInfo.boundObject.set(info, object);
        } else {
            DamageModifierManager.addModifiers(BoundDamageInfo.boundObject.get(info), DamageModifierManager.modifiers(object));
        }
    }

    /**
     * Forcefully sets a new bound object to the DamageInfo. Not patch friendly, but may be needed in certain circumstances.
     * @param info The DamageInfo to bind the object to
     * @param object The object to bind to the DamageInfo. This associates all DamageModifiers on the object to the DamageInfo
     */
    public static void overwriteBoundObject(DamageInfo info, Object object) {
        BoundDamageInfo.boundObject.set(info, object);
    }

    @SpirePatch(clz = AbstractGameAction.class, method = SpirePatch.CLASS)
    public static class BoundGameAction {
        public static SpireField<Object> boundObject = new SpireField<>(() -> null);
    }

    public static void addModifier(Object object, AbstractDamageModifier damageMod) {
        if (!boundObjects.containsKey(object)) {
            boundObjects.put(object, new ArrayList<>());
        }
        boundObjects.get(object).add(damageMod);
        Collections.sort(boundObjects.get(object));
    }

    public static void addModifiers(Object object, ArrayList<AbstractDamageModifier> damageMods) {
        if (!boundObjects.containsKey(object)) {
            boundObjects.put(object, new ArrayList<>());
        }
        boundObjects.get(object).addAll(damageMods);
        Collections.sort(boundObjects.get(object));
    }

    public static ArrayList<AbstractDamageModifier> modifiers(Object object) {
        return boundObjects.getOrDefault(object, new ArrayList<>());
    }

    public static void removeModifier(Object object, AbstractDamageModifier damageMod) {
        if (boundObjects.containsKey(object)) {
            boundObjects.get(object).remove(damageMod);
            Collections.sort(boundObjects.get(object));
        }
    }

    public static void removeModifiers(Object object, ArrayList<AbstractDamageModifier> damageMods) {
        if (boundObjects.containsKey(object)) {
            boundObjects.get(object).removeAll(damageMods);
            Collections.sort(boundObjects.get(object));
        }
    }

    public static void removeAllModifiers(Object object) {
        boundObjects.remove(object);
    }

}
