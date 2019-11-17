package sample;



enum TowerType{BasicTower, IceTower, Catapult, LaserTower}


enum TowerStatus{Active, Passive}

/**
 * 
 * Tower class is used to generated tower in the game
 * This class is the base class of the Basic, Ice, Catapult and laser Tower.
 *
 */
public abstract class Tower extends Item{
	/**
	 * max monster in a range
	 */
    protected int MAX_MONSTER=300;//you may change latter
    /**
     * attack range of tower
     */
    protected double range;
    /**
     * power of tower
     */
    protected int power;
    /**
     * type of tower
     */
    protected TowerType type;
    /**
     * status of tower
     */
    protected TowerStatus status;
    /**
     * cost to build tower
     */
    protected int cost;
    /**
     * current level of tower
     */
    protected int level;


    //monster data, the closest one
    /**
     * closest monster
     */
    protected Monster closestMon=null;
    
    /**
     * the distance from the closest monster to end zone
     */
    protected double closestMonDistance=0;


/**
 * A constructor
 * @param x an x coordinate of the tower in pixel
 * @param y an y coordinate of the tower in pixel
 * @param range the attack range of the tower
 * @param type the type of the tower
 * @param cost the cost for building this type of tower
 */
    public Tower(int x, int y, double range, TowerType type,int cost)
    {
        super(x,y);
        this.range=range;
        this.type=type;
        status=TowerStatus.Active;
        this.cost=cost;
        level=1;
    }
    /**
     * get cost needed to build the tower
     * 
     * @return the cost need for building the tower
     */
    public int getCost() {return cost;}
    
    /**
     * get the type of the tower
     * @return the type of the tower
     */
    public TowerType getTowerType() {return type;}
    
    /**
     * get the upgrade cost 
     * 
     * @return the cost for the tower upgrade
     */
    public int getUpgradeCost() {return (int)(cost*1.2);}//it is the cost for upgrade the tower
    
    /**
     * get cost need for attack
     * @return the cost for each attack (if any)
     */
    public int getAttackCost() {return 0;}

    //This function check whether the monster is in the attack range of tower
    /**
     * Check whether the monster is in the attack range
     * @param monster the array store all live monsters
     * @param size the size of the array
     */
    public void inAttackRange(Monster monster[],int size)
    {
    	/**
    	 * x coordinate
    	 */
        double tempX;
        
        /**
         * y coordinate
         */
        double tempY;
        
        /**
         * distance btw monster and tower
         */
        double tempRange;
        
        /**
         * distance btw monster to end zone
         */
        double tempClosest;
        
        if(monster==null)
        	return;
        
        for(int i=0;i<size;i++)
        {
            tempX=monster[i].coord.pixel_X;
            tempY=monster[i].coord.pixel_Y;
            tempRange=Math.sqrt(Math.pow((coord.pixel_X-tempX),2)+Math.pow((coord.pixel_Y-tempY),2));
            //System.out.println("temp range = " + tempRange);
            tempClosest=Math.sqrt(Math.pow((tempX-440),2)+Math.pow((tempY-0),2));

            if(tempRange<=range)
            {
                storeclosestMonster(monster[i],tempClosest);
            }
        }

    }


  
    /**
     * store the closest monster to the tower.
     * This function designed to fulfill the requirement that "a tower always pick the monster closest to the upper-left of end zone"
     * What here done to use the formula to find the monster which is closest to the end zone by coordinate, not by path
     * Because to fulfill the game physics, closest monster to end zone, so straight line from start to end zone always closest
     * @param monster a specific monster
     * @param tempClosest the distance between the monster and the tower
     * @return successful store or not
     */
    public boolean storeclosestMonster(Monster monster,double tempClosest)
    {

        if(closestMon==null ||tempClosest<closestMonDistance)
        {
            closestMon=monster;
            closestMonDistance=tempClosest;
            return true;
        }

        return false;

    }
    /**
     * clean the temp data before enter next frame
     */
    public void newFrame() //Each tower should  this to update the locate storage before start next frame
    {
        closestMon=null;
        closestMonDistance=0;
    }


    //for arena to plot the graph
    //Or to get the closest monster been attacked
    /**
     * This is a helper function help arena to plot graph
     * @return a monster +  slope of the line from laser tower (if need)
     */
    public Monster getGraph()
    {
        if(closestMon == null) {
            return null;
        }
        Monster temp=closestMon;

        temp.coord.slope = coord.slope;//store the slope btw tower and monster for laser tower;
        return temp;
    }
    /**
     * this is the update tower function
     */
    public abstract void upgrade(); // you should handle player's recourse in arena
    
    /**
     * This function used to attack the closest monster
     * 
     * @param monster a monster array
     * @param size size of the array
     * @return attack is successful or not
     */
    public abstract boolean attackMonster(Monster monster[], int size);


}