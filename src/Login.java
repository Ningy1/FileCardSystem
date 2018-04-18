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
		launch(args);
	}

	public void start(Stage primaryStage) throws Exception {
		
	loginWindow = primaryStage;
	loginWindow.setTitle("Login");
	
	loginWindow.setOnCloseRequest(e -> {
		boolean answer = ConfirmBox.display("Confirmation", "Are you sure you want to quit?");
		if(answer==true){
			loginWindow.close();
		}else {
			e.consume();
			}
	});
	
	username = new Label("Username:");
	password = new Label("Password:");
	loginButton = new Button("Login");
	registerButton = new Button("Register");
	usernameField = new TextField();
	passwordField = new PasswordField();
	
	registerButton.setOnAction(e -> loginWindow.setScene(registerScene));
	loginButton.setOnAction(e -> AlertBox.display("Logged in", "You successfully logged into the File Card System."));
	
	VBox loginWindowLayout = new VBox(20);
	loginWindowLayout.getChildren().addAll(username, usernameField, password, passwordField, loginButton, registerButton);
	
	loginScene = new Scene(loginWindowLayout, 800, 500);
	loginWindow.setScene(loginScene);
	
	loginWindow.show();
	
	}
}
