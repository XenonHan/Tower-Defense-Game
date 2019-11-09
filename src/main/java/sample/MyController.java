package sample;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Stack;

/**
 * Construction the Arena, process all the mouse event perform by the player
 */
public class MyController {
    @FXML
    private Button buttonNextFrame;
    @FXML
    private Button buttonSimulate;
    @FXML
    private Button buttonPlay;
    @FXML
    private AnchorPane paneArena;
    @FXML
    private Label labelBasicTower;
    @FXML
    private Label labelIceTower;
    @FXML
    private Label labelCatapult;
    @FXML
    private Label labelLaserTower;
    @FXML
    private Label labelMoneyLeft;

    private static final int ARENA_WIDTH = 480;
    private static final int ARENA_HEIGHT = 480;
    private static final int GRID_WIDTH = 40;
    private static final int GRID_HEIGHT = 40;
    private static final int MAX_H_NUM_GRID = 12;
    private static final int MAX_V_NUM_GRID = 12;

    private Label grids[][] = new Label[MAX_V_NUM_GRID][MAX_H_NUM_GRID]; //the grids on arena
    private Arena arena = null;
    private Stack<ImageView> monsterIcons = new Stack<>();

    /**
     * will be call when the player press the play button
     * <p>enable the next frame button and set the drag and drop event</p>
     */
    @FXML
    private void play(){
        System.out.println("Game Start");
        buttonNextFrame.setDisable(false);
        buttonPlay.setDisable(true);
        setDragAndDrop();
    }
    /**
     * create the arena which consist of white and green grid
     */
    @FXML
    public void createArena() {
        buttonNextFrame.setDisable(true);
        buttonSimulate.setDisable(true);
        if (grids[0][0] != null)
            return; //created already
        for (int i = 0; i < MAX_V_NUM_GRID; i++)
            for (int j = 0; j < MAX_H_NUM_GRID; j++) {
                Label newLabel = new Label();
                if (j % 2 == 0 || i == ((j + 1) / 2 % 2) * (MAX_V_NUM_GRID - 1))
                    newLabel.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                else
                    newLabel.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                newLabel.setLayoutX(j * GRID_WIDTH);
                newLabel.setLayoutY(i * GRID_HEIGHT);
                newLabel.setMinWidth(GRID_WIDTH);
                newLabel.setMaxWidth(GRID_WIDTH);
                newLabel.setMinHeight(GRID_HEIGHT);
                newLabel.setMaxHeight(GRID_HEIGHT);
                newLabel.setStyle("-fx-border-color: black;");
                grids[i][j] = newLabel;
                paneArena.getChildren().addAll(newLabel);
                arena = new Arena(paneArena);
            }
    }

    /**
     * process the next frame option
     * <p>remove all the graphic that show the tower attack, then process next round</p>
     * <p>then place monster icon in updated location</p>
     * <p>check if is game over if yes, stop the game, else update resources</p>
     */
    @FXML
    private void nextFrame(){
        //clear the attack line
        while(!arena.getAttackGraphic().empty()){
            //System.out.println("line clear");
            paneArena.getChildren().remove(arena.getAttackGraphic().pop());
        }
        arena.nextRound();                      //process next round
        //move monster icons
        //first clear all the monster icon
        while (!monsterIcons.empty()){
            paneArena.getChildren().remove(monsterIcons.pop());
        }
        //then place monster in updated location
        for(int i=0; i<arena.getNumItems(); i++){
            if((arena.getItems())[i] instanceof Monster){
                Monster monster = (Monster)((arena.getItems())[i]);
                String url;
                switch(monster.getType()){
                    case Fox: url = "file:src/main/resources/fox.png"; break;
                    case Penguin: url = "file:src/main/resources/penguin.png"; break;
                    case Unicorn: url = "file:src/main/resources/unicorn.png"; break;
                    default: throw new IllegalArgumentException("invalid type of monster");
                }
                //replace the icon if the monster is already dead
                if(monster.isDead()){
                    url = "file:src/main/resources/collision.png";
                }
                Image monsterImage = new Image(url, 30, 30, true, true);
                ImageView imageView = new ImageView(monsterImage);
                imageView.setX(monster.coord.img_X);
                imageView.setY(monster.coord.img_Y);
                paneArena.getChildren().add(imageView);
                //add event handler if the monster is not dead yet
                if (!monster.isDead()) {
                    MouseExitedMonsterEventHandler exitEvent = new MouseExitedMonsterEventHandler(paneArena);
                    imageView.setOnMouseEntered(new MouseEnterMonsterEventHandler(paneArena, monster, exitEvent));
                    imageView.setOnMouseExited(exitEvent);
                }
                monsterIcons.push(imageView);
            }
        }
        if(arena.isGameOver()){
            System.out.println("Game Over");
            buttonNextFrame.setDisable(true);
            labelBasicTower.setOnDragDetected(null);
            labelIceTower.setOnDragDetected(null);
            labelCatapult.setOnDragDetected(null);
            labelLaserTower.setOnDragDetected(null);
            return;
        }
        //update resources
        labelMoneyLeft.setText(String.valueOf(arena.getMoney()));
    }

