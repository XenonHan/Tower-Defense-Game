package sample;

import javafx.scene.layout.AnchorPane;
import org.junit.Assert;
import org.junit.Test;

public class ArenaTest {
    AnchorPane paneArena = new AnchorPane();
    @Test
    public void insertTower() {
        Arena a = new Arena(null);
        a.setMoney(1000);
        Assert.assertEquals(a.num_items, 0);
        Assert.assertEquals(a.money, 1000);

        a.addTower(1, 2, 3);
        Assert.assertEquals(a.num_items, 1);
        Assert.assertEquals(a.items.length, 1);
        Item tower = a.items[0];

        Assert.assertEquals(a.money, 1000-((Tower)tower).getCost());
        Assert.assertEquals(a.getTowerAt(new Coordinate(2, 3)), tower);

        Assert.assertTrue(a.upgradeTower((Tower) tower));
        Assert.assertEquals(a.money, 1000-((Tower)tower).getCost()-((Tower)tower).getUpgradeCost());
        Item items[] = {tower};
        Assert.assertArrayEquals(a.items, items);

        a.setMoney(1);
        Assert.assertFalse(a.addTower(1, 2, 3));
    }
    @Test
    public void upgradeTower() {
        Arena a = new Arena(null);
        a.setMoney(58);
        a.addTower(0, 2, 3);
        a.addTower(1, 2, 3);
        a.addTower(2, 2, 3);
        a.addTower(3, 2, 3);
        Assert.assertEquals(a.money, 0);
        Tower Btower = (Tower)a.items[0];
        Tower Itower = (Tower)a.items[1];
        Tower Ctower = (Tower)a.items[2];
        Tower Ltower = (Tower)a.items[3];
        a.setMoney(69);
        Assert.assertFalse(a.upgradeTower(null));
        Assert.assertTrue(a.upgradeTower(Btower));
        Assert.assertTrue(a.upgradeTower(Itower));
        Assert.assertTrue(a.upgradeTower(Ctower));
        Assert.assertTrue(a.upgradeTower(Ltower));
        Assert.assertEquals(a.money, 0);

        Assert.assertFalse(a.upgradeTower(Btower));
        Assert.assertFalse(a.upgradeTower(Itower));
        Assert.assertFalse(a.upgradeTower(Ctower));
        Assert.assertFalse(a.upgradeTower(Ltower));


    }
    @Test
    public void insertMoreTower() {
        Arena a = new Arena(null);
        a.setMoney(1000);
        a.addTower(0, 1, 3);
        a.addTower(1, 1, 4);
        a.addTower(2, 1, 5);
        Assert.assertTrue(a.addTower(3, 1, 6));

        Assert.assertEquals(a.num_items, 4);
        Assert.assertEquals(a.items.length, 4);
    }
    @Test
    public void addMonster() {
        Arena a = new Arena(null);
        a.setMoney(1000);
        a.generateMonster();
        Assert.assertEquals(a.num_items, 1);
        Assert.assertEquals(a.items.length, 1);
        Item monster = a.items[0];

        Item items[] = {monster};
        Assert.assertArrayEquals(a.items, items);
    }
    @Test
    public void removeMonster() {
        Arena a = new Arena(null);
        a.generateMonster();
        a.generateMonster();
        Item monster0 = a.items[0];
        Item monster1 = a.items[1];
        Item notEmpty[] = {monster0};
        a.removeItem(monster1);
        Assert.assertEquals(a.num_items, 1);
        Assert.assertArrayEquals(a.items, notEmpty);

        Item empty[] = {};
        a.removeItem(monster0);
        Assert.assertEquals(a.num_items, 0);
        Assert.assertArrayEquals(a.items, empty);

    }
    @Test
    public void removeTower() {
        Arena a = new Arena(null);
        a.addTower(1,2,3);
        Item tower = a.items[0];
        Item empty[] = {};
        a.removeItem(tower);
        Assert.assertEquals(a.num_items, 0);
        Assert.assertEquals(a.items.length, 0);
        Assert.assertArrayEquals(a.items, empty);
    }
    @Test
    public void getTower() {
        Arena a = new Arena(null);
        a.generateMonster();
        a.addTower(1,2,4);
        a.addTower(1,4,3);
        a.addTower(1,2,3);
        Item tower = a.items[3];
        Tower t = a.getTowerAt(new Coordinate(2,3));
        Assert.assertEquals(t, tower);
        Tower t2 = a.getTowerAt(new Coordinate(5,5));
        Assert.assertEquals(t2, null);
    }
    @Test
    public void checkGameOver(){
        Arena a = new Arena(null);
        a.addTower(1,2,3);
        Assert.assertFalse(a.isGameOver());
        a.generateMonster();
        Monster monster = (Monster) a.items[1];
        monster.coord = new Coordinate(10,0);
        Assert.assertFalse(a.isGameOver());
        monster.coord = new Coordinate(10.5,0);
        Assert.assertFalse(a.isGameOver());
        //game over state
        monster.coord = new Coordinate(10.6,0);
        Assert.assertTrue(a.isGameOver());
        monster.coord = new Coordinate(11,0);
        Assert.assertTrue(a.isGameOver());
    }
    @Test
    public void nextRound(){
        Arena a = new Arena(paneArena);
        a.addTower(0,1,1);
        Tower t = a.getTowerAt(new Coordinate(1,1));
        for(int i=0; i<10; i++){
            a.upgradeTower(t);
        }
        a.generateMonster();
        a.generateMonster();
        a.nextRound();
        a.nextRound();
        a.removeItem(t);
        a.addTower(3,1,2);
        a.addTower(2,1,4);
        a.nextRound();
        a.nextRound();
        a.nextRound();
        a.nextRound();
        a.nextRound();
        a.setMoney(0);
        a.nextRound();

    }
}
