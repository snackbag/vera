package net.snackbag.vera.style.animation;

import java.util.ArrayList;
import java.util.List;

public class VAnimation {
    public final String name;
    public final VeraApp app;

    public final int unwindTime;
    public final LoopMode loopMode;

    private final List<VKeyframe> keyframes = new ArrayList<>();

    public VAnimation(String name, int unwindTime, LoopMode loopMode, VeraApp app) {
        this.name = name;
        this.unwindTime = unwindTime;
        this.loopMode = loopMode;

        this.app = app;
    }

    public void addKeyframe(VKeyframe keyframe) {
        this.keyframes.add(keyframe);
    }
}
