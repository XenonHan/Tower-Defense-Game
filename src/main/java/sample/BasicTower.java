package sample;
/**
 * 
 * Basic tower extended from tower
 *
 */
public class BasicTower extends Tower{


	/**
	 * A constructor
	 * @param coodinateX x coordinate of tower
	 * @param coordinateY y coordinate of tower
	 * @param power power of basic tower
	 */
    public BasicTower(int coodinateX, int coordinateY,int power)
    {
        super(coodinateX, coordinateY, 65, TowerType.BasicTower,10);
        this.power=power;
    }

    /**
     * Overwrite the same function in Tower class, 
     * To perform special attack
     */
    public boolean attackMonster(Monster monster[],int size)
    {
        if(status==TowerStatus.Passive)
            return false;

        inAttackRange(monster,size);
        
        if(closestMon == null) {
        	return false;
        }
        closestMon.damage(power);
        return true;
    }

    /**
     * 
     * @return no cost needed
     */
    int costForAttack() {return 0;}
    
    /**
     * overwrite the same function base class Tower to upgrade
     */
    public void upgrade()
    {
        power+=3; //I don't know, you may change
        //You need to reduce recourse in arena
    }

}