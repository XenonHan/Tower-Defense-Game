package sample;

import org.junit.Assert;
import org.junit.Test;

public class MonsterTest {
	@Test
	public void MonsterTest1() {
	
		Monster[] m=new Monster[30];
		m[0]=new Monster(15,1,MonsterType.Fox,1); m[0].coord=new Coordinate(0,0);
		m[1]=new Monster(15,1,MonsterType.Fox,1); m[1].coord=new Coordinate(2,3);
		m[2]=new Monster(15,1,MonsterType.Fox,1); m[2].coord=new Coordinate(4,5);
		m[3]=new Monster(15,1,MonsterType.Fox,1); m[3].coord.pixel_X=20; m[3].coord.pixel_Y=460;
		m[4]=new Monster(15,1,MonsterType.Fox,1); m[4].coord.pixel_X=60; m[4].coord.pixel_Y=460;
		m[5]=new Monster(15,1,MonsterType.Fox,1); m[5].coord.pixel_X=80; m[5].coord.pixel_Y=460;
		m[6]=new Monster(15,1,MonsterType.Fox,1); m[6].coord.pixel_X=100; m[6].coord.pixel_Y=460;
		m[7]=new Monster(15,1,MonsterType.Fox,1); m[7].coord.pixel_X=100; m[7].coord.pixel_Y=0;
		m[8]=new Monster(15,1,MonsterType.Fox,1); m[8].coord.pixel_X=120; m[8].coord.pixel_Y=0;
		m[9]=new Monster(15,1,MonsterType.Fox,1); m[9].coord.pixel_X=160; m[9].coord.pixel_Y=0;
		m[10]=new Monster(15,1,MonsterType.Fox,1); m[10].coord.pixel_X=180; m[10].coord.pixel_Y=0;
		m[11]=new Monster(15,1,MonsterType.Fox,0); m[11].coord.pixel_X=260; m[11].coord.pixel_Y=460;
		m[12]=new Monster(15,1,MonsterType.Fox,7); m[12].coord.pixel_X=340; m[12].coord.pixel_Y=20;
		m[13]=new Monster(15,1,MonsterType.Fox,7); m[13].coord.pixel_X=180; m[13].coord.pixel_Y=20;
		m[14]=new Monster(15,1,MonsterType.Fox,7); m[14].coord.pixel_X=420; m[14].coord.pixel_Y=460;
		m[15]=new Monster(15,1,MonsterType.Fox,7); m[15].coord.pixel_X=260; m[15].coord.pixel_Y=460;
		m[16]=new Monster(15,1,MonsterType.Fox,7); m[15].coord.pixel_X=100; m[15].coord.pixel_Y=460;
		m[17]=new Monster(15,1,MonsterType.Fox,7); m[17].coord.pixel_X=79; m[17].coord.pixel_Y=460;
		
		m[18]=new Monster(15,1,MonsterType.Unicorn ,1); m[18].coord.pixel_X=321; m[18].coord.pixel_Y=20;
		m[19]=new Monster(15,1,MonsterType.Unicorn,1); m[19].coord.pixel_X=161; m[19].coord.pixel_Y=20;
		m[20]=new Monster(15,1,MonsterType.Unicorn,1); m[20].coord.pixel_X=420; m[20].coord.pixel_Y=460;
		m[21]=new Monster(15,1,MonsterType.Unicorn,1); m[21].coord.pixel_X=401; m[21].coord.pixel_Y=460;
		m[22]=new Monster(15,1,MonsterType.Unicorn,1); m[22].coord.pixel_X=260; m[22].coord.pixel_Y=460;
		m[23]=new Monster(15,1,MonsterType.Unicorn,1); m[23].coord.pixel_X=261; m[23].coord.pixel_Y=460;
		m[24]=new Monster(15,1,MonsterType.Unicorn,1); m[24].coord.pixel_X=100; m[24].coord.pixel_Y=460;
		m[25]=new Monster(15,1,MonsterType.Unicorn,1); m[25].coord.pixel_X=81; m[25].coord.pixel_Y=460;
		
		m[26]=new Monster(15,1,MonsterType.Penguin ,1); m[26].coord.pixel_X=240; m[26].coord.pixel_Y=460;
		m[27]=new Monster(15,1,MonsterType.Penguin ,1); m[27].coord.pixel_X=80; m[27].coord.pixel_Y=460;
		m[28]=new Monster(15,1,MonsterType.Penguin ,100); m[28].coord.pixel_X=79; m[28].coord.pixel_Y=460;
		m[29]=new Monster(15,1,MonsterType.Penguin ,1); m[29].coord.pixel_X=242; m[29].coord.pixel_Y=460;
		
		Assert.assertEquals(m[0].getSpeed(),m[0].speed);
		Assert.assertEquals(m[0].isDead(),false);
		
		m[1].health=0;
		Assert.assertEquals(m[1].isDead(),true);
		
		Assert.assertEquals(m[2].getType(),MonsterType.Fox);
		m[1].health=1;
		
		m[1].freezeTime=3;
		
		for(int i=0;i<30;i++)
			m[i].move();
		
	}
}