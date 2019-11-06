package sample;


public class Item {
    //enum ObjectState {NORMAL, DEAD, FROZEN};
    protected Coordinate coord;

    public Item(int x, int y){
        coord = new Coordinate(x,y);
        setCoord(x, y);
    }
    public Coordinate getCoordinate(){
        return coord;
    }
    public void setCoord(int x, int y){
        coord.x = x;
        coord.y = y;
    }
    //public isInRange(Monster m);
}

class Coordinate {
    public int x;
    public int y;
    public int pixel_X;
    public int pixel_Y;
    public int img_X;
    public int img_Y;

    public double slope;
    Coordinate(int x, int y){
        this.x = x;
        this.y = y;
        pixel_X = x*40 + 20;
        pixel_Y = y*40 + 20;
        img_X = x*40+5;
        img_Y = y*40+5;
    }
}