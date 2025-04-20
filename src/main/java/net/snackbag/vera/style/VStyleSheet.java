package net.snackbag.vera.style;

import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.widget.VWidget;

import java.util.HashMap;

public class VStyleSheet {
    public final VeraApp app;

    public final VStyleMap appStyles = new VStyleMap();
    private final HashMap<Class<?>, VStyleMap> widgetStyles = new HashMap<>();
    private final HashMap<String, VStyleMap> classStyles = new HashMap<>();

    public VStyleSheet(VeraApp app) {
        this.app = app;
    }

    public VStyleMap getStyles(VWidget<?> widget) {
        VStyleMap styles = new VStyleMap();

        // first apply widget-self styles (lowest priority)
        final Class<?> widgetClass = widget.getClass();
        if (widgetStyles.containsKey(widgetClass)) {
            styles.merge(widgetStyles.get(widgetClass));
        }

        // then apply classes (medium priority)
        for (String clazz : widget.getAllStyleClasses()) {
            styles.merge(getStylesForClass(clazz));
        }

        // apply custom styles per widget (high priority)
        styles.merge(widget.getLocalStyles());

        return styles;
    }


    public VStyleMap getStylesForClass(String clazz) {
        return classStyles.getOrDefault(clazz, new VStyleMap());
    }
}
