import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
	
	public static void display(String title, String message)
	{
		Stage alertboxWindow = new Stage();
		alertboxWindow.setTitle(title);
		alertboxWindow.setMinWidth(250);
		alertboxWindow.initModality(Modality.APPLICATION_MODAL);
		
		Label alertLabel = new Label(message);
		
		Button confirmButton = new Button("OK");
		confirmButton.setOnAction(e -> alertboxWindow.close());
		
		VBox alertboxLayout = new VBox(10);
		alertboxLayout.getChildren().addAll(alertLabel, confirmButton);
		alertboxLayout.setAlignment(Pos.CENTER);
		
		Scene alertboxScene = new Scene(alertboxLayout);
		
		alertboxWindow.setScene(alertboxScene);
		alertboxWindow.showAndWait();
		
	}
	
}
