package sample;

/**
 * It is the class for Penguin
 *
 */
public class Penguin extends Monster{
	/**
	 * it store the total amount of the HP at the start time
	 */
	private int HP;
	
	/**
	 * It is a constructor for Penguin
	 * @param health monster HP
	 * @param earning monster reward
	 * @param speed monster speed
	 */
	public Penguin(int health, int earning,int speed)
	{
		super(health,earning,MonsterType.Penguin,speed);
		HP=health;

	}
	/**
	 * This is the function to move the Penguin
	 * Also gain some HP after each round
	 */
	public void move()
	{
		super.move();

		if(health+5<=HP)
			health+=5;
		else
			health=HP;
	}

}
