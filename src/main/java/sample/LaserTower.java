package sample;

public class LaserTower extends Tower {

	int MAX_MONSTER=50; //you may change this, I do not know the about the arena
	Monster[] SetOfMonster=new Monster[MAX_MONSTER];
	Monster[] subMons=new Monster[MAX_MONSTER]; //other monster near the laser also attacked
	int subPower=power/2;
	int counter=0;
	int subCounter=0;
	int attackCost=3;
	
	public LaserTower(int coodinateX, int coordinateY,int power)
	{
		//range=680 means enough to cover any monster from any rigid with 480x480px
		super(coodinateX, coordinateY, 680 , TowerType.LaserTower,20);
		this.power=power;
	}
	

	
	//You should first call the above function to produce a line equation (shortest monster to tower)
	//Then you can input all the monster to this function to check whether they are in that line
	void PlotLaserRoute(Monster monster[],int size)
	{
		
		double distanceY;
		double distanceX;
		for(int i=0;i<size;i++)
		{
			//y-y1=m(x-x1) -> check the monster is on the line or not
			if(coord.y-monster[i].coord.y-coord.slope*(coord.x-monster[i].coord.x)<0.01)//double compare
			{
				SetOfMonster[counter]=monster[i];
				counter++;
				continue;
			}
		
			//within 3px also attacked
			distanceY=coord.y-monster[i].coord.y;
			distanceX=coord.slope*(coord.x-monster[i].coord.x);
		
			if(Math.abs(distanceY-distanceX)<=3.0001)
			{
				subMons[subCounter++]=monster[i];
				continue;
			}
			else if(Math.abs(distanceY-distanceX)<=coord.slope*3+0.0001)
			{
				subMons[subCounter++]=monster[i];
				continue;
			}
		}
	}
	boolean attackMonster(Monster monster[],int size)
	{
		if(status==TowerStatus.Passive||status==TowerStatus.Destroyed)
			return false;
		
		
		inAttackRange(monster,size);
		coord.slope=(coord.y-closestMon.coord.y)/(coord.x-closestMon.coord.x);
		PlotLaserRoute(monster,size);
		
		for(int i=0;i<counter;i++)
			SetOfMonster[i].damage(power);
		for(int i=0;i<subCounter;i++)
			subMons[i].damage(subPower);
		return true;
	}
	
	boolean upgrade()
	{
		power+=2; //I don't know, you may change
		return true; //You need to reduce recourse in arena
	}
	void newFrame() 
	{
		counter=subCounter=0; //lazy update the two arrays
		coord.slope=0;
		closestMon=null; 
		closestMonDistance=0;
		
	}
	void destroy()
	{
		status=TowerStatus.Destroyed;
		SetOfMonster=null;
		subMons=null;
	}
	
	//laser cost some recourse before attack, handle by arena
	int getAttackCost() {return attackCost;}
	

}

