package sample;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

import java.util.Random;

public class Arena {

	private Item items[];
	private int num_items;
	private int money;
	private int turn;
	private AnchorPane paneArena;

	//todo should make back to private afterwards
	public void generateMonster() {
		System.out.println("monster generated");
		Random rand = new Random();
		Monster e;
		int hp = 10;
		int randInt = rand.nextInt(3);
		switch(randInt){
			//todo implements to call correctly after Boby's work
			case 0: e = new Monster(0,0, hp, 10, MonsterType.Fox); System.out.println("Fox: " + hp + " hp generated"); break;
			case 1: e = new Monster(0,0, hp, 20, MonsterType.Penguin); System.out.println("Penguin: " + hp + " hp generated"); break;
			case 2: e = new Monster(0,0, hp, 30, MonsterType.Unicorn); System.out.println("Unicorn: " + hp + " hp generated"); break;
			default: throw new IllegalArgumentException();
		}
		addItem(e);
	}

	//public
	public Arena(AnchorPane paneArena) {
		items = null;
		num_items = 0;
		money = 1000;         //initial amount of resources
		turn = 0;
		this.paneArena = paneArena;
	}
	public int getNumItems(){
		return num_items;
	}
	public boolean isGameOver() {
		for(int i=0; i<num_items; i++) {
			if(items[i] instanceof Monster){
				Coordinate coord = items[i].coord;
				//todo check coordinate
				if(coord.x>12 && coord.y==0){
					return true;
				}
			}
		}
		return false;
	}

	public void addItem(Item newItem) {
		System.out.println("adding item");
		num_items++;
		Item temp[] = new Item[num_items];
		for(int i=0; i<num_items-1; i++){
			temp[i]=items[i];
		}
		temp[num_items-1] = newItem;
		items = temp;
	}

	Item getItemAt(Coordinate coord) {
		for(int i=0; i<num_items; i++){
			Coordinate c = items[i].coord;
			if(coord.x==c.x && coord.y==c.y){
				return items[i];
			}
		}
		throw new IllegalArgumentException("No such item");
	}

	public void nextRound(){
	    turn++;
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
				((Monster)items[i]).move();
			}
		}
		
		//todo process attack
		for(int i=0; i<num_items; i++){
			if(items[i] instanceof Tower){
				System.out.println(i + " is a tower");
				Tower t = (Tower)items[i];
				t.attackMonster(monsterArray, numMonster);
				//todo show the tower attack by System.out.println
				Monster attackedM = t.getGraph();
				if (attackedM != null) {
                    System.out.println(t.type + " at location (" + t.coord.x + "." + t.coord.y +
                            ") -> " + attackedM.getType() + " at location (" + attackedM.coord.x + "." + attackedM.coord.y +")");
//				    int mx=400, my=400;
//				    System.out.println(t.type + " at location (" + t.coord.x + "." + t.coord.y +
//					    	") -> " + 1 + "at location (" + mx + "." + my + ")");
                    //todo GUI to show the attack
                    Line line = new Line(t.coord.x, t.coord.y, attackedM.coord.x, attackedM.coord.y);
//				    Line line = new Line(t.coord.pixel_X, t.coord.pixel_Y, mx, my);
                    paneArena.getChildren().add(line);
                    //paneArena.getChildren().remove(line);
                }
				t.newFrame();
			}
		}
		//todo remove dead monster and collect resources
		int earning = 0;
		for(int i=0; i<num_items; i++){
			if(items[i] instanceof Monster){
				Monster m = (Monster)items[i];
				if(m.isDead()){
					earning += m.earning;
					removeItem(m);              //remove monster
					i--;
				}
			}
		}
		money += earning;

		generateMonster();
	}

//	public void nextRound() {
//		for(int i=0; i<num_items; i++){
//			if(items[i] instanceof Tower){
//				Tower t = (Tower)items[i];
//				int inRangeEnemyIndex[] = new int[num_items];
//				int numOfInRangeEnemy=0;
//				int smallest_id = -1;
//
//				Coordinate fireTowerCoord = items[i].coord;
//				for(int j=0; j<num_items; j++){
//					if(items[j] instanceof Monster){		//check whether a monster is in range of tower
//						Coordinate targerMonsterCoord = items[j].coord;
//						if(t.isInRange(targerMonsterCoord)){
//							inRangeEnemyIndex[numOfInRangeEnemy++] = j;
//						}
//					}
//				}
//
//				if(numOfInRangeEnemy>0){							//shoot the closest enemy
//					int distance=100;
//					for(int k=0; k<numOfInRangeEnemy; k++){
//						Coordinate temp = items[inRangeEnemyIndex[k]].coord;
//						int d = Math.abs(temp.x-fireTowerCoord.x) + Math.abs(temp.y-fireTowerCoord.y);
//						if(d < distance){
//							distance = d;
//							smallest_id=inRangeEnemyIndex[k];
//						}
//					}
//					t.fire((Monster)items[smallest_id]);
//				}
//			}
//		}
//		//remove dead monster
//		for(int i=0; i<num_items; i++){
//			if(items[i] instanceof Monster){
//				Monster m = (Monster)items[i];
//				if(m.isDead()){
//					removeItem(m);
//					i--;
//				}
//			}
//		}
//		generateMonster();
//	}

	public boolean addBuilding(int towerID, int x, int y) {
		Tower temp;
        //System.out.println("building a tower");
		//todo make tower
		switch(towerID){
			//todo default tower setting, it may change
			case 0: temp = new BasicTower(x , y, 2); break;
			case 1: temp = new IceTower(x , y, 0, 1); break;
			case 2: temp = new Catapult(x , y, 3); break;
			case 3: temp = new LaserTower(x , y, 1); break;
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

	public boolean upgradeTower(Tower t){
		if (t == null){
			throw new IllegalArgumentException();
		}
		if(money < t.getUpgradeCost()){
			switch (t.getTowerTyper()){
				case LaserTower: System.out.println("not enough resource to upgrade Laser Tower"); break;
				case BasicTower: System.out.println("not enough resource to upgrade Basic Tower"); break;
				case IceTower: System.out.println("not enough resource to upgrade Ice Tower"); break;
				case Catapult: System.out.println("Cnot enough resource to upgrade Catapult"); break;
				default: throw new IllegalArgumentException();
			}
			return false;
		}
		switch (t.getTowerTyper()){
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

	public int getMoney() {
		return money;
	}
	public Item[] getItems(){
		return items;
	}
}
