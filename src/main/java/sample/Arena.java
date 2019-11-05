package sample;

import java.util.Random;

public class Arena {

	private Item items[];
	private int num_items;
	private int money;
	private int turn;

	//todo should make back to private afterwards
	public void generateMonster() {
		System.out.println("monster generated");
        Random rand = new Random();
        Monster e;
        int randInt = rand.nextInt(3);
        switch(randInt){
        	//todo implements to call correctly after Boby's work
            case 0: e = new Monster(0,0); break;
            case 1: e = new Monster(2,2); break;
            case 2: e = new Monster(4,4); break;
            default: throw new IllegalArgumentException();
        }
		addItem(e);
	}

	//public
	public Arena() {
		items = null;
		num_items = 0;
		money = 10;         //initial amount of resources
		turn = 0;
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

	public void nextRound() {
		int numMonster = 0;
		Monster monsterArray[] = new Monster[num_items];
		for(int i=0; i<num_items; i++){
			if(items[i] instanceof Monster){
				monsterArray[numMonster++] = (Monster)items[i];
			}
		}
		for(int i=0; i<num_items; i++){
			if(items[i] instanceof Tower){
				Tower t = (Tower)items[i];
				t.attackMonster(monsterArray, numMonster);
			}
		}

		//remove dead monster
        for(int i=0; i<num_items; i++){
            if(items[i] instanceof Monster){
                Monster m = (Monster)items[i];
                if(m.isDead()){
                    removeItem(m);
                    i--;
                }
            }
        }
        //todo collect resources
        //todo move the monster i.e. update the new location of each monster
		generateMonster();
	}

	public boolean addBuilding(int towerID, int x, int y) {
		Tower temp;
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
		System.out.println("tower upgraded");
		if (t == null){
			throw new IllegalArgumentException();
		}
		if(money < t.getUpgradeCost()){
			System.out.println("not enough money to upgrade the tower");
			return false;
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
