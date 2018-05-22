import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
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
		confirmboxWindow.setResizable(false);
		
		Label confirmLabel = new Label(message);
		confirmLabel.setPadding(new Insets(0, 0, 40, 0));
		
		
		Button yesButton = new Button("Yes");
		Button noButton = new Button("No");
		
		yesButton.setPrefWidth(75);
		yesButton.setPrefHeight(50);
		
		noButton.setPrefWidth(75);
		noButton.setPrefHeight(50);	
		
		yesButton.setOnAction(e -> {
				userAnswer = true;
				confirmboxWindow.close();
		});
		
		noButton.setOnAction(e -> {
			userAnswer = false;
			confirmboxWindow.close();
		});
		
		GridPane confirmboxLayout = new GridPane();
		
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(25);
		RowConstraints row1 = new RowConstraints();
		row1.setPercentHeight(25);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(25);
		RowConstraints row2 = new RowConstraints();
		row2.setPercentHeight(25);
		confirmboxLayout.getColumnConstraints().addAll(col1,col2);
		confirmboxLayout.getRowConstraints().addAll(row1,row2);
		
		confirmboxLayout.setPadding(new Insets(10, 10, 10, 10));
		confirmboxLayout.add(confirmLabel, 0, 0, 3, 1);
		GridPane.setHalignment(confirmLabel, HPos.CENTER);
		confirmboxLayout.add(yesButton, 0, 1);
		GridPane.setHalignment(yesButton, HPos.RIGHT);
		confirmboxLayout.add(noButton, 1, 1);
		GridPane.setHalignment(noButton, HPos.RIGHT);
		
		confirmboxLayout.setId("pane2");
		yesButton.setId("buttonEditFileCards");
		noButton.setId("buttonEditFileCards");
		
		//confirmboxLayout.setGridLinesVisible(true);
		
		Scene confirmboxScene = new Scene(confirmboxLayout, 400, 150);
		
		confirmboxScene.getStylesheets().addAll(ConfirmBox.class.getResource("Style.css").toExternalForm());
		confirmboxWindow.setScene(confirmboxScene);
		confirmboxWindow.showAndWait();
		
		return userAnswer;
	}
	
}
