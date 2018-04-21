
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
	

	
	
	
	public Register(Stage create, HSQLDB db) {
	
	Label title = new Label("Register to the File Card System");
	title.setId("header");
	Label firstName = new Label("First Name:");
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
	
	registerButton.setOnAction(e-> db.dbRegisterQuery());
	cancelButton.setOnAction(e -> {
		Scene cssStyle = new Scene(new LoginLayout(create,db),1000,600);
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
	
