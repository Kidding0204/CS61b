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

    public boolean containCheck(Dot[][] world) {
        int width = world.length - 1;
        int height = world[0].length - 1;

        boolean one = 0 <= p.x & p.x < width;
        boolean two = 0 <= p.x & p.x < width;
        boolean three = 0 <= p.y & p.y < height;
        boolean four = 0 <= p.y & p.y < height;
        return one & two & three & four;
    }


}
