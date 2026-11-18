package net.snackbag.vera.util;

public class VGeometry {
    /**
     * Method to check whether a coordinate in within the boundaries of a box
     *
     * @param x       check x coord
     * @param y       check y coord
     * @param wx      box x coord
     * @param wy      box y coord
     * @param wwidth  box width
     * @param wheight box height
     * @return whether param x and y are in the specified box boundaries
     */
    public static boolean isInBox(int x, int y, int wx, int wy, int wwidth, int wheight) {
        return x >= wx && x <= wx + wwidth &&
                y >= wy && y <= wy + wheight;
    }

    public record IntRect(int x, int y, int width, int height) {
        public int left() {
            return x;
        }

        public int top() {
            return y;
        }

        public int right() {
            return x + width;
        }

        public int bottom() {
            return y + height;
        }

        public boolean isEmpty() {
            return width <= 0 || height <= 0;
        }

        public boolean intersects(IntRect other) {
            return right() > other.left()
                    && bottom() > other.top()
                    && left() < other.right()
                    && top() < other.bottom();
        }

        public IntRect intersection(IntRect other) {
            int nx = Math.max(left(), other.left());
            int ny = Math.max(top(), other.top());
            int nr = Math.min(right(), other.right());
            int nb = Math.min(bottom(), other.bottom());

            int nw = nr - nx;
            int nh = nb - ny;

            if (nw <= 0 || nh <= 0) return new IntRect(0, 0, 0, 0);
            return new IntRect(nx, ny, nw, nh);
        }
    }
}
