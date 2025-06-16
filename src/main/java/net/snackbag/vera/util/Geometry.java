package net.snackbag.vera.util;

public class Geometry {
    /**
     * Method to check whether a coordinate in within the boundaries of a box
     *
     * @param x check x coord
     * @param y check y coord
     * @param wx box x coord
     * @param wy box y coord
     * @param wwidth box width
     * @param wheight box height
     *
     * @return whether param x and y are in the specified box boundaries
     */
    public static boolean isInBox(int x, int y, int wx, int wy, int wwidth, int wheight) {
        return x >= wx && x <= wx + wwidth &&
               y >= wy && y <= wy + wheight;
    }
}
