import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginLayout extends GridPane{

	
	Label username = new Label("Username:");
	Label password = new Label("Password:");
	Button loginButton = new Button("Log in");
	Button registerButton = new Button("Register");
	TextField usernameField = new TextField();											// here we could implement that the last used username will be shown by passing it into the constructor
	PasswordField passwordField = new PasswordField();
	Login LoginViewControl;
	
	
	
	public LoginLayout(Stage create, Login LoginViewControl) {
		
		this.LoginViewControl = LoginViewControl;
		
		Label title = new Label("Welcome to the File Card System");
		title.setId("header");
		
		Label username = new Label("Username:");
		Label password = new Label("Password:");
		Button loginButton = new Button("Log in");
		Button registerButton = new Button("Register");
		TextField usernameField = new TextField();											// here we could implement that the last used username will be shown by passing it into the constructor
		usernameField.setPromptText("Enter a username");
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Enter a password");
		loginButton.setId("button");
		registerButton.setId("button");
		
		registerButton.setOnAction(e -> {
			Scene cssStyle = new Scene(new Register(create,this.LoginViewControl),1000,600);
			cssStyle.getStylesheets().addAll(this.getClass().getResource("Style.css").toExternalForm());
			create.setScene(cssStyle);
		});						  // the scene should change after the user clicks on the register button
		loginButton.setOnAction(e -> this.LoginViewControl.dbLoginQuery(usernameField, passwordField,create));
		setId("pane");
		setPadding(new Insets(10, 10, 10, 10));
		setHgap(10);
		setVgap(8);
		
		setConstraints(title, 48, 0, 2, 1);
		setHalignment(title, HPos.CENTER);
		setConstraints(username, 48, 6);
		setHalignment(username, HPos.CENTER);
		setConstraints(usernameField, 49, 6);
		setConstraints(password, 48, 7);
		setHalignment(password, HPos.CENTER);
		setConstraints(passwordField, 49, 7);
		setConstraints(loginButton, 48, 15);
		setConstraints(registerButton, 49, 15);
		setHalignment(loginButton, HPos.CENTER);
		setHalignment(registerButton, HPos.CENTER);
		
		getChildren().addAll(title, username,
				usernameField, password, passwordField, loginButton, registerButton);
		//loginWindowLayout.setGridLinesVisible(true); 				// for debugging of the GridPane
		
		//Scene loginScene = new Scene(loginWindowLayout, 1000, 600);
		//test.getStylesheets().addAll(this.getClass().getResource("Style.css").toExternalForm());
	}
	
	}
	
