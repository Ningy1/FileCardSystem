import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
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
		loginWindow.sizeToScene();
		loginWindow.show();
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		loginWindow.setX((primScreenBounds.getWidth() - loginWindow.getWidth()) / 2);
		loginWindow.setY((primScreenBounds.getHeight() - loginWindow.getHeight()) / 2);
	}
	

	


	public void dbLoginQuery(TextField usernameField, PasswordField passwordField, Stage uiStage)
	{
	  	ResultSet rs;
		try {
			rs = db.query("SELECT * FROM user WHERE Username = '"+usernameField.getText()+"' AND password = '"+passwordField.getText()+"'");
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
			rs = db.query("SELECT * FROM user WHERE Username = '"+usernameField.getText()+"'");
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
	
	public int dbRegisterQuery(TextField userNameField, TextField firstNameField, TextField lastNameFiled, TextField emailField, PasswordField password1, PasswordField password2)
	{
		ResultSet rs;
		int error = 0;
		int userExists = 0;
		int mailCorrect = 0;
		try {
			rs = db.query("SELECT 1 FROM user WHERE Username = '"+userNameField.getText()+"'");
			while(rs.next()) {
				userExists=rs.getInt(1);
			}
			
			if(userExists == 1) {
				userNameField.setText("Username already exists.");
				error = 1;
			}else {
				if(!userNameField.getText().matches("^[a-zA-Z].*")) {
					userNameField.setText("Username must start with a letter.");
					error = 1;
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
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
		
		if(emailField.getText().matches("^[a-zA-Z].*")) {
			
			if(emailField.getText().matches("[0-9a-zA-Z\\-\\_\\.]+@[0-9a-zA-Z]+\\.[a-z]{2,3}")) {
				mailCorrect = 1;
			}else {
				emailField.setText("Insert only valid character");
				error = 1;
			}
			
		}else {
			emailField.setText("Must start with a letter");
			error = 1;
		}
		
	
		if(userNameField.getText().length() < 1 || lastNameFiled.getText().length() < 1 || firstNameField.getText().length() <1 || 
				password1.getText().length() < 1 || emailField.getText().length() < 1)
		{
			error = 1;
			AlertBox.display("Error", "Fill out all fields");
			
		} else if(password1.getText().equals(password2.getText()) && userExists==0 && mailCorrect==1 && error==0)
		{
			if(password1.getLength()<=7) {
				AlertBox.display("Error", "The password must be at least 8 characters");
				error = 1;
			} else {
			
			try {
				db.update("INSERT INTO user (Username, firstname, lastname, Email, password) VALUES('"+userNameField.getText()+"','"+firstNameField.getText()+"','"+lastNameFiled.getText()+"','"+emailField.getText()+"','"+password1.getText()+"')");
				AlertBox.display("Congrat", userNameField.getText()+" is now in the system");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} }
		} else if(!password1.getText().equals(password2.getText()))
		{
			AlertBox.display("Error", "Password has to be identical in both fields");
			error=1;
		} else 
		{
			AlertBox.display("Error", "Registration failed");
			error = 1;
		}
		return error;
	}
}
