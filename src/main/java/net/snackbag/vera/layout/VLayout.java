package net.snackbag.vera.layout;

import net.snackbag.vera.VElement;
import net.snackbag.vera.core.VeraApp;

import java.util.ArrayList;
import java.util.List;

public abstract class VLayout extends VElement {
    private final List<VElement> elements = new ArrayList<>();

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

    public int calculateElementsHeight() {
        return elements.parallelStream()
                .mapToInt(VElement::getHeight)
                .sum();
    }
}
