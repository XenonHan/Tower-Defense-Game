package sample;
/**
 * 
 *Unicorn Class for the monster Unicorn
 *
 */
public class Unicorn extends Monster {
	/**
	 * It is the constructor for Unicorn
	 * @param health monster hp
	 * @param earning monster reward
	 * @param speed monster speed
	 */
	public Unicorn(int health, int earning,int speed)
	{
		super(health,earning,MonsterType.Unicorn,speed);
	}


}
