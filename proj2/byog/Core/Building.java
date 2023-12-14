package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public abstract class Building {
    protected Line[] walls;
    protected Line[] floors;
    protected  Plug[] exits;

    public void drawBuilding(TETile[][] world) {
        for (Line floor : floors) {
            floor.t = Tileset.FLOOR;
            floor.drawLine(world);
        }
        for (Line wall : walls) {
            wall.t = Tileset.WALL;
            wall.drawLine(world);
        }
        for (Dot exit : exits) {
            exit.t = Tileset.FLOOR;
            exit.drawDot(world);
        }
    }

    public Plug[] getExits() {
        return exits;
    }
}
