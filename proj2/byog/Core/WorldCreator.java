package byog.Core;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

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

       return new Plug(new Dot(randomPos, Tileset.LOCKED_DOOR), randomDirection);
    }

    private TETile[][] createWorld() {

        ter.initialize(width, height);
        TETile[][] world = new TETile[width][height];

        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        return world;
    }

    private Building randomBuilding(Plug entrance, int maxLength) {
        int choice = random.nextInt(3);
        int length = uniform(random, 1, maxLength);
        boolean rotateDirection = bernoulli(random);
        int width1 = uniform(random, 2, maxLength - 1);
        int width = uniform(random, width1 + 1, maxLength);
        int width2 = width - width1 + 1;
        return switch (choice) {
            case 0 -> new Hallway(entrance, length);
            case 1 -> new Corner(entrance, length, rotateDirection);
            case 2 -> new Room(entrance, width, maxLength, random);
            default -> null;
        };
    }

    private void randomlyMerge(Building[] buildings, int maxLength, int count, int currentCount) {
        if (count == buildings.length) {
            return;
        }

        Building current = buildings[currentCount];
        currentCount += 1;
        int buildingNum = current.exits.length;
        for (int i = 0; i < buildingNum; i++) {
            buildings[count] = randomBuilding(current.exits[i], maxLength);
            count++;
            if (count == buildings.length) {
                break;
            }
        }

        for (int i = 0; i < buildingNum; i++) {
            randomlyMerge(buildings, maxLength, count, currentCount + i);
        }
    }
    private Building[] allConcrete(Plug entrance, int maxLength, int maxBuildings) {
        Building[] buildings = new Building[maxBuildings];
        Building origination = randomBuilding(entrance, maxLength);
        buildings[0] = origination;

        randomlyMerge(buildings, maxLength, 1, 0);
        return buildings;
    }

    public static TETile[][] create(TERenderer ter, int width, int height, long seed) {

        WorldCreator creator = new WorldCreator(ter, width, height, seed);

        TETile[][] world = creator.createWorld();

        Plug entrance = creator.createEntrance();

        return world;
    }


    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        WorldCreator creator = new WorldCreator(ter, 100, 60, 123123);

        TETile[][] world = creator.createWorld();

        Plug entrance = creator.createEntrance();
        Building[] buildings = creator.allConcrete(entrance, 5, 30);

        for (Building building : buildings) {
            if (building == null) {
                break;
            }
            building.drawBuilding(world);
        }


        /*Hallway hallway = new Hallway(entrance, 5);
        hallway.drawBuilding(world);
        Corner corner = new Corner(hallway.getExits()[0],5, true);
        corner.drawBuilding(world);
        Room room = new Room(corner.getExits()[0], 4, 6, random);
        room.drawBuilding(world);
        Hallway hallway1 = new Hallway(room.getExits()[0], 3);
        hallway1.drawBuilding(world);*/
        entrance.t = Tileset.LOCKED_DOOR;
        entrance.drawDot(world);
        ter.renderFrame(world);
    }
}
