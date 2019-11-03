package sample;

public class BasicTower extends Tower{
	

	
	public BasicTower(int coodinateX, int coordinateY,int power)
	{
		super(coodinateX, coordinateY, 65, TowerType.BasicTower,10);
		this.power=power;
	}
	
	boolean attackMonster()
	{
		if(status==false)
			return false;
		
		closestMon.damage(power); 
		return true;
	}
	
	boolean upgrade()
	{
		power+=3; //I don't know, you may change
		return true; //You need to reduce recourse in arena
	}
}