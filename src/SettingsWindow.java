import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.rowset.JdbcRowSet;
import com.sun.rowset.JdbcRowSetImpl;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SettingsWindow extends PictureRowSetListener{
	
	private GridPane gridPane;
	private GridPane navigation;
	private GridPane page;
	private Button btnBack;
	private Button btnSavePrf;
	private Button btnSaveAcc;
	private Button btnPrf;
	private Button btnAcc;
	private Button btnPic;
	private Button btnUpload;
	private Button btnReset;
	private Button btnResetLvl;
	private Button btnDelAcc;
	private Label defProfile;
	private Label defAcc;
	private Label defPic;
	private Label catProfile;
	private Label catAccount;
	private Label catReset;
	private Label catPic;
	private Label basicInformation;
	private Label basicInformation1;
	private Label basicInformation2;
	private Label lvlHeader;
	private Label delAccHeader;
	private Text lvlText;
	private Text delAccText;
	private TextField fName;
	private TextField lName;
	private TextField emailField;
	private TextField usernameField;
	private PasswordField oldpassword;
	private PasswordField newpassword1;
	private PasswordField newpassword2;
	private Circle cir = null;
	private Line divLine;
	private Line divLine1;
	private Image pPicture;
	private ImageView imageView;
	private BufferedImage im = null;
	private FileChooser filechooser = new FileChooser();
	private ResultSet rs;
	private JdbcRowSet rowSetPicture;
	private JdbcRowSet rowSetPicture2;
	private JdbcRowSet rowSetName;
	private PictureRowSetListener listenerPicture = new PictureRowSetListener();
	private String firstname1;
	private String lastname1;
	private String name;
	private int counterPicture;
	private Stage loginStage;
	private Stage primaryStage;
	private Scene loginScene;

	SettingsWindow(Stage primaryStage, UserInterface ui) {
		
		this.loginScene = ui.getLoginScene();
		this.loginStage = ui.getLoginStage();
		this.primaryStage = primaryStage;
		
		primaryStage.setTitle("Settings");
		gridPane = new GridPane();
		gridPane.setId("pane2");
		
		//nodes of window
		btnBack = new Button("Back");
		btnBack.setPrefSize(115, 45);
		btnBack.setId("buttonSettings");
		
		navigation = addNavigation();
		page = addPage(primaryStage);
	
		
		//Set the ColumnContraints and add them
		ColumnConstraints column0 = new ColumnConstraints();
		column0.setPercentWidth(12);
		column0.setHgrow(Priority.ALWAYS);
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(20);
		column1.setHgrow(Priority.ALWAYS);
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(68);
		column2.setHgrow(Priority.ALWAYS);
		
		gridPane.getColumnConstraints().addAll(column0,column1,column2);
		
		//Set the RowConstraints and add them
		RowConstraints row0 = new RowConstraints();
		row0.setPercentHeight(10);
		row0.setVgrow(Priority.ALWAYS);
		RowConstraints row1 = new RowConstraints();
		row1.setPercentHeight(90);
		row1.setVgrow(Priority.ALWAYS);
		
		gridPane.getRowConstraints().addAll(row0,row1);
		
		//Eventhandler
		
		btnBack.setOnAction(e -> {		
			primaryStage.close();
			new UserInterface(ui.getLoginStage(), ui.getName(), ui.getLoginScene());
		});
		
		//GridPane handling
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(0, 0, 10, 0));
		
		try {
			
			rowSetPicture = new JdbcRowSetImpl(HSQLDB.con);
			rowSetPicture.setType(ResultSet.TYPE_FORWARD_ONLY);
			rowSetPicture.setCommand("SELECT * FROM Picture WHERE UserId = " + Login.userID);
			rowSetPicture.execute();
			rowSetPicture.setAutoCommit(true);
			rowSetPicture.addRowSetListener(listenerPicture);
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			rowSetName = new JdbcRowSetImpl(HSQLDB.con);
			rowSetName.setCommand("Select * FROM USER WHERE UserId = " + Login.userID);
			rowSetName.execute();
			rowSetName.addRowSetListener(listenerPicture);
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		GridPane.setConstraints(navigation,1,1,1,1,HPos.CENTER,VPos.CENTER);
		GridPane.setConstraints(page,2,1,1,1,HPos.CENTER,VPos.CENTER);
		
		gridPane.getChildren().setAll(navigation,page);
		//gridPane.setGridLinesVisible(true);
		
		
		//listen to booleanProperty in PictureRowSetListener
		listenerPicture.booleanProperty.addListener((p, o, n) ->{
			if(listenerPicture.booleanProperty.get()) {
				
				//remove old instance of navigation and page
				gridPane.getChildren().remove(navigation);
				gridPane.getChildren().remove(page);
				//if upload of picture was successful reload the data of the program
				GridPane navigationNew = addNavigation();
				GridPane pageNew = addPage(primaryStage);
				GridPane.setConstraints(navigationNew,1,1,1,1,HPos.CENTER,VPos.CENTER);
				GridPane.setConstraints(pageNew,2,1,1,1,HPos.CENTER,VPos.CENTER);
				gridPane.getChildren().setAll(navigationNew,pageNew);
				
				
			//reset the value of booleanProperty
			listenerPicture.booleanProperty.set(false);
			}
		});
		
		primaryStage.setOnCloseRequest(e -> {
			e.consume();
			
			boolean answer = ConfirmBox.display("Confirmation", "Are you sure you want to quit?");
			if(answer)
			{
				primaryStage.close();
				
			}
		});
		
		Scene sceneProfile = new Scene(gridPane,1000,600);
		sceneProfile.getStylesheets().addAll(this.getClass().getResource("Style.css").toExternalForm());
		primaryStage.setScene(sceneProfile);
		
	}
	

	private GridPane addNavigation(){
        
		GridPane layoutNavigation = new GridPane();
		layoutNavigation.setId("pane2");
		
		// Nodes of the navigation bar
		
		try {
			//check if a picture already exist
			rs = HSQLDB.getInstance().query("SELECT 1 FROM Picture WHERE UserID = " + Login.userID);
			while(rs.next()) {
				counterPicture=rs.getInt(1);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if(counterPicture!=0) {
				try {
					rs = HSQLDB.getInstance().query("SELECT Image FROM Picture WHERE UserID = " + Login.userID);
					while (rs.next()) {
						im = ImageIO.read(rs.getBinaryStream("Image"));
					}
					
					im = resizeImage(im,150,150);
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}		
		}
		
		if(im!=null) {
			 cir = new Circle(150,150,70);
		     cir.setStroke(Color.BLACK);
		     pPicture = SwingFXUtils.toFXImage(im, null);
		     cir.setFill(new ImagePattern(pPicture));
		     
		} else {
			cir = new Circle(150,150,70);
			cir.setStroke(Color.BLACK);
			pPicture = new Image(SettingsWindow.class.getResourceAsStream("Images/no-profile-pic.jpg"),150,150,false,false);
			cir.setFill(new ImagePattern(pPicture));
		}
		
		try {
			rs = HSQLDB.getInstance().query("SELECT Firstname FROM User WHERE UserID = " + Login.userID);
			while (rs.next()) {
				firstname1=rs.getString("Firstname");
			}
			
			rs = HSQLDB.getInstance().query("SELECT Lastname FROM User WHERE UserID = " + Login.userID);
			while (rs.next()) {
				lastname1=rs.getString("Lastname");
			}
			
			name = firstname1 + " " + lastname1;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	    Text title = new Text(name);
	    title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
	     
	    Text title1 = new Text("Categories");
	    title1.setFont(Font.font("Arial", FontWeight.BOLD, 16));
	     
	    btnPrf = new Button("Profile");
	    btnPrf.setPrefSize(198, 30);
	    btnPrf.setMaxSize(298, 30);
	    btnPrf.setId("buttonSettingsNavigation");
	        
	    btnPic = new Button("Picture");
	    btnPic.setPrefSize(198,30);
	    btnPic.setMaxSize(298, 30);
	    btnPic.setId("buttonSettingsNavigation");
	        
	    btnAcc = new Button("Account");
	    btnAcc.setPrefSize(198,30);
	    btnAcc.setMaxSize(298, 30);
	    btnAcc.setId("buttonSettingsNavigation");
	    
	    btnReset = new Button("Reset");
	    btnReset.setPrefSize(198, 30);
	    btnReset.setMaxSize(298, 30);
	    btnReset.setId("buttonSettingsNavigation");
		
		//Set the ColumnContraints and add them
		ColumnConstraints column0 = new ColumnConstraints();
		column0.setPercentWidth(100);
		column0.setHgrow(Priority.ALWAYS);
		
		layoutNavigation.getColumnConstraints().addAll(column0);
		
		//Set the RowConstraints and add them
		RowConstraints row0 = new RowConstraints(155,155,160);
		row0.setVgrow(Priority.ALWAYS);
		RowConstraints row1 = new RowConstraints(60,60,60);
		row1.setVgrow(Priority.ALWAYS);
		RowConstraints row2 = new RowConstraints(30,30,30);
		row2.setVgrow(Priority.ALWAYS);
		RowConstraints row3 = new RowConstraints(30,30,30);
		row2.setVgrow(Priority.ALWAYS);
		RowConstraints row4 = new RowConstraints(30,30,30);
		row4.setVgrow(Priority.ALWAYS);
		RowConstraints row5 = new RowConstraints(30,30,30);
		row5.setVgrow(Priority.ALWAYS);
		RowConstraints row6 = new RowConstraints(30,30,30);
		row6.setVgrow(Priority.ALWAYS);
		RowConstraints row7 = new RowConstraints(60,60,Double.MAX_VALUE);
		row7.setVgrow(Priority.ALWAYS);
		
		layoutNavigation.getRowConstraints().addAll(row0,row1,row2,row3,row4,row5,row6);
		
		//GridPane handling
		layoutNavigation.setHgap(5);
		layoutNavigation.setVgap(5);
		layoutNavigation.setPadding(new Insets(0,0,0,2));
						
		GridPane.setConstraints(cir,0,0,1,2,HPos.CENTER,VPos.TOP); 
		GridPane.setConstraints(title,0,0,1,1,HPos.CENTER,VPos.BOTTOM);
		GridPane.setConstraints(title1,0,2,1,1,HPos.LEFT,VPos.CENTER);
		GridPane.setConstraints(btnPrf,0,3,1,1,HPos.LEFT,VPos.CENTER);
		GridPane.setConstraints(btnPic,0,4,1,1,HPos.LEFT,VPos.CENTER);
		GridPane.setConstraints(btnAcc,0,5,1,1,HPos.LEFT,VPos.CENTER);
		GridPane.setConstraints(btnReset,0,6,1,1,HPos.LEFT,VPos.CENTER);
		
		layoutNavigation.getChildren().addAll(cir,title,title1,btnPrf,btnPic,btnAcc,btnReset);
		//layoutNavigation.setGridLinesVisible(true); // debugging
		
		return layoutNavigation;
    }
	
	private GridPane addPage(Stage stage) {
		
		GridPane layoutPage = new GridPane();
		layoutPage.setId("pane2");
		
		//Set the ColumnContraints and add them
		ColumnConstraints column0 = new ColumnConstraints();
		column0.setPercentWidth(15);
		column0.setHgrow(Priority.ALWAYS);
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(70);
		column1.setHgrow(Priority.ALWAYS);
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(15);
		column2.setHgrow(Priority.ALWAYS);
		
		layoutPage.getColumnConstraints().addAll(column0,column1,column2);
		
		//Set the RowConstraints and add them
		RowConstraints row0 = new RowConstraints(80,80,80);
		row0.setVgrow(Priority.ALWAYS);
		RowConstraints row1 = new RowConstraints(15,15,30);
		row1.setVgrow(Priority.ALWAYS);
		RowConstraints row2 = new RowConstraints(65,65,65);
		row2.setVgrow(Priority.ALWAYS);
		RowConstraints row3 = new RowConstraints(65,65,65);
		row3.setVgrow(Priority.ALWAYS);
		RowConstraints row4 = new RowConstraints(65,65,65);
		row4.setVgrow(Priority.ALWAYS);
		RowConstraints row5 = new RowConstraints(40,40,40);
		row5.setVgrow(Priority.ALWAYS);
		RowConstraints row6 = new RowConstraints(40,40,40);
		row6.setVgrow(Priority.ALWAYS);
		RowConstraints row7 = new RowConstraints(80,100,Double.MAX_VALUE);
		row7.setVgrow(Priority.ALWAYS);
		
		layoutPage.getRowConstraints().addAll(row0,row1,row2,row3,row4,row5,row6,row7);
		
		//GridPane handling
		layoutPage.setHgap(5);
		layoutPage.setVgap(5);
		layoutPage.setPadding(new Insets(0));
		
		btnPrf.setOnAction(event -> {
				// create nodes of Profile
				catProfile = new Label("Profile");
		    	catProfile.setId("header");
		        
		        defProfile = new Label("Add information about yourself.");
		        defProfile.setId("label");
		        
		        basicInformation = new Label("Prename: ");
		        basicInformation.setId("label");
		        
		        basicInformation1 = new Label("Surname: ");
		        basicInformation1.setId("label");

		        fName = new TextField();
		        fName.setId("text-field");
		        fName.setPromptText("First Name");
		        fName.setMaxSize(680, 40);
		        
		        try {
					rs = HSQLDB.getInstance().query("SELECT Firstname FROM User WHERE UserID = " + Login.userID);
					while (rs.next()) {
			            String firstname = rs.getString("Firstname");
			            fName.textProperty().set(firstname);
			        }
				} catch (Exception e) {
					e.printStackTrace();
				}
		        
		        lName = new TextField();
		        lName.setId("text-field");
		        lName.setPromptText("Last Name");
		        lName.setMaxSize(680, 40);
		        
		        try {
					rs = HSQLDB.getInstance().query("SELECT Lastname FROM User WHERE UserID = " + Login.userID);
					while (rs.next()) {
			            String lastname = rs.getString("Lastname");
			            lName.textProperty().set(lastname);
			        }
				} catch (Exception e) {
					e.printStackTrace();
				}
		        
		        btnSavePrf = new Button("Save");
				btnSavePrf.setPrefSize(115, 45);
				btnSavePrf.setId("buttonSettings");
				
				//Event handler
				btnSavePrf.setOnAction(e ->{
					
					String newfName=fName.getText();
					String newlName=lName.getText();
					
					try {
						while (rowSetName.next()) {
							if(rowSetName.getInt("UserID")==Login.userID) {
								rowSetName.updateString(3, newfName);
								rowSetName.updateString(4,newlName);
								rowSetName.updateRow();
							}		
						}
						rowSetName.beforeFirst();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					
				});
				
				//Gridpane handling
				layoutPage.getChildren().clear();
				GridPane.setConstraints(catProfile,1,0,1,1,HPos.CENTER,VPos.CENTER); 
				GridPane.setConstraints(defProfile,1,0,1,1,HPos.CENTER,VPos.BOTTOM); 
				GridPane.setConstraints(basicInformation,1,2,1,1,HPos.LEFT,VPos.TOP); 
				GridPane.setConstraints(fName,1,2,1,1,HPos.CENTER,VPos.BOTTOM); 
				GridPane.setConstraints(basicInformation1,1,3,1,1,HPos.LEFT,VPos.TOP); 
				GridPane.setConstraints(lName,1,3,1,1,HPos.CENTER,VPos.BOTTOM); 
				GridPane.setConstraints(btnSavePrf,1,7,1,1,HPos.RIGHT,VPos.CENTER);
				GridPane.setConstraints(btnBack,1,7,1,1,HPos.LEFT,VPos.CENTER);
				layoutPage.getChildren().addAll(catProfile,defProfile,basicInformation,fName,basicInformation1,lName,btnSavePrf,btnBack);
	    });
		
		btnAcc.setOnAction( e -> {
				// create nodes of Account 
				catAccount = new Label("Account");
		    	catAccount.setId("header");
		    	
		    	defAcc = new Label("Edit your account settings.");
		        defAcc.setId("label");
		        
		        basicInformation = new Label("Password: ");
			    basicInformation.setId("label");
		          
		        oldpassword = new PasswordField();
		        oldpassword.setId("text-field");
		        oldpassword.setPromptText("Enter Current Password");
		        oldpassword.setMaxSize(680, 40);
		        
		        newpassword1 = new PasswordField();
		        newpassword1.setId("text-field");
		        newpassword1.setPromptText("Enter New password");
		        newpassword1.setMaxSize(680, 40);
		        
		        newpassword2 = new PasswordField();
		        newpassword2.setId("text-field");
		        newpassword2.setPromptText("Re-type New password");
		        newpassword2.setMaxSize(680, 40);
		        
		    	basicInformation1 = new Label("Username: ");
			    basicInformation1.setId("label");
			    
			    usernameField = new TextField();
		        usernameField.setId("text-field");
		        usernameField.setPromptText("Username");
		        usernameField.setMaxSize(680, 40);
		        
		        try {
					rs = HSQLDB.getInstance().query("SELECT Username FROM User WHERE UserID = " + Login.userID);
					while (rs.next()) {
			            String userName = rs.getString("Username");
			            usernameField.textProperty().set(userName);
			        }
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			    
		    	basicInformation2 = new Label("Email: ");
			    basicInformation2.setId("label");
			    
			    emailField = new TextField();
		        emailField.setId("text-field");
		        emailField.setPromptText("Email");
		        emailField.setMaxSize(680, 40);
		        
		        try {
					rs = HSQLDB.getInstance().query("SELECT Email FROM User WHERE UserID = " + Login.userID);
					while (rs.next()) {
			            String eMail = rs.getString("Email");
			            emailField.textProperty().set(eMail);
			        }
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		        
		        btnSaveAcc = new Button("Save");
				btnSaveAcc.setPrefSize(115, 45);
				btnSaveAcc.setId("buttonSettings");
		        
				//Event handler
				btnSaveAcc.setOnAction(event ->{
					
					String oldUsername;
					String newUsername = usernameField.getText();
					String newmail = emailField.getText();
					String oldpw= oldpassword.getText();
					String newpw1 = newpassword1.getText();
					String newpw2 = newpassword2.getText();
					String actualPW=null;
					int userExists = 0;
					
					try {
						rs = HSQLDB.getInstance().query("SELECT 1 FROM user WHERE Username = '"+newUsername+"'");
						while(rs.next()) {
							userExists=rs.getInt(1);
						}
						
						if(userExists == 1) {
							
							rs = HSQLDB.getInstance().query("SELECT Username FROM User WHERE Userid = " + Login.userID);
							while(rs.next()) {
								oldUsername=rs.getString("Username");
								
								if (!newUsername.equals(oldUsername)) {
									AlertBox.display("Error", "Username already exists.");
									usernameField.clear();
								}
							}
							
						}else {
							if(newUsername.matches("^[a-zA-Z].*")) {
								HSQLDB.getInstance()
								.update("UPDATE user " + "SET Username = '" + newUsername + "' WHERE UserId = " + Login.userID);
								AlertBox.display("Congrat", "Username changed to " + newUsername);
							}
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					
				
					try {
						if(emailField.getText().matches("^[a-zA-Z].*")) {
									
							if(emailField.getText().matches("[0-9a-zA-Z\\-\\_\\.]+@[0-9a-zA-Z]+\\.[a-z]{2,3}")) {
								HSQLDB.getInstance()
								.update("UPDATE user " + "SET Email = '" + newmail + "' WHERE UserId = " + Login.userID);
								
							}else {
								emailField.setText("Insert a valid email address");
							}
							
						}else {
							emailField.setText("Must start with a letter");
						}
					
								
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					if(oldpw.length()>0){	
						
						if(oldpw.length()>0 && newpw1.length()>7 || newpw2.length()>7) {
							try {
								rs = HSQLDB.getInstance().query("SELECT Password FROM User WHERE UserID = " + Login.userID);
								while (rs.next()) {
					            actualPW = rs.getString("Password");
								}
					
								if(oldpw.equals(actualPW)) {
									if(newpw1.equals(newpw2) && newpw1.length()>0) {
									
										HSQLDB.getInstance()
										.update("UPDATE user " + "SET Password = '" + newpw1 + "' WHERE UserId = " + Login.userID);
										AlertBox.display("Congrat", "Password changed");
										oldpassword.clear();
										newpassword1.clear();
										newpassword2.clear();
									}else {
										AlertBox.display("Error", "Password has to be identical in both fields");
										oldpassword.clear();
										newpassword1.clear();
										newpassword2.clear();
									}
								}else {
									AlertBox.display("Error", "Wrong Password");
									oldpassword.clear();
									newpassword1.clear();
									newpassword2.clear();
								}
							} catch (SQLException e1) {
								e1.printStackTrace();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							
						}else {
						AlertBox.display("Error", "The password must be at least 8 characters");
						oldpassword.clear();
						newpassword1.clear();
						newpassword2.clear();
						}
					}
				});
				
				// Gridpane handling
				layoutPage.getChildren().clear();
				GridPane.setConstraints(catAccount,1,0,1,1,HPos.CENTER,VPos.CENTER);
				GridPane.setConstraints(defAcc,1,0,1,1,HPos.CENTER,VPos.BOTTOM); 
				GridPane.setConstraints(basicInformation1,1,2,1,1,HPos.LEFT,VPos.TOP);
				GridPane.setConstraints(usernameField,1,2,1,1,HPos.CENTER,VPos.BOTTOM);
				GridPane.setConstraints(basicInformation2,1,3,1,1,HPos.LEFT,VPos.TOP);
				GridPane.setConstraints(emailField,1,3,1,1,HPos.CENTER,VPos.BOTTOM);
				GridPane.setConstraints(basicInformation,1,4,1,1,HPos.LEFT,VPos.TOP);
				GridPane.setConstraints(oldpassword,1,4,1,1,HPos.CENTER,VPos.BOTTOM); 
				GridPane.setConstraints(newpassword1,1,5,1,1,HPos.CENTER,VPos.TOP);
				GridPane.setConstraints(newpassword2,1,6,1,1,HPos.CENTER,VPos.TOP);
				GridPane.setConstraints(btnSaveAcc,1,7,1,1,HPos.RIGHT,VPos.CENTER); 
				GridPane.setConstraints(btnBack,1,7,1,1,HPos.LEFT,VPos.CENTER);
				layoutPage.getChildren().addAll(catAccount,defAcc,basicInformation1,usernameField,basicInformation2,
						emailField,basicInformation,oldpassword,newpassword1,newpassword2,btnSaveAcc,btnBack);
		});
		
		btnPic.setOnAction(event -> {
			
				//create nodes of Picture
				
				catPic = new Label("Picture");
				catPic.setId("header");
	    	
				defPic = new Label("Upload a profil picture.");
				defPic.setId("label");
			
				btnUpload = new Button("Upload");
				btnUpload.setId("buttonSettings");
			    btnUpload.setPrefSize(115, 45);
			    
			    try {
					//check if a picture already exist
					rs = HSQLDB.getInstance().query("SELECT 1 FROM Picture WHERE UserID = " + Login.userID);
					while(rs.next()) {
						counterPicture=rs.getInt(1);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				if(counterPicture!=0) {
						try {
							rs = HSQLDB.getInstance().query("SELECT Image FROM Picture WHERE UserID = " + Login.userID);
							while (rs.next()) {
								im = ImageIO.read(rs.getBinaryStream("Image"));
							}
							
							im = resizeImage(im,400,300);
							
						} catch (SQLException e1) {
							e1.printStackTrace();
						} catch (Exception e1) {
							e1.printStackTrace();
						}		
				}
				
				if(im!=null) {
					
					pPicture = SwingFXUtils.toFXImage(im, null);
					imageView = new ImageView(pPicture);
				     
				} else {
					pPicture = new Image(SettingsWindow.class.getResourceAsStream("Images/no-profile-pic.jpg"),300,300,false,false);
					imageView = new ImageView(pPicture);
				}
			   
			    
			    //Event handler
				btnUpload.setOnAction(f -> {
					
					File picture = filechooser.showOpenDialog(stage);
			
					if(picture!=null) {
						
						try {
							
							rs = HSQLDB.getInstance().query("SELECT 1 FROM Picture WHERE UserID = " + Login.userID);
							while(rs.next()) {
								counterPicture=rs.getInt(1);
							}
						} catch (SQLException e1) {
							e1.printStackTrace();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						
						try {
							String mimetype = Files.probeContentType(picture.toPath());
							// upload only if the file is a picture
							if (mimetype != null && mimetype.split("/")[0].equals("image")) {
								
								FileInputStream fis = new FileInputStream(picture);
								//Upload picture for first time
								//rowSetPicture.absolute(1);
								
								try {
									
									rowSetPicture2 = new JdbcRowSetImpl(HSQLDB.con);
									rowSetPicture2.setCommand("SELECT * FROM Picture WHERE UserId = " + Login.userID);
									rowSetPicture2.setReadOnly(false);
									rowSetPicture2.execute();
									rowSetPicture2.addRowSetListener(listenerPicture);
									
								} catch (SQLException e2) {
									e2.printStackTrace();
								}
								
							
								if(counterPicture==0){
									try {
										rowSetPicture.moveToInsertRow();
										rowSetPicture.updateInt(1,Login.userID);
										rowSetPicture.updateBinaryStream(2, (InputStream)fis, (int)picture.length());
										rowSetPicture.insertRow();
										rowSetPicture.moveToCurrentRow();
										
										//start the event of btnPic 
										btnPic.fire();
									} catch (SQLException e1) {
										e1.printStackTrace();
									}
								}else{
									
									rowSetPicture2.absolute(1);
									rowSetPicture2.updateInt(1, Login.userID);
									rowSetPicture2.updateBinaryStream(2, (InputStream)fis, (int)picture.length());
									rowSetPicture2.updateRow();	
									
									//start the event of btnPic 
									btnPic.fire();
								}
							}else {
								AlertBox.display("Error", "The specified file could not be uploaded.\nOnly JPEG and PNG image formats can be used.");
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
					
				});
			    
				//Gridpane handling
				layoutPage.getChildren().clear();
				GridPane.setConstraints(catPic,1,0,1,1,HPos.CENTER,VPos.CENTER);
				GridPane.setConstraints(defPic,1,0,1,1,HPos.CENTER,VPos.BOTTOM); 
				GridPane.setConstraints(imageView,1,6,1,1,HPos.CENTER,VPos.BOTTOM); 
				GridPane.setConstraints(btnUpload,1,7,1,1,HPos.RIGHT,VPos.CENTER);
				GridPane.setConstraints(btnBack,1,7,1,1,HPos.LEFT,VPos.CENTER);
				layoutPage.getChildren().addAll(catPic,defPic,imageView,btnUpload,btnBack);

		});
		
		btnReset.setOnAction(e ->{
			
			//create nodes of Reset 
			catReset = new Label("Reset");
	    	catReset.setId("header");
			
	    	lvlHeader = new Label("Reset your testing level");
	    	lvlHeader.setId("label");
	    	
	    	lvlText = new Text("Warning: If you reset your testing level, \nyou will lose your progress in testing.");
	    	lvlText.setId("Text");
	    	
	    	delAccHeader = new Label("Delete my Account");
	    	delAccHeader.setId("label");
	    	
	    	delAccText = new Text("Warning: If you delete your account, you will not be able to reactive your account\n"
	    						+ "or retrive any of information you have added.");
	    	delAccText.setId("Text");
	    	
	    	btnDelAcc = new Button("Delete Account");
	    	btnDelAcc.setPrefSize(100, 30);
	    	btnDelAcc.setId("buttonSettingsSmall");
	    	
	    	btnResetLvl = new Button("Reset Level");
	    	btnResetLvl.setPrefSize(100, 30);
	    	btnResetLvl.setId("buttonSettingsSmall");
	    	
	    	divLine = new Line();
	    	divLine.setStartX(100);
	    	divLine.setStartY(150);
	    	divLine.setEndX(550);
	    	divLine.setEndY(150);
	    	
	    	divLine1 = new Line();
	    	divLine1.setStartX(100);
	    	divLine1.setStartY(150);
	    	divLine1.setEndX(550);
	    	divLine1.setEndY(150);
	    
			//Event handler
	    	

	    	btnDelAcc.setOnAction(event -> {
	    		
	    	
				
				boolean answer = ConfirmBox.display("", "Are you sure you want to delete your account?");
				if(answer) {
					
					int id = Login.userID;
		    		
		    		loginStage.setScene(loginScene);
					primaryStage.close();
					loginStage.show();
					
					try {
						HSQLDB.getInstance().update("DELETE FROM Translate "
												+ "WHERE WordID1 in (SELECT WordID " 
												+ "FROM Words WHERE UserID = " + id + ")");
						
						HSQLDB.getInstance().update("DELETE FROM Definition "
												+ "WHERE WordID in (select WordID FROM Words WHERE UserID = " + id + ")");
					
						HSQLDB.getInstance().update("DELETE FROM Words "
												+ "WHERE UserID = " + id);
					
						HSQLDB.getInstance().update("DELETE FROM Picture "
												+ "WHERE UserID = " + id);
					
						HSQLDB.getInstance().update("DELETE FROM User "
												+ "WHERE UserID = " + id);
					
					
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
	    	});
	    	
	    	btnResetLvl.setOnAction(event -> {
	    		
	    		boolean answer = ConfirmBox.display("", "Are you sure you want to reset the levels?");
				if(answer) {
					try {
						HSQLDB.getInstance().update("UPDATE Translate "
								+ "SET Level = " + 1 
								+ " WHERE WordID1 in (SELECT WordID " 
								+ "FROM Words WHERE UserID = " + Login.userID + ")");
					
						HSQLDB.getInstance().update("UPDATE Definition "
								+ "SET Level = " + 1 
								+ " WHERE WordID in (SELECT WordID " 
								+ "FROM Words WHERE UserID = " + Login.userID + ")");
					
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
	    	});
			
			//Gridpane handling 
			layoutPage.getChildren().clear();
			GridPane.setConstraints(catReset,1,0,1,1,HPos.CENTER,VPos.CENTER);
			GridPane.setConstraints(lvlHeader,1,2,1,1,HPos.LEFT,VPos.TOP);
			GridPane.setConstraints(lvlText,1,2,1,1,HPos.LEFT,VPos.BOTTOM);
			GridPane.setConstraints(btnResetLvl,1,3,1,1,HPos.LEFT,VPos.TOP);
			GridPane.setConstraints(divLine,1,4,1,1,HPos.LEFT,VPos.TOP);
			GridPane.setConstraints(delAccHeader,1,4,1,1,HPos.LEFT,VPos.BOTTOM);
			GridPane.setConstraints(delAccText,1,5,1,1,HPos.LEFT,VPos.CENTER);
			GridPane.setConstraints(btnDelAcc,1,6,1,1,HPos.LEFT,VPos.TOP);
			GridPane.setConstraints(divLine1,1,7,1,1,HPos.LEFT,VPos.TOP);
			GridPane.setConstraints(btnBack,1,7,1,1,HPos.CENTER,VPos.CENTER);
			layoutPage.getChildren().addAll(catReset,lvlHeader,lvlText,btnResetLvl,divLine,
					delAccHeader,delAccText,btnDelAcc,divLine1,btnBack);
		});
		
		
		if(!btnAcc.isPressed()&&!btnPic.isPressed()) {
			//start the event of btnPic
			btnPrf.fire();
		}
		
		//layoutPage.setGridLinesVisible(true);
		return layoutPage;
		
	}
	
	static public BufferedImage resizeImage(BufferedImage original, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height ,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        float xScale = (float)width / original.getWidth();
        float yScale = (float)height / original.getHeight();
        AffineTransform at = AffineTransform.getScaleInstance(xScale,yScale);
        g.drawRenderedImage(original,at);
        g.dispose();
        return resizedImage;
	}
	
}