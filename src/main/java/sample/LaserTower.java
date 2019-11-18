package sample;

/**
 * Laser tower class expend from Tower
 */
public class LaserTower extends Tower {

    /**
     * monsters in the laser line
     */
    Monster[] SetOfMonster=new Monster[MAX_MONSTER];
    /**
     * monster within 3px of laser line
     */
    Monster[] subMons=new Monster[MAX_MONSTER]; //other monster near the laser also attacked
    /**
     * power for not hit directly
     */
    int subPower=0;//no on the line receive less damage
    /**
     * array counter
     */
    int counter=0;
    /**
     * another array counter
     */
    int subCounter=0;
    /**
     * cost for attack
     */
    int attackCost=2;

    /**
     * A constructor
     * @param coodinateX x coordinate
     * @param coordinateY y coordinate
     * @param power power of the tower
     */
    public LaserTower(int coodinateX, int coordinateY,int power)
    {
        super(coodinateX, coordinateY, 160 , TowerType.LaserTower,20);
        this.power=power;
        subPower=power-2;
    }



    //You should first call the above function to produce a line equation (shortest monster to tower)
    //Then you can input all the monster to this function to check whether they are in that line
    /**
     * plot the laser line and use this to check whether there is any monster in the line or 3px within the line
     */
    private void PlotLaserRoute(Monster monster[],int size)
    {
    	/**
    	 * A attribute to find the distance btw line and point
    	 */
    	double A=0;
    	
    	/**
    	 * B attribute to find the distance btw line and point
    	 */
    	double B=0;
    	
    	/**
    	 * C attribute to find the distance btw line and point
    	 */
    	double C=0;
    	/**
    	 * distance btw a point and line
    	 */
    	double distance=0;
 
        for(int i=0;i<size;i++)
        {
        	if(closestMon.coord.pixel_X-coord.pixel_X>0&&monster[i].coord.pixel_X-coord.pixel_X<=0)
				continue;

			else if (closestMon.coord.pixel_X-coord.pixel_X<0&&monster[i].coord.pixel_X-coord.pixel_X>=0)
				continue;

        	if(closestMon.coord.pixel_X==coord.pixel_X)
        	{
        		if(monster[i].coord.pixel_X==coord.pixel_X)
        		{
        			 SetOfMonster[counter++]=monster[i];
        			 continue;
        		}
        		
        			
        	}
        	else if(Math.abs(coord.pixel_Y-monster[i].coord.pixel_Y-coord.slope*(coord.pixel_X-monster[i].coord.pixel_X))<0.01)//double compare
            {
                SetOfMonster[counter]=monster[i];
                counter++;
                continue;
            }
            
            //find whether a point with 3px
           B=1;
           A=-coord.slope;
           C=coord.pixel_X*coord.slope-coord.pixel_Y;
           distance = Math.abs((A*monster[i].coord.pixel_X)+(B*monster[i].coord.pixel_Y)+C) / (A*A+B*B); //Don't ask me to prove this 
           if(distance<=3)
        	   subMons[subCounter++]=monster[i];

        }
    }
    /**
     * attack monster
     */
    public boolean attackMonster(Monster monster[],int size)
    {
        if(status==TowerStatus.Passive)
            return false;


        inAttackRange(monster,size);

        if(closestMon == null) {
            return false;
        }
        
        if(coord.pixel_X-closestMon.coord.pixel_X!=0)
        	coord.slope=(coord.pixel_Y-closestMon.coord.pixel_Y)/(coord.pixel_X-closestMon.coord.pixel_X);
        

        PlotLaserRoute(monster,size);

        for(int i=0;i<counter;i++)
        {
            SetOfMonster[i].damage(power);
        }
        for(int i=0;i<subCounter;i++)
        {
            subMons[i].damage(subPower);
        }
        return true;
    }

    /**
     * upgrade
     */
    public void upgrade()
    {
        power+=2; //I don't know, you may change
        attackCost++;//You need to reduce recourse in arena
        
    }
    public void newFrame()
    {
        counter=subCounter=0; //lazy update the two arrays
        coord.slope=0;
        closestMon=null;
        closestMonDistance=0;

    }

    //laser cost some recourse before attack, handle by arena
    /**
     * return attack cost
     * @return attack cost
     */
    public int getAttackCost() {return attackCost;}


}