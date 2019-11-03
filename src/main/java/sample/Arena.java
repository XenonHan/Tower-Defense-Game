package sample;

public class Arena {

	private Item items[];
	int num_items;
	private int money;
	int turn;

	private void generateMonster() {
		System.out.println("monster generated");
		Monster e;
		e = new Monster( 2,3);
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
				if(coord.x>12 && coord.y==0){
					return true;
				}
			}
		}
		return false;
	}

	public void removeMonster(Monster monster) {
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
		generateMonster();
	}

	public boolean addBuilding(int buildingID, int x, int y) {
		Tower temp;
		switch(buildingID){
			case 0: temp = new BasicTower(x , y, 2); break;
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
		System.out.println("tower generated");
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

	public int getMoney() {
		return money;
	}
	//void getConstObjects(const Object**&, int&) const;
}
