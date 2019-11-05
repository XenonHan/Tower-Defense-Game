package sample;

import java.util.Random;

public class Arena {

	private Item items[];
	int num_items;
	private int money;
	int turn;

	private void generateMonster() {
		System.out.println("monster generated");
        Random rand = new Random();
        Monster e;
        int randInt = rand.nextInt(3);
        switch(randInt){
            case 0: e = new Monster( 0,0, EnemyType.Fox, 10); break;
            case 1: e = new Monster( 0,0, EnemyType.Unicorn,20 ); break;
            case 2: e = new Monster( 0,0, EnemyType.Penguin, 30); break;
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

	public void removeDeadMonster(Monster monster) {
		if (monster==null){
			throw new IllegalArgumentException();
		}
		//remove monster
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

	Item getItemtAt(Coordinate coord) {
		for(int i=0; i<num_items; i++){
			Coordinate c = items[i].coord;
			if(coord.x==c.x && coord.y==c.y){
				return items[i];
			}
		}
		throw new IllegalArgumentException("No such item");
	}

	public void nextRound() {
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
        //todo move the monster i.e. update the new location of each monster

		generateMonster();
	}

	public boolean addBuilding(int buildingID, int x, int y) {
		Tower temp;
		//todo make tower
		switch(buildingID){     //junk
			case 0: temp = new BasicTower(x , y, 13); break;
            case 1: temp = new BasicTower(x , y, 14); break;
            case 2: temp = new BasicTower(x , y, 15); break;
            case 3: temp = new BasicTower(x , y, 16); break;
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
