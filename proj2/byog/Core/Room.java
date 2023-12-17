package byog.Core;

import byog.TileEngine.Tileset;

import java.util.Random;
import static byog.Core.RandomUtils.uniform;

public class Room extends Building{

    public Room(Plug entrance, int width, int length, Random random, Dot[][] world) {
        Plug exit = new Plug(entrance, entrance.direction);
        int leftWidth = uniform(random, 1, width - 1);
        int rightWidth = width - leftWidth - 1;

        floors = new Line[width - 2];
        floors[0] = new Line(exit, exit.direction, length);
        Line[] leftFloors = floors[0].getSquare(leftWidth + 1, -1, world);
        Line[] rightFloors = floors[0].getSquare(rightWidth + 1, 1, world);
        System.arraycopy(leftFloors, 1, floors, 1, leftFloors.length - 2);
        System.arraycopy(rightFloors, 1, floors, leftFloors.length - 1, rightFloors.length - 2);

        leftFloors[leftWidth].changeT(Tileset.WALL);
        rightFloors[rightWidth].changeT(Tileset.WALL);
        walls = new Line[5];
        walls[0] = leftFloors[leftWidth];
        walls[1] = rightFloors[rightWidth];
        walls[2] = walls[0].getVerticalLine(0, width, true, Tileset.WALL);
        int width1;
        if (entrance.direction[1]) {
            width1 = -length + 1;
        } else {
            width1 = length - 1;
        }
        walls[3] = walls[0].getVerticalLine(width1, leftWidth, true, Tileset.WALL);
        walls[4] = walls[1].getVerticalLine(width1, rightWidth, false, Tileset.WALL);

        pos[0] = walls[0].head;
        pos[1] = walls[1].end;

        exits = randomPlugs(walls, random, world);
        for (Dot dot : exits) {
            if (dot == null) {
                break;
            }
            dot.t = Tileset.FLOOR;
        }
    }
    public static Room getNewRoom(Plug entrance, int width, int length, Random random, Dot[][] world) {
        Room r = new Room(entrance,width, length, random, world);
        if (r.overlapCheck(world) && (length < 4 || width < 4)) {
            r.contain = false;
            return r;
        }
        if (!r.overlapCheck(world)) {
            r.contain = true;
            return r;
        }
        return getNewRoom(entrance, width - 1, length - 1, random, world);
    }

    private static Plug randomPlug(Line currentWall, Random random, Dot[][] world) {
        Dot dot = currentWall.dots[uniform(random, 1, currentWall.length - 1)];
        if (!dot.containCheck(world)) {
            return null;
        }
        boolean[] exitDirection = new boolean[2];
        exitDirection[0] = !currentWall.direction[0];
        if (exitDirection[0]) {
            exitDirection[1] = world[dot.p.x ][dot.p.y + 1].t.equals(Tileset.NOTHING);
        } else {
            exitDirection[1] = world[dot.p.x + 1][dot.p.y].t.equals(Tileset.NOTHING);
        }
        return new Plug(dot, exitDirection);
    }

    private static Plug[] randomPlugs(Line[] walls, Random random, Dot[][] world) {
        int exitsNum = uniform(random, 1, 4);
        Plug[] exits = new Plug[exitsNum];
        int previousNum = -1;

        for (int i = 0; i < exitsNum; i++) {
            int randomNum = uniform(random, 3);
            if (randomNum == previousNum) {
                i--;
                continue;
            }

            Line randomWall = walls[randomNum];
            previousNum = randomNum;

            exits[i] = randomPlug(randomWall, random, world);
        }
        return exits;
    }
}
