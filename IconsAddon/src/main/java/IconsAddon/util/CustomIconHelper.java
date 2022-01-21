package IconsAddon.util;

import IconsAddon.icons.*;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomIconHelper {
    private static final ArrayList<AbstractCustomIcon> allIcons = new ArrayList<>();
    private static final HashMap<String, AbstractCustomIcon> icons = new HashMap<>();

    public static void addCustomIcon(AbstractCustomIcon icon) {
        allIcons.add(icon);
        icons.put(icon.cardCode(), icon);
    }

    public static AbstractCustomIcon getIcon(String key) {
        return icons.get(key);
    }

    public static ArrayList<AbstractCustomIcon> getAllIcons() {
        return allIcons;
    }
}
