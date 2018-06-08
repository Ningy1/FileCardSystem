import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
	private	ArrayList<FileCardsDB> resultSets = new ArrayList<FileCardsDB>();
	private ArrayList<FileCardsDB> duplicates = new ArrayList<FileCardsDB>();
	private Iterator<FileCardsDB> iterator;
	private Iterator<FileCardsDB> duplicateIterator;
	private String givenAnswers = "";
	private String possibleAnswers = "";
	private Integer numOfPossibleAnswers;
	private Integer duplicateCounter = 0;
	private Integer numOfDuplicates;
	private Integer numOfCards;
	private String from;
	private Label categoryLabel;
	private Label cardLabel = new Label("Kartei: ");
	private Integer counter = 1;
	private Label outOfLabel = new Label(counter+"/"+numOfCards);
	private Label levelLabel;
	private String language;
	private Label whatIsLabel = new Label("What is");
	private Label fileCard = new Label("blablabla");
	private Label inLanguageLabel;
	private TextField answerField = new TextField();
	private TextArea answerArea = new TextArea();
	private Label answerFileCard;
	private Label answerLabel;
	private Button checkButton = new Button("Check");
	private Button editButton = new Button("Edit Filecard");
	private Button cancelButton = new Button("Quit learning");
	private Button nextButton = new Button("Next");
	private Integer levelUp = 0;
	private FileCardsDB card;
	
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
		numOfDuplicates = checkDuplicatesForTotalNumOfCards();
		numOfCards = resultSets.size() - numOfDuplicates;
		outOfLabel = new Label("1/"+numOfCards);
		
		card = iterator.next();
		iterator.remove();
		hasDuplicates(card);
		
		fileCard = new Label(card.getSideA());
		answerFileCard = new Label(card.getSideB());
		answerLabel = new Label("blablub");
		
		fileCard.setMaxWidth(130);
		
		answerFileCard.setMinHeight(100);
		answerFileCard.setMinWidth(300);
		answerFileCard.setMaxHeight(100);
		answerFileCard.setMaxWidth(300);
		
		answerLabel.setMinWidth(230);
		answerLabel.setMaxWidth(230);
		
		cardLabel.setId("headerTesting");
		outOfLabel.setId("headerTesting");
		levelLabel.setId("headerTesting");
		nextButton.setId("button");
		editButton.setId("button");
		cancelButton.setId("button");
		checkButton.setId("button");
		
		answerField.setPromptText("Enter your answer");
		fileCard.setUnderline(true);
		answerField.setPrefSize(fileCard.getWidth(), fileCard.getHeight());
		answerArea.setPromptText("Enter your answer");
		answerArea.setWrapText(true);
		answerArea.setStyle("-fx-font-size: 16");
		answerField.setStyle("-fx-font-size: 16");
		
		answerArea.setMinHeight(100);
		answerArea.setMinWidth(350);
		answerArea.setMaxHeight(100);
		answerArea.setMaxWidth(350);
		
		answerField.setMinWidth(300);
		answerField.setMaxWidth(300);
		answerField.setMinHeight(40);
		answerField.setMaxHeight(40);
		
		answerField.textProperty().addListener(new ChangeListener<String>() {
			
			public void changed(ObservableValue<? extends String> observable,
		            String oldValue, String newValue) {
				if(newValue.length()>80) {
					answerField.setText(oldValue);
				}
		    }	
		});
		
		answerArea.textProperty().addListener(new ChangeListener<String>() {
			
			public void changed(ObservableValue<? extends String> observable,
		            String oldValue, String newValue) {
				if(newValue.length()>160) {
					answerArea.setText(oldValue);
				}
		    }	
		});
		
		answerLabel.setVisible(false);
		editButton.setVisible(false);
		answerFileCard.setVisible(false);
		answerFileCard.setWrapText(true);
		
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
			grid.add(checkButton, 4, 5);
			GridPane.setHalignment(checkButton, HPos.LEFT);
			grid.add(nextButton, 5, 5);
			GridPane.setHalignment(nextButton, HPos.LEFT);
			grid.add(pane2, 0, 7);
			grid.add(answerLabel, 1, 7, 1, 2);
			GridPane.setHalignment(answerLabel, HPos.RIGHT);
			grid.add(answerFileCard, 2, 7, 3, 2);
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
			grid.add(checkButton, 4, 5, 1, 2);
			GridPane.setHalignment(checkButton, HPos.LEFT);
			grid.add(nextButton, 5, 5, 1, 2);
			GridPane.setHalignment(nextButton, HPos.LEFT);
			grid.add(pane2, 0, 7);
			grid.add(answerLabel, 1, 8, 1, 2);
			GridPane.setHalignment(answerLabel, HPos.RIGHT);
			grid.add(answerFileCard, 2, 8, 3, 2);
			GridPane.setHalignment(answerFileCard, HPos.LEFT);
			grid.add(editButton, 1, 11);
			GridPane.setHalignment(editButton, HPos.CENTER);
			grid.add(cancelButton, 4, 11);
			GridPane.setHalignment(cancelButton, HPos.LEFT);
		}
		
		//grid.setGridLinesVisible(true);
		
		answerField.setOnKeyPressed(new EventHandler<KeyEvent>()
	    {
	        public void handle(KeyEvent key)
	        {
	            if (key.getCode().equals(KeyCode.ENTER))
	            { 	
	            	if(duplicateIterator != null && (answerField.getText().trim().length() != 0 && category.equals("Translation")) || 
	            			((answerArea.getText().trim().length() != 0 && category.equals("Definition"))))
	            	{
	            		nextButton.setDisable(true);
	            		FileCardsDB tmp = duplicateIterator.next();
	            		
	            		if(checkForPossibleAnswers(answerField.getText().toLowerCase()))
	            		{
	            			if(numOfDuplicates == duplicateCounter)
	                		{
	                			nextButton.setDisable(false);
	                			givenAnswers = givenAnswers + " " + answerField.getText();
	                			answerLabel.setText("Everything correct!");
	                			answerFileCard.setText("Entered: " + givenAnswers + ".");
	                			answerLabel.setVisible(true);
	                			answerFileCard.setVisible(true);
	                			answerField.setDisable(true);
	                			answerArea.setDisable(true);
	                			checkButton.setDisable(true);
	                			editButton.setVisible(true);
	                			answerField.clear();
	                			answerArea.clear();
	                		}
	            			else
	                		{
	            				numOfDuplicates--;
	            				duplicateCounter--;
	            				givenAnswers = givenAnswers + " " + answerField.getText();
		            			answerLabel.setText("Correct, what else?");
		            			answerFileCard.setText("Entered: " + givenAnswers + ". " + numOfDuplicates + " answer(s) remaining.");
		            			answerLabel.setVisible(true);
		            			answerFileCard.setVisible(true);
		            			answerField.clear();
		            			answerArea.clear();
		            			
		            			if(!(level.equals("4")))
			            		{
			            			levelUp = 1;
			            		}else
			            			levelUp = 0;
			            		
			            		//dbUpdateQuery(Integer.parseInt(level)+levelUp, tmp.getIdSideA().toString(), tmp.getIdSideB().toString(), category);
	                		}
	            		}
	            		else
	            		{
	            			answerField.setDisable(true);
		            		answerArea.setDisable(true);
	            			
	            			answerLabel.setText("Wrong, possible answers are: ");
	            			answerLabel.setVisible(true);
		            		answerFileCard.setVisible(true);
		            		answerFileCard.setText(possibleAnswers);
		            		editButton.setVisible(true);
		            		nextButton.setDisable(false);
		            		checkButton.setDisable(true);
		            		
		            		dbUpdateQuery(1, tmp.getIdSideA().toString(), tmp.getIdSideB().toString(), category);
	            		}
	            	}
	            	else
	            	{
	            		if(((answerField.getText().toLowerCase().equals(answerFileCard.getText().toLowerCase()) || answerField.getText().toLowerCase().equals(answerFileCard.getText().toLowerCase())) && category.equals("Translation")) ||
		            			(((answerArea.getText().toLowerCase().equals(answerFileCard.getText().toLowerCase()) || answerArea.getText().toLowerCase().equals(answerFileCard.getText().toLowerCase())) && category.equals("Definition"))))
		            	{
		            		answerField.setDisable(true);
		            		answerArea.setDisable(true);
		            		
		            		answerLabel.setText("Correct, well done!");
		            		answerLabel.setVisible(true);
		            		editButton.setVisible(true);
		            		
		            		
		            		if(!(level.equals("4")))
		            		{
		            			levelUp = 1;
		            		}else
		            			levelUp = 0;
		            		
		            		dbUpdateQuery(Integer.parseInt(level)+levelUp, card.getIdSideA().toString(), card.getIdSideB().toString(), category);
		            		
		            	}else if((answerField.getText().trim().length() == 0 && category.equals("Translation")) || 
		            			((answerArea.getText().trim().length() == 0 && category.equals("Definition"))))
		    			{
		    				answerLabel.setText("You didn't type anything!");
		    				answerLabel.setVisible(true);
		    				
		    			}else{
		            	
		            		answerField.setDisable(true);
		            		answerArea.setDisable(true);
		            		
		            		answerLabel.setText("Wrong, the answer is: ");
		            		answerLabel.setVisible(true);
		            		answerFileCard.setVisible(true);
		            		editButton.setVisible(true);
		            		
		            		dbUpdateQuery(1, card.getIdSideA().toString(), card.getIdSideB().toString(), category);
		            	}
	            	}
	            }
	        }
	    });
		
		checkButton.setOnAction(e -> {
			
			if(duplicateIterator != null && (answerField.getText().trim().length() != 0 && category.equals("Translation")) || 
        			((answerArea.getText().trim().length() != 0 && category.equals("Definition"))))
        	{
        		nextButton.setDisable(true);
        		FileCardsDB tmp = duplicateIterator.next();
        		
        		if(checkForPossibleAnswers(answerField.getText().toLowerCase()))
        		{
        			if(numOfDuplicates == duplicateCounter)
            		{
            			nextButton.setDisable(false);
            			givenAnswers = givenAnswers + " " + answerField.getText();
            			answerLabel.setText("Everything correct!");
            			answerFileCard.setText("Entered: " + givenAnswers + ".");
            			answerLabel.setVisible(true);
            			answerFileCard.setVisible(true);
            			answerField.setDisable(true);
            			answerArea.setDisable(true);
            			checkButton.setDisable(true);
            			editButton.setVisible(true);
            			answerField.clear();
            			answerArea.clear();
            		}
        			else
            		{
        				numOfDuplicates--;
        				duplicateCounter--;
        				givenAnswers = givenAnswers + " " + answerField.getText();
            			answerLabel.setText("Correct, what else?");
            			answerFileCard.setText("Entered: " + givenAnswers + ". " + numOfDuplicates + " answer(s) remaining.");
            			answerLabel.setVisible(true);
            			answerFileCard.setVisible(true);
            			answerField.clear();
            			answerArea.clear();
            			
            			if(!(level.equals("4")))
	            		{
	            			levelUp = 1;
	            		}else
	            			levelUp = 0;
	            		
	            		//dbUpdateQuery(Integer.parseInt(level)+levelUp, tmp.getIdSideA().toString(), tmp.getIdSideB().toString(), category);
            		}
        		}
        		else
        		{
        			answerField.setDisable(true);
            		answerArea.setDisable(true);
        			
        			answerLabel.setText("Wrong, possible answers are: ");
        			answerLabel.setVisible(true);
            		answerFileCard.setVisible(true);
            		answerFileCard.setText(possibleAnswers);
            		editButton.setVisible(true);
            		nextButton.setDisable(false);
            		checkButton.setDisable(true);
            		
            		dbUpdateQuery(1, tmp.getIdSideA().toString(), tmp.getIdSideB().toString(), category);
        		}
        	}
        	else
        	{
        		if(((answerField.getText().toLowerCase().equals(answerFileCard.getText().toLowerCase()) || answerField.getText().toLowerCase().equals(answerFileCard.getText().toLowerCase())) && category.equals("Translation")) ||
            			(((answerArea.getText().toLowerCase().equals(answerFileCard.getText().toLowerCase()) || answerArea.getText().toLowerCase().equals(answerFileCard.getText().toLowerCase())) && category.equals("Definition"))))
            	{
            		answerField.setDisable(true);
            		answerArea.setDisable(true);
            		
            		answerLabel.setText("Correct, well done!");
            		answerLabel.setVisible(true);
            		editButton.setVisible(true);
            		
            		
            		if(!(level.equals("4")))
            		{
            			levelUp = 1;
            		}else
            			levelUp = 0;
            		
            		dbUpdateQuery(Integer.parseInt(level)+levelUp, card.getIdSideA().toString(), card.getIdSideB().toString(), category);
            		
            	}else if((answerField.getText().trim().length() == 0 && category.equals("Translation")) || 
            			((answerArea.getText().trim().length() == 0 && category.equals("Definition"))))
    			{
    				answerLabel.setText("You didn't type anything!");
    				answerLabel.setVisible(true);
    				
    			}else{
            	
            		answerField.setDisable(true);
            		answerArea.setDisable(true);
            		
            		answerLabel.setText("Wrong, the answer is: ");
            		answerLabel.setVisible(true);
            		answerFileCard.setVisible(true);
            		editButton.setVisible(true);
            		
            		dbUpdateQuery(1, card.getIdSideA().toString(), card.getIdSideB().toString(), category);
            	}
        	}
		});
		
		nextButton.setOnAction(e -> {
			
			if(counter<numOfCards && (answerField.isDisabled() || answerArea.isDisabled()))
			{
				counter++;
				
				outOfLabel.setText(counter+"/"+numOfCards);
				
				answerLabel.setVisible(false);
				editButton.setVisible(false);
				answerFileCard.setVisible(false);
				
				answerField.clear();
				answerArea.clear();
				answerField.setDisable(false);
        		answerArea.setDisable(false);
        		checkButton.setDisable(false);
				
        		iterator = resultSets.iterator();
        		
        		possibleAnswers = "";
        		duplicates.clear();
        		
				card = iterator.next();
				iterator.remove();
				hasDuplicates(card);
				
				duplicateCounter = 0;
				givenAnswers = "";
				
				
				fileCard.setText(card.getSideA());
				answerFileCard.setText(card.getSideB());
			}
			else if(counter==numOfCards && (answerField.isDisabled() || answerArea.isDisabled()))
			{
				AlertBox.display("Congratulations", "Congratulations, you made it through the "+level+". level!\nYou can quit now.");
				answerField.setDisable(true);
				answerArea.setDisable(true);
				
				answerLabel.setVisible(true);
        		editButton.setVisible(true);
        	}
			else
			{
				answerLabel.setText("You can't skip just like that!");
				answerLabel.setVisible(true);
			}
			
		});
		
		editButton.setOnAction(e -> {
			if(category.equalsIgnoreCase("Translation")) {
				new EditFileCards(testingStage, this, new FileCardsDB(card.getIdSideA(), card.getIdSideB(), card.getSideA(), card.getSideB(), category, from, language));
			} else if (category.equalsIgnoreCase("Definition")) {
				new EditFileCards(testingStage, this, new FileCardsDB(card.getIdSideA(), card.getIdSideB(), card.getSideA(), card.getSideB(), category, from, "Definition"));
			}
		});
		
		cancelButton.setOnAction(e -> {
			
			boolean answer = ConfirmBox.display("Confirmation", "Do you want to stop learning?");
			if(answer)
			{
				new UserInterface(ui.getLoginStage(), ui.getName(), ui.getLoginScene());
				testingStage.close();
			}
		});
		
		testingStage.setOnCloseRequest(e -> {
			e.consume();
			
			boolean answer = ConfirmBox.display("Confirmation", "Are you sure you want to quit?");
			if(answer)
			{
				testingStage.close();
				
			}
		});
	
		Scene testingScene;
		
		if(category.equals("Translation"))
		{
			testingScene = new Scene(grid, 800, 450);
		}
		else
		{
			testingScene = new Scene(grid, 800, 500);
		}
	
		testingScene.getStylesheets().addAll(this.getClass().getResource("Style.css").toExternalForm());
		
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		
		testingStage.setScene(testingScene);	
		testingStage.setX((primScreenBounds.getWidth() - testingStage.getWidth()) / 2);
		testingStage.setY((primScreenBounds.getHeight() - testingStage.getHeight()) / 2);
		
	}
	
	private void dbQuery(String levelChoice, String categoryChoice, String from, String to)
	{
		ResultSet rs;
		
		if(categoryChoice.equals("Translation"))
		{	
			try {
			
				rs = db.query("select w1.wordid, w2.wordid, w1.word, w2.word "
						+ "from words w1 join words w2 on w1.userid= w1.userid "
						+ "where  ((w1.wordid, w2.wordid) in (select wordid1, wordid2 from translate where Level = "+levelChoice+") "
						+ "or (w1.wordid, w2.wordid) in (select wordid2, wordid1 from translate where Level = "+levelChoice+")) "
						+ "and (w1.language = '" + from + "'  and w2.language='"
						+ to + "') " + "and w1.userid = " + Login.userID + " ");
				
				if(rs.isBeforeFirst())
				{
					while(rs.next())
					{
						FileCardsDB card = new FileCardsDB(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), categoryChoice, from, to);
						resultSets.add(card);
					}
					iterator = resultSets.iterator();
				}
			} catch (SQLException e) {
				System.out.println("Could not get resultset for testing.");
				e.printStackTrace();
			}
		}else{
			
			try {
				rs = db.query("SELECT w.wordID, d.definitionID, w.word, d.Definition  "
						+ "FROM Words w NATURAL JOIN Definition d " + "WHERE w.UserID = " + Login.userID
						+ "AND w.Language = '" + from + "' AND d.Level = "+levelChoice+"");
				
				if(rs.isBeforeFirst())
				{
					while(rs.next())
					{
						FileCardsDB card = new FileCardsDB(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), categoryChoice, from, "Definition");
						resultSets.add(card);
					}
					iterator = resultSets.iterator();
				}
			} catch (SQLException e) {
				System.out.println("Could not get resultset for testing.");
				e.printStackTrace();
			}
		}
	}
	
	private void dbUpdateQuery(Integer levelUp, String wordID1, String wordID2, String categoryChoice) 
	{	
		if(categoryChoice.equals("Translation"))
		{
			try {
				HSQLDB.getInstance().update("UPDATE Translate " + "SET Level =" + levelUp + " " + " WHERE (WordID1 = " + wordID1 + " AND WordID2 = " + wordID2 + ")");
			} catch (SQLException e) {
				System.out.println("Could not update level of Translate table.");
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("Could not update level of Translate table.");
				e.printStackTrace();
			}
		}else {
			
			try {
				HSQLDB.getInstance().update("UPDATE Definition " + "SET Level =" + levelUp + " " + " WHERE (WordID = " + wordID1 + "" + " AND DefinitionID =" + wordID2 + ")");
			} catch (SQLException e) {
				System.out.println("Could not update level of Definition table.");
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("Could not update level of Definition table.");
				e.printStackTrace();
			}
		}
	}
	
	private Integer checkDuplicatesForTotalNumOfCards()
	{
		FileCardsDB tmp1 = new FileCardsDB();
		FileCardsDB tmp2 = new FileCardsDB();
		Integer counter = 0;
		ArrayList<FileCardsDB> duplicateSets = new ArrayList<FileCardsDB>(resultSets);
		
    	for(Iterator<FileCardsDB> iter1 = duplicateSets.iterator(); iter1.hasNext();) {
    		
    		tmp1 = iter1.next();
    		iter1.remove();
    		
    		for(Iterator<FileCardsDB> iter2 = duplicateSets.iterator(); iter2.hasNext();) {
    			
    			tmp2 = iter2.next();
    			
    			if(tmp1.getSideA().toLowerCase().equals(tmp2.getSideA().toLowerCase()) && !(tmp1.getSideB().toLowerCase().equals(tmp2.getSideB().toLowerCase())))
    			{
    				counter++;
    			}
    		}
    	}

    	return counter;
	}
	
	private void hasDuplicates(FileCardsDB currentCard)
	{
		FileCardsDB tmp = new FileCardsDB();
		duplicates.add(currentCard);
		System.out.println(currentCard.getSideA() + "   " + currentCard.getSideB());
		possibleAnswers = possibleAnswers + currentCard.getSideB() + " ";
		
		for(Iterator<FileCardsDB> iter1 = resultSets.iterator(); iter1.hasNext();) {
			
			tmp = iter1.next();
			
    		if(currentCard.getSideA().toLowerCase().equals(tmp.getSideA().toLowerCase()))
    		{
    			possibleAnswers = possibleAnswers + tmp.getSideB() + " ";
    			iter1.remove();
    			duplicates.add(tmp);
    			System.out.println(tmp.getSideA() + "  " + tmp.getSideB());
    		}
		}
		
		if(duplicates.size() == 1)			// only current card
		{
			duplicates.clear();
			duplicateIterator = null;
			numOfDuplicates = 0;
		}
		else
		{
			duplicateIterator = duplicates.iterator();
			numOfDuplicates = duplicates.size();
		}
		
	}
	
	private Boolean checkForPossibleAnswers(String answer)
	{
		FileCardsDB tmp = new FileCardsDB();
		
		for(Iterator<FileCardsDB> iter1 = duplicates.iterator(); iter1.hasNext();) {
			
			tmp = iter1.next();
			
    		if(tmp.getSideB().toLowerCase().equals(answer))
    		{
    			iter1.remove();
    			duplicateCounter++;
    			duplicateIterator = duplicates.iterator();
    			return true;
    		}
		}
		
		return false;
	}
}