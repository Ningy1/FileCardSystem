import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class InfoPopup {
	
	static Boolean active = false;
	
	
	public InfoPopup(String message, Integer ttl, Stage editStage) {
	active = true;
	Stage popupStage = new Stage();
	popupStage.initModality(Modality.APPLICATION_MODAL);
	VBox vbox = new VBox();
	Label label = new Label();
	label.setText(message);
	vbox.getChildren().add(label);
	Scene popupScene = new Scene(vbox);
	popupStage.setX((editStage.getX()+editStage.getWidth()/2) - 150);
	popupStage.setY((editStage.getY()+editStage.getHeight()/2) - 50);
	popupStage.setWidth(300);
	popupStage.setHeight(100);
	vbox.setId("pane2");
	label.setId("header");
	popupScene.getStylesheets().addAll(AlertBox.class.getResource("Style.css").toExternalForm());
	popupStage.initStyle(StageStyle.UNDECORATED);
	popupStage.setScene(popupScene);
	popupStage.show();
	PauseTransition delay = new PauseTransition(Duration.seconds(ttl));
	delay.setOnFinished( event -> {
		active = false;
		popupStage.close();
	});
	delay.play();

	}
	
	
}
