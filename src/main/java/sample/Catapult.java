package sample;

/**
 * 
 * Catapult expend from Tower
 *
 */
public class Catapult extends Tower {

	/**
	 * store the monster within 25 px of the attacked monster
	 */
	Monster[] subMonser=new Monster[MAX_MONSTER];
	/**
	 * count the element in array
	 */
	int counter=0;
	/**
	 * damage for the other monster inside 25px of the target
	 */
	int subPower;
	/**
	 * reload time
	 */
	int coolingTime=3; //I set to 3 frame, after that, set the status to active
	/**
	 * remain reload time
	 */
	int remainCoolingPeriod=0;

	/**
	 * A constructor
	 * @param coodinateX x coordinate
	 * @param coordinateY y coordinate
	 * @param power power
	 */
	public Catapult(int coodinateX, int coordinateY,int power)
	{
		super(coodinateX, coordinateY, 150, TowerType.Catapult,15);
		this.power=power;
		subPower = power/2;

	}
	/**
	 * In attackRange of not
	 */
	public void inAttackRange(Monster monster[],int size)
	{
		/**
		 * temp x coordinate
		 */
		double tempX;
		/**
		 * temp y coordinate
		 */
		double tempY;
		/**
		 * temp range
		 */
		double tempRange;
		/**
		 * distance btw monster and end zone
		 */
		double tempClosest;
		if(monster==null)
			return;
		for(int i=0;i<size;i++)
		{
			tempX=monster[i].coord.pixel_X;
			tempY=monster[i].coord.pixel_Y;
			tempRange=Math.sqrt(Math.pow((coord.pixel_X-tempX),2)+Math.pow((coord.pixel_Y-tempY),2));
			tempClosest=Math.sqrt(Math.pow((tempX-440),2)+Math.pow((tempY-0),2));


			if(tempRange<range&&tempRange>50)
			{
				storeclosestMonster(monster[i],tempClosest);

			}
		}


	}


	/**
	 * attack monster
	 *
	 */
	public boolean attackMonster(Monster monster[],int size)
	{

		if(status==TowerStatus.Passive)
		{
			return false;
		}

		inAttackRange(monster,size);


		if(closestMon == null) {
			return false;
		}

		for(int i=0;i<size;i++)
		{
			if(monster[i]==closestMon)
				continue;
			if(Math.abs(Math.sqrt(Math.pow((closestMon.coord.pixel_X-monster[i].coord.pixel_X),2)+Math.pow((closestMon.coord.pixel_Y-monster[i].coord.pixel_Y),2)))<=25)
				subMonser[counter++]=monster[i];
			
		}

		closestMon.damage(power); //attack the target

		for(int i=0;i<counter;i++)// attack other within 25 px of the target
			subMonser[i].damage(subPower);

		status=TowerStatus.Passive; //make it active after some round, now is reloading
		remainCoolingPeriod=coolingTime;

		return true;
	}

	/**
	 * upgrade
	 */
	public void upgrade()
	{
		if(coolingTime==0)
		{//I think we should not allow the ice tower without cooling, at least 1 frame, you may change this as well
			return;
		}
		else
			coolingTime-=1; //I don't know, you may change
		 //You need to reduce recourse in arena
	}

	/**
	 * next round
	 */
	public void newFrame()
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



}