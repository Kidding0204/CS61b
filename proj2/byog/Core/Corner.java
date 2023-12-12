package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Corner extends Building{

    public Corner(Plug exit, int length, boolean rotateDirection) {

        floors = new Line[2];
        walls = new Line[4];
        exits = new Plug[1];

        floors[0] = new Line(exit, exit.direction, length);
        walls[0] = floors[0].getParallelLine(1, length - 1);
        walls[1] = floors[0].getParallelLine(-1, length + 1);
        floors[1] = floors[0].getVerticalLine(0, floors[0].length + 1, rotateDirection);
        walls[2] = walls[0].getVerticalLine(0, walls[0].length, rotateDirection);
        walls[3] = walls[1].getVerticalLine(0, walls[1].length, rotateDirection);
        exits[0] = new Plug(floors[1].end, exit.direction);
    }
}
