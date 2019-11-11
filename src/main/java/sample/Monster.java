package sample;

//this is for test only, you can modifiy to the real Monster.java
//the function below is necessary for Tower.java to implement, please include those functions in your Monster.java

enum MonsterType{NA, Fox, Penguin, Unicorn};

public class Monster extends Item{

	int health;
	int speed;
	int earning;		//how much the player should be rewarded if killed this monster
	MonsterType type;
	//FreezeTime=0;
	//normalSpeed=0;

	public Monster(int x, int y, int health, int earning, MonsterType type)
	{
		super(x,y);
		this.health = health;
		this.earning = earning;
		this.type = type;

	}
	
	public void damage(int health) 
	{
		this.health-=health;
	}
	
	public void slowDown(int freezeTime)
	{
		//FreezeTime = freezeTime;
		speed-=3;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public Boolean isDead() {
		return (health<=0)?true:false;
	}

	public MonsterType getType() {
		return type;
	}

	public void move(){
		//incorrect move function for testing only
		System.out.println("move");
		double tempY = this.coord.y + 1;
		double tempX = this.coord.x;
		this.coord = new Coordinate(tempX, tempY);
		
		/*
		if(FreezeTime>0)
			FreezeTime--;
		if(FreezeTime==0)
			speed = normalSpeed;
			*/
	}

}