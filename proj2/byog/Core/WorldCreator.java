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
            case 0 -> new Hallway(entrance, length, world);
            case 1 -> new Corner(entrance, length, rotateDirection, world);
            default -> new Room(entrance, width, length, random, world);
        };
        if (returnBuilding.boundaryExceedingCheck(this.width, this.height)) {
            returnBuilding.setFields(world);
            return returnBuilding;
        } else {
            return null;
        }
    }

    private void randomlyMerge(Building[] buildings, int maxLength,int count, int currentCount, Dot[][] world) {
        Building current = buildings[currentCount];
        if (current == null || count == 50) {
            return;
        }

        int buildingNum = current.exits.length;
        for (int i = 0; i < buildingNum; i++) {
            buildings[count] = randomBuilding(current.exits[i], maxLength, world);
            count++;
            if (count == buildings.length) {
                break;
            }
        }

        randomlyMerge(buildings, maxLength, count, currentCount + 1, world);
    }
    private Building[] allConcrete(Plug entrance, Dot[][] world) {
        Building[] buildings = new Building[50];
        Building origination = randomBuilding(entrance, 7, world);
        buildings[0] = origination;

        randomlyMerge(buildings, 7, 1, 0, world);
        return buildings;
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
        Building[] buildings = creator.allConcrete(entrance, dots);

        /*buildings[0].drawBuilding(world);
        buildings[1].drawBuilding(world);
        buildings[2].drawBuilding(world);
        buildings[3].drawBuilding(world);
        buildings[4].drawBuilding(world);
        buildings[5].drawBuilding(world);
        buildings[6].drawBuilding(world);

         */
        for (Building building : buildings) {
            building.drawBuilding(world);
        }








        /*Dot dotTest = new Dot(new Dot.Position(50, 30), Tileset.LOCKED_DOOR);
        Plug entrance = new Plug(dotTest, new boolean[]{false, false});
        Hallway hallway = new Hallway(entrance, 5);
        hallway.drawBuilding(world);
        Corner corner = new Corner(hallway.getExits()[0],5, false);
        corner.drawBuilding(world);
        Room room = new Room(corner.getExits()[0], 4, 6, random, dots);
        room.drawBuilding(world);
        Hallway hallway1 = new Hallway(room.getExits()[0], 3);
        hallway1.drawBuilding(world);
        Room room1 = new Room(hallway1.getExits()[0], 4, 6, random, dots);
        room1.drawBuilding(world);

         */


        entrance.t = Tileset.LOCKED_DOOR;
        entrance.drawDot(world);
        ter.renderFrame(world);
    }
}
