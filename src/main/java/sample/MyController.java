package sample;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
    /**
     * the arena that the controller will take control of
     */
    Arena arena = null;
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
        labelMoneyLeft.setText(String.valueOf(arena.money));
        //add a faster method to process to next round
        buttonNextFrame.setOnMouseDragged(e->nextFrame());
    }
    /**
     * create the arena which consist of white and green grid.
     * Add symbolic graphic to end zone and monster generate zone
     */
    @FXML
    public void createArena() {
        buttonNextFrame.setDisable(true);
        buttonSimulate.setDisable(true);
        if (grids[0][0] != null){
            return; //created already
        }
        for (int i = 0; i < MAX_V_NUM_GRID; i++) {
            for (int j = 0; j < MAX_H_NUM_GRID; j++) {
                Label newLabel = new Label();
                if (j % 2 == 0 || i == ((j + 1) / 2 % 2) * (MAX_V_NUM_GRID - 1)) {
                    newLabel.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                }
                else {
                    newLabel.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                }
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
        //add icon to start and end zone
        Node endZone = new ImageView(new Image("file:src/main/resources/endZone.jpg", 35, 35, true, true));
        Node startZone = new ImageView(new Image("file:src/main/resources/startZone.png", 35, 35, true, true));
        grids[0][0].setGraphic(startZone);
        grids[0][0].setAlignment(Pos.CENTER);
        grids[0][11].setGraphic(endZone);
        grids[0][11].setAlignment(Pos.CENTER);
    }

    /**
     * player can process to next round by clicking the nextFrame button or
     * press the nextFrame button and keep dragging the mouse.
     * <p>this function help process the next frame option</p>
     * <p>first, remove all the graphic that show the tower attack, then process next round</p>
     * then place monster icon in updated location<
     * check if is game over if yes, stop the game, pop up a pane to inform the player, else update resources</p>
     */
    @FXML
    private void nextFrame(){
        //clear the attack line
        clearGraphic();
        //process next round
        arena.nextRound();
        //move monster icons
        while (!monsterIcons.empty()){              //first clear all the monster icon
            paneArena.getChildren().remove(monsterIcons.pop());
        }
        for(int i=0; i<arena.num_items; i++){       //then place monster in updated location
            if((arena.items)[i] instanceof Monster){
                Monster monster = (Monster)((arena.items)[i]);
                String url;
                //choose correct icon according to monster type
                switch(monster.getType()){
                    case Fox: url = "file:src/main/resources/fox.png"; break;
                    case Penguin: url = "file:src/main/resources/penguin.png"; break;
                    default: url = "file:src/main/resources/unicorn.png";
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
            String info = "Game Over ! _ ! You pass " + arena.turn + " turn";
            Alert loseInfo = new Alert(Alert.AlertType.INFORMATION, info);
            loseInfo.setHeaderText("Opps...");
            loseInfo.show();
            return;
        }
        //update resources
        labelMoneyLeft.setText(String.valueOf(arena.money));
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
        paneArena.setOnMouseMoved(e-> clearGraphic());
    }

    /**
     * clear all the graphic that representing the tower attack
     */
    private void clearGraphic(){
        while(!arena.attackGraphic.empty()){
            //System.out.println("line clear");
            paneArena.getChildren().remove(arena.attackGraphic.pop());
        }
    }

    //just for testing, should not be called in the game
    /**
     * get grid
     * @return number of grid
     */
    public Label[][] getGrid(){return grids;};
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
    /**
     * process the target item
     * @param target the target item
     */
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
    /**
     * process the target item
     * @param target the target item
     */
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
    /**
     * same to process the target item
     * @param target the target item
     */
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

/**
 * event handler for placing the tower
 */
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
        Circle shadedArea = new Circle();
        shadedArea.setCenterX(x*40 + 20);
        shadedArea.setCenterY(y*40 + 20);
        shadedArea.setRadius(tower.range);
        shadedArea.setFill(Color.RED);
        shadedArea.setOpacity(0.4);
        if(tower instanceof Catapult){
            shadedArea.setRadius(100);
            shadedArea.setStroke(Color.RED);
            shadedArea.setStrokeWidth(100);
            shadedArea.setFill(null);
        }
        return shadedArea;
    }
    private Node getTowerIcon(Dragboard db){
        String url;
        switch (db.getString()){
            case "Basic Tower": url= "file:src/main/resources/basicTower.png"; towerID=0; break;
            case "Ice Tower":  url= "file:src/main/resources/iceTower.png"; towerID=1; break;
            case "Catapult":  url= "file:src/main/resources/catapult.png"; towerID=2; break;
           default:  url= "file:src/main/resources/laserTower.png"; towerID=3;
        }
        Image towerImage = new Image(url, 40, 40, true, true);
        return new ImageView(towerImage);
    }

    @Override
    public void handle(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        Node towerIcon = getTowerIcon(db);

        if (target.getGraphic() == null) {  //if it already have tower, cannot build tower
            if(arena.addTower(towerID, x, y)){
                success = true;
                target.setGraphic(towerIcon);
                target.setAlignment(Pos.CENTER);
                //update resources
                labelMoneyLeft.setText(String.valueOf(arena.money));
                tower = arena.getTowerAt(new Coordinate(x,y));
                Circle shadedArea = generateShadedRegion();
                MouseExitedTowerEventHandler exitEvent = new MouseExitedTowerEventHandler(target, paneArena, shadedArea);
                target.setOnMouseExited(exitEvent);
                MouseEnterTowerEventHandler mouseEnter = new MouseEnterTowerEventHandler(target, paneArena, shadedArea, tower, exitEvent);
                target.setOnMouseEntered(mouseEnter);
                target.setOnMouseClicked(new MouseClickedEventHandler(target, paneArena, tower, arena, labelMoneyLeft));
            }
            else {
                Alert info = new Alert(Alert.AlertType.WARNING, "Not enough resources to build the tower");
                info.show();
            }
        }
        event.setDropCompleted(success);
        event.consume();
    }
}

/**
 * event handler for show tower info and shaded fire area
 */
class MouseEnterTowerEventHandler implements EventHandler<MouseEvent>{
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
                "\nUpgrade Cost: " + tower.cost+ "\nRange: " + tower.range + "\nLevel = "+ tower.level);
        //Todo improve here
        //freeze power of ice tower, cool down time of catapult not handel yet
        infoPane.getText();
        switch(tower.getTowerType()){
            case Catapult: infoPane.setText(infoPane.getText()+ "\nFreeze Time: " + ((Catapult)tower).coolingTime + "\nRemain Cooldown Period: " + ((Catapult)tower).remainCoolingPeriod); break;
            case IceTower: infoPane.setText(infoPane.getText()+ "\nCool Down Time: " + ((IceTower)tower).freezeTime); break;
            case BasicTower: break;
            default: infoPane.setText(infoPane.getText()+ "\nMoney Consume: " + ((LaserTower)tower).attackCost);
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
/**
 * event handler for remove tower info and shaded fire area
 */
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
/**
 * event handler for let the player choose upgrade or destory tower option
 */
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
        VBox operationPane = new VBox();
        Label notes = new Label("Tower max Level Reached");
        HBox btnPlatform = new HBox();
        btnPlatform.setAlignment(Pos.CENTER);
        Button destroy = new Button("Destroy Tower");
        destroy.setOnAction(new DestroyActionHandler(stage, tower, arena, target));
        Button upgrade = new Button("Upgrade Tower");
        upgrade.setOnAction(new UpgradeActionHandler(stage, tower, arena, labelMoneyLeft));

        btnPlatform.getChildren().add(destroy);
        btnPlatform.getChildren().add(upgrade);
        operationPane.getChildren().add(btnPlatform);
        if(tower.level>3){              //a tower can only be upgraded three times
            upgrade.setDisable(true);
            operationPane.getChildren().add(notes);
        }
        operationPane.setAlignment(Pos.CENTER);
        //the new scene pop up to let player choose tower operation
        Scene scene = new Scene(operationPane, 220, 50);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);
        stage.setScene(scene);
        stage.setTitle("Tower Operation");
        stage.show();
        event.consume();
    }
}

/**
 * event handler for upgrade tower option
 */
class UpgradeActionHandler implements EventHandler<ActionEvent> {
    Stage stage;
    Tower tower;
    Arena arena;
    Label labelMoneyLeft;

    UpgradeActionHandler(Stage stage, Tower tower, Arena arena, Label labelMoneyLeft){
        this.stage = stage;
        this.tower = tower;
        this.arena = arena;
        this.labelMoneyLeft = labelMoneyLeft;
    }
    @Override
    public void handle(ActionEvent event) {
        //System.out.println("Upgrade button clicked");
        arena.upgradeTower(tower);
        //update resources
        labelMoneyLeft.setText(String.valueOf(arena.money));
        stage.close();    //todo should uncomment this line afterward
    }
}
/**
 * event handler for destory tower option
 */
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
        //Todo implement the remove operation
        //System.out.println("Destroy button clicked");
        target.setOnMouseEntered(null);
        target.setOnMouseExited(null);
        target.setOnMouseClicked(null);
        target.setGraphic(null);
        arena.removeItem(tower);
        stage.close();
    }
}

/**
 * event handler for showing monster remaining HP
 */
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
/**
 * event handler for removing showing monster remaining HP
 */
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