    private void setDragAndDrop() {
        labelBasicTower.setOnDragDetected(new DragEventHandler(labelBasicTower));
        labelIceTower.setOnDragDetected(new DragEventHandler(labelIceTower));
        labelCatapult.setOnDragDetected(new DragEventHandler(labelCatapult));
        labelLaserTower.setOnDragDetected(new DragEventHandler(labelLaserTower));

        for (int i = 0; i < MAX_V_NUM_GRID; i++) {
            for (int j = 0; j < MAX_H_NUM_GRID; j++) {
                if (!(j % 2 == 0 || i == ((j + 1) / 2 % 2) * (MAX_V_NUM_GRID - 1))) {   //those are green grid
                    Label target = grids[i][j];
                    target.setOnDragDropped(new DragDroppedEventHandler(target, j, i, arena, labelMoneyLeft, paneArena));
                    target.setOnDragOver(new DragOverEventHandler(target));
                    target.setOnDragEntered(new DragEnteredEventHandler(target));
                    target.setOnDragExited(new DragExitedEventHandler(target));
                }
            }
        }
        paneArena.setOnMouseMoved(new MouseMovedEventHandler(paneArena, arena.getAttackGraphic()));
    }
}

class DragEventHandler implements EventHandler<MouseEvent> {
    private Label source;

    public DragEventHandler(Label e) {
        source = e;
    }
    @Override
    public void handle (MouseEvent event) {
        //System.out.println("Draging a " + source.getText());
        Dragboard db = source.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString(source.getText());
        db.setContent(content);

        event.consume();
    }
}

class DragOverEventHandler implements EventHandler<DragEvent> {
    private Label target;

    public DragOverEventHandler(Label target){
        this.target = target;
    }
    @Override
    public void handle(DragEvent event) {
        /* data is dragged over the target */
        //System.out.println("onDragOver");

        /* accept it only if it is  not dragged from the same node
         * and if it has a string data */
        if (event.getGestureSource() != target &&
                event.getDragboard().hasString()) {
            /* allow for both copying and moving, whatever user chooses */
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }
}

class DragEnteredEventHandler implements EventHandler<DragEvent> {
    private Label target;

    public DragEnteredEventHandler(Label target){
        this.target = target;
    }
    @Override
    public void handle(DragEvent event) {
        /* the drag-and-drop gesture entered the target */
        //System.out.println("onDragEntered");
        /* show to the user that it is an actual gesture target */
        if (event.getGestureSource() != target &&
                event.getDragboard().hasString()) {
            target.setStyle("-fx-border-color: blue;");
        }

        event.consume();
    }
}

class DragExitedEventHandler implements EventHandler<DragEvent> {
    private Label target;

