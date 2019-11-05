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
import javafx.stage.Stage;
import javafx.stage.StageStyle;


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
    private Arena arena = new Arena();


    private int x = -1, y = 0; //where is my monster
    /**
     * A dummy function to show how button click works
     */
    @FXML
    private void play() {
        System.out.println("Game Start");
        buttonNextFrame.setDisable(false);
        buttonPlay.setDisable(true);
        arena.nextRound();
        //testing, it is useless
        Image monsterImage = new Image("file:src/main/resources/fox.png", 20, 20, true, true);
        grids[0][0].setGraphic(new ImageView(monsterImage));
        grids[0][0].setAlignment(Pos.CENTER);
    }

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
            }
        setDragAndDrop();
    }

    @FXML
    private void nextFrame() {
        arena.nextRound();                      //process next round
        //todo clear all the white grid
        for(int i=0; i<MAX_V_NUM_GRID; i++){
            for(int j=0; j<MAX_H_NUM_GRID; j++){
                if (j % 2 == 0 || i == ((j + 1) / 2 % 2) * (MAX_V_NUM_GRID - 1)){
                    grids[i][j].setGraphic(null);       //remove all white grid image;
                }
            }
        }
        for(int i=0; i<arena.getNumItems(); i++){               //place monster in updated location
            if((arena.getItems())[i] instanceof Monster){
                Monster monster = (Monster)((arena.getItems())[i]);
                int x = monster.coord.x;
                int y = monster.coord.y;
                String url;
                switch(monster.getType()){
                    case Fox: url = "file:src/main/resources/fox.png"; break;
                    case Penguin: url = "file:src/main/resources/penguin.png"; break;
                    case Unicorn: url = "file:src/main/resources/unicorn.png"; break;
                    default: throw new IllegalArgumentException("invalid type of monster");
                }
                Image monsterImage = new Image(url, 30, 30, true, true);
                grids[y][x].setGraphic(new ImageView(monsterImage));
                grids[y][x].setAlignment(Pos.CENTER);
                grids[y][x].setGraphic(null);
            }
        }
        if(arena.isGameOver()){
            System.out.println("Game Over");
            return;
        }
        //update resources
        labelMoneyLeft.setText(String.valueOf(arena.getMoney()));
    }

    /**
     * A function that demo how drag and drop works
     */
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
    }
}

class DragEventHandler implements EventHandler<MouseEvent> {
    private Label source;

    public DragEventHandler(Label e) {
        source = e;
    }
    @Override
    public void handle (MouseEvent event) {
        System.out.println("Draging a " + source.getText());
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
        System.out.println("onDragOver");

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
        System.out.println("onDragEntered");
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
        System.out.println("Exit");
        event.consume();
    }
}

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
        //todo calculate and represent the range carefully
        //double range = tower.getRange();      //uncomment this will have bug idk why
        Circle shadedArea = new Circle();
        shadedArea.centerXProperty().set(x*40 + 20);
        shadedArea.centerYProperty().set(y*40 + 20);
        shadedArea.setRadius(100);
        shadedArea.setFill(Color.RED);
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
        System.out.println("xx");
        Dragboard db = event.getDragboard();
        boolean success = false;
        Node towerIcon = getTowerIcon(db);
        Circle shadedArea = generateShadedRegion();

        if (target.getGraphic() == null) {  //if it already have tower,cannot build tower
            if(arena.addBuilding(towerID, x, y)){
                success = true;
                target.setGraphic(towerIcon);
                target.setAlignment(Pos.CENTER);
                //update resources
                labelMoneyLeft.setText(String.valueOf(arena.getMoney()));
                tower = (Tower)arena.getItemAt(new Coordinate(x,y));
                MouseExitedEventHandler exitEvent = new MouseExitedEventHandler(target, paneArena, shadedArea);
                target.setOnMouseExited(exitEvent);
                MouseEnterEventHandler mouseEnter = new MouseEnterEventHandler(target, paneArena, shadedArea, tower, exitEvent);
                target.setOnMouseEntered(mouseEnter);
                target.setOnMouseClicked(new MouseClickedEventHandler(target, paneArena, tower, arena, labelMoneyLeft));
            }
        }
        event.setDropCompleted(success);
        event.consume();
    }
}

