import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Testing {

	private HSQLDB db;
	private Stage testingStage;
	private UserInterface ui;
	private String category;
	private String level;
	private	ArrayList<String> resultSets = new ArrayList<String>();
	private Iterator<String> iterator; 
	private String numOfCards;
	private String from;
	private Label categoryLabel;
	private Label cardLabel = new Label("Kartei: ");
	private Label outOfLabel = new Label("1/"+numOfCards);
	private Label levelLabel;
	private String language;
	private Label whatIsLabel = new Label("What is");
	private Label fileCard = new Label("blablabla");
	private Label inLanguageLabel;
	private TextField answerField = new TextField();
	private TextArea answerArea = new TextArea();
	private Label answerFileCard;
	private Label answerLabel;
	private Button editButton = new Button("Edit Filecard");
	private Button cancelButton = new Button("Quit learning");
	private Button nextButton = new Button("Next");
	
	public Testing(Stage testFileCardsStage, UserInterface ui, String levelChoice, String categoryChoice, String from, String to)
	{	
		try {
			db = HSQLDB.getInstance();
			dbQuery(levelChoice, categoryChoice, from, to);
		} catch (Exception e) {
			e.printStackTrace();
		}
		category = categoryChoice;
		level = levelChoice;
		this.from = from;
		language = to;
		testingStage = testFileCardsStage;
		this.ui = ui;
		createTestingWindowScene();
	}
	
	private void createTestingWindowScene()
	{
		levelLabel = new Label("Level "+level);
		numOfCards = new Integer(resultSets.size()).toString();
		outOfLabel = new Label("1/"+numOfCards);
		
		String tmp = iterator.next();
		String[] splittedTmp = tmp.split(" ");
		
		fileCard = new Label(splittedTmp[0]);
		answerFileCard = new Label(splittedTmp[1]);
		answerLabel = new Label("blablub");
		
		cardLabel.setId("headerTesting");
		outOfLabel.setId("headerTesting");
		levelLabel.setId("headerTesting");
		nextButton.setId("button");
		editButton.setId("button");
		cancelButton.setId("button");
		
		answerField.setPromptText("Enter your answer");
		fileCard.setUnderline(true);
		answerField.setPrefSize(fileCard.getWidth(), fileCard.getHeight());
		
		answerLabel.setVisible(false);
		editButton.setVisible(false);
		answerFileCard.setVisible(false);
		
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
		
		if(category.equals("Translation"))
		{
			inLanguageLabel = new Label("in "+language.toLowerCase()+"?");
			
			categoryLabel = new Label("Translate the word from "+from.toLowerCase()+" to "+language.toLowerCase()+"!");
			categoryLabel.setId("headerTesting");
			
			grid.add(cardLabel, 0, 0);
			GridPane.setHalignment(cardLabel, HPos.CENTER);
			grid.add(outOfLabel, 1, 0);
			GridPane.setHalignment(outOfLabel, HPos.LEFT);
			grid.add(categoryLabel, 0, 1, 6, 2);
			GridPane.setHalignment(categoryLabel, HPos.CENTER);
			grid.add(levelLabel, 5, 0);
			GridPane.setHalignment(levelLabel, HPos.CENTER);
			grid.add(pane, 0, 3);
			grid.add(whatIsLabel, 1, 4);
			GridPane.setHalignment(whatIsLabel, HPos.RIGHT);
			grid.add(fileCard, 2, 4);
			GridPane.setHalignment(fileCard, HPos.CENTER);
			grid.add(inLanguageLabel, 3, 4);
			GridPane.setHalignment(inLanguageLabel, HPos.LEFT);
			grid.add(answerField, 1, 5, 3, 1);
			GridPane.setHalignment(answerField, HPos.RIGHT);
			grid.add(nextButton, 4, 5);
			GridPane.setHalignment(nextButton, HPos.LEFT);
			grid.add(pane2, 0, 7);
			grid.add(answerLabel, 1, 7, 1, 2);
			GridPane.setHalignment(answerLabel, HPos.RIGHT);
			grid.add(answerFileCard, 2, 7, 2, 2);
			GridPane.setHalignment(answerFileCard, HPos.LEFT);
			grid.add(editButton, 1, 10);
			GridPane.setHalignment(editButton, HPos.CENTER);
			grid.add(cancelButton, 4, 10);
			GridPane.setHalignment(cancelButton, HPos.LEFT);
		}
		else
		{
			inLanguageLabel = new Label("?");
			whatIsLabel = new Label("What means");
			
			categoryLabel = new Label("Give a definition for the word in "+from.toLowerCase()+"!");
			categoryLabel.setId("headerTesting");
			
			grid.add(cardLabel, 0, 0);
			GridPane.setHalignment(cardLabel, HPos.CENTER);
			grid.add(outOfLabel, 1, 0);
			GridPane.setHalignment(outOfLabel, HPos.LEFT);
			grid.add(categoryLabel, 0, 1, 6, 2);
			GridPane.setHalignment(categoryLabel, HPos.CENTER);
			grid.add(levelLabel, 5, 0);
			GridPane.setHalignment(levelLabel, HPos.CENTER);
			grid.add(pane, 0, 3);
			grid.add(whatIsLabel, 1, 4);
			GridPane.setHalignment(whatIsLabel, HPos.RIGHT);
			grid.add(fileCard, 2, 4);
			GridPane.setHalignment(fileCard, HPos.CENTER);
			grid.add(inLanguageLabel, 3, 4);
			GridPane.setHalignment(inLanguageLabel, HPos.LEFT);
			grid.add(answerArea, 1, 5, 3, 2);
			GridPane.setHalignment(answerField, HPos.CENTER);
			grid.add(nextButton, 4, 5, 1, 2);
			GridPane.setHalignment(nextButton, HPos.LEFT);
			grid.add(pane2, 0, 7);
			grid.add(answerLabel, 1, 7, 1, 2);
			GridPane.setHalignment(answerLabel, HPos.RIGHT);
			grid.add(answerFileCard, 2, 7, 2, 2);
			GridPane.setHalignment(answerFileCard, HPos.LEFT);
			grid.add(editButton, 1, 10);
			GridPane.setHalignment(editButton, HPos.CENTER);
			grid.add(cancelButton, 4, 10);
			GridPane.setHalignment(cancelButton, HPos.LEFT);
		}
		
		//grid.setGridLinesVisible(true);
		
		answerField.setOnKeyPressed(new EventHandler<KeyEvent>()
	    {
	        @Override
	        public void handle(KeyEvent key)
	        {
	            if (key.getCode().equals(KeyCode.ENTER))
	            {
	            	answerField.setDisable(true);
	            	
	            	if(answerField.getText().equals(answerFileCard.getText()))
	            	{
	            		answerLabel.setText("Correct, well done!");
	            		answerLabel.setVisible(true);
		        		editButton.setVisible(true);
		        		
	            	}else{
	            	
	            		answerLabel.setText("Wrong, the anwer is: ");
	            		answerLabel.setVisible(true);
	            		answerFileCard.setVisible(true);
	            		editButton.setVisible(true);
	            	}
	            }
	        }
	    });
		
		nextButton.setOnAction(e -> {
			 
		});
		
		editButton.setOnAction(e -> {
			 
		});
		
		cancelButton.setOnAction(e -> {
			quitLearning();
		});
		
		testingStage.setOnCloseRequest(e -> {
			e.consume();
			quitLearning();
		});
	
		Scene testingScene = new Scene(grid, 800, 400);
		testingScene.getStylesheets().addAll(this.getClass().getResource("Style.css").toExternalForm());
		
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		
		testingStage.setScene(testingScene);	
		testingStage.setX((primScreenBounds.getWidth() - testingStage.getWidth()) / 2);
		testingStage.setY((primScreenBounds.getHeight() - testingStage.getHeight()) / 2);
		
	}
	
	private void quitLearning()
	{
		boolean answer = ConfirmBox.display("Confirmation", "Are you sure you want to quit?");
		if(answer)
		{
			testingStage.close();
			new UserInterface(testingStage, ui.getName(), ui.getLoginScene());
		}
	}
	
	private void dbQuery(String levelChoice, String categoryChoice, String from, String to)
	{
		ResultSet rs;
		
		if(categoryChoice.equals("Translation"))
		{
			categoryChoice = "Translate";
			
			try {
				rs = db.query("SELECT w1.Word, w2.Word"
						+ " FROM Words w1 NATURAL JOIN "+categoryChoice+" INNER JOIN Words w2 ON w2.WordID = Translate.WordID2"
						+ " WHERE UserID = "+Login.userID+" AND Level = "+levelChoice
						+ " AND w1.WordID = Translate.WordID1 AND w1.Language = '"+from+"' AND w2.Language = '"+to+"'");
				
				if(rs.isBeforeFirst())
				{
					while(rs.next())
					{
						resultSets.add(rs.getString(1) + " " + rs.getString(2));
					}
					iterator = resultSets.iterator();
				}
			} catch (SQLException e) {
				System.out.println("Could not get resultset for testing.");
				e.printStackTrace();
			}
		}
	}
}