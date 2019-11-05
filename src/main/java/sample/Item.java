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
    public double slope; //for plot the attack route
    Coordinate(int x, int y){
        this.x = x;
        this.y = y;
        slope=0;
    }
}
