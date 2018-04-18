import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
	
	/* Start of the Program in this class */
	
	public static void main(String[] args) {
		launch(args);			// calls the start-method of the Application class
	}

	public void start(Stage primaryStage) throws Exception {
		
	loginWindow = primaryStage;
	loginWindow.setTitle("Login");
	
	// in case the user wants to quit the login window by clicking on the X on the top right, he needs to deal with a confirm window
	loginWindow.setOnCloseRequest(e -> {
		e.consume();
		closeProgram();
	});
	
	username = new Label("Username:");
	password = new Label("Password:");
	loginButton = new Button("Login");
	registerButton = new Button("Register");
	usernameField = new TextField();
	passwordField = new PasswordField();
	
	registerButton.setOnAction(e -> loginWindow.setScene(registerScene));											  // the scene should change after the user clicks on the register button
	loginButton.setOnAction(e -> AlertBox.display("Logged in", "You successfully logged into the File Card System."));// this should be changed later on 
																														//(should open the main window of the program after a successful login)

	VBox loginWindowLayout = new VBox(20);																			   // very simple layout, needs further details
	loginWindowLayout.getChildren().addAll(username, usernameField, password, passwordField, loginButton, registerButton);
	
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
			}
		}
	}
}
