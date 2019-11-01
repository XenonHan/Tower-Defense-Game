package sample;

import org.junit.Assert;
import org.junit.Test;

public class JunkTest {
	@Test
	public void testSpeed() { 
		RubbishMonster m = new RubbishMonster(10,20);
		Assert.assertEquals(m.getSpeed(), 20);
	}
	@Test
	public void testHealth() { 
		RubbishMonster m = new RubbishMonster(10,20);
		Assert.assertTrue(m.getHealth()==10);
	}
	@Test
	public void testDie() { 
		RubbishMonster m = new RubbishMonster(-10,20);
		Assert.assertTrue(m.isDead(-10));
		Assert.assertFalse(m.isDead(10));
	}

}
