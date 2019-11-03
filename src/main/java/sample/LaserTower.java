package sample;

public class LaserTower extends Tower {


	int MAX_MONSTER=250; //you may change this, I do not know the about the arena
	double slope;
	Monster[] SetOfMonster=new Monster[MAX_MONSTER];
	Monster[] subMons=new Monster[MAX_MONSTER]; //other monster near the laser also attacked
	int subPower=power/2;
	int counter=0;
	int subCounter=0;
	
	public LaserTower(double coodinateX, double coordinateY,int power)
	{
		super(coodinateX, coordinateY, 480 , TowerType.LaserTower);
		this.power=power;
	}
	
	//In function is different to other towers'
	//You should input all monster in the current frame to find the closest monster first
	//This function will store the closest monster and calculate the slope
	//After you call this function and get a slope, you should then call other
	void createLinearEquation(Monster monster)
	{
		double tempX=monster.getX();
		double tempY=monster.getY();
		double tempRange=Math.sqrt(Math.pow((coordinateX-tempX),2)+Math.pow((coordinateY-tempY),2));
		double tmpSlope=(coordinateY-tempY)/(coordinateX-tempX); 
	
		if(closestMon==null||tempRange<closestMonDistance )
		{
			closestMon=monster;
			closestMonDistance=tempRange;
			slope=tmpSlope;
			
		}
		
		
		
	}
	
	//You should first call the above function to produce a line equation (shortest monster to tower)
	//Then you can input all the monster to this function to check wheather they are in that line
	boolean inAttackRange(Monster monster)
	{
		//y-y1=m(x-x1) -> check the monster is on the line or not
		if(coordinateY-monster.getY()-slope*(coordinateX-monster.getX())<0.01)//double compare
		{
			SetOfMonster[counter]=monster;
			counter++;
			return true;
		}
		
		//within 3px also attacked
		double distanceY=coordinateY-monster.getY();
		double distanceX=slope*(coordinateX-monster.getX());
		
		if(distanceY+3-distanceX<=0.01||distanceY-3-distanceX<=0.01)
		{
			subMons[subCounter++]=monster;
			return true;
		}
		else if(distanceY-distanceX-slope*3<=0.01||distanceY-distanceX+slope*3<=0.01)
		{
			subMons[subCounter++]=monster;
			return true;
		}
		return false;
	}
	boolean attackMonster()
	{
		if(status==false)
			return false;
		
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
		slope=0;
		closestMon=null; 
		closestMonDistance=0;
		
	}
}

