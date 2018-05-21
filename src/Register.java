
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
	Label firstName;
	TextField firstNameField = new TextField() ;
	Label lastName;
	TextField lastNameField;
	Label password1;
	PasswordField passwordField1;
	Label password2;
	PasswordField passwordField2;
	Button registerButton;
	Button cancelButton;
	
	public Register(Stage create, Login LoginViewControl) {
	
	this.LoginViewControl = LoginViewControl;
	
	title = new Label("Register to the File Card System");
	firstName = new Label("First Name:");
	TextField firstNameField = new TextField() ;
	lastName = new Label("Last Name:");
	lastNameField = new TextField();
	password1 = new Label("Password:");
	passwordField1 = new PasswordField();
	password2 = new Label("Verify entered password:");
	passwordField2 = new PasswordField();
	registerButton = new Button("Register now");
	cancelButton = new Button("Cancel registration");
	
	
	title.setId("header");
	registerButton.setId("button");
	cancelButton.setId("button");
	
	registerButton.setOnAction(e-> {
		this.LoginViewControl.dbRegisterQuery(firstNameField, lastNameField, passwordField1, passwordField2);
		Scene cssStyle = new Scene(new LoginLayout(create,this.LoginViewControl),1000,600);
		cssStyle.getStylesheets().addAll(this.getClass().getResource("Style.css").toExternalForm());
		create.setScene(cssStyle);
	});
	cancelButton.setOnAction(e -> {
		Scene cssStyle = new Scene(new LoginLayout(create,this.LoginViewControl),1000,600);
		cssStyle.getStylesheets().addAll(this.getClass().getResource("Style.css").toExternalForm());
		create.setScene(cssStyle);
	});	
	
	
	setId("pane");
	setPadding(new Insets(10,10,10,10));
	setHgap(10);
	setVgap(8);
	
	setConstraints(title, 48, 0, 2, 1);
	setHalignment(title, HPos.CENTER);
	setConstraints(firstName, 48, 6);
	setHalignment(firstName, HPos.CENTER);
	setConstraints(firstNameField, 49, 6);
	setConstraints(lastName, 48, 7);
	setHalignment(lastName, HPos.CENTER);
	setConstraints(lastNameField, 49, 7);
	setConstraints(password1, 48, 8);
	setHalignment(password1, HPos.CENTER);
	setConstraints(passwordField1, 49, 8);
	setConstraints(password2, 48, 9);
	setHalignment(password2, HPos.CENTER);
	setConstraints(passwordField2, 49, 9);
	setConstraints(registerButton, 48, 15);
	setHalignment(registerButton, HPos.CENTER);
	setConstraints(cancelButton, 49, 15);
	setHalignment(cancelButton, HPos.CENTER);
	
	getChildren().addAll(title, firstName,
			firstNameField, lastName, lastNameField, password1, passwordField1,
			password2, passwordField2, registerButton, cancelButton);
	//loginWindowLayout.setGridLinesVisible(true);
	
	//Scene registerScene = new Scene(loginWindowLayout, 1000, 600);
	//registerScene.getStylesheets().addAll(this.getClass().getResource("Style.css").toExternalForm());
	
	
	
	
	}
}
	
