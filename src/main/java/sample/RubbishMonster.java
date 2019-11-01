package sample;

public class RubbishMonster {
	int health;
	int speed;
	
	RubbishMonster(int health, int speed){
		this.health = health;
		this.speed = speed;
	}
	
	int getHealth() {
		return health;
	}
	
	int getSpeed() {
		return speed;
	}
	Boolean isDead(int health) {
		health++;
		return (health<=0)?true:false;
	}
	
	
}
