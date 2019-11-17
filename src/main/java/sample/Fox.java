package sample;
/**
 * This is the class for monster Fox
 *
 */
public class Fox extends Monster{
	/**
	 * Fox constructor
	 * 
	 * @param health fox hp
	 * @param earning the reward could earn from fox
	 * @param speed the speed of the fox
	 */
	public Fox(int health, int earning,int speed)
	{
		super(health,earning,MonsterType.Fox,speed);
		
	}
	
	 
}
