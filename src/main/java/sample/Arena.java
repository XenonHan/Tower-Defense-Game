package sample;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.util.Random;
import java.util.Stack;

/**
 * Is the arena of game, contain information about the current game state
 */
public class Arena {

	private Item items[];
	private int num_items;
	private int money;
	private int turn;
	private AnchorPane paneArena;
	private Stack<Shape> attackGraphic = new Stack<>();
	private Coordinate endZone = new Coordinate(11, 0);
	private int generate;
	
	/**
	 * This is the array use to store the monster HP
	 */
	int []monstorHP= {15,30,50};//record the monster HP

	private int getNumOfMOnsterGererate(){
		if(turn%30 == 0){
			generate++;
		}
		if (turn % (generate+1) > 0) {
			//System.out.println("monster generated");
			return generate;
		}
		return 0;
        //return 20;
	}
	//todo should make back to private afterwards

	/**
	 * generate 0 or more number of monster in each turn
	 */
	protected void generateMonster() {
		Random rand = new Random();
		Monster e;
		
		
		if(turn !=0 && turn%10==0)
		{
			for(int i=0;i<3;i++)
				monstorHP[i]+=5;
		}
//
//		int numMonsterGenerate = getNumOfMOnsterGererate();
//		//todo numMonsterGenerate should increase as time passes
//		for (int i=0; i<numMonsterGenerate; i++){
//			todo hp should increase as time passes
			int randInt = rand.nextInt(3);
//			int X = rand.nextInt(6);
			int randX[] = {0,2,4,6,8,10};
			switch(randInt){
				//todo implements to call correctly after Boby's work
				case 0: e = new Fox(monstorHP[0], 8,20); break;
				case 1: e = new Penguin(monstorHP[1],15,10);break;
				case 2: e = new Unicorn(monstorHP[2],10,15); break;
				default: throw new IllegalArgumentException();
			}
			addItem(e);
//		}
	}

	/**
	 * Create a Arena with given AnchorPane
	 * @param paneArena AnchorPane of the game scene
	 */
	public Arena(AnchorPane paneArena) {
		items = null;
		num_items = 0;
		money = 45;         //initial amount of resources
		turn = 1;
		this.paneArena = paneArena;
		generate=1;
	}

	/**
	 * @return the number of items in the arena including both monsters and towers
	 */
	public int getNumItems(){ return num_items; }
	/**
	 * @return the resources (money) the player have
	 */
	public int getMoney() { return money; }

    /**
     * @return an array of item in the arena, include all tower and monster
     */
	public Item[] getItems(){ return items; }

