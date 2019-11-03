package sample;

public class Catapult extends Tower {
	
	int MAX_MONSTER=50; //this indicate the max. # of monster within 25 px of the closest monster to the tower
	Monster[] subMonser=new Monster[MAX_MONSTER];//this array store the set of monster that within 25 px of closest monster, but cannot closer than closest monster.
													//otherwise, I will update that monster to the closest one
	int counter=0;//array counter
	int subPower=power/2; //since other monster within 25px of the closest monster also been attacked, but less power, you may change this
	
	int coolingTime=4; //I set to 3 frame, after that, set the status to active
	int remainCoolingPeriod;
	
	public Catapult(double coodinateX, double coordinateY,int power)
	{
		super(coodinateX, coordinateY, 150, TowerType.Catapult);
		this.power=power;
		
	}
	
	boolean inAttackRange(Monster monster)
	{
		double tempX=monster.getX();
		double tempY=monster.getY();
		double tempRange=Math.sqrt(Math.pow((coordinateX-tempX),2)+Math.pow((coordinateY-tempY),2));
		
		if(tempRange<range&&tempRange>50)
		{
			storeclosestMonster(monster,tempRange);
			return true;
		}
		return false;
	
		
	}
	
	boolean storeclosestMonster(Monster monster,double tempRange)
	{
		//no tower, so first one is closest
		if(closestMon==null)
		{
			closestMon=monster;
			closestMonDistance=tempRange;
			return true;
		}
		
		//another monster within the tower attack range, but farer than closest monster
		if(tempRange>=closestMonDistance && tempRange-closestMonDistance<=25 )
		{
			subMonser[counter]=monster;
			counter++;
			return true;
		}
		
		//if the new monster shorter than previous one, update the closest monster, and also the array
		//if the new monster shorter than previous one and the distance between them larger than  25, simply use new array
		//because all the monster inside the array must also larger than the new monster
		if(tempRange<closestMonDistance )
		{
			Monster[] tempMonster=new Monster[MAX_MONSTER];
			double tempDistance;
			int k=0;
			
			if(closestMonDistance-tempRange<=25)
			{
				
				for(int i=0;i<counter;i++)
				{
					tempDistance=Math.sqrt(Math.pow((coordinateX-subMonser[i].getX()),2)+Math.pow((coordinateY-subMonser[i].getY()),2));
					if(tempDistance-tempRange<=25)
					{
						tempMonster[k]=subMonser[i];
						k++;
					}
				}
				tempMonster[k++]=closestMon; //we also need to store this to the array as it aslo within 25px
					
			}
			
			counter=k;
			subMonser= tempMonster; //Java have garbage collector
			
			closestMon=monster;
			closestMonDistance=tempRange;
			return true;
		}
		
		return false;
	
	
	}
	 
	boolean attackMonster()
	{	
		
		if(status==false)
		{
			return false;
		}
		
		
		closestMon.damage(power); //attack the target
		
		for(int i=0;i<counter;i++)// attack other within 25 px of the target
			subMonser[i].damage(subPower);
		
		status=false; //make it active after some round, now is reloading
		remainCoolingPeriod=coolingTime;
		
		return true;
	}
	
	boolean upgrade()
	{
		if(coolingTime==1) //I think we should not allow the ice tower without cooling, at least 1 frame, you may change this as well
			return false; 
					
		coolingTime-=1; //I don't know, you may change
		return true; //You need to reduce recourse in arena
	}
	
	void newFrame() 
	{
		//if the tower not active, cannot attack, but still reduce the remaining cooling time, when no cooling time, active the tower
		if(remainCoolingPeriod==0)
		{
			status=true;
		}
		else
			remainCoolingPeriod--;
		
		counter=0; //lazy update the array
		
		closestMon=null; 
		closestMonDistance=0;
		
	}

}
