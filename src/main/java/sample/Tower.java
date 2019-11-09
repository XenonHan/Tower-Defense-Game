package sample;


enum TowerType{BasicTower, IceTower, Catapult, LaserTower}
enum TowerStatus{Active, Passive}


public abstract class Tower extends Item{
    protected int MAX_MONSTER=100;//you may change latter
    protected double range;
    protected int power;
    protected TowerType type;
    protected TowerStatus status;
    protected int cost;


    //monster data, the closest one
    protected Monster closestMon=null;
    protected double closestMonDistance=0;



    Tower(int x, int y, double range, TowerType type,int cost)
    {
        super(x,y);
        this.range=range;
        this.type=type;
        status=TowerStatus.Active;
        this.cost=cost;

    }
    int getCost() {return cost;}
    TowerType getTowerType() {return type;}
    int getUpgradeCost() {return cost/3;}//it is the cost for upgrade the tower
    int getAttackCost() {return 0;}

    //This function check whether the monster is in the attack range of tower
    void inAttackRange(Monster monster[],int size)
    {
        double tempX;
        double tempY;
        double tempRange;
        double tempClosest;
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


    //This function store/update the closest monster to the tower
    boolean storeclosestMonster(Monster monster,double tempClosest)
    {

        if(closestMon==null ||tempClosest<closestMonDistance)
        {
            closestMon=monster;
            closestMonDistance=tempClosest;
            return true;
        }

        return false;

    }

    void newFrame() //Each tower should  this to update the locate storage before start next frame
    {
        closestMon=null;
        closestMonDistance=0;
    }


    //for arena to plot the graph
    //Or to get the closest monster been attacked
    Monster getGraph()
    {
        if(closestMon == null) {
            return null;
        }
        Monster temp=closestMon;

        temp.coord.slope = coord.slope;//store the slope btw tower and monster for laser tower;
        return temp;
    }
    abstract boolean upgrade(); // you should handle player's recourse in arena
    abstract boolean attackMonster(Monster monster[], int size);


}