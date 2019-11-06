package sample;

public class Catapult extends Tower {

	int MAX_MONSTER=50; //this indicate the max. # of monster within 25 px of the closest monster to the tower
	Monster[] subMonser=new Monster[MAX_MONSTER];//this array store the set of monster that within 25 px of closest monster, but cannot closer than closest monster.
	//otherwise, I will update that monster to the closest one
	int counter=0;//array counter
	int subPower=power/2; //since other monster within 25px of the closest monster also been attacked, but less power, you may change this

	int coolingTime=4; //I set to 3 frame, after that, set the status to active
	int remainCoolingPeriod;

	public Catapult(int coodinateX, int coordinateY,int power)
	{
		super(coodinateX, coordinateY, 150, TowerType.Catapult,15);
		this.power=power;

	}

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
			tempClosest=Math.sqrt(Math.pow((coord.pixel_X-440),2)+Math.pow((coord.pixel_Y-0),2));

			if(tempRange<range&&tempRange>50)
			{
				storeclosestMonster(monster[i],tempClosest);

			}
		}

	}


	boolean attackMonster(Monster monster[],int size)
	{

		if(status==TowerStatus.Passive||status==TowerStatus.Destroyed)
		{
			return false;
		}

		inAttackRange(monster,size);
		

		if(closestMon == null) {
        	return false;
        }

		for(int i=0;i<size;i++)
		{
			if(Math.sqrt(Math.pow((closestMon.coord.pixel_X-monster[i].coord.pixel_X),2)+Math.pow((closestMon.coord.pixel_X-monster[i].coord.pixel_Y),2))<=25)
				subMonser[counter++]=monster[i];
		}

		closestMon.damage(power); //attack the target

		for(int i=0;i<counter;i++)// attack other within 25 px of the target
			subMonser[i].damage(subPower);

		status=TowerStatus.Passive; //make it active after some round, now is reloading
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
			status=TowerStatus.Active;
		}
		else
			remainCoolingPeriod--;

		counter=0; //lazy update the array

		closestMon=null;
		closestMonDistance=0;

	}

	void destroy()
	{
		status=TowerStatus.Destroyed;
		subMonser=null;
	}

}