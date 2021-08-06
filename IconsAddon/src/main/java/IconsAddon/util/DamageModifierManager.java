package IconsAddon.util;

import IconsAddon.damageModifiers.AbstractDamageModifier;
import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DamageModifierManager implements CustomSavable<Boolean> {

    private static final HashMap<Object, List<AbstractDamageModifier>> boundDamageObjects = new HashMap<>();

    @Override
    public Boolean onSave() {
        return true;
    }

    @Override
    public void onLoad(Boolean aBoolean) {
        boundDamageObjects.clear();
    }

    @SpirePatch(clz = DamageInfo.class, method = SpirePatch.CLASS)
    private static class BoundDamageInfo {
        public static final SpireField<List<AbstractDamageModifier>> boundDamageMods = new SpireField<>(ArrayList::new);
        public static final SpireField<AbstractCard> instigatorCard = new SpireField<>(() -> null);
    }

    public static List<AbstractDamageModifier> getDamageMods(DamageInfo info) {
        return BoundDamageInfo.boundDamageMods.get(info);
    }

    public static AbstractCard getInstigatorCard(DamageInfo info) {
        return BoundDamageInfo.instigatorCard.get((info));
    }

    public static void bindInstigatorCard(DamageInfo info, AbstractCard c) {
        BoundDamageInfo.instigatorCard.set(info, c);
    }

    public static void bindDamageModsFromObject(DamageInfo info, Object object) {
        bindDamageMods(info, boundDamageObjects.get(object));
    }

    public static void bindDamageMods(DamageInfo info, List<AbstractDamageModifier> list) {
        for (AbstractDamageModifier m : list) {
            if (!BoundDamageInfo.boundDamageMods.get(info).contains(m)) {
                BoundDamageInfo.boundDamageMods.get(info).add(m);
            }
        }
    }

    public static void addModifier(Object object, AbstractDamageModifier damageMod) {
        if (!boundDamageObjects.containsKey(object)) {
            boundDamageObjects.put(object, new ArrayList<>());
        }
        boundDamageObjects.get(object).add(damageMod);
        Collections.sort(boundDamageObjects.get(object));
    }

    public static void addModifiers(Object object, List<AbstractDamageModifier> damageMods) {
        if (!boundDamageObjects.containsKey(object)) {
            boundDamageObjects.put(object, new ArrayList<>());
        }
        boundDamageObjects.get(object).addAll(damageMods);
        Collections.sort(boundDamageObjects.get(object));
    }

    public static List<AbstractDamageModifier> modifiers(Object object) {
        if (boundDamageObjects.containsKey(object)) {
            return boundDamageObjects.get(object);
        }
        return Collections.emptyList();
    }

    public static void removeModifier(Object object, AbstractDamageModifier damageMod) {
        if (boundDamageObjects.containsKey(object)) {
            boundDamageObjects.get(object).remove(damageMod);
            Collections.sort(boundDamageObjects.get(object));
        }
    }

    public static void removeModifiers(Object object, ArrayList<AbstractDamageModifier> damageMods) {
        if (boundDamageObjects.containsKey(object)) {
            boundDamageObjects.get(object).removeAll(damageMods);
            Collections.sort(boundDamageObjects.get(object));
        }
    }

    public static void removeAllModifiers(Object object) {
        boundDamageObjects.remove(object);
    }

}
