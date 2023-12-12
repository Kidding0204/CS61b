package byog.Core;

import byog.TileEngine.TETile;

public class Plug extends Dot{
    boolean[] direction;
    public Plug(Dot t, boolean[] direction) {
        super(t.p, t.t);
        this.p = t.p;
        this.t = t.t;
        this.direction = direction;
    }
}
