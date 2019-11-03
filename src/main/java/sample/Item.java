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
    public int x = -1;
    public int y = -1 ;
    Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }
}
