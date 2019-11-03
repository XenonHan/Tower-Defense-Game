package sample;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import java.awt.*;
import java.net.URISyntaxException;

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
                    target.setOnDragDropped(new DragDroppedEventHandler(target, i, j, arena, labelMoneyLeft));
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
    DragDroppedEventHandler(Label target , int x, int y, Arena arena, Label labelMoneyLeft){
        this.target = target;
        this.x = x;
        this.y = y;
        this.labelMoneyLeft = labelMoneyLeft;
        this.arena = arena;
    }
    @Override
    public void handle(DragEvent event) {
        System.out.println("xx");
        Dragboard db = event.getDragboard();
        boolean success = false;
        //System.out.println(db.getString());

        String url = null;
        switch (db.getString()){
            case "Basic Tower": url= "file:src/main/resources/basicTower.png"; break;
            case "Ice Tower":  url= "file:src/main/resources/iceTower.png"; break;
            case "Catapult":  url= "file:src/main/resources/catapult.png"; break;
            case "Laser Tower":  url= "file:src/main/resources/laserTower.png"; break;
            default: throw new IllegalArgumentException("drag tower error");
        }

        Image towerImage = new Image(url, 40, 40, true, true);
        Node towerIcon = new ImageView(towerImage);

        if (target.getGraphic() == null) {  //if it already have tower,cannot build tower
            if(!arena.addBuilding(0, x, y)){
                return;
            }
            //((Label)event.getGestureTarget()).setText(db.getString());
            target.setGraphic(towerIcon);
            labelMoneyLeft.setText(String.valueOf(arena.getMoney()));

            success = true;
        }
        target.setOnMouseEntered(new MouseEnterEventHandler(target));
        target.setOnMouseExited(new MouseExitedEventHandler(target));
        event.setDropCompleted(success);
        event.consume();
    }
}

class MouseEnterEventHandler implements EventHandler<MouseEvent>{
    //show tower info and shaded fire area
    private Label source;
    public MouseEnterEventHandler(Label e) {
        source = e;
    }
    @Override
    public void handle (MouseEvent event) {
        System.out.println("mouse over");
        event.consume();
    }
}

class MouseExitedEventHandler implements EventHandler<MouseEvent>{
    //remove tower info and shaded fire area
    private Label source;
    public MouseExitedEventHandler(Label e) {
        source = e;
    }
    @Override
    public void handle (MouseEvent event) {
        System.out.println("mouse exit");
        event.consume();
    }
}