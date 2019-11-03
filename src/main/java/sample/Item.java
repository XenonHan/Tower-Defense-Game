package sample;


public class Item {
    //enum ObjectState {NORMAL, DEAD, FROZEN};
    protected Coordinate coord;

    public Item(int x, int y){
        coord = new Coordinate();
    }
    //public isInRange(Monster m);
}

class Coordinate {
    public int x = -1;
    public int y = -1 ;
}
