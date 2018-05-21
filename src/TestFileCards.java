import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class TestFileCards {

	private Stage testStage;
	private Label filecardLabel = new Label("Filecard");
	private Label categoryLabel = new Label("Category:");
	private Label sideLabel = new Label("Side:");
	private Label sideALabel = new Label("Side A");
	private Label sideBLabel = new Label("Side B");
	private Label levelLabel = new Label("Level:");
	private Button startButton = new Button("Start");
	private Button cancelButton = new Button("Cancel");
	private ComboBox<String> categoryBox = new ComboBox();
	private ComboBox<String> levelBox = new ComboBox();
	private ComboBox<String> sideABox = new ComboBox();
	private ComboBox<String> sideBBox = new ComboBox();
	
	public TestFileCards(Stage loginStage)
	{
		testStage = loginStage;
		createTestWindowScene();
	}
	
	public void createTestWindowScene()
	{
		filecardLabel.setId("header");
		categoryLabel.setId("label");
		sideLabel.setId("label");
		sideALabel.setId("label");
		sideBLabel.setId("label");
		levelLabel.setId("label");
		startButton.setId("button");
		cancelButton.setId("button");
		
		GridPane.setHalignment(filecardLabel, HPos.CENTER);
		GridPane.setHalignment(categoryLabel, HPos.CENTER);
		GridPane.setHalignment(sideLabel, HPos.CENTER);
		GridPane.setHalignment(levelLabel, HPos.CENTER);
		GridPane.setHalignment(sideALabel, HPos.CENTER);
		GridPane.setHalignment(sideBLabel, HPos.CENTER);
		GridPane.setHalignment(categoryBox, HPos.CENTER);
		GridPane.setHalignment(levelBox, HPos.CENTER);
		GridPane.setHalignment(sideABox, HPos.CENTER);
		GridPane.setHalignment(sideBBox, HPos.CENTER);
		categoryBox.setPrefWidth(200);
		levelBox.setPrefWidth(50);
		sideABox.setPrefWidth(75);
		sideBBox.setPrefWidth(75);
		
		GridPane grid = new GridPane();
		
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setId("pane2");
		grid.setPadding(new Insets(50, 10, 50, 10));
		grid.add(filecardLabel, 0, 0, 3, 1);
		grid.add(categoryLabel, 0, 2);
		grid.add(categoryBox, 1, 2, 2, 1);
		grid.add(sideALabel, 1, 5);
		grid.add(sideBLabel, 2, 5);
		grid.add(sideLabel, 0, 6);
		grid.add(sideABox, 1, 6);
		grid.add(sideBBox, 2, 6);
		grid.add(levelLabel, 0, 8);
		grid.add(levelBox, 1, 8);
		grid.add(startButton, 0, 12);
		grid.add(cancelButton, 2, 12);
		
		//grid.setGridLinesVisible(true);
		
		Scene testWindowScene = new Scene(grid, 500, 600);
		testWindowScene.getStylesheets().addAll(this.getClass().getResource("Style.css").toExternalForm());
		
		testStage.setScene(testWindowScene);
		
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		testStage.setX((primScreenBounds.getWidth() - testStage.getWidth()) / 2);
		testStage.setY((primScreenBounds.getHeight() - testStage.getHeight()) / 2);	
	}
}
