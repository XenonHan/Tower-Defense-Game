package sample;

public class IceTower extends Tower {
	
	int freezeTime;//The monster will slow down by freezeTime after attacked, move slower, you decide the freeze time 
	
	public IceTower(int coodinateX, int coordinateY,int power,int freezeTime)
	{
		super(coodinateX, coordinateY, 65, TowerType.IceTower,13);
		this.power=power;
		this.freezeTime=freezeTime;
		
	}
	
	boolean attackMonster(Monster monster[],int size)
	{
		if(status==TowerStatus.Passive||status==TowerStatus.Destroyed )
			return false;
		
		inAttackRange(monster,size);
		
		closestMon.damage(power); 
		closestMon.slowDown(freezeTime); 
		return true;
		
	}
	boolean upgrade()
	{
		freezeTime+=3; //I don't know, you may change
		return true; //You need to reduce recourse in arena
	}

}
