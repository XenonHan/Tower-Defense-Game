/**
 *
 * You might want to uncomment the following code to learn testFX. Sorry, no tutorial session on this.
 *
 */
package sample;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;


import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;


public class FxTest extends ApplicationTest {

	private Scene s;
	private Stage primaryStage;
	private Label grid[][];

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample.fxml"));
		Parent root = loader.load();
		primaryStage.setTitle("Tower Defence");
		s = new Scene(root, 600, 480);
		primaryStage.setScene(s);
		primaryStage.show();
		MyController appController = (MyController)loader.getController();
		appController.createArena();
		grid = appController.getGrid();
	}

	@Test
	public void testAddTower() {
		AnchorPane b = (AnchorPane) s.lookup("#paneArena");
		clickOn("#buttonPlay");

		//success add
		drag("#labelBasicTower");
		dropTo(grid[1][1]);
		Assert.assertFalse(grid[1][1].getGraphic() == null);
		drag("#labelIceTower");
		dropTo(grid[3][1]);
		Assert.assertFalse(grid[3][1].getGraphic() == null);
		drag("#labelCatapult");
		dropTo(grid[5][1]);
		Assert.assertFalse(grid[5][1].getGraphic() == null);
		drag("#labelLaserTower");
		dropTo(grid[7][3]);
		Assert.assertFalse(grid[7][3].getGraphic() == null);


		//fail add
		drag("#labelLaserTower");
		moveTo(grid[2][2]);
		dropTo(grid[2][2]);
		Assert.assertTrue(grid[2][2].getGraphic() == null);
		Assert.assertTrue(grid[1][0].getGraphic() == null);

		moveTo(grid[8][8]);
		moveTo(grid[1][1]);
		moveTo(grid[3][1]);
		moveTo(grid[5][1]);
		moveTo(grid[7][3]);

		for(int i=0; i<200; i++){
			clickOn("#buttonNextFrame");
		}
	}

//	@Test
//	public void showTowerInfoTest(){
//
//	}

//		Assert.assertTrue();
//		for (javafx.scene.Node i : b.getChildren()) {
//			if (i.getClass().getName().equals("javafx.scene.control.Label")) {
//				Label h = (Label)i;
//				if (h.getLayoutX() == 0 && h.getLayoutY() == 0)
//					Assert.assertEquals(h.getText(), "M");
//			}
//		}
//	}
}