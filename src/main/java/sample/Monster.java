package sample;

/**
 * monster type enum
 *
 */
enum MonsterType{Fox, Penguin, Unicorn};

/**
 * The basic class for the three monsters
 *
 */
public  class Monster extends Item{

	/**
	 *  hp
	 */
	int health;
	/**
	 * monster moving speed
	 */
	int speed;
	/**
	 * reward when the monster were killed
	 */
	int earning;
	/**
	 * monster type
	 */
	MonsterType type;
	/**
	 * the array store the monster name
	 */
	String[] Montype= {"Fox", "Penguin", "Unicorn"};
	/**
	 * the freeze time of the monster by ice tower
	 */
	int freezeTime=0;
	/**
	 * the monster's original speed, use by ice tower
	 */
	int normalSpeed=0;

	/**
	 * monster constructor
	 * @param health monster hp		
	 * @param earning reward gained from monster
	 * @param type type of monster
	 * @param speed speed of monster
	 */
	public Monster(int health, int earning, MonsterType type, int speed)
	{
		super(0,0);
		this.health = health;
		this.earning = earning;
		this.type = type;
		this.speed =normalSpeed=speed;

		System.out.println(Montype[type.ordinal()] +" "+ health + " hp generated");
	}

	/**
	 * reduce the monster  hp
	 *
	 * @param health monster hp
	 */
	public void damage(int health)
	{
		this.health-=health;
	}

	/**
	 * update the freeze time by ice tower, the time cannot more then 3, and the monster would been stopped
	 * @param freezeTime the time freeze by ice tower
	 */
	public void slowDown(int freezeTime)
	{
		this.freezeTime = freezeTime;
		if(speed==normalSpeed)
			speed-=9;
	}

	/**
	 * return speed
	 * @return speed
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * check whether the monster dead or not
	 * @return true or false
	 */
	public Boolean isDead() {
		return (health<=0)?true:false;
	}
	/**
	 * return the monster type
	 * @return monster type
	 */
	public MonsterType getType() {
		return type;
	}

	/**
	 * move the monster
	 * Also update the remaining freeze time, if any
	 */
	public void move(){

		/**
		 * temp variable for monster's x coordinate
		 */
		double moveX=0;
		/**
		 * temp variable for monster's y coordinate
		 */
		double moveY=0;
		/**
		 * the distance the monster move
		 */
		double distance;
		if(speed>0)
		{
			if(coord.pixel_X<40||coord.pixel_X>160&&coord.pixel_X<200||coord.pixel_X>320&&coord.pixel_X<360)
			{
				distance=460-coord.pixel_Y-speed; //420 means in the middle of the grid, monster not move to the bottom of the bottom grid
				if(coord.pixel_Y==20&&coord.pixel_X!=20)
				{
					if(coord.pixel_X-320>0&&coord.pixel_X+speed>=340)
					{
						moveX=340-coord.pixel_X;
						moveY=speed-moveX;
					}
					else if(coord.pixel_X-160>0&&coord.pixel_X+speed>=180)
					{
						moveX=180-coord.pixel_X;
						moveY=speed-moveX;
					}
					else
					{
						moveX=speed;
					}
				}
				else if(distance >=0)
				{
					moveY=speed;
				}
				else
				{
					moveY=460-coord.pixel_Y;
					moveX=speed-moveY;
				}

				coord.pixel_X+=moveX;
				coord.pixel_Y+=moveY;
				this.coord=new Coordinate((coord.pixel_X-20)/40,(coord.pixel_Y-20)/40);


			}
			else if(coord.pixel_X>80&&coord.pixel_X<120||coord.pixel_X>240&&coord.pixel_X<280||coord.pixel_X>400&&coord.pixel_X<440)
			{
				distance=coord.pixel_Y-speed-20;
				if(coord.pixel_Y==460)
				{
					if(coord.pixel_X-400>0&&coord.pixel_X+speed>=420)
					{
						moveX=420-coord.pixel_X;
						moveY=speed-moveX;
					}
					else if(coord.pixel_X-240>0&&coord.pixel_X+speed>=260)
					{
						moveX=260-coord.pixel_X;
						moveY=speed-moveX;
					}
					else if(coord.pixel_X-80>0&&coord.pixel_X+speed>=100)
					{
						moveX=100-coord.pixel_X;
						moveY=speed-moveX;
					}
					else
					{

						moveX=speed;
					}
				}
				else if(distance >=0)
				{
					moveY=speed;
				}
				else
				{
					moveY=coord.pixel_Y-20;
					moveX=speed-moveY;

				}

				coord.pixel_X+=moveX;
				coord.pixel_Y-=moveY;
				this.coord=new Coordinate((coord.pixel_X-20)/40,(coord.pixel_Y-20)/40);
			}
			else
			{
				coord.pixel_X+=speed;
				this.coord=new Coordinate((coord.pixel_X-20)/40,(coord.pixel_Y-20)/40);
			}

		}
		if(freezeTime>0)
			freezeTime--;
		if(freezeTime==0)
			speed = normalSpeed;


	}


}