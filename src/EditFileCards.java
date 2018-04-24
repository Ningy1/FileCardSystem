import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditFileCards {
	
	private TableView table = new TableView();
	private Stage editStage = new Stage();
	private Label labelTitle = new Label();
	private VBox vbox = new VBox();
	static EditFileCards instanceEditFileCards = null;
	
	public EditFileCards()
	{
		
		editStage.setTitle("MyFileCards");

		
		table.setEditable(true);
		
		TableColumn id = new TableColumn("ID");
		TableColumn sideA = new TableColumn("SideA");
        TableColumn sideB = new TableColumn("SideB");
        TableColumn category = new TableColumn("Category");
        TableColumn subcategory = new TableColumn("Subcategory");
        
        table.getColumns().addAll(id, sideA, sideB, category, subcategory);
 
       
        GridPane root = new GridPane();
        root.getChildren().addAll(table);
       
        editStage.setWidth(450);
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
