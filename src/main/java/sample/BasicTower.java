package sample;

public class BasicTower extends Tower{



    public BasicTower(int coodinateX, int coordinateY,int power)
    {
        super(coodinateX, coordinateY, 65, TowerType.BasicTower,10);
        this.power=power;
    }

    boolean attackMonster(Monster monster[],int size)
    {
        if(status==TowerStatus.Passive)
            return false;

        inAttackRange(monster,size);
        
        if(closestMon == null) {
        	return false;
        }
        closestMon.damage(power);
        return true;
    }

    int costForAttack() {return 0;}

    boolean upgrade()
    {
        power+=3; //I don't know, you may change
        return true; //You need to reduce recourse in arena
    }

}