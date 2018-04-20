import java.sql.SQLException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Login extends Application  {

	private Button loginButton;
	private Button registerButton;
	private Stage loginWindow;
	private Scene loginScene, registerScene;
	private Label username;
	private Label password;
	private TextField usernameField;
	private PasswordField passwordField;
	private ImageView imageFilecardsystem;
	private HSQLDB db;
	/* Start of the Program in this class */
	
	public static void main(String[] args) {
		launch(args);			// calls the start-method of the Application class
	}

	public void start(Stage primaryStage) throws Exception {
	db = HSQLDB.getInstance();
	loginWindow = primaryStage;
	loginWindow.setTitle("Login");
	
	// in case the user wants to quit the login window by clicking on the X on the top right, he needs to deal with a confirm window
	loginWindow.setOnCloseRequest(e -> {
		e.consume();
		closeProgram();
	});
	
	username = new Label("Username:");
	password = new Label("Password:");
	loginButton = new Button("Log in");
	registerButton = new Button("Register");
	usernameField = new TextField();											// here we could implement that the last used username will be shown by passing it into the constructor
	passwordField = new PasswordField();
	passwordField.setPromptText("Enter a password");
	imageFilecardsystem = new ImageView(new Image("Images/FileCard.png"));
	imageFilecardsystem.setFitHeight(350);
	imageFilecardsystem.setFitWidth(350);
	
	
/*	registerButton.setOnAction(new EventHandler<ActionEvent>()
			{

				@Override
				public void handle(ActionEvent arg0) {
					loginWindow.setScene(registerScene);
					registerScene = new Scene(loginWindowLayout, 800, 500);
				}
		
			});											  // the scene should change after the user clicks on the register button
*/	
	loginButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
        	AlertBox.display("Logged in", "You successfully logged into the File Card System.");
        	// this should be changed later on 
			//(should open the main window of the program after a successful login)
        }
    });
	
	
	GridPane loginWindowLayout = new GridPane();
	loginWindowLayout.setStyle("-fx-background-color: #FFA70D;");
	loginWindowLayout.setPadding(new Insets(10, 10, 10, 10));
	loginWindowLayout.setHgap(10);
	loginWindowLayout.setVgap(8);
	
	loginWindowLayout.setConstraints(imageFilecardsystem, 3, 0, 40, 50);
	loginWindowLayout.setConstraints(username, 41, 25);
	loginWindowLayout.setConstraints(usernameField, 42, 25);
	loginWindowLayout.setConstraints(password, 41, 26);
	loginWindowLayout.setConstraints(passwordField, 42, 26);
	loginWindowLayout.setConstraints(loginButton, 41, 27);
	loginWindowLayout.setConstraints(registerButton, 42, 27);
	loginWindowLayout.getChildren().addAll(imageFilecardsystem, username, usernameField, password, passwordField, loginButton, registerButton);
	//loginWindowLayout.setGridLinesVisible(true); 				// for debugging of the GridPane
	
	loginScene = new Scene(loginWindowLayout, 800, 500);
	loginWindow.setScene(loginScene);
	loginWindow.show();
	}
	
	
	
	private void closeProgram()
	{
		{
			boolean answer = ConfirmBox.display("Confirmation", "Are you sure you want to quit?");
			if(answer){
				loginWindow.close();
				try {
					db.close();
					System.out.println("Connection closed");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
}
