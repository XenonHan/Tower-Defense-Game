package sample;

public class LaserTower extends Tower {


	int MAX_MONSTER=250; //you may change this, I do not know the about the arena
	Monster[] SetOfMonster=new Monster[MAX_MONSTER];
	Monster[] subMons=new Monster[MAX_MONSTER]; //other monster near the laser also attacked
	int subPower=power/2;
	int counter=0;
	int subCounter=0;
	
	public LaserTower(int coodinateX, int coordinateY,int power)
	{
		super(coodinateX, coordinateY, 480 , TowerType.LaserTower,20);
		this.power=power;
	}
	
	//In function is different to other towers'
	//You should input all monster in the current frame to find the closest monster first
	//This function will store the closest monster and calculate the slope
	//After you call this function and get a slope, you should then call other
	void createLinearEquation(Monster monster)
	{
		double tempX=monster.coord.x;
		double tempY=monster.coord.y;
		double tempRange=Math.sqrt(Math.pow((coord.x-tempX),2)+Math.pow((coord.y-tempY),2));
		double tmpSlope=(coord.y-tempY)/(coord.x-tempX); 
	
		if(closestMon==null||tempRange<closestMonDistance )
		{
			closestMon=monster;
			closestMonDistance=tempRange;
			coord.slope=tmpSlope;
			
		}
		
		
		
	}
	
	//You should first call the above function to produce a line equation (shortest monster to tower)
	//Then you can input all the monster to this function to check wheather they are in that line
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
		
			if(Math.abs(distanceY-distanceX)<=3)
			{
				subMons[subCounter++]=monster[i];
				continue;
			}
			else if(Math.abs(distanceY-distanceX)<=coord.slope*3)
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
}

