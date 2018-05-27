import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
	
	public static void display(String title, String message)
	{
		Stage alertboxWindow = new Stage();
		alertboxWindow.setTitle(title);
		alertboxWindow.setWidth(400);
		alertboxWindow.setHeight(150);
		alertboxWindow.initModality(Modality.APPLICATION_MODAL);
		alertboxWindow.setResizable(false);
		
		Label alertLabel = new Label(message);
		
		Button confirmButton = new Button("OK");
		confirmButton.setOnAction(e -> alertboxWindow.close());
		
		confirmButton.setPrefWidth(75);
		confirmButton.setPrefHeight(25);
		
		VBox alertboxLayout = new VBox(20);
		alertboxLayout.setAlignment(Pos.CENTER);
		alertLabel.setTextAlignment(TextAlignment.CENTER);
		alertboxLayout.getChildren().addAll(alertLabel, confirmButton);
		
		alertboxLayout.setId("pane2");
		confirmButton.setId("buttonEditFileCards");
		
		Scene alertboxScene = new Scene(alertboxLayout);
		alertboxScene.getStylesheets().addAll(AlertBox.class.getResource("Style.css").toExternalForm());
		alertboxWindow.setScene(alertboxScene);
		alertboxWindow.showAndWait();
	}
	
}
