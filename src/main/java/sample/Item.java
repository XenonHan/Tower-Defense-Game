package sample;


public class Item {
    //enum ObjectState {NORMAL, DEAD, FROZEN};
    protected Coordinate coord;

    public Item(int x, int y){
        coord = new Coordinate(x, y);
    }
    //public isInRange(Monster m);
}

class Coordinate {
    public int x;
    public int y;
    Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }
}
