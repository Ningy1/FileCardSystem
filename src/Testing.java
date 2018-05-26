import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Testing {

	private Stage testingStage;
	private UserInterface ui;
	private String category;
	private String level;
	private String numOfCards;
	private Label cardLabel = new Label("Kartei: ");
	private Label outOfLabel = new Label("1/"+numOfCards);
	private Label levelLabel;
	private String language = "Something";
	private Label whatIsLabel = new Label("What is");
	private Label fileCard = new Label("blablabla");
	private Label inLanguageLabel = new Label("in "+language+"?");
	private TextField answerField = new TextField();
	private Label answerFileCard = new Label("blablablub");
	private Label answerLabel = new Label("Wrong, the answer is: ");
	private Button editButton = new Button("Edit Filecard");
	private Button cancelButton = new Button("Quit learning");
	private Button nextButton = new Button("Next");
	
	public Testing(Stage testFileCardsStage, UserInterface ui, String levelChoice, String categoryChoice)
	{	
		category = categoryChoice;
		level = levelChoice;
		testingStage = testFileCardsStage;
		this.ui = ui;
		createTestingWindowScene();
	}
	
	private void createTestingWindowScene()
	{
		levelLabel = new Label("Level "+level);
		
		cardLabel.setId("headerTesting");
		outOfLabel.setId("headerTesting");
		levelLabel.setId("headerTesting");
		nextButton.setId("button");
		editButton.setId("button");
		cancelButton.setId("button");
		
		answerField.setPromptText("Enter your answer");
		fileCard.setUnderline(true);
		answerField.setPrefSize(fileCard.getWidth(), fileCard.getHeight());
		
		
		Pane pane = new Pane();
		pane.minHeightProperty().bind(whatIsLabel.heightProperty());
		pane.minWidthProperty().bind(levelLabel.heightProperty());
		
		Pane pane2 = new Pane();
		pane2.minHeightProperty().bind(whatIsLabel.heightProperty());
		pane2.minWidthProperty().bind(levelLabel.heightProperty());
		
		GridPane grid = new GridPane();
		
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(10);
		grid.setHgap(10);
		
		grid.setId("pane2");
		
		grid.add(cardLabel, 0, 0);
		GridPane.setHalignment(cardLabel, HPos.CENTER);
		grid.add(outOfLabel, 1, 0);
		GridPane.setHalignment(outOfLabel, HPos.LEFT);
		grid.add(levelLabel, 5, 0);
		GridPane.setHalignment(levelLabel, HPos.CENTER);
		grid.add(pane, 0, 1);
		grid.add(whatIsLabel, 1, 2);
		GridPane.setHalignment(whatIsLabel, HPos.RIGHT);
		grid.add(fileCard, 2, 2);
		GridPane.setHalignment(fileCard, HPos.CENTER);
		grid.add(inLanguageLabel, 3, 2);
		GridPane.setHalignment(inLanguageLabel, HPos.LEFT);
		grid.add(answerField, 2, 3, 2, 1);
		GridPane.setHalignment(answerField, HPos.CENTER);
		grid.add(nextButton, 4, 3);
		GridPane.setHalignment(nextButton, HPos.LEFT);
		grid.add(pane2, 0, 5);
		grid.add(answerLabel, 1, 6);
		GridPane.setHalignment(answerLabel, HPos.RIGHT);
		grid.add(answerFileCard, 2, 6, 3, 1);
		GridPane.setHalignment(answerFileCard, HPos.LEFT);
		grid.add(editButton, 1, 8);
		GridPane.setHalignment(editButton, HPos.CENTER);
		grid.add(cancelButton, 4, 8);
		GridPane.setHalignment(cancelButton, HPos.LEFT);
		
		//grid.setGridLinesVisible(true);
		//answerLabel.setVisible(false);
		//editButton.setVisible(false);
		//answerFileCard.setVisible(false);
		
		nextButton.setOnAction(e -> {
			 
		});
		
		editButton.setOnAction(e -> {
			 
		});
		
		cancelButton.setOnAction(e -> {
			quitLearning(testingStage);
		});
		
		testingStage.setOnCloseRequest(e -> {
			e.consume();
			quitLearning(testingStage);
		});
	
		Scene testingScene = new Scene(grid, 800, 400);
		testingScene.getStylesheets().addAll(this.getClass().getResource("Style.css").toExternalForm());
		
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		
		testingStage.setScene(testingScene);	
		testingStage.setX((primScreenBounds.getWidth() - testingStage.getWidth()) / 2);
		testingStage.setY((primScreenBounds.getHeight() - testingStage.getHeight()) / 2);
		
	}
	
	private void quitLearning(Stage stage)
	{
		boolean answer = ConfirmBox.display("Confirmation", "Are you sure you want to quit?");
		if(answer)
		{
			testingStage.close();
			new UserInterface(testingStage, ui.getName(), ui.getLoginScene());
		}
	}
	
}
