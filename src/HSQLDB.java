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
		    
		         con = DriverManager.getConnection("jdbc:hsqldb:file:FileCardSystemDB;shutdown=true","SA","");
		        
			        
			         update("CREATE TABLE IF NOT EXISTS User ("
			         		+ "UserID INTEGER IDENTITY PRIMARY KEY NOT NULL, "
			         		+ "Firstname VARCHAR(80) NOT NULL, "
			         		+ "Lastname VARCHAR(80) NOT NULL, "
			         		+ "Password VARCHAR(80) not null)"
			         		);
			        
			         update("CREATE TABLE IF NOT EXISTS Words ("
				         		+ "WordID INTEGER IDENTITY PRIMARY KEY NOT NULL, "
				         		+ "Word VARCHAR(80) NOT NULL, "
				         		+ "Language VARCHAR(80) NOT NULL, "
				         		+ "UserID INTEGER not null,"
				         		+ "FOREIGN KEY (UserID) REFERENCES User (UserID) "
				           		+ "ON DELETE CASCADE "
				         		+ "ON UPDATE CASCADE)"
				         		);
			         update("CREATE TABLE IF NOT EXISTS Definition ("
				         		+ "DefinitionID INTEGER IDENTITY PRIMARY KEY NOT NULL, "
				         		+ "Definition VARCHAR(80) NOT NULL, "
				         		+ "WordID INTEGER NOT NULL, "
				         		+ "Level INTEGER, "
				         		+ "Totalcount INTEGER, "
				         		+ "Success INTEGER, "
				         		+ "FOREIGN KEY (WordID) REFERENCES Words (WordID) "
				         		+ "ON DELETE CASCADE "
				         		+ "ON UPDATE CASCADE, "
				         		+ "UNIQUE (Definition, WordID))"
				         		);
			         update("CREATE TABLE IF NOT EXISTS Translate ("
				         		+ "WordID1 INTEGER NOT NULL, "
				         		+ "WordID2 INTEGER NOT NULL, "
				         		+ "Level INTEGER, "
				         		+ "Totalcount INTEGER, "
				         		+ "Success INTEGER, "
				         		+ "PRIMARY KEY(WordID1, WORDID2), "
				         		+ "FOREIGN KEY (WordID1) REFERENCES Words (WordID) "
				         		+ "ON DELETE CASCADE "
				         		+ "ON UPDATE CASCADE, "
				         		+ "FOREIGN KEY (WordID2) REFERENCES Words (WordID) "
				         		+ "ON DELETE CASCADE "
				         		+ "ON UPDATE CASCADE) "
				         		);
				  
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
			int result = con.createStatement().executeUpdate(sqlStatement); 
			/*
			Statement statement = null;
			statement = con.createStatement();
			int result = statement.executeUpdate(sqlStatement); 
			statement.close();
			
			if (result != 0) {
				throw new SQLException("Error in expression: "+sqlStatement);
			}
			*/
			
		}
	
		
//////////////////////////////////////AUSTAUSCH DER METHODEN, VON DER MAIN ZU HSQLDB///////////////////////
	/*	public void dbLoginQuery(TextField usernameField, PasswordField passwordField)
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
		}*/
	}


