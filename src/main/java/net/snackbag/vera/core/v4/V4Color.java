package net.snackbag.vera.core.v4;

import net.snackbag.vera.core.VColor;

public class V4Color {
    private final VColor v1;
    private final VColor v2;
    private final VColor v3;
    private final VColor v4;

    public V4Color(VColor v1, VColor v2, VColor v3, VColor v4) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
    }

    public V4Color(VColor v1x2, VColor v3x4) {
        this(v1x2, v1x2, v3x4, v3x4);
    }

    public V4Color(VColor all) {
        this(all, all, all, all);
    }

    public VColor get1() {
        return v1;
    }

    public VColor get2() {
        return v2;
    }

    public VColor get3() {
        return v3;
    }

    public VColor get4() {
        return v4;
    }

    public V4Color with1(VColor v1) {
        return new V4Color(v1, v2, v3, v4);
    }

    public V4Color with2(VColor v2) {
        return new V4Color(v1, v2, v3, v4);
    }

    public V4Color with3(VColor v3) {
        return new V4Color(v1, v2, v3, v4);
    }

    public V4Color with4(VColor v4) {
        return new V4Color(v1, v2, v3, v4);
    }
}
