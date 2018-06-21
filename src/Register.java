
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class Register extends GridPane {
	

	Login LoginViewControl;
	Label title;
	Label userName;
	TextField userNameField;
	Label firstName;
	TextField firstNameField = new TextField() ;
	Label lastName;
	TextField lastNameField;
	Label password1;
	PasswordField passwordField1;
	Label password2;
	PasswordField passwordField2;
	Label email;
	TextField emailField;
	Button registerButton;
	Button cancelButton;
	
	public Register(Stage create, Login LoginViewControl) {
	
	this.LoginViewControl = LoginViewControl;
	
	title = new Label("Register to the File Card System");
	firstName = new Label("First Name:");
	userName = new Label("Username:");
	userNameField = new TextField();
	firstNameField = new TextField() ;
	lastName = new Label("Last Name:");
	lastNameField = new TextField();
	password1 = new Label("Password:");
	passwordField1 = new PasswordField();
	password2 = new Label("Verify entered password:");
	passwordField2 = new PasswordField();
	email = new Label("Email:");
	emailField = new TextField();
	registerButton = new Button("Register now");
	cancelButton = new Button("Cancel registration");
	
	
	title.setId("header");
	registerButton.setId("button");
	cancelButton.setId("button");
	
	registerButton.setOnAction(e-> {
		int error=0;
		error = this.LoginViewControl.dbRegisterQuery(userNameField, firstNameField, lastNameField, emailField, passwordField1, passwordField2);
		if(error==0) {
		Scene cssStyle = new Scene(new LoginLayout(create,this.LoginViewControl),1000,600);
		cssStyle.getStylesheets().addAll(this.getClass().getResource("Style.css").toExternalForm());
		create.setScene(cssStyle);
		create.setTitle("Login");
		}
	});
	cancelButton.setOnAction(e -> {
		Scene cssStyle = new Scene(new LoginLayout(create,this.LoginViewControl),1000,600);
		cssStyle.getStylesheets().addAll(this.getClass().getResource("Style.css").toExternalForm());
		create.setScene(cssStyle);
		create.setTitle("Login");
	});	
	
	
	setId("pane");
	setPadding(new Insets(10,10,10,10));
	setHgap(10);
	setVgap(8);
	
	setConstraints(title, 48, 0, 2, 1);
	setHalignment(title, HPos.CENTER);
	setConstraints(userName, 48, 6);
	setHalignment(userName, HPos.CENTER);
	setConstraints(userNameField, 49, 6);
	setHalignment(userNameField, HPos.CENTER);
	setConstraints(firstName, 48, 7);
	setHalignment(firstName, HPos.CENTER);
	setConstraints(firstNameField, 49, 7);
	setConstraints(lastName, 48, 8);
	setHalignment(lastName, HPos.CENTER);
	setConstraints(lastNameField, 49, 8);
	setConstraints(email, 48, 9);
	setHalignment(email, HPos.CENTER);
	setConstraints(emailField, 49, 9);
	setHalignment(emailField, HPos.CENTER);
	setConstraints(password1, 48, 10);
	setHalignment(password1, HPos.CENTER);
	setConstraints(passwordField1, 49, 10);
	setConstraints(password2, 48, 11);
	setHalignment(password2, HPos.CENTER);
	setConstraints(passwordField2, 49, 11);
	setConstraints(registerButton, 48, 17);
	setHalignment(registerButton, HPos.CENTER);
	setConstraints(cancelButton, 49, 17);
	setHalignment(cancelButton, HPos.CENTER);
	
	getChildren().addAll(title, firstName, userName, userNameField,
			firstNameField, lastName, lastNameField, email, emailField, password1, passwordField1,
			password2, passwordField2, registerButton, cancelButton);
	//loginWindowLayout.setGridLinesVisible(true);
	
	//Scene registerScene = new Scene(loginWindowLayout, 1000, 600);
	//registerScene.getStylesheets().addAll(this.getClass().getResource("Style.css").toExternalForm());
	
	
	
	
	}
}
	
