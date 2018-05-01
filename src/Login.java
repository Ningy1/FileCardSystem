import java.security.GeneralSecurityException;
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

	static int userID = -1;
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

		
		Scene cssStyle = new Scene(new LoginLayout(loginWindow,this),1000,600);
		cssStyle.getStylesheets().addAll(this.getClass().getResource("Style.css").toExternalForm());
		loginWindow.setScene(cssStyle);
		loginWindow.show();
	}
	

	


	public void dbLoginQuery(TextField usernameField, PasswordField passwordField, Stage uiStage)
	{
	  	ResultSet rs;
		try {
			rs = db.query("SELECT * FROM user WHERE firstname = '"+usernameField.getText()+"' AND password = '"+passwordField.getText()+"'");
		if(rs.isBeforeFirst())
		{
			rs.next();
			AlertBox.display("Logged in", "You successfully logged into the File Card System.");
			userID = rs.getInt(1);
			new UserInterface(uiStage,usernameField.getText(),uiStage.getScene());
			uiStage.close();
			usernameField.clear();
			passwordField.clear();
			
		}
		else
		{
			rs = db.query("SELECT * FROM user WHERE firstname = '"+usernameField.getText()+"'");
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
	
	public void dbRegisterQuery(TextField firstNameField, TextField lastNameFiled, PasswordField password1, PasswordField password2)
	{
		int error = 0;
		
		for(int i = 0; i < firstNameField.getText().length(); i++)
		{
			int c = (int)firstNameField.getText().charAt(i);
			if(c < 65 || (c > 90 && c < 97) || c > 122 )
			{
				firstNameField.setText("Insert only valid character");
				error = 1;
			}
		}
		for(int i = 0; i < lastNameFiled.getText().length(); i++)
		{
			int c = (int)lastNameFiled.getText().charAt(i);
			if(c < 65 || (c > 90 && c < 97) || c > 122 )
			{
				lastNameFiled.setText("Insert only valid character");
				error = 1;
			}
		}
		if(lastNameFiled.getText().length() < 1 || firstNameField.getText().length() <1 || password1.getText().length() < 1)
		{
			error = 1;
			AlertBox.display("Error", "Fill out all fields");
			
		} else if(password1.getText().equals(password2.getText()) && error==0)
		{
			try {
				db.update("INSERT INTO user (firstname, lastname, password) VALUES('"+firstNameField.getText()+"','"+lastNameFiled.getText()+"','"+password1.getText()+"')");
				AlertBox.display("Congrat", firstNameField.getText()+" is now in the system");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
		{
			AlertBox.display("Error", "Password has to be identical in both fields");
		}
	}
}
