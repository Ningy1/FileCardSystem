import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


// class should return some values in the end
public class ConfirmBox {
	
	private static boolean userAnswer;
	
	public static boolean display(String title, String message)
	{
		Stage confirmboxWindow = new Stage();
		confirmboxWindow.setTitle(title);
		confirmboxWindow.setMinWidth(250);
		confirmboxWindow.initModality(Modality.APPLICATION_MODAL);
		
		Label confirmLabel = new Label(message);
		
		Button yesButton = new Button("Yes");
		Button noButton = new Button("No");
		
		yesButton.setOnAction(e -> {
				userAnswer = true;
				confirmboxWindow.close();
		});
		
		noButton.setOnAction(e -> {
			userAnswer = false;
			confirmboxWindow.close();
		});
		
		VBox confirmboxLayout = new VBox(10);
		confirmboxLayout.getChildren().addAll(confirmLabel, yesButton, noButton);
		confirmboxLayout.setAlignment(Pos.CENTER);
		
		Scene confirmboxScene = new Scene(confirmboxLayout);
		
		confirmboxWindow.setScene(confirmboxScene);
		confirmboxWindow.showAndWait();
		
		return userAnswer;
	}
	
}
