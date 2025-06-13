package net.snackbag.vera.style;

import net.snackbag.vera.widget.VWidget;

import java.util.HashMap;

public class VStyleSheet {
    private final HashMap<VWidget<?>, HashMap<String, Object>> widgetSpecificStyles = new HashMap<>();
    private final HashMap<String, Object> styleClasses = new HashMap<>();
    private final HashMap<String, StyleValueType> typeRegistry = new HashMap<>();
}
