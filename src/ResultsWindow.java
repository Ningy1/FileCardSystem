import java.sql.ResultSet;
import javafx.scene.control.Tab;
import java.sql.SQLException;
import javafx.scene.control.TabPane;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.chart.*;



public class ResultsWindow {
	
	private Button btnBack;
	private Tab tabOverview;
	private Tab tabSuccess;
	
	private BarChart<String,Number> barchart;
	private GridPane gridPane;
	private TabPane tabPane;
	
	private CategoryAxis xAxis;
	private NumberAxis yAxis;
	
	private String box1 = "Box 1";
	private String box2 = "Box 2";
	private String box3 = "Box 3";
	private String box4 = "Box 4";
	private String box5 = "Box 5";
	private String box6 = "Box 6";
	private ResultSet rs = null;
	
	//need this parameters to come back to the User Interface-Scene
	ResultsWindow(Stage primaryStage, Scene uiScene) {
		
		primaryStage.setTitle("Results");
		
		gridPane = new GridPane();
		gridPane.setId("pane");
		
		btnBack = new Button("Back");
		btnBack.setPrefSize(70,50);
		
		//TabPane and Tab handling
		tabPane = new TabPane();
		tabOverview = new Tab("Overview");
		tabSuccess = new Tab("Success");
		
		tabPane.getTabs().addAll(tabOverview,tabSuccess);
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
		
		//Barchart Handling
	    xAxis = new CategoryAxis();
	    yAxis = new NumberAxis();
	    
	    barchart = new BarChart<String,Number>(xAxis, yAxis);
		
	    barchart.setTitle("Overview");
	    xAxis.setLabel("Card file boxes");
	    yAxis.setLabel("Total number of Cards");
	    
	    //add the barchart in the Tab "Overview"
	    tabOverview.setContent(barchart);
	   
	    tabPane.setStyle("-fx-background-color: grey");
	    
	    
	    //Create a Serie of bars and add the bars in these Serie
	    XYChart.Series boxSeries1 = new XYChart.Series(); 

	    
	    
	    /* Code für später. Ggf. nachschauen um vorher die Zeilen der Tabelle zu bestimmen
	     * damit bei der while-schleife keine Exception geschmissen wird.n.
	     * 
	     * 
	     * try {
	     * for(x=1;x<7;x++) {
	      rs = HSQLDB.getInstance().query("Select count(*) FROM Box wehre ID="+x);
	     * while(rs.next()) {
	     * 
	     * boxSeries1.getData().add(new XYChart.Data<>(rs.getString(1))
	     * 
	     * }
	     * }
	     * barchart.getData().addAll(boxSeries);
	     * 
	     */
	    
	   // boxSeries1.setName("box 1");
	    
	    
	    try {
	    	rs = HSQLDB.getInstance().query("Select count(*) FROM FILECARDS");
	    	ResultSet result = HSQLDB.getInstance().query("Select count(*) FROM USER");
	    	if(rs.next()) {
	    	int a = rs.getInt(1);
	    	boxSeries1.getData().add(new XYChart.Data(box1, a));
	    	
	    	}
	    	if(result.next()) {
	    	int b =result.getInt(1);
	    	
	    	 boxSeries1.getData().add(new XYChart.Data(box2, b));
	    	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    barchart.getData().addAll(boxSeries1);
	   
			
	    //GridPane handling

		gridPane.setHgap(10);
		gridPane.setVgap(10);
		
		GridPane.setConstraints(btnBack,0,0,2,1); //First row
		GridPane.setHalignment(btnBack, HPos.LEFT);
		GridPane.setConstraints(tabPane,1,1,1,8);
		
		
		gridPane.getChildren().addAll(btnBack,tabPane);
		
		//Set the ColumnContraints and add them

		ColumnConstraints column0 = new ColumnConstraints(15,50,250);
		ColumnConstraints column1 = new ColumnConstraints(60,65,Double.MAX_VALUE);
		ColumnConstraints column2 = new ColumnConstraints(30,30,30);
		
		//Both Columns have the same priority
		column0.setHgrow(Priority.ALWAYS);
		column1.setHgrow(Priority.ALWAYS);
		column2.setHgrow(Priority.ALWAYS);
		
		gridPane.getColumnConstraints().addAll(column0,column1,column2);
		
		
		//Set the RowConstraints and add them
		
		//old Values 10,75,75
		RowConstraints row1 = new RowConstraints(40,40,40);
		RowConstraints row2 = new RowConstraints(0,20,Double.MAX_VALUE);
		RowConstraints row3 = new RowConstraints(10,60,60);
		RowConstraints row4 = new RowConstraints(10,60,60);
		RowConstraints row5 = new RowConstraints(10,60,60);
		RowConstraints row6 = new RowConstraints(10,60,60);
		RowConstraints row7 = new RowConstraints(10,60,60);
		RowConstraints row8 = new RowConstraints(30,60,Double.MAX_VALUE);
		
		
		//Every row have the same priority
		
		row1.setVgrow(Priority.ALWAYS);
		row2.setVgrow(Priority.ALWAYS);
		row3.setVgrow(Priority.ALWAYS);
		row4.setVgrow(Priority.ALWAYS);
		row5.setVgrow(Priority.ALWAYS);
		row6.setVgrow(Priority.ALWAYS);
		row7.setVgrow(Priority.ALWAYS);
		row8.setVgrow(Priority.ALWAYS);
		
		gridPane.getRowConstraints().addAll(row1,row2,row3,row4,row5,row6,row7,row8);
		
        
    	Scene cssStyle = new Scene(gridPane,1000,600);
		cssStyle.getStylesheets().addAll(this.getClass().getResource("blank.css").toExternalForm());
		primaryStage.setScene(cssStyle);
		
		gridPane.setGridLinesVisible(true); //Debugging
		
		
		//Eventhandler	
		
		btnBack.setOnAction(e -> {
			
			primaryStage.setScene(uiScene);
				});
		}
	}
