package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Line {
    Dot head;
    boolean[] direction;
    int length;

    Dot end;
    TETile t;
    Dot[] dots;

    public Line(Dot head, boolean[] direction, int length) {
        this.head = head;
        this.length = length;
        this.direction = direction;
        t = head.t;
        dots = new Dot[length];
        dots[0] = head;
        if (direction[0]) {
            if (direction[1]) {
                for (int i = 1; i < length; i++) {
                    dots[i] = new Dot(new Dot.Position(head.p.x, head.p.y + i), t);
                }
            } else {
                for (int i = 1; i < length; i++) {
                    dots[i] = new Dot(new Dot.Position(head.p.x, head.p.y - i), t);
                }
            }
        } else {
            if (direction[1]) {
                for (int i = 1; i < length; i++) {
                    dots[i] = new Dot(new Dot.Position(head.p.x + i, head.p.y), t);
                }
            } else {
                for (int i = 1; i < length; i++) {
                    dots[i] = new Dot(new Dot.Position(head.p.x - i, head.p.y), t);
                }
            }
        }
        end = dots[length - 1];
    }

    public void drawLine(TETile[][] world) {
        for (Dot dot : dots) {
            dot.t = this.t;
            dot.drawDot(world);
        }
    }

    public Line getParallelLine(int width, int newLen) {
        Dot anotherHead = new Dot(new Dot.Position(head.p.x, head.p.y), t);
        if (direction[0]) {
            anotherHead.p.x += width;
        } else {
            anotherHead.p.y += width;
        }
        return new Line (anotherHead, direction, newLen);
    }

    public Line getVerticalLine(int width, int newLen, boolean rotateDirection) {
        Dot anotherHead = new Dot(new Dot.Position(end.p.x, end.p.y), t);
        if (direction[0]) {
            anotherHead.p.y += width;
        } else {
            anotherHead.p.x += width;
        }
        boolean[] newDirection = {!direction[0], rotateDirection};
        return new Line (anotherHead, newDirection, newLen);
    }

    private Line[] getSquare(Line[] lines, int width, int leftOrRight) {
        if (width == 1) {
            lines[0] = this;
            return lines;
        }
        getSquare(lines, width - 1, leftOrRight);
        lines[width - 1] = lines[width - 2].getParallelLine(leftOrRight, length);
        return lines;
    }

    public Line[] getSquare(int width, int leftOrRight) {
        return getSquare(new Line[width], width, leftOrRight);
    }

    public Plug getExit() {
        return new Plug(end, direction);
    }
}
