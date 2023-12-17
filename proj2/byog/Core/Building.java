package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public abstract class Building {
    protected Line[] walls;
    protected Line[] floors;
    protected Plug[] exits;
    protected Building last;
    protected boolean contain;
    protected Dot[] pos = new Dot[2];

    public void drawBuilding(TETile[][] world) {
        for (Line floor : floors) {
            floor.drawLine(world);
        }
        for (Line wall : walls) {
            wall.drawLine(world);
        }
        for (Dot exit : exits) {
            exit.drawDot(world);
        }
    }

    public void setFields(Dot[][] world) {
        for (Line floor : floors) {
            for (Dot dot : floor.dots) {
                world[dot.p.x][dot.p.y] = dot;
            }
        }
        for (Line wall : walls) {
            for (Dot dot : wall.dots) {
                world[dot.p.x][dot.p.y] = dot;
            }
        }
    }

    public boolean boundaryContainCheck(int width, int height) {
        boolean one = 0 <= pos[0].p.x & pos[0].p.x < width;
        boolean two = 0 <= pos[1].p.x & pos[1].p.x < width;
        boolean three = 0 <= pos[0].p.y & pos[0].p.y < height;
        boolean four = 0 <= pos[1].p.y & pos[1].p.y < height;
        return one & two & three & four;
    }

    public boolean overlapCheck(Dot[][] world) {
        boolean r = false;
        for (Line line : floors) {
            for (Dot dot : line.dots) {
                if (dot.containCheck(world)) {
                    if (world[dot.p.x][dot.p.y].t.equals(Tileset.WALL)) {
                        r = true;
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        return r;
    }

    public Plug[] getExits() {
        return exits;
    }
}
