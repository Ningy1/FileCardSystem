	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Statement;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

	public class HSQLDB {
		
		static Connection con = null;
	    static HSQLDB instance = null;
	    
		public HSQLDB(){
		      try {
		         //Registering the HSQLDB JDBC driver
		         Class.forName("org.hsqldb.jdbc.JDBCDriver");
		         //Creating the connection with HSQLDB
		         con = DriverManager.getConnection("jdbc:hsqldb:file:neue;shutdown=true","SA","");
		         // Check if Connection was successfull
		         if (con!= null){
		            System.out.println("Connection created successfully");
		            
		         }else{
		            System.out.println("Problem with creating connection");
		         }
		      
		      }  catch (Exception e) {
		         e.printStackTrace(System.out);
		      }     
		}
		// Get instance DB, if not instantiated instantiate a new connection
		public static HSQLDB getInstance() throws Exception {	// Singleton-Pattern
			if (instance == null) {
				instance = new HSQLDB();
			}
			return instance;
		}
		public void close() throws SQLException {
			con.close();
		}
		public ResultSet query(String sqlStatement) throws SQLException {
			return con.createStatement().executeQuery(sqlStatement);
		}
		
		public void update(String sqlStatement) throws SQLException {
			Statement statement = null;
			statement = con.createStatement();
			int result = statement.executeUpdate(sqlStatement); 
			statement.close();
			if (result != 0) {
				throw new SQLException("Error in expression: "+sqlStatement);
			}
		}
	
		
//////////////////////////////////////AUSTAUSCH DER METHODEN, VON DER MAIN ZU HSQLDB///////////////////////
		public void dbLoginQuery(TextField usernameField, PasswordField passwordField)
		{
			// this should be changed later on 
			//(should open the main window of the program after a successful login)
	    	ResultSet rs;
			try {
				rs = query("SELECT * FROM user WHERE name = '"+usernameField.getText()+"' AND password = '"+passwordField.getText()+"'");
			if(rs.isBeforeFirst())
			{
				AlertBox.display("Logged in", "You successfully logged into the File Card System.");
			}
			else
			{
				rs = query("SELECT * FROM user WHERE name = '"+usernameField.getText()+"'");
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
		
		public void dbRegisterQuery()
		{
			// dbRegister query needs to be implemented
		}
	}


