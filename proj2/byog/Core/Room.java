package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;
import static byog.Core.RandomUtils.bernoulli;
import static byog.Core.RandomUtils.uniform;

public class Room extends Building{

    public Room(Plug exit, int width1, int width2, int length, Random random) {

        floors = new Line[width1 + width2 - 3];
        Line entranceFloor = new Line(exit, exit.direction, length);
        Line[] floor1 = entranceFloor.getSquare(width1 - 1, -1);
        Line[] floor2 = entranceFloor.getSquare(width2 - 1, 1);
        System.arraycopy(floor1, 0, floors, 0, floor1.length);
        System.arraycopy(floor2, 1, floors, floor1.length, floor2.length - 1);

        walls = new Line[4];
        int width = width1 + width2 - 1;
        walls[0] = floors[floors.length - 1].getParallelLine(1, length);
        walls[1] = walls[0].getVerticalLine(0, width, false);
        walls[2] = walls[1].getVerticalLine(0, length, false);
        walls[3] = walls[2].getVerticalLine(0, width, true);

        int exitNum = uniform(random,1, 4);
        exits = new Plug[exitNum];
        for (int i = 0; i < exitNum; i++) {
            Line wall = walls[random.nextInt(4)];
            int index = random.nextInt(wall.length);
            Dot plugDot = wall.dots[index];
            if (plugDot.p.equals(floors[0].end.p) || index == 0) {
                continue;
            }
            boolean[] plugDirection = new boolean[2];
            plugDirection[0] = !wall.direction[0];
            if (plugDirection[0]) {
                plugDirection[1] = wall.direction[1];
            } else {
                plugDirection[1] = !wall.direction[1];
            }
            exits[i] = new Plug(plugDot,plugDirection);
        }
    }

    @Override
    public void drawBuilding(TETile[][] world) {
        for (Line f : floors) {
            f.t = Tileset.FLOOR;
            f.drawLine(world);
        }
        for (Line wall : walls) {
            wall.t = Tileset.WALL;
            wall.drawLine(world);
        }
        for (Dot exit : exits) {
            exit.t = Tileset.FLOOR;
            exit.drawDot(world);
        }
        floors[0].end.t = Tileset.FLOOR;
        floors[0].end.drawDot(world);
    }
}
