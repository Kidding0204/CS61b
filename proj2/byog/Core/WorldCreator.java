package byog.Core;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import static byog.Core.Hallway.getNewHallway;
import static byog.Core.Corner.getNewCorner;
import static byog.Core.Room.getNewRoom;

import java.util.Random;
import static byog.Core.RandomUtils.uniform;
import static byog.Core.RandomUtils.bernoulli;

public class WorldCreator {
    private final int width;
    private final int height;
    private static long seed;
    private static final Random random = new Random(seed);
    private final TERenderer ter;
    public WorldCreator(TERenderer ter, int width, int height, long seed) {

        this.width = width;
        this.height = height;
        WorldCreator.seed = seed;
        this.ter = ter;
    }

    private Plug createEntrance() {

        int xPos = random.nextInt(width);
        int yPos = random.nextInt(height);
        Dot.Position randomPos = new Dot.Position(xPos, yPos);

        int[] direction = {random.nextInt(2), random.nextInt(2)};
        boolean[] randomDirection = {direction[0] == 1, direction[1] == 1};

       return new Plug(new Dot(randomPos, Tileset.FLOOR), randomDirection);
    }

    private Dot[][] initialWorld() {
        Dot[][] world = new Dot[width][height];

        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = new Dot(new Dot.Position(x, y), Tileset.NOTHING);
            }
        }

        return world;
    }

    private TETile[][] createWorld() {
        ter.initialize(width, height);
        TETile[][] tiles = new TETile[width][height];
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
        return tiles;
    }

    private Building randomBuilding(Plug entrance, int maxLength, Dot[][] world) {
        int length = uniform(random, 3, maxLength);
        boolean rotateDirection = bernoulli(random);
        int width1 = uniform(random, 2, maxLength - 1);
        int width = uniform(random, width1 + 1, maxLength);
        Building returnBuilding = switch (random.nextInt(3)) {
            case 0 -> getNewHallway(entrance, length, world);
            case 1 -> getNewCorner(entrance, length, rotateDirection, world);
            default -> getNewRoom(entrance, width, length, random, world);
        };

        boolean existNullExits = false;
        for (Plug exit : returnBuilding.exits) {
            if (exit == null) {
                existNullExits = true;
                break;
            }
        }

        if (returnBuilding.boundaryContainCheck(this.width, this.height) && !existNullExits) {
            returnBuilding.setFields(world);
            returnBuilding.contain = true;
        } else {
            returnBuilding.contain = false;
        }

        return returnBuilding;
    }

    private void randomlyMerge(Building[] buildings, int maxLength,int count, int currentCount, Dot[][] world) {
        Building current = buildings[currentCount];
        if (count == buildings.length) {
            return;
        }

        if (current.contain) {
            int buildingNum = current.exits.length;
            for (int i = 0; i < buildingNum; i++) {
                buildings[count] = randomBuilding(current.exits[i], maxLength, world);
                buildings[count].last = current;
                count++;
                if (count == buildings.length) {
                    break;
                }
            }
        } else {
            current.last.exits = new Plug[0];
        }

        randomlyMerge(buildings, maxLength, count, currentCount + 1, world);
    }
    private void allConcrete(Plug entrance, Dot[][] world) {
        Building[] buildings = new Building[200];
        Building origination = randomBuilding(entrance, 7, world);
        origination.last = origination;
        buildings[0] = origination;

        randomlyMerge(buildings, 7, 1, 0, world);
    }

    public static TETile[][] create(TERenderer ter, int width, int height, long seed) {

        WorldCreator creator = new WorldCreator(ter, width, height, seed);
        Dot[][] dots = creator.initialWorld();
        TETile[][] world = creator.createWorld();

        Plug entrance = creator.createEntrance();

        return world;
    }


    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        WorldCreator creator = new WorldCreator(ter, 100, 60, 123123);
        Dot[][] dots = creator.initialWorld();
        TETile[][] world = creator.createWorld();

        Plug entrance = creator.createEntrance();
        creator.allConcrete(entrance, dots);


        for (Dot[] line : dots) {
            for (Dot dot : line) {
                dot.drawDot(world);
            }
        }


        entrance.t = Tileset.LOCKED_DOOR;
        entrance.drawDot(world);
        ter.renderFrame(world);
    }
}
