package net.snackbag.vera;

import java.util.HashMap;

public class Vera {
    private static final HashMap<String, VeraProvider> providers = new HashMap<>();

    public static void registerProvider(String name, VeraProvider provider) {
        if (providers.containsKey(name)) {
            throw new UnsupportedOperationException("There is already a provider named '" + name + "' registered");
        }

        providers.put(name, provider);
    }

    public static VeraProvider getProvider(String name) {
        if (providers.containsKey(name)) return providers.get(name);
        throw new NullPointerException("Unknown UI provider '" + name + "'");
    }
}