class MouseEnterEventHandler implements EventHandler<MouseEvent>{
    //show tower info and shaded fire area
    private Label target;
    private AnchorPane paneArena;
    private Circle shadedArea;
    private Tower tower;
    private MouseExitedEventHandler exitEvent;

    public MouseEnterEventHandler(Label target, AnchorPane paneArena, Circle shadedArea, Tower tower, MouseExitedEventHandler exitEvent) {
        this.target = target;
        this.paneArena = paneArena;
        this.shadedArea = shadedArea;
        this.tower = tower;
        this.exitEvent = exitEvent;
    }
    private Label getInfoPane(){
        int x = tower.coord.x;
        int y = tower.coord.y;
        Label infoPane = new Label();
        if(x<6){        //x = 1,2,3
            infoPane.setLayoutX((4+x) * 40);
            if(y<6) infoPane.setLayoutY((4+y) * 40);

            else infoPane.setLayoutY((y-4) * 40);
        }
        else{           //x = 4, 5, 6
            infoPane.setLayoutX((x-4) * 40);
            if(y<6) infoPane.setLayoutY((4+y) * 40);
            else infoPane.setLayoutY((y-4) * 40);

        }
        infoPane.setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));
        infoPane.setMaxHeight(100);
        infoPane.setMaxWidth(100);
        infoPane.setMinHeight(100);
        infoPane.setMinWidth(100);
        infoPane.setStyle("-fx-border-color: black;");
        infoPane.setText("Type: " + tower.type + "\nLevel: " + 1 + "\nPower: " + tower.power +
                "\nUpgrade Cost: " + tower.cost+ "\nRange: " + tower.range);
        //Todo improve here
        //freeze power of ice tower, cool down time of catapult not handel yet
        return infoPane;
    }
    @Override
    public void handle (MouseEvent event) {
        System.out.println("mouse over");
        paneArena.getChildren().add(shadedArea);
        Node infoPane = getInfoPane();
        paneArena.getChildren().add(infoPane);
        exitEvent.setInfoPane(infoPane);
        target.toFront();
        event.consume();
    }
}

class MouseExitedEventHandler implements EventHandler<MouseEvent>{
    //remove tower info and shaded fire area
    private Label target;
    private AnchorPane paneArena;
    private Circle shadedArea;
    private Node infoPane;
    public MouseExitedEventHandler(Label target, AnchorPane paneArena, Circle shadedArea) {
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
        System.out.println("mouse exit");
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
        System.out.println("mouse clicked");
        Stage stage = new Stage();
        HBox btnPlatform = new HBox();
        btnPlatform.setAlignment(Pos.CENTER);
        Button destroy = new Button("Destroy Tower");
        destroy.setOnAction(new DestroyActionHandler(stage, tower, arena, target));
        Button upgrade = new Button("Upgrade Tower");
        upgrade.setOnAction(new UpgradeActionHandler(stage, tower, arena, labelMoneyLeft));
        btnPlatform.getChildren().add(destroy);
        btnPlatform.getChildren().add(upgrade);

//        btnPlatform.setPadding(new Insets(15, 15, 15, 15));
//        btnPlatform.setStyle("-fx-background-color: black");
//        btnPlatform.setStyle("-fx-border-color: black;");
//        btnPlatform.setLayoutX(20);
//        btnPlatform.setLayoutX(20);
//        btnPlatform.setMaxHeight(100);
//        btnPlatform.setMaxWidth(100);
//        btnPlatform.setMinHeight(100);
//        btnPlatform.setMinWidth(100);

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
        System.out.println("Upgrade button clicked");
        arena.upgradeTower(tower);
        //update resources
        labelMoneyLeft.setText(String.valueOf(arena.getMoney()));
        stage.close();
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
        System.out.println("Destroy button clicked");
        target.setOnMouseEntered(null);
        target.setOnMouseExited(null);
        target.setOnMouseClicked(null);
        target.setGraphic(null);
        System.out.println("remove mouse event");
        arena.removeItem(tower);
        stage.close();
    }
}