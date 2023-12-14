package byog.Core;

import byog.TileEngine.Tileset;

import java.util.Random;
import static byog.Core.RandomUtils.uniform;

public class Room extends Building{

    public Room(Plug entrance, int width, int length, Random random) {
        Plug exit = new Plug(entrance, entrance.direction);
        int leftWidth = uniform(random, 1, width - 1);
        int rightWidth = width - leftWidth - 1;

        floors = new Line[width - 2];
        floors[0] = new Line(exit, exit.direction, length);
        Line[] leftFloors = floors[0].getSquare(leftWidth + 1, -1);
        Line[] rightFloors = floors[0].getSquare(rightWidth + 1, 1);
        System.arraycopy(leftFloors, 1, floors, 1, leftFloors.length - 2);
        System.arraycopy(rightFloors, 1, floors, leftFloors.length - 1, rightFloors.length - 2);

        walls = new Line[5];
        walls[0] = leftFloors[leftWidth];
        walls[1] = rightFloors[rightWidth];
        walls[2] = walls[0].getVerticalLine(0, width, true);
        walls[3] = walls[0].getVerticalLine(-length + 1, leftWidth, true);
        walls[4] = walls[1].getVerticalLine(-length + 1, rightWidth, false);

        exits = randomPlugs(exit, walls, random);
    }

    private static Plug randomPlug(Plug exit, Line currentWall, Random random, int randomNum) {
        boolean[] exitDirection = new boolean[2];
        exitDirection[0] = !currentWall.direction[0];
        if (randomNum == 2) {
            exitDirection[1] = exit.direction[1];
        } else if (randomNum == 1) {
            if (!exit.direction[0]) {
                exitDirection[1] = exit.direction[1];
            } else {
                exitDirection[1] = !exit.direction[1];
            }
        } else {
            if (exit.direction[0]) {
                exitDirection[1] = !exit.direction[1];
            } else {
                exitDirection[1] = exit.direction[1];
            }
        }
        return new Plug(currentWall.dots[uniform(random, 1, currentWall.length - 1)], exitDirection);
    }

    private static Plug[] randomPlugs(Plug exit, Line[] walls, Random random) {
        int exitsNum = uniform(random, 0, 3);
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
            exits[i] = randomPlug(exit, randomWall, random, randomNum);
        }
        return exits;
    }
}
