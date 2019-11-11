package sample;

//this is for test only, you can modifiy to the real Monster.java
//the function below is necessary for Tower.java to implement, please include those functions in your Monster.java

enum MonsterType{NA, Fox, Penguin, Unicorn};

public class Monster extends Item{

	int health;
	int speed;
	int earning;		//how much the player should be rewarded if killed this monster
	MonsterType type;

	public Monster(int x, int y, int health, int earning, MonsterType type)
	{
		super(x,y);
		this.health = health;
		this.earning = earning;
		this.type = type;

	}
	
	public void damage(int health) 
	{
		System.out.println("power = " + health);
		System.out.println("before attack hp = " + this.health);
		this.health-=health;
		if(this.health<0) {System.out.println("get killed");}
	}
	
	public void slowDown(int speed)
	{
		speed-=speed;
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
		//System.out.println("move");
		double fractionOfGrid = 0.3;		//set to 2 for easy debug but actually should be smaller then 1
		double tempY = this.coord.y + fractionOfGrid;
		double tempX = this.coord.x;
		this.coord = new Coordinate(tempX, tempY);
	}

}