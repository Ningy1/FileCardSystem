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

		
		Scene cssStyle = new Scene(new LoginLayout(loginWindow,db),1000,600);
		cssStyle.getStylesheets().addAll(this.getClass().getResource("Style.css").toExternalForm());
		loginWindow.setScene(cssStyle);
		loginWindow.show();
	}
	

	
///////////////////////////AUSLAGERUNG IN HSQLDB////////////////////////////////////


	/*private void dbLoginQuery(TextField usernameField, PasswordField passwordField)
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
	}*/
	/*
	private void dbRegisterQuery()
	{
		// dbRegister query needs to be implemented
	}*/
}
