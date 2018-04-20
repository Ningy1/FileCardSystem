
	

	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Statement;

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

	}

