package sample;

//this is for test only, you can modifiy to the real Monster.java
//the function below is necessary for Tower.java to implement, please include those functions in your Monster.java

enum MonsterType{NA, Fox, Penguin, Unicorn};

public class Monster extends Item{

	int health;
	int speed;
	MonsterType type;

	public Monster(int x, int y)
	{
		super(x,y);
	}
	
	public void damage(int health) 
	{
		this.health-=health;
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
}