package net.snackbag.vera;

import net.snackbag.mcvera.impl.MCVeraProvider;
import net.snackbag.mcvera.impl.MCVeraRenderer;

public class Vera {
    public static final MCVeraProvider provider = new MCVeraProvider();
    public static final MCVeraRenderer renderer = new MCVeraRenderer();

    public static final String FONT_DEFAULT = provider.getDefaultFontName();
    public static final String FONT_ARIAL = "minecraft:arial";
}
