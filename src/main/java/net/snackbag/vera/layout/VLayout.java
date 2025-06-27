package net.snackbag.vera.layout;

import net.snackbag.vera.VElement;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VeraApp;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class VLayout extends VElement {
    protected final List<VElement> elements = new ArrayList<>();

    /**
     * ID for the last position cache call. To be compared with the current render cache ID
     *
     * @reason So we don't calculate the positions of widgets for each getX or getY call
     */
    private long cacheId = 0;
    protected final HashMap<VElement, Vector2i> cache = new HashMap<>();

    public VLayout(VeraApp app, int x, int y, int width, int height) {
        super(app, x, y, width, height);
    }

//    @Override
//    public int getWidth() {
//        return width < 0 ? calculateElementsWidth() : Math.min(calculateElementsWidth(), width);
//    }

    @Override
    public int getHeight() {
        return height < 0 ? calculateElementsHeight() : Math.min(calculateElementsHeight(), height);
    }

    public Vector2i posOf(VElement elem) {
        checkCache();

        if (!cache.containsKey(elem)) throw new RuntimeException("Layout cache does not contain requested element");
        return cache.get(elem);
    }

    private void checkCache() {
        if (cacheId != Vera.renderCacheId) {
            cache.clear();
            cacheId = Vera.renderCacheId;

            rebuild();
        }
    }

    public abstract void rebuild();

    public int calculateElementsHeight() {
        return elements.parallelStream()
                .mapToInt(VElement::getHeight)
                .sum();
    }

    public void addElement(VElement elem) {
        if (elements.contains(elem)) return;
        elements.add(elem);
    }
}
