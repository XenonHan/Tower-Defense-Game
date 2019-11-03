package sample;

//this is for test only, you can modifiy to the real Monster.java
//the function below is necessary for Tower.java to implement, please include those functions in your Monster.java
public class Monster {

	
	double x,y;
	int health;
	int speed;
	

	public Monster(){}
	public Monster(double x, double y)
	{
		this.x=x;
		this.y=y;
	}
	
	public double getX() {return x;}
	public double getY() {return y;}
	
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
	
	public Boolean isDead(int health) {
		return (health<=0)?true:false;
	}
}
