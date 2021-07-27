package IconsAddon.util;

import IconsAddon.damageModifiers.AbstractDamageModifier;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;

import java.util.ArrayList;
import java.util.HashMap;

public class DamageModifierManager {

    private static final HashMap<Object, ArrayList<AbstractDamageModifier>> boundObjects = new HashMap<>();

    @SpirePatch(clz = DamageInfo.class, method = SpirePatch.CLASS)
    public static class BoundDamageInfo {
        public static final SpireField<Object> boundObject = new SpireField<>(() -> null);

        public static void splice(DamageInfo info, Object object) {
            if (boundObject.get(info) == null) {
                boundObject.set(info, object);
            } else {
                DamageModifierManager.addModifiers(boundObject.get(info), DamageModifierManager.modifiers(object));
            }
        }
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
    }

    public static void addModifiers(Object object, ArrayList<AbstractDamageModifier> damageMods) {
        if (!boundObjects.containsKey(object)) {
            boundObjects.put(object, new ArrayList<>());
        }
        boundObjects.get(object).addAll(damageMods);
    }

    public static ArrayList<AbstractDamageModifier> modifiers(Object object) {
        return boundObjects.getOrDefault(object, new ArrayList<>());
    }

    public static void removeModifier(Object object, AbstractDamageModifier damageMod) {
        if (boundObjects.containsKey(object)) {
            boundObjects.get(object).remove(damageMod);
        }
    }

    public static void removeModifiers(Object object, ArrayList<AbstractDamageModifier> damageMods) {
        if (boundObjects.containsKey(object)) {
            boundObjects.get(object).removeAll(damageMods);
        }
    }

    public static void removeAllModifiers(Object object) {
        boundObjects.remove(object);
    }



}
