import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class TestFileCards {

	private Stage testFileCardsStage;
	private UserInterface ui;
	private String name;
	private Label filecardLabel = new Label("Filecard");
	private Label categoryLabel = new Label("Category:");
	private Label sideLabel = new Label("Language:");
	private Label sideALabel = new Label("Side A");
	private Label sideBLabel = new Label("Side B");
	private Label levelLabel = new Label("Level:");
	private Button startButton = new Button("Start");
	private Button cancelButton = new Button("Cancel");
	private ComboBox<String> categoryBox = new ComboBox<String>();
	private ComboBox<String> levelBox = new ComboBox<String>();
	private ComboBox<String> sideABox = new ComboBox<String>();
	private ComboBox<String> sideBBox = new ComboBox<String>();
	private ObservableList<String> optionsCategory = FXCollections.observableArrayList("Translation", "Definition");
	private ObservableList<String> optionsSides = FXCollections.observableArrayList("English", "German", "French", "Spanish");
	private ObservableList<String> optionsLevel = FXCollections.observableArrayList("1", "2", "3", "4");
	
	public TestFileCards(Stage loginStage, UserInterface ui)
	{
		this.ui = ui;
		testFileCardsStage = loginStage;
		createTestFileCardsWindowScene();
	}
	
	public void createTestFileCardsWindowScene()
	{
		filecardLabel.setId("header");
		startButton.setId("button");
		cancelButton.setId("button");
		categoryBox.setId("select");
		levelBox.setId("select");
		sideABox.setId("select");
		sideBBox.setId("select");
		
		categoryBox.setItems(optionsCategory);
		categoryBox.setPromptText("Please choose a category");
		
		levelBox.setItems(optionsLevel);
		levelBox.setValue("1");
		
		sideABox.setItems(optionsSides);
		sideBBox.setItems(optionsSides);
		sideABox.setPromptText("From");	
		sideBBox.setPromptText("To");
		
		categoryBox.setOnAction((event) -> {
		    String selectedCategory = categoryBox.getSelectionModel().getSelectedItem();
		   
		    if(selectedCategory.equals("Translation"))
		    {
		    	sideABox.setValue(null);
		    	sideBBox.setValue(null);
				sideBBox.setVisible(true);
		    	sideBLabel.setVisible(true);
		    	sideALabel.setVisible(true);
		    	
		    	startButton.disableProperty().bind(
						categoryBox.valueProperty().isNull()
						.or(sideABox.valueProperty().isNull() )
						.or(sideBBox.valueProperty().isNull() ) );
		    	
		    }
		    else
		    {
		    	sideABox.setValue("English");
		    	sideBBox.setValue(sideABox.getValue());
		    	sideBBox.setVisible(false);
		    	sideBLabel.setVisible(false);
		    	sideALabel.setVisible(false);
		    	
		    	startButton.disableProperty().bind(
						categoryBox.valueProperty().isNull()
						.or(sideABox.valueProperty().isNull() ) );
		    }
		});
	
		GridPane.setHalignment(filecardLabel, HPos.CENTER);
		GridPane.setHalignment(categoryLabel, HPos.RIGHT);
		GridPane.setHalignment(sideLabel, HPos.RIGHT);
		GridPane.setHalignment(levelLabel, HPos.RIGHT);
		GridPane.setHalignment(sideALabel, HPos.CENTER);
		GridPane.setHalignment(sideBLabel, HPos.CENTER);
		GridPane.setHalignment(categoryBox, HPos.CENTER);
		GridPane.setHalignment(levelBox, HPos.LEFT);
		GridPane.setHalignment(sideABox, HPos.CENTER);
		GridPane.setHalignment(sideBBox, HPos.CENTER);
		categoryBox.setPrefWidth(225);
		levelBox.setPrefWidth(50);
		sideABox.setPrefWidth(100);
		sideBBox.setPrefWidth(100);
		
		GridPane grid = new GridPane();
		
		grid.setId("pane2");
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(30, 10, 50, 10));
		grid.add(filecardLabel, 0, 0, 3, 1);
		grid.add(categoryLabel, 0, 3);
		grid.add(categoryBox, 1, 3, 2, 1);
		grid.add(sideALabel, 1, 6);
		grid.add(sideBLabel, 2, 6);
		grid.add(sideLabel, 0, 7);
		grid.add(sideABox, 1, 7);
		grid.add(sideBBox, 2, 7);
		grid.add(levelLabel, 0, 10);
		grid.add(levelBox, 1, 10);
		grid.add(startButton, 1, 14);
		grid.add(cancelButton, 2, 14);
		
		startButton.setOnAction(e -> {
			new Testing(testFileCardsStage, ui, levelBox.getValue().toString(), categoryBox.getValue().toString(), sideABox.getValue(), sideBBox.getValue());
		});
		
		cancelButton.setOnAction(e -> {
			testFileCardsStage.close();
			new UserInterface(ui.getLoginStage(), ui.getName(), ui.getLoginScene());
		});
		
		
		
		//grid.setGridLinesVisible(true);
		
		Scene testWindowScene = new Scene(grid, 500, 600);
		testWindowScene.getStylesheets().addAll(this.getClass().getResource("Style.css").toExternalForm());
		
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		
		testFileCardsStage.setScene(testWindowScene);	
		testFileCardsStage.setX((primScreenBounds.getWidth() - testFileCardsStage.getWidth()) / 2);
		testFileCardsStage.setY((primScreenBounds.getHeight() - testFileCardsStage.getHeight()) / 2);
		
	}
}
