import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.sound.midi.MidiDevice.Info;

import com.sun.javafx.image.impl.ByteIndexed.Getter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
	private ComboBox<String> filterCategory; // Filter Category tableview
	private ComboBox<String> filterSubCategory; // Filter SubCategory tableview

	
	
	// Result of a sql query
	private ResultSet rs = null;
		  
    
    // Lists for combobox to filter for Categories and Subcategories
    ObservableList<String> optionsCategory = 
    	    FXCollections.observableArrayList(
    	        "Translation",
    	        "Definition"
    	    ); 
    ObservableList<String> optionsSubCategoryDefiniton = 
    	    FXCollections.observableArrayList(
    	        "English",
    	        "German"
    	    ); 
    ObservableList<String> optionsSubCategoryTranslation = 
    	    FXCollections.observableArrayList(
    	        "English_Spanish",
    	        "English_German",
    	        "English_French"
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
            	
            	if(filterCategory.getValue().equals("Translation"))
            	{
                	String[] split = filterSubCategory.getValue().split("_");
                	String wordNew = edit.getNewValue();
                	
                	int wordID1; 
                	try {
	            		HSQLDB.getInstance().update("insert into Words (Word, Language, UserID)"
								+ "values('"+wordNew+"','"+split[0]+"', "+Login.userID+")");
						
							rs = HSQLDB.getInstance().query("SELECT wordID "
									+ "FROM words "
									+ "WHERE word = '"+wordNew
									+ "' AND Language = '"+split[0]
									+ "' AND UserID = "+Login.userID);
						
	                	rs.next();
	                	wordID1 = rs.getInt(1);
	                	
	                	HSQLDB.getInstance().update("UPDATE Translate SET WordID1 ="+wordID1+" "
	                			+ "WHERE WordID1 = "+edit.getRowValue().getIdSideA()+" "
	                					+ "AND WordID2 ="+edit.getRowValue().getIdSideB()+"");
	                	table.getSelectionModel().getSelectedItem().setSideA(edit.getNewValue());
	                	table.getSelectionModel().getSelectedItem().setIdSideA(wordID1);
                	} catch(java.sql.SQLIntegrityConstraintViolationException e) {
                		data.get(table.getSelectionModel().getSelectedIndex()).setSideA(edit.getOldValue());
                		edit.getTableView().getColumns().get(0).setVisible(false);
                		edit.getTableView().getColumns().get(0).setVisible(true);
                	} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					

            		

            	} else if(filterCategory.getValue().equals("Definition"))
            	{
            		String[] split = filterSubCategory.getValue().split("_");
                	String wordNew = edit.getNewValue();
                	
                	int wordID1; 
                	
                	try {
                		HSQLDB.getInstance().update("insert into Words (Word, Language, UserID)"
								+ "values('"+wordNew+"','"+split[0]+"', "+Login.userID+")");
						
							rs = HSQLDB.getInstance().query("SELECT wordID "
									+ "FROM words "
									+ "WHERE word = '"+wordNew
									+ "' AND Language = '"+split[0]
									+ "' AND UserID = "+Login.userID);
						
	                	rs.next();
	                	wordID1 = rs.getInt(1);
	                	
	                	HSQLDB.getInstance().update("UPDATE Definition SET WordID ="+wordID1+" "
	                			+ "WHERE DefinitionID = "+edit.getRowValue().getIdSideB()+" "
	                					+ "AND WordID ="+edit.getRowValue().getIdSideA()+"");
	                	table.getSelectionModel().getSelectedItem().setSideA(edit.getNewValue());
	                	table.getSelectionModel().getSelectedItem().setIdSideA(wordID1);
	            	} catch(java.sql.SQLIntegrityConstraintViolationException e) {
                		data.get(table.getSelectionModel().getSelectedIndex()).setSideA(edit.getOldValue());
                		edit.getTableView().getColumns().get(0).setVisible(false);
                		edit.getTableView().getColumns().get(0).setVisible(true);
                	} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	
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
            	// We have to delete the old entries and create new entries
            	// Get the IDs of the current entries
            	if(filterCategory.getValue().equals("Translation"))
            	{
                	String[] split = filterSubCategory.getValue().split("_");
                	String wordNew = edit.getNewValue();
                	
                	int wordID2; 
                	try {
	            		HSQLDB.getInstance().update("insert into Words (Word, Language, UserID)"
								+ "values('"+wordNew+"','"+split[1]+"', "+Login.userID+")");
						
							rs = HSQLDB.getInstance().query("SELECT wordID "
									+ "FROM words "
									+ "WHERE word = '"+wordNew
									+ "' AND Language = '"+split[1]
									+ "' AND UserID = "+Login.userID);
						
	                	rs.next();
	                	wordID2 = rs.getInt(1);
	                	
	                	HSQLDB.getInstance().update("UPDATE Translate SET WordID2 ="+wordID2+" "
	                			+ "WHERE WordID1 = "+edit.getRowValue().getIdSideA()+" "
	                					+ "AND WordID2 ="+edit.getRowValue().getIdSideB()+"");
	                	table.getSelectionModel().getSelectedItem().setSideB(edit.getNewValue());
	                	table.getSelectionModel().getSelectedItem().setIdSideB(wordID2);;
                	} catch(java.sql.SQLIntegrityConstraintViolationException e) {
                		data.get(table.getSelectionModel().getSelectedIndex()).setSideB(edit.getOldValue());
                		edit.getTableView().getColumns().get(0).setVisible(false);
                		edit.getTableView().getColumns().get(0).setVisible(true);
                	} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	} else if(filterCategory.getValue().equals("Definition"))
            	{
                	
                	try {
	            		HSQLDB.getInstance().update("UPDATE Definition SET Definition = '"+edit.getNewValue()+"' "
	            				+ "WHERE Definition = '"+edit.getOldValue()+" ' "
	            						+ "AND WordID = "+edit.getRowValue().getIdSideA()+"");
	                	table.getSelectionModel().getSelectedItem().setSideB(edit.getNewValue());
                	} catch(java.sql.SQLIntegrityConstraintViolationException e) {
                		data.get(table.getSelectionModel().getSelectedIndex()).setSideB(edit.getOldValue());
                		edit.getTableView().getColumns().get(0).setVisible(false);
                		edit.getTableView().getColumns().get(0).setVisible(true);
                	} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            	
            }
        });
        
        
        // Set the tableView editable
        table.setEditable(true);
        

        // Add columns to the tableView
        table.getColumns().addAll(sideA, sideB);
        
        // Read entries from database for Start
		try {
			rs = HSQLDB.getInstance().query("SELECT w1.wordID, w2.wordID, w1.word, w2.word  "
					+ "FROM (Words w1 join Words w2 on w1.UserID = w2.UserID) " + 
					"join Translate t on t.wordID1=w1.wordID and t.wordID2=w2.wordID " + 
					"where w1.userid = -1");
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
        
        // Fill Comboboxes with options
        filterCategory = new ComboBox<String>(optionsCategory);
        filterSubCategory = new ComboBox<String>();
        // Set default text of Comboboxes
        filterCategory.setPromptText("Please Choose");
        filterSubCategory.setPromptText("Please Choose");
        filterSubCategory.setDisable(true);
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
        addButton.setDisable(true);
        deleteButton.setMaxWidth(sideB.getMaxWidth());
        deleteButton.setMinWidth(sideB.getMinWidth());
        deleteButton.setPrefWidth(sideB.getPrefWidth());
        deleteButton.setDisable(true);
        filterSubCategory.setMaxWidth(sideB.getMaxWidth());
        filterSubCategory.setMinWidth(sideB.getMinWidth());
        filterSubCategory.setPrefWidth(sideB.getPrefWidth());
        filterCategory.setMaxWidth(sideA.getMaxWidth());
        filterCategory.setMinWidth(sideA.getMinWidth());
        filterCategory.setPrefWidth(sideA.getPrefWidth());
        // Let elements grow equally in horizontal direction
        HBox.setHgrow(addButton, Priority.ALWAYS);
        HBox.setHgrow(deleteButton, Priority.ALWAYS);
        HBox.setHgrow(addSideA, Priority.ALWAYS);
        HBox.setHgrow(addSideB, Priority.ALWAYS);
        HBox.setHgrow(filterSubCategory, Priority.ALWAYS);
        HBox.setHgrow(filterCategory, Priority.ALWAYS);
        
        // ActionListener for key pressed (Enter) when adding entry and focusing on textfieldSideA again
        addSideB.setOnKeyReleased(e -> {
        	if(!(addSideA.getText().isEmpty()) && e.getCode().equals(KeyCode.ENTER))
        	{
	        	addButton.fire();
	        	addSideA.requestFocus();
        	}
        });
        // ActionListener for key pressed (delete) when focusing on a row in the tableview
        
        table.setOnKeyReleased(e -> {
        	if(e.getCode().equals(KeyCode.DELETE) || e.getCode().equals(KeyCode.BACK_SPACE))
        	{
        		deleteCurrentEntry();
        	}
        });
        
        // Actionlistener for removing an entry
        deleteButton.setOnAction(edit -> {
        	
        	deleteCurrentEntry();
        });
        
        // Actionlistener for Button to add new entries
        addButton.setOnAction(e ->{
        		// Insert new entry in database
        		Integer wordID1;
        		Integer wordID2 = 0;

                try {
                	if((filterCategory.getValue() != null)  && (filterSubCategory.getValue() != null))
                	{
	                	String[] split = filterSubCategory.getValue().split("_");
	                	
						HSQLDB.getInstance().update("insert into Words (Word, Language, UserID)"
								+ "values('"+addSideA.getText()+"','"+split[0]+"', "+Login.userID+")");
						rs = HSQLDB.getInstance().query("SELECT wordID "
								+ "FROM words "
								+ "WHERE word = '"+addSideA.getText()
								+ "' AND Language = '"+split[0]
								+ "' AND UserID = "+Login.userID);
	                	rs.next();
	                	wordID1 = rs.getInt(1);
	                	
	                	if(filterCategory.getValue().equals("Translation"))
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
	                	}  else if(filterCategory.getValue().equals("Definition"))
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
        
        // Add ChangeListener to the Combobox Category
        filterCategory.valueProperty().addListener((obs, oldValue, newValue) -> {
        		if(filterCategory.getValue().equals("Definition"))
        		{
        			filterSubCategory.setItems(optionsSubCategoryDefiniton);
        			filterSubCategory.setDisable(false);
        			addButton.setDisable(true);
        			deleteButton.setDisable(true);
        		} else if(filterCategory.getValue().equals("Translation")) {
        			filterSubCategory.setItems(optionsSubCategoryTranslation);
        			filterSubCategory.setDisable(false);
        			addButton.setDisable(true);
        			deleteButton.setDisable(true);
        		}
        });
     // Add ChangeListener to the Combobox SubCategory
        filterSubCategory.valueProperty().addListener((obs, oldValue, newValue) -> {
        	try {
        		if(filterCategory.getValue().equals("Translation") && (filterSubCategory.getValue() != null))
        		{
            		String[] split = filterSubCategory.getValue().split("_");
        			rs = HSQLDB.getInstance().query("SELECT w1.wordID, w2.wordID, w1.word, w2.word  "
        					+ "FROM (Words w1 join Words w2 on w1.UserID = w2.UserID) "  
        					+ "join Translate t on t.wordID1=w1.wordID and t.wordID2=w2.wordID "  
        					+ "where w1.userid = "+Login.userID
        					+ "AND w1.Language = '"+split[0]+ "' "
        					+ "AND w2.Language = '"+split[1]+"' "); 
        			sideA.setText(split[0]);
        			sideB.setText(split[1]);
        			addButton.setDisable(false);
        			deleteButton.setDisable(false);
        		} else if(filterCategory.getValue().equals("Definition") && (filterSubCategory.getValue() != null))
        		{
            		String[] split = filterSubCategory.getValue().split("_");
        			rs = HSQLDB.getInstance().query("SELECT w.wordID, d.definitionID, w.word, d.Definition  "
        					+ "FROM Words w NATURAL JOIN Definition d "
        					+ "WHERE w.UserID = "+ Login.userID
        					+ "AND w.Language = '"+split[0]+"' "); 
        			sideA.setText(split[0]);
        			sideB.setText(filterCategory.getValue());
        			addButton.setDisable(false);
        			deleteButton.setDisable(false);
        		}
    			data.clear();        		
    			while(rs.next()) {
    				data.add(new FileCardsDB(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4)));
    			}
    		} catch (Exception e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
        });
        // Arrange different elements in boxes and bind them together in grid  
        hboxTextFields.getChildren().addAll(addSideA, addSideB);
        hboxButtons.getChildren().addAll(addButton, deleteButton);
        hboxTitle.getChildren().addAll(filterCategory,filterSubCategory );
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
	public void deleteCurrentEntry()
    {
		
    	
    	if(table.getSelectionModel().getSelectedItem() != null && table.getSelectionModel().getSelectedItem() != null) 
    	{
    		int wordID1 = table.getSelectionModel().getSelectedItem().getIdSideA();
        	int wordID2 = table.getSelectionModel().getSelectedItem().getIdSideB();
	    	// Remove the current entry (with the old values) from the observable list
	    	data.remove(table.getSelectionModel().getSelectedItem());
	    	// Remove the current entry (with the old values) from the database
	    	String[] split;
	    	split = filterSubCategory.getValue().split("_");
	    	try {
	    	if(filterCategory.getValue().equals("Translation"))
	    	{
	    		HSQLDB.getInstance().update("DELETE FROM Translate where WordID1 = "+wordID1+" "
	    				+ " AND WordID2 = "+wordID2+"");
	    	} else if(filterCategory.getValue().equals("Definition"))
	    	{
	    		HSQLDB.getInstance().update("DELETE FROM Definition where DefinitionID = "+wordID2+"");
	    	}
	    	        
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }

	
}
