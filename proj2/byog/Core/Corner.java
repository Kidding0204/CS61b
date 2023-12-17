package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Corner extends Building{

    public Corner(Plug exit, int length, boolean rotateDirection, Dot[][] world) {

        floors = new Line[2];
        walls = new Line[4];
        exits = new Plug[1];
        int width = -1;
        if (rotateDirection) {
            width = 1;
        }
        floors[0] = new Line(exit, exit.direction, length);
        walls[0] = floors[0].getParallelLine(width, length - 1, Tileset.WALL);
        walls[1] = floors[0].getParallelLine(- width, length + 1, Tileset.WALL);
        floors[1] = floors[0].getVerticalLine(0, floors[0].length + 1, rotateDirection, Tileset.FLOOR);
        walls[2] = walls[0].getVerticalLine(0, walls[0].length, rotateDirection, Tileset.WALL);
        walls[3] = walls[1].getVerticalLine(0, walls[1].length, rotateDirection, Tileset.WALL);

        exits[0] = new Plug(floors[1].end, floors[1].direction);
        for (Dot dot : exits) {
            dot.t = Tileset.FLOOR;
        }

        pos[0] = walls[1].end;
        pos[1] = walls[3].end;

    }

    public static Corner getNewCorner(Plug exit, int length, boolean rotateDirection, Dot[][] world) {
        Corner r = new Corner(exit, length, rotateDirection, world);
        if (r.overlapCheck(world) && length < 3) {
            r.contain = false;
            return r;
        }
        if (!r.overlapCheck(world)) {
            r.contain = true;
            return r;
        }
        return getNewCorner(exit, length - 1, rotateDirection, world);
    }
}
