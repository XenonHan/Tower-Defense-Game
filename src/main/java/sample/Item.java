package sample;

/**
 * Is the base class of Monster and Tower class, it contain information about coordinate
 * <p>i.e. where it tower or monster locate</p>
 */
public class Item {
    protected Coordinate coord;

    public Item(double x, double y){
        coord = new Coordinate(x,y);
    }
}

/**
 *Is the class representing location
 */
class Coordinate {
    //0< x < 13  && 0< y < 13
    //the coordinate of grid
    public double x;
    public double y;
    //convert grid to pixel
    public double pixel_X;
    public double pixel_Y;
    //only controller will use that info
    public double img_X;
    public double img_Y;
    //slope info is the slope between monster and tower
    public double slope;

    /**
     * Construct an Coordinate object
     * @param x x coordinate of the grid
     * @param y y coordinate of the grid
     */
    Coordinate(double x, double y){
        this.x = x;
        this.y = y;
        pixel_X = x*40 + 20;
        pixel_Y = y*40 + 20;
        img_X = x*40+5;
        img_Y = y*40+5;
    }
}