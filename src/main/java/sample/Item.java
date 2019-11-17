package sample;

/**
 * Is the base class of Monster and Tower class, it contain information about coordinate
 * <p>i.e. where it tower or monster locate</p>
 */
public class Item {
    /**
     * the coordinate that item locate
     */
    protected Coordinate coord;

    /**
     * set the coordinate of that item
     * @param x the x coordinate that the item locate
     * @param y the y coordinate that the item locate
     */
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
    /**
     * x coordinate of the grid in terms of number of grids
     */
    public double x;
    /**
     * y coordinate of the grid in terms of number of grids
     */
    public double y;
    //convert grid to pixel
    /**
     * x coordinate of the grid in terms of pixel
     */
    public double pixel_X;
    /**
     * y coordinate of the grid in terms of pixel
     */
    public double pixel_Y;
    //only controller will use that info
    /**
     * x coordinate of top left corner of an image in terms of pixel
     */
    public double img_X;
    /**
     * y coordinate of top left corner of an image in terms of pixel
     */
    public double img_Y;
    //slope info is the slope between monster and tower
    /**
     * slope between tower and targeted monster, this data will be modify by the tower class
     */
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