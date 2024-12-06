package net.snackbag.vera.core;

public class V4Int {
    private final int v1;
    private final int v2;
    private final int v3;
    private final int v4;

    public V4Int(int v1, int v2, int v3, int v4) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
    }

    public V4Int(int v1x2, int v3x4) {
        this(v1x2, v1x2, v3x4, v3x4);
    }

    public V4Int(int all) {
        this(all, all, all, all);
    }

    public int get1() {
        return v1;
    }

    public int get2() {
        return v2;
    }

    public int get3() {
        return v3;
    }

    public int get4() {
        return v4;
    }

    public V4Int with1(int v1) {
        return new V4Int(v1, v2, v3, v4);
    }

    public V4Int with2(int v2) {
        return new V4Int(v1, v2, v3, v4);
    }

    public V4Int with3(int v3) {
        return new V4Int(v1, v2, v3, v4);
    }

    public V4Int with4(int v4) {
        return new V4Int(v1, v2, v3, v4);
    }
}
