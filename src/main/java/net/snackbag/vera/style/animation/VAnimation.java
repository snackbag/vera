package net.snackbag.vera.style.animation;

import java.util.ArrayList;
import java.util.List;

public class VAnimation {
    public final String name;
    public final VeraApp app;

    public final int unwindTime;
    public final LoopMode loopMode;

    private final List<VKeyframe> keyframes = new ArrayList<>();

    public VAnimation(String name, int discardTime) {
        this.name = name;
        this.discardTime = discardTime;
    }

    public void addKeyframe(VKeyframe keyframe) {
        this.keyframes.add(keyframe);
    }
}
