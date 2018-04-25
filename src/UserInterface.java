import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.*;


public class UserInterface {
	
	private Label header;
	private Label filler1,filler2; //filler for the second and 
	private Button btnEdit;
	private Button btnTest;
	private Button btnResults;
	private Button btnLogout;
	private Button btnSettings;
	
	private GridPane gridPane;
	
	
	public void createUI(Stage uiStage, String name, Scene loginScene) {
		
	
		uiStage.setTitle("File Card System");
		
		header = new Label("Welcome "+name);
		header.setStyle("-fx-font-size: 20px;");
		header.setAlignment(Pos.CENTER);
		header.setMaxWidth(Double.MAX_VALUE);
		header.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
		
		gridPane = new GridPane();
		filler1 = new Label("");
		filler2 = new Label("");
		btnSettings = new Button("Settings");
		btnEdit = new Button("Edit");
		btnTest = new Button("Test");
		btnResults = new Button("Results");
		btnLogout = new Button("Logout");

		
		//125, 75 old Values
		btnEdit.setPrefSize(175, 60);
		btnTest.setPrefSize(175, 60);
		btnResults.setPrefSize(175, 60);
		btnLogout.setPrefSize(175, 60);
		btnSettings.setPrefSize(175, 60);

		
		gridPane.setId("pane");
	
		gridPane.setHgap(15);
		gridPane.setVgap(15);
		
		GridPane.setConstraints(header,0,0,2,1); //First row
		GridPane.setHalignment(header, HPos.CENTER);
		GridPane.setConstraints(filler2,1,1);
		GridPane.setConstraints(btnEdit,1,2); 
		GridPane.setConstraints(btnTest,1,3);
		GridPane.setConstraints(btnResults,1,4);
		GridPane.setConstraints(btnSettings,1,5);
		GridPane.setConstraints(btnLogout,1,6);
		GridPane.setConstraints(filler1,1,7); //Seventh row
		
		
		gridPane.getChildren().addAll(header,filler2,btnEdit,btnTest,btnResults,
				btnLogout,filler1,btnSettings);
		
		//Set the ColumnContraints and add them

		ColumnConstraints column0 = new ColumnConstraints(15,50,600);
		ColumnConstraints column1 = new ColumnConstraints(60,65,Double.MAX_VALUE);
		
		//Both Columns have the same priority
		column0.setHgrow(Priority.ALWAYS);
		column1.setHgrow(Priority.ALWAYS);
		
		gridPane.getColumnConstraints().addAll(column0,column1);
		
		
		//Set the RowConstraints and add them
		
		//old Values 10,75,75
		RowConstraints row1 = new RowConstraints(25,25,25);
		RowConstraints row2 = new RowConstraints(0,20,Double.MAX_VALUE);
		RowConstraints row3 = new RowConstraints(10,60,60);
		RowConstraints row4 = new RowConstraints(10,60,60);
		RowConstraints row5 = new RowConstraints(10,60,60);
		RowConstraints row6 = new RowConstraints(10,60,60);
		RowConstraints row7 = new RowConstraints(10,60,60);
		RowConstraints row8 = new RowConstraints(30,60,Double.MAX_VALUE);
		
		
		//Every row have the same priority
		
		row1.setVgrow(Priority.ALWAYS);
		row2.setVgrow(Priority.ALWAYS);
		row3.setVgrow(Priority.ALWAYS);
		row4.setVgrow(Priority.ALWAYS);
		row5.setVgrow(Priority.ALWAYS);
		row6.setVgrow(Priority.ALWAYS);
		row7.setVgrow(Priority.ALWAYS);
		row8.setVgrow(Priority.ALWAYS);
		
		gridPane.getRowConstraints().addAll(row1,row2,row3,row4,row5,row6,row7,row8);
		
		
		//set the GridPane with the new row/column values in the scene and in the stage
		
		Scene cssStyle = new Scene(gridPane,1000,600);
		cssStyle.getStylesheets().addAll(this.getClass().getResource("Style.css").toExternalForm());
		uiStage.setScene(cssStyle);
		
		
		//gridPane.setGridLinesVisible(true); //Debugging
		
		
		//Eventhandler
		
		btnEdit.setOnAction(e -> {
			new EditFileCards();
		});
		
		btnTest.setOnAction(e -> {
			//Some Code
		});
		
		btnResults.setOnAction(e -> {
			//Some Code
		});
		
		btnLogout.setOnAction(e -> {
			//Close the User-DB!?
			//Close Stage?
			
			boolean answer = ConfirmBox.display("", "Are you sure you want to logout?");
			if(answer) {
				uiStage.setScene(loginScene);
			}
			});
		}
}