    public DragExitedEventHandler(Label target){
        this.target = target;
    }
    @Override
    public void handle(DragEvent event) {
        target.setStyle("-fx-border-color: black;");
        //System.out.println("Exit");
        event.consume();
    }
}

//the following event handler class is added by me
class DragDroppedEventHandler implements EventHandler<DragEvent> {
    private Label target;
    private int x;
    private int y;
    private Label labelMoneyLeft;
    private Arena arena;
    private AnchorPane paneArena;
    private Tower tower;
    private int towerID;

    DragDroppedEventHandler(Label target , int x, int y, Arena arena, Label labelMoneyLeft, AnchorPane paneArena){
        this.target = target;
        this.x = x;
        this.y = y;
        this.labelMoneyLeft = labelMoneyLeft;
        this.arena = arena;
        this.paneArena = paneArena;
        tower = null;
        towerID = -1;
    }
    private Circle generateShadedRegion(){
        //todo should draw a ring instead of circle if it is catapult
        Circle shadedArea = new Circle();
        shadedArea.centerXProperty().set(x*40 + 20);
        shadedArea.centerYProperty().set(y*40 + 20);
        shadedArea.setRadius(tower.range);
        shadedArea.setFill(Color.RED);
        //shadedArea.setFill(Color.TRANSPARENT);
        shadedArea.setOpacity(0.4);
        return shadedArea;
    }
    private Node getTowerIcon(Dragboard db){
        String url;
        switch (db.getString()){
            case "Basic Tower": url= "file:src/main/resources/basicTower.png"; towerID=0; break;
            case "Ice Tower":  url= "file:src/main/resources/iceTower.png"; towerID=1; break;
            case "Catapult":  url= "file:src/main/resources/catapult.png"; towerID=2; break;
            case "Laser Tower":  url= "file:src/main/resources/laserTower.png"; towerID=3; break;
            default: throw new IllegalArgumentException("drag tower error");
        }
        Image towerImage = new Image(url, 40, 40, true, true);
        return new ImageView(towerImage);
    }

    @Override
    public void handle(DragEvent event) {
        //System.out.println("xx");
        Dragboard db = event.getDragboard();
        boolean success = false;
        Node towerIcon = getTowerIcon(db);

        if (target.getGraphic() == null) {  //if it already have tower,cannot build tower
            if(arena.addTower(towerID, x, y)){
                success = true;
                target.setGraphic(towerIcon);
                target.setAlignment(Pos.CENTER);
                //update resources
                labelMoneyLeft.setText(String.valueOf(arena.getMoney()));
                tower = arena.getTowerAt(new Coordinate(x,y));
                Circle shadedArea = generateShadedRegion();
                MouseExitedTowerEventHandler exitEvent = new MouseExitedTowerEventHandler(target, paneArena, shadedArea);
                target.setOnMouseExited(exitEvent);
                MouseEnterTowerEventHandler mouseEnter = new MouseEnterTowerEventHandler(target, paneArena, shadedArea, tower, exitEvent);
                target.setOnMouseEntered(mouseEnter);
                target.setOnMouseClicked(new MouseClickedEventHandler(target, paneArena, tower, arena, labelMoneyLeft));
            }
        }
        event.setDropCompleted(success);
        event.consume();
    }
}

class MouseEnterTowerEventHandler implements EventHandler<MouseEvent>{
    //show tower info and shaded fire area
    private Label target;
    private AnchorPane paneArena;
    private Circle shadedArea;
    private Tower tower;
    private MouseExitedTowerEventHandler exitEvent;

