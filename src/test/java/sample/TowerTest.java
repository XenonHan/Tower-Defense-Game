package sample;

import org.junit.Assert;
import org.junit.Test;

public class TowerTest {
	@Test
	public void testLaserTower1() {
		//Test whether all monster on the straight line been attacked
		LaserTower a=new LaserTower(5,5,3);
		Monster[] m=new Monster[3];
		m[0]=new Monster(15,1,MonsterType.Fox,1); m[0].coord=new Coordinate(0,0);
		m[1]=new Monster(15,1,MonsterType.Fox,1); m[1].coord=new Coordinate(3,3);
		m[2]=new Monster(15,1,MonsterType.Fox,1); m[2].coord=new Coordinate(4,4);
		
		a.attackMonster(m, 3);
		
		Assert.assertEquals(m[0].health,12);
		Assert.assertEquals(m[1].health,12);
		Assert.assertEquals(m[2].health,12);
		
	}
	@Test
	public void testLaserTower2() {
		//Test whether all monster within 3px of the straight been attacked.
		LaserTower a=new LaserTower(6,6,3);
		Monster[] m=new Monster[7];
		m[0]=new Monster(15,1,MonsterType.Fox,1); m[0].coord=new Coordinate(5,5);//clostMost, use to plot straight line
		
		m[1]=new Monster(15,1,MonsterType.Fox,1); m[1].coord.pixel_X=143; m[1].coord.pixel_Y=140; //x+3 , y not change
		m[2]=new Monster(15,1,MonsterType.Fox,1); m[2].coord.pixel_X=140; m[2].coord.pixel_Y=137; //y-3 , x not change
		m[3]=new Monster(15,1,MonsterType.Fox,1); m[3].coord.pixel_X=137; m[3].coord.pixel_Y=140; //x-3 , y not change
		m[4]=new Monster(15,1,MonsterType.Fox,1); m[4].coord.pixel_X=140; m[4].coord.pixel_Y=143; //y+3 , x not change
		
		m[5]=new Monster(15,1,MonsterType.Penguin,1);  m[5].coord=new Coordinate(7,7); //in the line
		
		a.attackMonster(m, 6);
		
		Assert.assertEquals(m[0].health,12);
		Assert.assertEquals(m[1].health,14);
		Assert.assertEquals(m[2].health,14);
		Assert.assertEquals(m[3].health,14);
		Assert.assertEquals(m[4].health,14);
		Assert.assertEquals(m[5].health,15);

		
	}
	
	@Test
	public void testLaserTower3() {
		//Test whether all monster within 3px of the straight been attacked.
		LaserTower a=new LaserTower(2,2,3);
		a.range=250;
		Monster[] m=new Monster[7];
		m[0]=new Monster(15,1,MonsterType.Fox,1); m[0].coord=new Coordinate(5,5);//clostMost, use to plot straight line
		
		m[1]=new Monster(15,1,MonsterType.Fox,1); m[1].coord.pixel_X=143; m[1].coord.pixel_Y=140; //x+3 , y not change
		m[2]=new Monster(15,1,MonsterType.Fox,1); m[2].coord.pixel_X=140; m[2].coord.pixel_Y=137; //y-3 , x not change
		m[3]=new Monster(15,1,MonsterType.Fox,1); m[3].coord.pixel_X=137; m[3].coord.pixel_Y=140; //x-3 , y not change
		m[4]=new Monster(15,1,MonsterType.Fox,1); m[4].coord.pixel_X=140; m[4].coord.pixel_Y=143; //y+3 , x not change
		
		m[5]=new Monster(15,1,MonsterType.Penguin,1);  m[5].coord.pixel_X=20; m[5].coord.pixel_Y=20; //not been attacked
		m[6]=new Monster(15,1,MonsterType.Penguin,1); m[6].coord=new Coordinate(3,8);// out of 3px
		
		a.attackMonster(m, 7);
		
		Assert.assertEquals(m[0].health,12);
		Assert.assertEquals(m[1].health,14);
		Assert.assertEquals(m[2].health,14);
		Assert.assertEquals(m[3].health,14);
		Assert.assertEquals(m[4].health,14);
		Assert.assertEquals(m[5].health,15);
		Assert.assertEquals(m[6].health,15);

		
	}
	
