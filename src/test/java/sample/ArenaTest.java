//package sample;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//public class ArenaTest {
//    @Test
//    public void insertTest() {
//        Arena a = new Arena(null);
//        Assert.assertEquals(a.getNumItems(), 0);
//
//        a.addBuilding(1,2,3);
//        Assert.assertEquals(a.getNumItems(), 1);
//        Assert.assertEquals(a.getItems().length, 1);
//        //Assert.assertEquals(a.getMoney(), 8);
//        Item tower = a.getItems()[0];
//        Assert.assertEquals(a.getItemAt(new Coordinate(2,3)), tower);
//
//        Assert.assertTrue(a.upgradeTower((Tower)tower));
//        //Assert.assertEquals(a.getMoney(), 7);
//        //Assert.assertEquals(((Tower) tower).power, 17);
//
//        a.generateMonster();
//        Assert.assertEquals(a.getNumItems(), 2);
//        Assert.assertEquals(a.getItems().length, 2);
//        Item monster = a.getItems()[1];
//        Assert.assertEquals(a.getItemAt(new Coordinate(0,0)), monster);
//
//        Item items[] = {tower, monster};
//        Assert.assertArrayEquals(a.getItems(), items);
//
//        Item items1[] = {monster};
//        a.removeItem(tower);
//        Assert.assertEquals(a.getNumItems(), 1);
//        Assert.assertEquals(a.getItems().length, 1);
//        Assert.assertArrayEquals(a.getItems(), items1);
//        //Assertions.assertThrows();
//
//        Item items2[] = {};
//        a.removeItem(monster);
//        Assert.assertEquals(a.getNumItems(), 0);
//        Assert.assertEquals(a.getItems().length, 0);
//        Assert.assertArrayEquals(a.getItems(), items2);
//
//        Assert.assertFalse(a.isGameOver());
//    }
//}
