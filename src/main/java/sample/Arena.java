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
	/**
	 * an array of Item, which contain all monsters and towers in exists in the arena
	 */
	Item items[];
	/**
	 * number of items exists in the arena,
	 */
	int num_items;
	/**
	 * Indicate the amount of resources the player have
	 */
	int money;
	/**
	 * Indicate the the number game turn, initialized as 1
	 */
	int turn;
	/**
	 * Is the AnchorPane of the arena
	 */
	AnchorPane paneArena;
	/**
	 * contains all the shape(line and circle) added to the GUI in order to demonstrate tower attack
	 */
	Stack<Shape> attackGraphic = new Stack<>();
	/**
	 * define the end zone of the arena
	 */
	Coordinate endZone = new Coordinate(11, 0);
	/**
	 * This is the array use to store the current monster HP
	 */
	int monstorHP[]= {15,30,50};//record the monster HP (fox, penguin, unicorn)

	private int generate;

	/**
	 * generate 0 or more number of monster in each turn
	 */
	protected void generateMonster() {

		Random rand = new Random();
		int randInt = rand.nextInt(3);
		//randInt = 0;
		Monster e;
		if (turn != 0 && turn % 10 == 0) {
			for (int i = 0; i < 3; i++)
				monstorHP[i] *= 1.2;		//health growth rate of monster
		}
		switch (randInt) {
			//todo implements to call correctly after Boby's work
			case 0: e = new Fox(monstorHP[0], 8, 20); break;
			case 1: e = new Penguin(monstorHP[1], 15, 10); break;
			default: e = new Unicorn(monstorHP[2], 10, 15);
		}
		addItem(e);
	}


	/**
	 * Create a Arena with given AnchorPane
	 * @param paneArena AnchorPane of the game scene
	 */
	public Arena(AnchorPane paneArena) {
		items = null;
		num_items = 0;
		money = 60;         //initial amount of resources (60)
		turn = 1;
		this.paneArena = paneArena;
		generate=1;
	}
	/**
	 * check weather the player lose the game or not
	 * @return True if lose, False if otherwise
	 */
	public boolean isGameOver() {
		for(int i=0; i<num_items; i++) {
			if(items[i] instanceof Monster){
				Coordinate coord = items[i].coord;
				if((coord.pixel_X > endZone.pixel_X-20)){
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
	public void removeItem(Item item){
		//I assume the item must not null and its exists in the arena
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
		return null;
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
			case 1: temp = new IceTower(x , y, 0, 3); break;
			case 2: temp = new Catapult(x , y, 6); break;
			case 3: temp = new LaserTower(x , y, 8); break;
			default:
				throw new IllegalStateException();
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
	 * Perform upgrade to the tower. A tower can only being upgraded for three times
	 * otherwise the game will be too easy.
	 * @param t	the tower that will receive the upgrade
	 * @return true if update successful i.e. have enough resources to upgrade, False if otherwise
	 */
	public boolean upgradeTower(Tower t){
		if (t == null){
			System.out.println("should not input null to upgradeTower() method");
			return false;
		}
		if(money < t.getUpgradeCost()){
			switch (t.getTowerType()){
				case LaserTower: System.out.println("not enough resource to upgrade Laser Tower"); break;
				case BasicTower: System.out.println("not enough resource to upgrade Basic Tower"); break;
				case IceTower: System.out.println("not enough resource to upgrade Ice Tower"); break;
				default: System.out.println("not enough resource to upgrade Catapult");
			}
			return false;
		}
		switch (t.getTowerType()){
			case LaserTower: System.out.println("Laser Tower is being upgraded"); break;
			case BasicTower: System.out.println("Basic Tower is being upgraded"); break;
			case IceTower: System.out.println("Ice Tower is being upgraded"); break;
			default: System.out.println("Catapult is being upgraded");
		}
		money -= t.getUpgradeCost();
		t.upgrade();
		t.level++;
		return true;
	}
	/**
	 * Will be called when the player press next frame
	 * process the game to new turn.
	 * <p>first remove all the dead monster. Then Monster will move. If any monster step into the end
	 * zone, return and do nothing.
	 * Then pass an array of monster to each tower to
	 * process attack, show the attack in GUI and console. After that collect resources from dead monster.
	 * Finally generate new monster.
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
		//todo move the monster i.e. update the new location of each monster
		for(int i=0; i<num_items; i++){
			if(items[i] instanceof Monster){
				((Monster)items[i]).move();
			}
		}
		if(isGameOver()){
			//if monster step into end zone, no more attack, end the game immediately
			return;
		}
		//collect info that will be pass to tower to process attack
		int numMonster = 0;
		Monster monsterArray[] = new Monster[num_items];
		for(int i=0; i<num_items; i++){
			if(items[i] instanceof Monster){
				monsterArray[numMonster++] = (Monster)items[i];
			}
		}
		//todo process attack
		for(int i=0; i<num_items; i++){
			if(items[i] instanceof Tower){
				Tower t = (Tower)items[i];
				//if the tower is laser tower, check weather it has enough resources first before attack
				if(t instanceof LaserTower){
					if(money-((LaserTower) t).attackCost < 0){
						t.newFrame();
						continue;
					}
				}
				if(t.attackMonster(monsterArray, numMonster)){
					if(t instanceof LaserTower){
						//reduce money if laserTower shoot
						money -= ((LaserTower) t).attackCost;
					}
					Monster attackedM = t.getGraph();
					System.out.println(t.type + " at location (" + t.coord.pixel_X + " , " + t.coord.pixel_Y +
							") -> " + attackedM.getType() + " at location (" + attackedM.coord.pixel_X+ " , " + attackedM.coord.pixel_Y +")");
					Line line = new Line(t.coord.pixel_X, t.coord.pixel_Y, attackedM.coord.pixel_X, attackedM.coord.pixel_Y);
					line.setStyle("-fx-stroke: red;");
					//draw the laser line until the edge of the arena
					if(t instanceof LaserTower){
						drawLaserAttackLine(attackedM, t, line);
					}
					//draw Catapult attacked region
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
		//collect resources due to monster dead
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
		if(turn==1||turn%3==0){		//monster will be generate every three turn
			generateMonster();
		}
		turn++;
	}
	/**
	 * it set the amount of money. Is for testing only, should not be called in the game
	 * @param money the amount of money you want to set
	 */
	public void setMoney(int money) {
		this.money = money;
	}

	/**
	 * draw the laser attack line to the edge of the arena
	 * @param attackedM the monster being attacked by the tower
	 * @param t	the tower that initiated the attack
	 * @param line	the line that being editing
	 */
	private void drawLaserAttackLine(Monster attackedM, Tower t, Line line){
		line.setStrokeWidth(6);
		double x = attackedM.coord.pixel_X;
		double y = attackedM.coord.pixel_Y;
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
}
