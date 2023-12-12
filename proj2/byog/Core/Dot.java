package byog.Core;

import byog.TileEngine.TETile;

public class Dot {
    public static class Position {
        int x;
        int y;
        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    Position p;
    TETile t;

    public Dot(Position p, TETile t) {
        this.p = p;
        this.t = t;
    }

    public void drawDot(TETile[][] world) {
        world[p.x][p.y] = t;
    }

}
