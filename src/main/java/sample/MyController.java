package sample;

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

import java.awt.*;
import java.net.URISyntaxException;

import static java.lang.Thread.sleep;


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
    }
    private void updateMoney(){
        labelMoneyLeft.setText(String.valueOf(arena.getMoney()));
    }

    /**
     * A function that create the Arena
     */
    @FXML
    public void createArena() {
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
//        if (x == -1) {
//            grids[0][0].setText("M");
//            x = 0;
//            return;
//        }
//        if (y == MAX_V_NUM_GRID - 1)
//            return;
//        grids[y++][x].setText("");
//        grids[y][x].setText("M");
        arena.nextRound();
        if(arena.isGameOver()){
            System.out.println("Game Over");
        }
        updateMoney();
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
        //((Node) (event.getSource())).getScene().setCursor(Cursor.NONE);
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

    DragDroppedEventHandler(Label target , int x, int y, Arena arena, Label labelMoneyLeft, AnchorPane paneArena){
        this.target = target;
        this.x = x;
        this.y = y;
        this.labelMoneyLeft = labelMoneyLeft;
        this.arena = arena;
        this.paneArena = paneArena;
        tower = null;
    }
    private Circle generateShadedRegion(){
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
            case "Basic Tower": url= "file:src/main/resources/basicTower.png"; break;
            case "Ice Tower":  url= "file:src/main/resources/iceTower.png"; break;
            case "Catapult":  url= "file:src/main/resources/catapult.png"; break;
            case "Laser Tower":  url= "file:src/main/resources/laserTower.png"; break;
            default: throw new IllegalArgumentException("drag tower error");
        }
        Image towerImage = new Image(url, 40, 40, true, true);
        return new ImageView(towerImage);
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
        infoPane.setText("Type: " + tower.getType() + "\nLevel: " + 1 + "\nPower: " + 10 +
                "\nUpgrade Cost: " + tower.getCost()+ "\nRange: " + 20);
        return infoPane;
    }
    @Override
    public void handle(DragEvent event) {
        System.out.println("xx");
        Dragboard db = event.getDragboard();
        boolean success = false;
        Node towerIcon = getTowerIcon(db);
        Circle shadedArea = generateShadedRegion();

        if (target.getGraphic() == null) {  //if it already have tower,cannot build tower
            if(!arena.addBuilding(0, x, y)){
                return;
            }
            target.setGraphic(towerIcon);
            labelMoneyLeft.setText(String.valueOf(arena.getMoney()));
            success = true;
        }
        tower = (Tower)arena.getItemtAt(new Coordinate(x,y));
        Label infoPane = getInfoPane();

        target.setOnMouseEntered(new MouseEnterEventHandler(target, paneArena, x, y, shadedArea, infoPane));
        target.setOnMouseExited(new MouseExitedEventHandler(target, paneArena, shadedArea, infoPane));
        target.setOnMouseClicked(new MouseClickedEventHandler(target, paneArena));
        event.setDropCompleted(success);
        event.consume();
    }
}

class MouseEnterEventHandler implements EventHandler<MouseEvent>{
    //show tower info and shaded fire area
    private Label target;
    private AnchorPane paneArena;
    private int x;
    private int y;
    private Circle shadedArea;
    private Node infoPane;

    public MouseEnterEventHandler(Label target, AnchorPane paneArena, int x, int y, Circle shadedArea, Node infoPane) {
        this.target = target;
        this.paneArena = paneArena;
        this.x = x;
        this.y = y;
        this.shadedArea = shadedArea;
        this.infoPane = infoPane;
    }
    @Override
    public void handle (MouseEvent event) {
        System.out.println("mouse over");
        paneArena.getChildren().add(shadedArea);
        paneArena.getChildren().add(infoPane);
        target.toFront();
        event.consume();
    }
}

class MouseExitedEventHandler implements EventHandler<MouseEvent>{
    //remove tower info and shaded fire area
    private Label target;
    private AnchorPane paneArena;
    private Circle shadedArea;
    private Label infoPane;
    public MouseExitedEventHandler(Label target, AnchorPane paneArena, Circle shadedArea, Label infoPane) {
        this.target = target;
        this.paneArena = paneArena;
        this.shadedArea = shadedArea;
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
    public MouseClickedEventHandler(Label target, AnchorPane paneArena) {
        this.target = target;
        this.paneArena = paneArena;
    }
    @Override
    public void handle (MouseEvent event) {
        System.out.println("mouse clicked");
        HBox btnPlatform = new HBox();
        //btnPlatform.setPadding(new Insets(15, 15, 15, 15));
        btnPlatform.setStyle("-fx-background-color: black");
        Button destroy = new Button("Destroy Tower");
        destroy.setOnAction(new DestroyActionHandler());
        Button upgrade = new Button("Upgrade Tower");
        upgrade.setOnAction(new UpgradeActionHandler());
        
        btnPlatform.getChildren().add(destroy);
        btnPlatform.getChildren().add(upgrade);
        btnPlatform.setStyle("-fx-border-color: black;");
        btnPlatform.setLayoutX(20);
        btnPlatform.setLayoutX(20);
        btnPlatform.setMaxHeight(100);
        btnPlatform.setMaxWidth(100);
        btnPlatform.setMinHeight(100);
        btnPlatform.setMinWidth(100);

        Scene scene = new Scene(btnPlatform, 300, 50);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        event.consume();
    }
}

class DestroyActionHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent event) {
        System.out.println("Destroy button clicked");
    }
}
class UpgradeActionHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent event) {
        System.out.println("Upgrade button clicked");
    }
}