    public MouseEnterTowerEventHandler(Label target, AnchorPane paneArena, Circle shadedArea, Tower tower, MouseExitedTowerEventHandler exitEvent) {
        this.target = target;
        this.paneArena = paneArena;
        this.shadedArea = shadedArea;
        this.tower = tower;
        this.exitEvent = exitEvent;
    }
    private Label getInfoPane(){
        double x = tower.coord.x;
        double y = tower.coord.y;
        Label infoPane = new Label();
        if(x<6){        //x = 1,2,3
            infoPane.setLayoutX((3+x) * 40);
            if(y<6) infoPane.setLayoutY((3+y) * 40);

            else infoPane.setLayoutY((y-3) * 40);
        }
        else{           //x = 4, 5, 6
            infoPane.setLayoutX((x-3) * 40);
            if(y<6) infoPane.setLayoutY((3+y) * 40);
            else infoPane.setLayoutY((y-3) * 40);

        }
        infoPane.setPadding(new Insets(5));
        infoPane.setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));
        infoPane.setStyle("-fx-border-color: black;");
        infoPane.setText("Type: " + tower.type + "\nPower: " + tower.power +
                "\nUpgrade Cost: " + tower.cost+ "\nRange: " + tower.range);
        //Todo improve here
        //freeze power of ice tower, cool down time of catapult not handel yet
        infoPane.getText();
        switch(tower.getTowerType()){
            case Catapult: infoPane.setText(infoPane.getText()+ "\nFreeze Time: " + ((Catapult)tower).coolingTime + "\nRemain Cooldown Period: " + ((Catapult)tower).remainCoolingPeriod); break;
            case IceTower: infoPane.setText(infoPane.getText()+ "\nCool Down Time: " + ((IceTower)tower).freezeTime); break;
            case BasicTower: break;
            case LaserTower: infoPane.setText(infoPane.getText()+ "\nMoney Consume: " + ((LaserTower)tower).attackCost); break;
            default: throw new IllegalArgumentException();
        }
        return infoPane;
    }
    @Override
    public void handle (MouseEvent event) {
        //System.out.println("mouse over");
        paneArena.getChildren().add(shadedArea);
        Node infoPane = getInfoPane();
        paneArena.getChildren().add(infoPane);
        exitEvent.setInfoPane(infoPane);
        target.toFront();
        event.consume();
    }
}

class MouseExitedTowerEventHandler implements EventHandler<MouseEvent>{
    //remove tower info and shaded fire area
    private Label target;
    private AnchorPane paneArena;
    private Circle shadedArea;
    private Node infoPane;
    public MouseExitedTowerEventHandler(Label target, AnchorPane paneArena, Circle shadedArea) {
        this.target = target;
        this.paneArena = paneArena;
        this.shadedArea = shadedArea;
        infoPane = null;
    }

    public void setInfoPane(Node infoPane) {
        this.infoPane = infoPane;
    }

    @Override
    public void handle (MouseEvent event) {
        //System.out.println("mouse exit");
        paneArena.getChildren().remove(shadedArea);
        paneArena.getChildren().remove(infoPane);
        event.consume();
    }
}

class MouseClickedEventHandler implements EventHandler<MouseEvent>{
    //remove tower info and shaded fire area
    private Label target;
    private AnchorPane paneArena;
    private Tower tower;
    private Arena arena;
    Label labelMoneyLeft;
    public MouseClickedEventHandler(Label target, AnchorPane paneArena, Tower tower, Arena arena, Label labelMoneyLeft) {
        this.target = target;
        this.paneArena = paneArena;
        this.tower = tower;
        this.arena = arena;
        this.labelMoneyLeft = labelMoneyLeft;
    }
    @Override
    public void handle (MouseEvent event) {
        //System.out.println("mouse clicked");
        Stage stage = new Stage();
        HBox btnPlatform = new HBox();
        btnPlatform.setAlignment(Pos.CENTER);
        Button destroy = new Button("Destroy Tower");
        destroy.setOnAction(new DestroyActionHandler(stage, tower, arena, target));
        Button upgrade = new Button("Upgrade Tower");
        upgrade.setOnAction(new UpgradeActionHandler(tower, arena, labelMoneyLeft));
        btnPlatform.getChildren().add(destroy);
        btnPlatform.getChildren().add(upgrade);

        Scene scene = new Scene(btnPlatform, 220, 40);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);
        stage.setScene(scene);
        stage.setTitle("Tower Operation");
        stage.show();
        event.consume();
    }
}

