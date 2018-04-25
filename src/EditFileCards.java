import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditFileCards {
	
	private TableView<FileCardsDB> table = new TableView<FileCardsDB>();
	private Stage editStage = new Stage();
	private Label labelTitle = new Label();
	private HBox hbox = new HBox();
	static EditFileCards instanceEditFileCards = null;
	
	TextField addSideA = new TextField();
    TextField addSideB = new TextField();
    ComboBox addCategory;
    ComboBox addSubcategory;
  //  TextField addSubcategory = new TextField();
    
    Button addButton = new Button("Add");
    // Lists for comboboxes
    ObservableList<String> optionsCategory = 
    	    FXCollections.observableArrayList(
    	        "Vocabel",
    	        "Definition"
    	    );
    ObservableList<String> optionsSubcategoryVocabels = 
    	    FXCollections.observableArrayList(
    	        "DeutschEnglisch",
    	        "EnglischDeutsch"
    	    ); 
    ObservableList<String> optionsSubcategoryDefinitions = 
    	    FXCollections.observableArrayList(
    	        "Deutsch",
    	        "Englisch"
    	    ); 
    
	
    
    
    
	
	
	private final ObservableList<FileCardsDB> data =
	        FXCollections.observableArrayList();
	
	public EditFileCards()
	{
		
		editStage.setTitle("MyFileCards");

		
		table.setEditable(true);
		
		TableColumn id = new TableColumn("ID");
		id.setPrefWidth(50);
        id.setCellValueFactory(
                new PropertyValueFactory<FileCardsDB, Integer>("id"));
        id.setMaxWidth(400);
		TableColumn sideA = new TableColumn("SideA");
		sideA.setPrefWidth(100);
        sideA.setCellValueFactory(
                new PropertyValueFactory<FileCardsDB, String>("sideA"));
        sideA.setMaxWidth(400);
        TableColumn sideB = new TableColumn("SideB");
        sideB.setPrefWidth(100);
        sideB.setCellValueFactory(
                new PropertyValueFactory<FileCardsDB, String>("sideB"));
        sideB.setMaxWidth(400);
        TableColumn category = new TableColumn("Category");
        category.setPrefWidth(100);
        category.setCellValueFactory(
                new PropertyValueFactory<FileCardsDB, String>("category"));
        category.setMaxWidth(400);
        TableColumn subcategory = new TableColumn("Subcategory");
        subcategory.setPrefWidth(100);
        subcategory.setCellValueFactory(
                new PropertyValueFactory<FileCardsDB, String>("subcategory"));
        subcategory.setMaxWidth(400);
        
        

        table.getColumns().addAll(id, sideA, sideB, category, subcategory);
        
        // Read entries from database
        String SQL = "Select * from filecards";
		ResultSet rs = null;
		try {
			rs = HSQLDB.getInstance().query(SQL);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			while(rs.next())
			{
				data.add(new FileCardsDB(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        table.setItems(data);
        // Texfields to enter new filecards
        addCategory = new ComboBox(optionsCategory);
        addSubcategory = new ComboBox();
        addSubcategory.setDisable(true);
    	addSideA.setMaxWidth(sideA.getMaxWidth());
    	addSideA.setPrefWidth(sideA.getPrefWidth());
    	addSideA.setPromptText("SideA");
        addSideB.setMaxWidth(sideB.getMaxWidth());
        addSideB.setPrefWidth(sideB.getPrefWidth());
        addSideB.setPromptText("SideB");
        addCategory.setMaxWidth(category.getMaxWidth());
        addCategory.setPrefWidth(category.getPrefWidth());
        addCategory.setPromptText("Category");
        addSubcategory.setMaxWidth(subcategory.getMaxWidth());
        addSubcategory.setPrefWidth(subcategory.getPrefWidth());
        addSubcategory.setPromptText("Subcategory");
        addButton.setMinWidth(id.getMinWidth());
        addButton.setPrefWidth(id.getPrefWidth());
        
        
        // Adjust Combobox to show only subcateogries for specific category
        addCategory.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
            	addSubcategory.getItems().clear();
            	addSubcategory.setDisable(true);
            } else if (newValue.equals("Vocabel")){
                addSubcategory.getItems().setAll(optionsSubcategoryVocabels);
                addSubcategory.setDisable(false);
            } else if (newValue.equals("Definition")){
                addSubcategory.getItems().setAll(optionsSubcategoryDefinitions);
                addSubcategory.setDisable(false);
            }
        });
        
        // Eventhandler Button add new entries
        addButton.setOnAction(e ->{
                try {
					HSQLDB.getInstance().query("insert into filecards (sidea, sideb, category, subcategory)"
							+ "values('"+addSideA.getText()+"','"+addSideB.getText()+"','"+ addCategory.getValue()+"','"+addSubcategory.getValue()+"')");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                     
                addSideA.clear();
                addSideB.clear();
                addCategory.setValue(null);
                addSubcategory.setValue(null);
            
        });
        
        hbox.getChildren().addAll(addButton, addSideA, addSideB, addCategory, addSubcategory);
        GridPane root = new GridPane();
        root.getChildren().addAll(table, hbox);

        GridPane.setConstraints(table, 0, 0);
        GridPane.setConstraints(hbox, 0, 1);
       
        editStage.setWidth(500);
        editStage.setHeight(300);
        
        Scene scene = new Scene(root);
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setPrefSize(editStage.getWidth(), editStage.getHeight());
        root.setMinSize(editStage.getWidth(), editStage.getHeight());
        root.setGridLinesVisible(true);

        editStage.setScene(scene);
        editStage.show();
        
	}
	public static EditFileCards getInstanceEditFileCards()
	{
		if (instanceEditFileCards == null) {
			instanceEditFileCards = new EditFileCards();
		}
		return instanceEditFileCards;
	}
	
}
