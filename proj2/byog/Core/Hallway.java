package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Hallway extends Building{

    public Hallway(Plug exit, int length) {

        floors = new Line[1];
        walls = new Line[2];
        exits = new Plug[1];

        floors[0] = new Line(exit, exit.direction, length + 1);
        walls[0] = floors[0].getParallelLine(-1, length);
        walls[1] = floors[0].getParallelLine(1, length);
        exits[0] = new Plug(floors[0].end, exit.direction);
    }
}
