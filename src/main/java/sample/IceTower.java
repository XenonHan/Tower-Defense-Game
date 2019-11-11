package sample;

/**
 * IceTower expend from Tower
 */
public class IceTower extends Tower {

	/**
	 * the period the monster been slow down
	 */
	int freezeTime;//The monster will slow down by freezeTime after attacked, move slower, you decide the freeze time 
	
	/**
	 * A constructor
	 * @param coodinateX x coordinate
	 * @param coordinateY y coordinate
	 * @param power power
	 * @param freezeTime time to freeze the monster
	 */
	public IceTower(int coodinateX, int coordinateY,int power,int freezeTime)
	{
		super(coodinateX, coordinateY, 65, TowerType.IceTower,13);
		this.power=power;
		this.freezeTime=freezeTime;

	}
	/**
	 * attack monster
	 */
	public boolean attackMonster(Monster monster[],int size)
	{
		if(status==TowerStatus.Passive )
			return false;

		inAttackRange(monster,size);

		if(closestMon == null) {
        	return false;
        }
		
		closestMon.damage(power);
		closestMon.slowDown(freezeTime);
		return true;

	}
	/**
	 * upgrade the tower
	 */
	public void upgrade()
	{
		freezeTime+=3; //I don't know, you may change
		 //You need to reduce recourse in arena
	}


}