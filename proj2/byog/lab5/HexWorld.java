package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final long SEED = 2874123;
    private static final Random RANDOM = new Random(SEED);
    public static class Position {
        int x;
        int y;
        final int z;
        Position(int x, int y) {
            this.x = x;
            this.y = y;
            this.z = y;
        }
    }
    public static TETile[][] createWorld(TERenderer ter, int width, int height) {
        ter.initialize(width, height);
        TETile[][] world = new TETile[width][height];
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        return world;
    }
    private static void changePos1(Position p, int s) {
        p.y = 2 * (s + p.z) - p.y;
        p.x -= 1;
    }
    private static void changePos2(Position p, int s) {
        p.y = 2 * (s + p.z) - p.y - 1;
    }
    private static void changePos3(Position p, int width) {
        p.y -= 3;
        p.x = p.x - width * 10 + 5;
    }
    private static void changePos4(Position p, int width) {
        p.x += width * 10;
    }

    private static void drawLine(TETile[][] world, Position p,int width, TETile t) {
        for (int i = 0; i < width; i++) {
            world[p.x + i][p.y] = t;
        }
    }

    private static void addHexagon(TETile[][] world, Position p, int size, int width, int maxWidth, TETile t) {
        if (width > maxWidth) {
            return;
        }
        drawLine(world, p, width, t);
        changePos2(p, size);
        drawLine(world, p, width, t);
        changePos1(p, size);
        addHexagon(world, p, size, width + 2, maxWidth, t);
    }
    public static void addHexagon(TETile[][] world, Position p, int s, TETile t) {
        Position l = new Position(p.x, p.y);
        addHexagon(world, l, s, s, s + 2 * (s - 1) + 1, t);
    }

    private static void tessellateLine(TETile[][] world, Position p, int n) {
        Position l = new Position(p.x, p.y);
        for (int i = 0; i < n; i++) {
            addHexagon(world, l, 3, randomTETile());
            l.y += 6;
        }
    }
    private static TETile randomTETile() {
        int tileNum = RANDOM.nextInt(5);
        return switch (tileNum) {
            case 0 -> Tileset.WALL;
            case 1 -> Tileset.FLOWER;
            case 2 -> Tileset.FLOOR;
            case 3 -> Tileset.GRASS;
            case 4 -> Tileset.MOUNTAIN;
            default -> Tileset.NOTHING;
        };
    }
    private static void tessellateHexagons(TETile[][] world, Position p, int height, int maxHeight) {
        if (height == maxHeight) {
            tessellateLine(world, p, 5);
            return;
        }
        tessellateLine(world, p, height);
        changePos4(p, maxHeight - height);
        tessellateLine(world, p, height);
        changePos3(p, maxHeight - height);
        tessellateHexagons(world, p, height + 1, maxHeight);
    }

    public static void tessellateHexagons(TETile[][] world, Position p) {
        tessellateHexagons(world, p, 3, 5);
    }
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        TETile[][] world = createWorld(ter, 100, 100);
        Position p = new Position(10, 35);
        tessellateHexagons(world, p);
        ter.renderFrame(world);
    }
}
