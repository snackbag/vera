package net.snackbag.vera.style.animation;

import net.snackbag.vera.Vera;
import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.widget.VWidget;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashSet;

/**
 * Per-widget handler for animations. This is after stylesheet.getKey, so there is no differentiation between
 * widget-specific styles, class styles and standard styles.
 */
public class AnimationEngine {
    public final VWidget<?> widget;
    private final HashMap<VAnimation, Long> activeAnimations = new HashMap<>();

    private long cacheId = 0;
    private final HashMap<String, Object> cache = new HashMap<>();

    public AnimationEngine(VWidget<?> widget) {
        this.widget = widget;
    }

    public boolean isActive(String name) {
        return activeAnimations.keySet().stream().anyMatch(anim -> anim.name.equals(name));
    }

    public void activate(VAnimation animation) {
        activeAnimations.add(animation);
    }

    public @Nullable VAnimation getIfActive(String name) {
        return activeAnimations.keySet().stream().filter(anim -> anim.name.equals(name)).findFirst().orElse(null);
    }

    public VAnimation[] getAllActive() {
        return activeAnimations.keySet().toArray(new VAnimation[0]);
    }

    public void checkCache() {
        if (cacheId != Vera.renderCacheId) {
            cache.clear();
            cacheId = Vera.renderCacheId;
        }
    }

    public <T> T animateStyle(String style, T value) {
        checkCache();

        if (cache.containsKey(style)) return (T) cache.get(style);

        StyleValueType type = StyleValueType.get(value, null);
        return widget.app.pipeline.applyComposites(this, style, type, value);
    }
}