	@Test
	public void testLaserTower4() {
		//Test whether all monster within 3px of the straight been attacked.
		LaserTower a=new LaserTower(6,6,3);
		a.upgrade();
		Assert.assertEquals(a.power,5);
		
		a.newFrame();
		Assert.assertEquals(a.counter,0);
		Assert.assertEquals(a.subCounter,0);
		Assert.assertEquals((int)a.coord.slope, 0);
		Assert.assertEquals(a.closestMon, null);
		Assert.assertEquals((int)a.closestMonDistance, 0);
		
		Assert.assertEquals(a.getAttackCost(), a.attackCost);
		
		a.status=TowerStatus.Passive;
		Assert.assertEquals(a.attackMonster(null, 0), false);
		
		a.status=TowerStatus.Active;
		Assert.assertEquals(a.attackMonster(null, 0), false);
		
	}
	
	@Test
	public void BasicTower1() {
		//Test whether all monster within 3px of the straight been attacked.
		BasicTower a=new BasicTower(1,1,3);
		a.upgrade();
		Assert.assertEquals(a.power,6);
		Monster []m=new Monster[2];
		m[0]=new Monster(3,3,MonsterType.Unicorn ,1);
		m[1]=new Monster(3,3,MonsterType.Unicorn ,1);m[1].coord=new Coordinate(10,4);
		
		Assert.assertEquals(a.getCost(), a.cost);
		
		a.status=TowerStatus.Passive;
		Assert.assertEquals(a.attackMonster(null, 0), false);
		
		a.status=TowerStatus.Active;
		Assert.assertEquals(a.attackMonster(null, 0), false);
		Assert.assertEquals(a.attackMonster(m, 2), true);
		
		a.getGraph();
		a.closestMon=null;
		Assert.assertEquals(a.getGraph(), null);
		
		a.newFrame();
	}
	@Test
	public void IceTower1() {
		//Test whether all monster within 3px of the straight been attacked.
		IceTower a=new IceTower(1,1,3,3);
		a.upgrade();
		Assert.assertEquals(a.freezeTime,6);
		Monster []m=new Monster[1];
		m[0]=new Monster(0,0,MonsterType.Unicorn ,1);
		
		a.status=TowerStatus.Passive;
		Assert.assertEquals(a.attackMonster(null, 0), false);
		
		a.status=TowerStatus.Active;
		Assert.assertEquals(a.attackMonster(null, 0), false);
		Assert.assertEquals(a.attackMonster(m, 1), true);
	}
	@Test
	public void Catapult1() {
		//Test whether all monster within 3px of the straight been attacked.
		Catapult a=new Catapult(1,1,3);
		a.upgrade();
		Assert.assertEquals(a.coolingTime,2);
		Monster []m=new Monster[6];
		m[0]=new Monster(5,5,MonsterType.Unicorn ,1);m[0].coord=new Coordinate(0,2);
		m[1]=new Monster(5,5,MonsterType.Fox ,1);m[1].coord=new Coordinate(3,0);
		m[2]=new Monster(5,5,MonsterType.Fox ,1);m[2].coord=new Coordinate(10,5);
		m[3]=new Monster(5,5,MonsterType.Fox ,1);m[3].coord=new Coordinate(0,1);
		m[4]=new Monster(5,5,MonsterType.Fox ,1);m[4].coord=new Coordinate(3,0);
		m[5]=new Monster(5,5,MonsterType.Fox ,1);m[5].coord=new Coordinate(2,4);
		
		a.status=TowerStatus.Active;
		Assert.assertEquals(a.attackMonster(null, 0), false);
		Assert.assertEquals(a.attackMonster(m, 6), true);	
		
		a.status=TowerStatus.Passive;
		Assert.assertEquals(a.attackMonster(null, 0), false);
		
		a.newFrame();
		Assert.assertEquals(a.counter,0);
		Assert.assertEquals(a.closestMon, null);
		Assert.assertEquals((int)a.closestMonDistance, 0);
		
		a.remainCoolingPeriod=3;
		a.status=TowerStatus.Passive;
		a.newFrame();
		
		a.remainCoolingPeriod=0;
		a.newFrame();
		
		a.coolingTime=0;
		a.upgrade();
		
		a.getGraph();
		Assert.assertEquals(a.getTowerType(), a.type);
		Assert.assertEquals(a.getUpgradeCost(), (int)(a.cost*1.2));
		Assert.assertEquals(a.getAttackCost(), 0);
	}

}
