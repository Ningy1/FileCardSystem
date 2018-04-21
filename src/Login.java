import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Login extends Application  {

	private HSQLDB db;
	
	/* Start of the Program in this class */
	
	public static void main(String[] args) {
		launch(args);			// calls the start-method of the Application class
	}

	public void start(Stage primaryStage) throws Exception {
		
	db = HSQLDB.getInstance();
	createLoginWindow(primaryStage);
	}
	
	private void closeProgram(Stage loginWindow)
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
	
	private void createLoginWindow(Stage primaryStage)
	{
		Stage loginWindow = primaryStage;
		loginWindow.setTitle("Login");
		
		// in case the user wants to quit the login window by clicking on the X on the top right, he needs to deal with a confirm window
		loginWindow.setOnCloseRequest(e -> {
			e.consume();
			closeProgram(loginWindow);
		});
		
		loginWindow.setScene(createLoginScene(loginWindow));
		loginWindow.show();
	}
	
	private Scene createLoginScene(Stage loginWindow)
	{
		Label title = new Label("Welcome to the File Card System");
		title.setId("header");
		
		Label username = new Label("Username:");
		Label password = new Label("Password:");
		Button loginButton = new Button("Log in");
		Button registerButton = new Button("Register");
		TextField usernameField = new TextField();											// here we could implement that the last used username will be shown by passing it into the constructor
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Enter a password");
		loginButton.setId("button");
		registerButton.setId("button");
		
		registerButton.setOnAction(e -> loginWindow.setScene(createRegisterScene(loginWindow)));						  // the scene should change after the user clicks on the register button
		loginButton.setOnAction(e -> dbLoginQuery(usernameField, passwordField));
		
		GridPane loginWindowLayout = new GridPane();
		loginWindowLayout.setId("pane");
		loginWindowLayout.setPadding(new Insets(10, 10, 10, 10));
		loginWindowLayout.setHgap(10);
		loginWindowLayout.setVgap(8);
		
		loginWindowLayout.setConstraints(title, 48, 0, 2, 1);
		loginWindowLayout.setHalignment(title, HPos.CENTER);
		loginWindowLayout.setConstraints(username, 48, 6);
		loginWindowLayout.setHalignment(username, HPos.CENTER);
		loginWindowLayout.setConstraints(usernameField, 49, 6);
		loginWindowLayout.setConstraints(password, 48, 7);
		loginWindowLayout.setHalignment(password, HPos.CENTER);
		loginWindowLayout.setConstraints(passwordField, 49, 7);
		loginWindowLayout.setConstraints(loginButton, 48, 15);
		loginWindowLayout.setConstraints(registerButton, 49, 15);
		loginWindowLayout.setHalignment(loginButton, HPos.CENTER);
		loginWindowLayout.setHalignment(registerButton, HPos.CENTER);
		
		loginWindowLayout.getChildren().addAll(title, username,
				usernameField, password, passwordField, loginButton, registerButton);
		//loginWindowLayout.setGridLinesVisible(true); 				// for debugging of the GridPane
		
		Scene loginScene = new Scene(loginWindowLayout, 1000, 600);
		loginScene.getStylesheets().addAll(this.getClass().getResource("Style.css").toExternalForm());
		return loginScene;
	}
	
	private Scene createRegisterScene(Stage loginWindow)
	{
		Label title = new Label("Register to the File Card System");
		title.setId("header");
		Label firstName = new Label("Fist Name:");
		TextField firstNameField = new TextField();
		Label lastName = new Label("Last Name:");
		TextField lastNameField = new TextField();
		Label password1 = new Label("Password:");
		PasswordField passwordField1 = new PasswordField();
		Label password2 = new Label("Verify entered password:");
		PasswordField passwordField2 = new PasswordField();
		Button registerButton = new Button("Register now");
		registerButton.setId("button");
		Button cancelButton = new Button("Cancel registration");
		cancelButton.setId("button");
		
		registerButton.setOnAction(e-> dbRegisterQuery());
		cancelButton.setOnAction(e-> loginWindow.setScene(createLoginScene(loginWindow)));
		
		GridPane loginWindowLayout = new GridPane();
		loginWindowLayout.setId("pane");
		loginWindowLayout.setPadding(new Insets(10,10,10,10));
		loginWindowLayout.setHgap(10);
		loginWindowLayout.setVgap(8);
		
		loginWindowLayout.setConstraints(title, 48, 0, 2, 1);
		loginWindowLayout.setHalignment(title, HPos.CENTER);
		loginWindowLayout.setConstraints(firstName, 48, 6);
		loginWindowLayout.setHalignment(firstName, HPos.CENTER);
		loginWindowLayout.setConstraints(firstNameField, 49, 6);
		loginWindowLayout.setConstraints(lastName, 48, 7);
		loginWindowLayout.setHalignment(lastName, HPos.CENTER);
		loginWindowLayout.setConstraints(lastNameField, 49, 7);
		loginWindowLayout.setConstraints(password1, 48, 8);
		loginWindowLayout.setHalignment(password1, HPos.CENTER);
		loginWindowLayout.setConstraints(passwordField1, 49, 8);
		loginWindowLayout.setConstraints(password2, 48, 9);
		loginWindowLayout.setHalignment(password2, HPos.CENTER);
		loginWindowLayout.setConstraints(passwordField2, 49, 9);
		loginWindowLayout.setConstraints(registerButton, 48, 15);
		loginWindowLayout.setHalignment(registerButton, HPos.CENTER);
		loginWindowLayout.setConstraints(cancelButton, 49, 15);
		loginWindowLayout.setHalignment(cancelButton, HPos.CENTER);
		
		loginWindowLayout.getChildren().addAll(title, firstName,
				firstNameField, lastName, lastNameField, password1, passwordField1,
				password2, passwordField2, registerButton, cancelButton);
		//loginWindowLayout.setGridLinesVisible(true);
		
		Scene registerScene = new Scene(loginWindowLayout, 1000, 600);
		registerScene.getStylesheets().addAll(this.getClass().getResource("Style.css").toExternalForm());
		return registerScene;
	}
	
	private void dbLoginQuery(TextField usernameField, PasswordField passwordField)
	{
		// this should be changed later on 
		//(should open the main window of the program after a successful login)
    	ResultSet rs;
		try {
			rs = db.query("SELECT * FROM user WHERE name = '"+usernameField.getText()+"' AND password = '"+passwordField.getText()+"'");
		if(rs.isBeforeFirst())
		{
			AlertBox.display("Logged in", "You successfully logged into the File Card System.");
		}
		else
		{
			rs = db.query("SELECT * FROM user WHERE name = '"+usernameField.getText()+"'");
			if(rs.isBeforeFirst())
			{	
				AlertBox.display("Error", "Password is wrong");
			}
			else
			{
				AlertBox.display("Error", "Username does not exist");
			}
		}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	private void dbRegisterQuery()
	{
		// dbRegister query needs to be implemented
	}
}