	/**
	 * @return a stack of graphic(line and circle) that show in the GUI to indicate the tower attack
	 */
	public Stack<Shape> getAttackGraphic(){
		return attackGraphic;
	}
	/**
	 * check weather the player lose the game or not
	 * @return True if lose, False if otherwise
	 */
	public boolean isGameOver() {
		for(int i=0; i<num_items; i++) {
			if(items[i] instanceof Monster){
				Coordinate coord = items[i].coord;
				//todo check coordinate
				if(coord.pixel_X > endZone.pixel_X-20){
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Adding an item to the arena
	 * @param newItem the item that will be add to the arena, it can be monster or tower
	 */
	private void addItem(Item newItem) {
		//System.out.println("adding item");
		Item temp[] = new Item[++num_items];
		for(int i=0; i<num_items-1; i++){
			temp[i]=items[i];
		}
		temp[num_items-1] = newItem;
		items = temp;
	}
	/**
	 * Remove an item from the arena
	 * @param item the item that will be remove from the arena, it can be monster or tower
	 */
	public void removeItem(Item item){				//remove a monster or tower from the item array
		if(item==null){
			throw new IllegalArgumentException();
		}
		num_items--;
		Item temp[] = new Item[num_items];
		for(int i=0, j=0; i<num_items+1; i++, j++){
			if(items[i]==item){
				j--;
				continue;
			}
			temp[j] = items[i];
		}
		items = temp;
	}
	/**
	 * get the tower form a given green grid
	 * @param coord the coordinate of a grid
	 * @return the tower in given coordinate
	 */
	public Tower getTowerAt(Coordinate coord) {
		for(int i=0; i<num_items; i++){
			if (items[i] instanceof Tower) {
				Coordinate c = items[i].coord;
				if (coord.x == c.x && coord.y == c.y) {
					return (Tower) items[i];
				}
			}
		}
		throw new IllegalArgumentException("No such tower");
	}

	/**
	 * genarate and add a Tower to the arena
	 * @param towerID the ID representing a type of tower 0: Basic tower 1: Ice Tower 2: Catapult 3: Laser Tower
	 * @param x the x coordinate that the player want the new tower to be
	 * @param y the x coordinate that the player want the new tower to be
	 * @return true if successfully adding the tower i.e. have enough resources to build, False if otherwise
	 */
	public boolean addTower(int towerID, int x, int y) {
		Tower temp;
		//System.out.println("building a tower");
		//todo make tower
		switch(towerID){
			//todo default tower setting, it may change
			case 0: temp = new BasicTower(x , y, 4); break;
			case 1: temp = new IceTower(x , y, 0, 1); break;
			case 2: temp = new Catapult(x , y, 6); break;
			case 3: temp = new LaserTower(x , y, 8); break;
			default: throw new IllegalArgumentException();
		}
		//money limit
		if(money-temp.getCost()<0){
			System.out.println("not enough resource to build the tower");
			return false;
		}
		money-=temp.getCost();
		addItem(temp);
		return true;
	}
	/**
	 * Perform upgrade to the tower
	 * @param t	the tower that will receive the upgrade
	 * @return true if update successful i.e. have enough resources to upgrade, False if otherwise
	 */
	public boolean upgradeTower(Tower t){
		if (t == null){
			throw new IllegalArgumentException();
		}
		if(money < t.getUpgradeCost()){
			switch (t.getTowerType()){
				case LaserTower: System.out.println("not enough resource to upgrade Laser Tower"); break;
				case BasicTower: System.out.println("not enough resource to upgrade Basic Tower"); break;
				case IceTower: System.out.println("not enough resource to upgrade Ice Tower"); break;
				case Catapult: System.out.println("Cnot enough resource to upgrade Catapult"); break;
				default: throw new IllegalArgumentException();
			}
			return false;
		}
		switch (t.getTowerType()){
			case LaserTower: System.out.println("Laser Tower is being upgraded"); break;
			case BasicTower: System.out.println("Basic Tower is being upgraded"); break;
			case IceTower: System.out.println("Ice Tower is being upgraded"); break;
			case Catapult: System.out.println("Catapult is being upgraded"); break;
			default: throw new IllegalArgumentException();
		}
		money -= t.getUpgradeCost();
		t.upgrade();
		return true;
	}
	/**
	 * Will be called when the player press next frame
	 * process the game to new turn.
	 * <p>first remove all the dead monster. Then Monster will move.
	 * Then pass an array of monster to each tower to
	 * process attack, show the attack in GUI and console. After that collect resources from dead monster.
	 * Finally generate new monster
	 * </p>
	 */
	public void nextRound(){
		
		//collect monster body before process attack
		for(int i=0; i<num_items; i++){
			if(items[i] instanceof Monster){
				Monster m = (Monster)items[i];
				if(m.isDead()){
					removeItem(m);              //remove monster
					i--;
				}
			}
		}

		int numMonster = 0;
		Monster monsterArray[] = new Monster[num_items];
		for(int i=0; i<num_items; i++){
			if(items[i] instanceof Monster){
				monsterArray[numMonster++] = (Monster)items[i];
			}
		}

		//todo move the monster i.e. update the new location of each monster
		for(int i=0; i<num_items; i++){
			if(items[i] instanceof Monster){
				Monster m = (Monster)(items[i]);
				if(!m.isDead()) {
					m.move();
				}
			}
		}

		//todo process attack
		for(int i=0; i<num_items; i++){
			if(items[i] instanceof Tower){
				Tower t = (Tower)items[i];
				if(t.attackMonster(monsterArray, numMonster)){
					Monster attackedM = t.getGraph();
					//if the tower is laser tower, check weather it has enough resources first
					if(t instanceof LaserTower){
						if(money-((LaserTower) t).attackCost < 0){
							t.newFrame();
							continue;
						}
						else{
							money -= ((LaserTower) t).attackCost;
						}
					}
					System.out.println(t.type + " at location (" + t.coord.x + " , " + t.coord.y +
							") -> " + attackedM.getType() + " at location (" + attackedM.coord.x + " , " + attackedM.coord.y +")");
					//todo GUI to show the attack
					Line line = new Line(t.coord.pixel_X, t.coord.pixel_Y, attackedM.coord.pixel_X, attackedM.coord.pixel_Y);
					line.setStyle("-fx-stroke: red;");
					if(t instanceof LaserTower){
						line.setStrokeWidth(6);
						//todo draw the laser line untill it reach the out side of the arena
						double x = attackedM.coord.pixel_X;
						double y = attackedM.coord.pixel_Y;
						//not debug yet
						//Monster is on LHS of tower
						if(attackedM.coord.pixel_X<t.coord.pixel_X){
							while (x>0 && y<480 && y>0){
								x--;
								y -= attackedM.coord.slope;
							}
						}
						//Monster is on RHS of tower
						if(attackedM.coord.pixel_X>t.coord.pixel_X){
							while (x<480 && y<480 && y>0){
								x++;
								y += attackedM.coord.slope;
							}
						}
						//Monster is exactly below the tower
						if (attackedM.coord.pixel_X == t.coord.pixel_X && attackedM.coord.pixel_Y > t.coord.pixel_Y) {
							y = 480;
						}
						//Monster is exactly on top of the tower
						if (attackedM.coord.pixel_X == t.coord.pixel_X && attackedM.coord.pixel_Y < t.coord.pixel_Y) {
							y = 0;
						}
						line.setEndX(x);
						line.setEndY(y);
					}
					if(t instanceof Catapult){
                        Circle dot = new Circle(attackedM.coord.pixel_X, attackedM.coord.pixel_Y,25);
                        dot.setFill(Color.RED);
                        dot.setOpacity(0.5);
                        paneArena.getChildren().add(dot);
                        attackGraphic.push(dot);
                    }
					attackGraphic.push(line);
					paneArena.getChildren().add(line);
				}
				t.newFrame();
			}
		}
		//collect resources diu to monster dead
		int earning = 0;
		for(int i=0; i<num_items; i++){
			if(items[i] instanceof Monster){
				Monster m = (Monster)items[i];
				if(m.isDead()){
					earning += m.earning;
				}
			}
		}
		money += earning;
		if(turn==1||turn%3==0)//you may change if you like
		{
			generateMonster();
		}
		turn++;
	}
}