class UpgradeActionHandler implements EventHandler<ActionEvent> {
    Tower tower;
    Arena arena;
    Label labelMoneyLeft;

    UpgradeActionHandler(Tower tower, Arena arena, Label labelMoneyLeft){
        this.tower = tower;
        this.arena = arena;
        this.labelMoneyLeft = labelMoneyLeft;
    }
    @Override
    public void handle(ActionEvent event) {
        //System.out.println("Upgrade button clicked");
        arena.upgradeTower(tower);
        //update resources
        labelMoneyLeft.setText(String.valueOf(arena.getMoney()));
    }
}

class DestroyActionHandler implements EventHandler<ActionEvent> {
    Stage stage;
    Tower tower;
    Arena arena;
    Label target;
    public DestroyActionHandler(Stage stage, Tower tower, Arena arena, Label target){
        this.stage = stage;
        this.tower = tower;
        this.arena = arena;
        this.target = target;
    }
    @Override
    public void handle(ActionEvent event) {
        //Todo implement the remove operation// not quit sure correct or not
        //System.out.println("Destroy button clicked");
        target.setOnMouseEntered(null);
        target.setOnMouseExited(null);
        target.setOnMouseClicked(null);
        target.setGraphic(null);
        //System.out.println("remove mouse event");
        arena.removeItem(tower);
        stage.close();
    }
}

class MouseEnterMonsterEventHandler implements EventHandler<MouseEvent>{
    //show tower info and shaded fire area
    private AnchorPane paneArena;
    private Monster monster;
    private MouseExitedMonsterEventHandler exitEvent;

    public MouseEnterMonsterEventHandler(AnchorPane paneArena, Monster monster, MouseExitedMonsterEventHandler exitEvent) {
        this.paneArena = paneArena;
        this.monster = monster;
        this.exitEvent = exitEvent;
    }
    private Label getInfoPane(){
        double x = monster.coord.x;
        double y = monster.coord.y;
        Label infoPane = new Label();
        infoPane.setPadding(new Insets(5));
        //todo place the infoPane in a better way
        infoPane.setLayoutY((y+0.3) * 40);
        if(x<6){        //x = 1,2,3
            infoPane.setLayoutX((1+x) * 40);
        }
        else{           //x = 4, 5, 6
            infoPane.setLayoutX((x-3) * 40);
        }
        infoPane.setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));
        infoPane.setStyle("-fx-border-color: black;");
        infoPane.setText("Hp remaining: " + monster.health);
        return infoPane;
    }
    @Override
    public void handle (MouseEvent event) {
        //System.out.println("mouse over a monster");
        Node infoPane = getInfoPane();
        exitEvent.setInfoPane(infoPane);
        paneArena.getChildren().add(infoPane);
        event.consume();
    }
}

class MouseExitedMonsterEventHandler implements EventHandler<MouseEvent>{
    //show tower info and shaded fire area
    private AnchorPane paneArena;
    private Node infoPane = null;

    public MouseExitedMonsterEventHandler(AnchorPane paneArena) {
        this.paneArena = paneArena;
    }
    public void setInfoPane(Node infoPane) {
        this.infoPane = infoPane;
    }
    @Override
    public void handle (MouseEvent event) {
        //System.out.println("mouse exited a monster");
        paneArena.getChildren().remove(infoPane);
        event.consume();
    }
}

class MouseMovedEventHandler implements EventHandler<MouseEvent>{
    //remove tower info and shaded fire area
    private AnchorPane paneArena;
    private Stack<Shape> attackLine;
    public MouseMovedEventHandler(AnchorPane paneArena, Stack<Shape> attackLine) {
        this.paneArena = paneArena;
        this.attackLine = attackLine;
    }
    @Override
    public void handle (MouseEvent event) {
        while(!attackLine.empty()){
            //System.out.println("line clear");
            paneArena.getChildren().remove(attackLine.pop());
        }
        event.consume();
    }
}