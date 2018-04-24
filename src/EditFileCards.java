import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditFileCards {
	
	private TableView<FileCardsDB> table = new TableView<FileCardsDB>();
	private Stage editStage = new Stage();
	private Label labelTitle = new Label();
	private VBox vbox = new VBox();
	static EditFileCards instanceEditFileCards = null;
	
	private final ObservableList<FileCardsDB> data =
	        FXCollections.observableArrayList();
	
	public EditFileCards()
	{
		
		editStage.setTitle("MyFileCards");

		
		table.setEditable(true);
		
		TableColumn id = new TableColumn("ID");
		id.setMinWidth(50);
        id.setCellValueFactory(
                new PropertyValueFactory<FileCardsDB, Integer>("id"));
 
		TableColumn sideA = new TableColumn("SideA");
		sideA.setMinWidth(100);
        sideA.setCellValueFactory(
                new PropertyValueFactory<FileCardsDB, String>("sideA"));
        TableColumn sideB = new TableColumn("SideB");
        sideB.setMinWidth(100);
        sideB.setCellValueFactory(
                new PropertyValueFactory<FileCardsDB, String>("sideB"));
        TableColumn category = new TableColumn("Category");
        category.setMinWidth(100);
        category.setCellValueFactory(
                new PropertyValueFactory<FileCardsDB, String>("category"));
        TableColumn subcategory = new TableColumn("Subcategory");
        subcategory.setMinWidth(100);
        subcategory.setCellValueFactory(
                new PropertyValueFactory<FileCardsDB, String>("subcategory"));
        
        table.setItems(data);
        
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
        
        table.getColumns().addAll(id, sideA, sideB, category, subcategory);
 
       
        GridPane root = new GridPane();
        root.getChildren().addAll(table);
       
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
