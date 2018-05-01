import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditFileCards {
	
	// Tableview
	private TableView<FileCardsDB> table = new TableView<FileCardsDB>();
	// Observable List to feed tableview with data with the object type FileCardsDB
	private final ObservableList<FileCardsDB> data = FXCollections.observableArrayList();
	// Layout stage
	private Stage editStage = new Stage();
	// Arangement of Objects
	private GridPane root = new GridPane();
	private HBox hboxTextFields = new HBox();
	private HBox hboxButtons = new HBox();
	private HBox hboxTitle = new HBox();
	
	// Layout
	private Label labelCategory = new Label("Category:"); // Display current category
	private TextField addSideA = new TextField(); // col1
	private TextField addSideB = new TextField(); // col2
	private Button addButton = new Button("Add");
	private Button deleteButton = new Button("Delete");
	private ComboBox<String> filterCategory; // Filter tableview
    
	
	
	// Result of a sql query
	private ResultSet rs = null;
		  
    
    // Lists for combobox to filter for categories
    ObservableList<String> optionsCategory = 
    	    FXCollections.observableArrayList(
    	        "English_Spanish",
    	        "English_German",
    	        "English_French",
    	        "English_Definition",
    	        "German_Definition"
    	    ); 
    

	
	
	
	public EditFileCards()
	{
		// Name the new stage (window)
		editStage.setTitle("MyFileCards");

			
		// Set up the columns        
        TableColumn<FileCardsDB, String> sideA = new TableColumn<FileCardsDB, String>("SideA");
        
		// Set the cells of the column to the propertytype string from the object fileCardsDB(attribute sideA)
		// By being a property, the table gets informed if a value changes and updates automatically
		// Defines how to display data on the cell
        sideA.setCellValueFactory(new PropertyValueFactory<FileCardsDB, String>("sideA"));
        // Set the cell to a textfield when editing
        sideA.setCellFactory(TextFieldTableCell.forTableColumn());
        
        // Tell what should happen when editing
        // Implement anonymous class
        sideA.setOnEditCommit(new EventHandler<CellEditEvent<FileCardsDB, String>>() {
        	// Add necessary mehtod
            @Override
            public void handle(CellEditEvent<FileCardsDB, String> edit) {
            	// Set the cell value to the new value
                (edit.getTableView().getItems().get(
                		edit.getTablePosition().getRow())
                    ).setSideA(edit.getNewValue());
                (edit.getTableView().getItems().get(
                		edit.getTablePosition().getRow())
                    ).setSideA(edit.getNewValue());
                // Update the entry in the database as well
                try {
					HSQLDB.getInstance().update("update filecards set sidea='"+edit.getNewValue()+"';");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        
        // Set up the columns   
        TableColumn<FileCardsDB, String> sideB = new TableColumn<FileCardsDB, String>("SideB");
        
        sideB.setCellValueFactory(
                new PropertyValueFactory<FileCardsDB, String>("sideB"));
        // Set the cell to a textfield when editing
        sideB.setCellFactory(TextFieldTableCell.forTableColumn());
        
        // Tell what should happen when editing
        // Implement anonymous class
        sideB.setOnEditCommit(new EventHandler<CellEditEvent<FileCardsDB, String>>() {
        	// Add necessary mehtod
            @Override
            public void handle(CellEditEvent<FileCardsDB, String> edit) {
            	// Set the cell value to the new value
                (edit.getTableView().getItems().get(
                		edit.getTablePosition().getRow())
                    ).setSideA(edit.getNewValue());
                // Update the entry in the database as well
                try {
					HSQLDB.getInstance().update("update filecards set sideb='"+edit.getNewValue()+"';");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        
        
        // Set the tableView editable
        table.setEditable(true);
        

        // Add columns to the tableView
        table.getColumns().addAll(sideA, sideB);
        
        // Read entries from database
		try {
			rs = HSQLDB.getInstance().query("SELECT w1.wordID, w2.wordID, w1.word, w2.word  "
					+ "FROM (Words w1 join Words w2 on w1.UserID = w2.UserID) " + 
					"join Translate t on t.wordID1=w1.wordID and t.wordID2=w2.wordID " + 
					"where w1.userid = 1");
			// Iterate through the ResultSet and add each row to the Observable List
			while(rs.next())
			{
				data.add(new FileCardsDB(rs.getInt(1), rs.getInt(2),rs.getString(3), rs.getString(4)));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		// Feed the tableview with the data of the observable list
        table.setItems(data);
        
        // Fill Combobox with options
        filterCategory = new ComboBox<String>(optionsCategory);
        // Set default text of Combobox
        filterCategory.setPromptText("Please Choose");
        // Set size of the columns
     	sideA.setPrefWidth(150);
     	sideA.setMaxWidth(700);
     	sideA.setMinWidth(50);
     	sideB.setPrefWidth(150);
        sideB.setMaxWidth(700);
        sideB.setMinWidth(50);
        // Set columns to grow equally in width
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Set size constraints of the textfields, buttons and label
    	addSideA.setMaxWidth(sideA.getMaxWidth());
    	addSideA.setPrefWidth(sideA.getPrefWidth());
    	addSideA.setMinWidth(sideA.getMinWidth());
    	addSideA.setPromptText("SideA");
        addSideB.setMaxWidth(sideB.getMaxWidth());
        addSideB.setPrefWidth(sideB.getPrefWidth());
        addSideB.setPromptText("SideB");
        addButton.setMaxWidth(sideA.getMaxWidth());
        addButton.setMinWidth(sideA.getMinWidth());
        addButton.setPrefWidth(sideA.getPrefWidth());
        deleteButton.setMaxWidth(sideB.getMaxWidth());
        deleteButton.setMinWidth(sideB.getMinWidth());
        deleteButton.setPrefWidth(sideB.getPrefWidth());
        filterCategory.setMaxWidth(sideB.getMaxWidth());
        filterCategory.setMinWidth(sideB.getMinWidth());
        filterCategory.setPrefWidth(sideB.getPrefWidth());
        labelCategory.setMaxWidth(sideA.getMaxWidth());
        labelCategory.setMinWidth(sideA.getMinWidth());
        labelCategory.setPrefWidth(sideA.getPrefWidth());
        // Let elements grow equally in horizontal direction
        HBox.setHgrow(addButton, Priority.ALWAYS);
        HBox.setHgrow(deleteButton, Priority.ALWAYS);
        HBox.setHgrow(addSideA, Priority.ALWAYS);
        HBox.setHgrow(addSideB, Priority.ALWAYS);
        HBox.setHgrow(labelCategory, Priority.ALWAYS);
        HBox.setHgrow(filterCategory, Priority.ALWAYS);
        
        
        // Actionlistener for Button to add new entries
        addButton.setOnAction(e ->{
        		// Insert new entry in database
        		Integer wordID1;
        		Integer wordID2 = 0;

                try {
                	if(filterCategory.getValue() != null)
                	{
	                	String[] split = filterCategory.getValue().split("_");
	                	
						HSQLDB.getInstance().update("insert into Words (Word, Language, UserID)"
								+ "values('"+addSideA.getText()+"','"+split[0]+"', "+Login.userID+")");
						rs = HSQLDB.getInstance().query("SELECT wordID "
								+ "FROM words "
								+ "WHERE word = '"+addSideA.getText()
								+ "' AND Language = '"+split[0]
								+ "' AND UserID = "+Login.userID);
	                	rs.next();
	                	wordID1 = rs.getInt(1);
	                	
	                	if(!(split[1].equals("Definition")))
	                	{
	                		HSQLDB.getInstance().update("insert into Words (Word, Language, UserID)"
	    							+ "values('"+addSideB.getText()+"','"+split[1]+"', "+Login.userID+")");
	    					rs = HSQLDB.getInstance().query("SELECT wordID "
	    							+ "FROM words "
	    							+ "WHERE word = '"+addSideB.getText()
	    							+ "' AND Language = '"+split[1]
	    							+ "' AND UserID = "+Login.userID);
	                    	rs.next();
	                    	wordID2 = rs.getInt(1);
	                    	HSQLDB.getInstance().update("insert into Translate (WordID1, WordID2)"
	    							+ "values("+wordID1+",'"+wordID2+"')");
							data.add(new FileCardsDB(wordID1, wordID2, addSideA.getText(), addSideB.getText()));	
	                	}  else if(split[1].equals("Definition"))
	                	{
	                		HSQLDB.getInstance().update("insert into Definition (definition, WordID)"
	    							+ "values('"+addSideB.getText()+"','"+wordID1+"')");
	                		rs = HSQLDB.getInstance().query("SELECT DefinitionID "
	    							+ "FROM Definition "
	    							+ " WHERE wordID = "+ wordID1
	    							+ " AND Definition = '"+addSideB.getText()+"'");
	                    	rs.next();
	                    	wordID2 = rs.getInt(1);
	                    	// Add the new entry to the observable list for the tableview
							data.add(new FileCardsDB(wordID1, wordID2, addSideA.getText(), addSideB.getText()));
	    				}
                	}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                // Set textfields to placeholder
                addSideA.clear();
                addSideB.clear();            
        });
        
        // Filter Functionality for database
        // Add ChangeListener to the Combobox
        filterCategory.valueProperty().addListener((obs, oldValue, newValue) -> {
        	try {
        		String[] split = filterCategory.getValue().split("_");
        		if(!(split[1].equals("Definition")))
        		{
        			rs = HSQLDB.getInstance().query("SELECT w1.wordID, w2.wordID, w1.word, w2.word  "
        					+ "FROM (Words w1 join Words w2 on w1.UserID = w2.UserID) "  
        					+ "join Translate t on t.wordID1=w1.wordID and t.wordID2=w2.wordID "  
        					+ "where w1.userid = "+Login.userID
        					+ "AND w1.Language = '"+split[0]+ "' "
        					+ "AND w2.Language = '"+split[1]+"' "); 
        			sideA.setText(split[0]);
        			sideB.setText(split[1]);
        		} else if(split[1].equals("Definition"))
        		{
        			rs = HSQLDB.getInstance().query("SELECT w.wordID, d.definitionID, w.word, d.Definition  "
        					+ "FROM Words w NATURAL JOIN Definition d "
        					+ "WHERE w.UserID = "+ Login.userID
        					+ "AND w.Language = '"+split[0]+"' "); 
        			sideA.setText(split[0]);
        			sideB.setText(split[1]);
        		}
    			data.clear();        		
    			while(rs.next()) {
    				data.add(new FileCardsDB(rs.getInt(2), rs.getInt(2), rs.getString(3), rs.getString(4)));
    			}
    		} catch (Exception e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
        });
        
        // Arrange different elements in boxes and bind them together in grid  
        hboxTextFields.getChildren().addAll(addSideA, addSideB);
        hboxButtons.getChildren().addAll(addButton, deleteButton);
        hboxTitle.getChildren().addAll(labelCategory, filterCategory);
        root.getChildren().addAll(hboxTitle, table, hboxTextFields, hboxButtons);
        
        labelCategory.setAlignment(Pos.CENTER);
        // Set location of different boxes
        GridPane.setConstraints(hboxTitle, 0, 0);
        GridPane.setConstraints(table, 0, 1);
        GridPane.setConstraints(hboxTextFields, 0, 2);
        GridPane.setConstraints(hboxButtons, 0, 3);
        
        // Set size properties of the stage
        editStage.setWidth(400);
        editStage.setHeight(450);
        editStage.setMaxWidth(addSideA.getMaxWidth()+addSideB.getMaxWidth());
        
        // Bin the width of the table(and following also the other elements) to the width of the stage
        table.prefWidthProperty().bind(editStage.widthProperty());
        
        // The scene is characterized by the gridlayout
        Scene scene = new Scene(root);
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setPrefSize(editStage.getWidth(), editStage.getHeight());
        root.setMinSize(editStage.getWidth(), editStage.getHeight());
        editStage.setScene(scene);
        editStage.show();
        
	}

	
}
