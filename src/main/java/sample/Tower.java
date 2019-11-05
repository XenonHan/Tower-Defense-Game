package sample;


enum TowerType{BasicTower, IceTower, Catapult,LaserTower}
enum TowerStatus{Active, Passive, Destroyed}


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
    TowerType getTowerTyper() {return type;}
    int getUpgradeCost() {return cost/3;}//it is the cost for upgrade the tower


    //This function check whether the monster is in the attack range of tower
    void inAttackRange(Monster monster[],int size)
    {
        double tempX;
        double tempY;
        double tempRange;
        for(int i=0;i<size;i++)
        {
            tempX=monster[i].coord.x;
            tempY=monster[i].coord.y;
            tempRange=Math.sqrt(Math.pow((coord.x-tempX),2)+Math.pow((coord.y-tempY),2));

            if(tempRange<=range)
            {
                storeclosestMonster(monster[i],tempRange);
            }
        }

    }


    //This function store/update the closest monster to the tower
    boolean storeclosestMonster(Monster monster,double tempRange)
    {
        if(closestMon==null ||tempRange<closestMonDistance)
        {
            closestMon=monster;
            closestMonDistance=tempRange;
            return true;
        }

        return false;

    }

    void newFrame() //Each tower should  this to update the locate storage before start next frame
    {
        closestMon=null;
        closestMonDistance=0;
    }

    //destroy the tower
    void destroy()
    {
        status=TowerStatus.Destroyed;
    }

    //for arena to plot the graph
    Coordinate getGraph()
    {
        Coordinate temp=closestMon.coord;
        temp.slope=coord.slope;//store the slope btw tower and monster for laser tower;
        return temp;
    }
    abstract boolean upgrade(); // you should handle player's recourse in arena
    abstract boolean attackMonster(Monster monster[], int size);

}