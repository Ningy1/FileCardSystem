import java.sql.ResultSet;
import javafx.scene.control.Tab;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.sun.javafx.charts.Legend;
import javafx.scene.Node;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import javafx.animation.Animation;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
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
import javafx.scene.chart.XYChart.Data;
import com.sun.javafx.charts.Legend;
import com.sun.javafx.charts.Legend.LegendItem;

public class ResultsWindow {
	
	private Button btnBack;

	private GridPane gridPane;
	private GridPane gridForTabVoc;
	private GridPane gridForTabDef;
	private TabPane tabPane;
	private Tab tabVocabulary;
	private Tab tabDefinition;
	private Tab tabLvlDef;
	private Tab tabLvlVoc;
	private ComboBox<String> cbLvlFilterVoc; // Filter the different Levels
	private ComboBox<String> cbLvlFilterDef; // Filter the different Levels
	private ObservableList<String> lvlFilter;
	private CategoryAxis xAxisVoc;
	private NumberAxis yAxisVoc;
	private CategoryAxis xAxisDef;
	private NumberAxis yAxisDef;
	private CategoryAxis xAxislvlDef;
	private NumberAxis yAxislvlDef;
	private CategoryAxis xAxislvlVoc;
	private NumberAxis yAxislvlVoc;
	
	private String[] diffLanguages = {"German","English","Spanish","French"};
	/*
	 * In this ArrayList will be added all entries from the user, which he adds 
	 * through the EditFileCard. After the add of the user, the method "doublerCheck" 
	 * checks if there a double language combination. All non double combination will be
	 * added in this Arraylist and later, after the Database request, the two strings will be
	 * concatenated
	 */
	private ArrayList<String> lvlLanguage1 = new ArrayList<String>();
	private ArrayList<String> lvlLanguage2 = new ArrayList<String>();
	
	//Holds the total Values for the counted different Sections
	//Every Arrayfield represent the level of the Vocabulary or Definition
	private ArrayList<Integer> barflowDefinition;
	private ArrayList<Integer> barflowVocabulary;
	ArrayList<ArrayList<Integer>> barflowLvlDef;
	ArrayList<ArrayList<Integer>> barflowLvlVoc;

	private HashMap<String,String> doublerCheck = new HashMap<String,String>();

	
	//need this parameters to come back to the User Interface-Scene
	ResultsWindow(Stage primaryStage, Scene uiScene) {
		//Login.userID=0;
		barflowDefinition = new ArrayList<Integer>();
		barflowVocabulary = new ArrayList<Integer>();
		//Define the size of the Levels for Definition and Vocabulary.
		//In our case we have 4 levels
		barflowLvlDef = new ArrayList<ArrayList<Integer>>();
		barflowLvlVoc = new ArrayList<ArrayList<Integer>>();
		//fill Array list with the Levels 1-4
		for(int i=0; i<4;i++) {
			barflowLvlDef.add(new ArrayList<Integer>());
			barflowLvlVoc.add(new ArrayList<Integer>());
		}
		
		//Level Filter for the Barchart of Vocabulary and Definition
		lvlFilter = FXCollections.observableArrayList
				("Level 1", "Level 2", "Level 3", "Level 4");
		//For the first initialisation. 0 is lvl 1, 1 is lvl 2 and so on
		int[] levels = {0,1,2,3};
		
		primaryStage.setTitle("Results");
		
		gridPane = new GridPane();
		gridPane.setId("paneTabPane");
		gridForTabVoc = new GridPane();
		gridForTabDef = new GridPane();
		
		btnBack = new Button("Back");
		btnBack.setPrefSize(100,70);
		btnBack.setId("button");
		
		tabPane = new TabPane();
		tabVocabulary = new Tab("Vocabulary");
		tabDefinition = new Tab("Definition");
		tabLvlVoc = new Tab("Level Vocabulary");
		tabLvlDef = new Tab("Level Definition");
		tabPane.setId("paneTabPane");
		
		
		cbLvlFilterVoc = new ComboBox<String>(lvlFilter);
		cbLvlFilterVoc.getSelectionModel().selectFirst();
		cbLvlFilterVoc.setId("select");
		
		cbLvlFilterDef = new ComboBox<String>(lvlFilter);
		cbLvlFilterDef.getSelectionModel().selectFirst();
		cbLvlFilterDef.setId("select");
		
		//Barchart Handling
	    xAxisVoc = new CategoryAxis();
	    yAxisVoc = new NumberAxis();
	    xAxisDef = new CategoryAxis();
	    yAxisDef = new NumberAxis();
	    xAxislvlDef = new CategoryAxis();
	    yAxislvlDef = new NumberAxis();
	    xAxislvlVoc = new CategoryAxis();
	    yAxislvlVoc = new NumberAxis();
	    
	    yAxisVoc.setAutoRanging(false);
	    yAxisVoc.setTickUnit(1);
	    yAxisVoc.setMinorTickVisible(true);
	    
	    yAxisDef.setAutoRanging(false);
	    yAxisDef.setTickUnit(1);
	    yAxisDef.setMinorTickVisible(true);
	    
	    yAxislvlDef.setAutoRanging(false);
	    yAxislvlDef.setTickUnit(1);
	    yAxislvlDef.setMinorTickVisible(true);
	    
	    yAxislvlVoc.setAutoRanging(false);
	    yAxislvlVoc.setTickUnit(1);
	    yAxislvlVoc.setMinorTickVisible(true);
	    
	    
	    BarChart<String,Number> vocabularyChart = new BarChart<String,Number>(xAxisVoc,
	    																	yAxisVoc);
	    BarChart<String,Number> definitionChart = new BarChart<String,Number>(xAxisDef,
	    																	yAxisDef);
	    BarChart<String,Number> levelVocChart = new BarChart<String,Number>(xAxislvlDef, 
	    																yAxislvlVoc);
	    BarChart<String,Number> levelDefChart = new BarChart<String,Number>(xAxislvlVoc,
	    																yAxislvlDef);
	    vocabularyChart.setTitle("Overview");
	    definitionChart.setTitle("Overview");
	    levelVocChart.setTitle("");
	    levelDefChart.setTitle("");
	    yAxisVoc.setLabel("Total number of Cards");
	    yAxisDef.setLabel("Total number of Cards");
	    yAxislvlVoc.setLabel("Total number of Cards");
	    yAxislvlDef.setLabel("Total number of Cards");
	    
	    //Create a Serie of bars and add the bars in these Serie
	    XYChart.Series<String,Number> seriesVocabulary1 = 
	    											new XYChart.Series<String, Number>();
	    XYChart.Series<String,Number> seriesDefinition1 =
	    											new XYChart.Series<String, Number>();
	    XYChart.Series<String, Number> seriesLvlVocabulary1 = 
	    											new XYChart.Series<String,Number>();
	    XYChart.Series<String, Number> seriesLvlVocabulary2 = 
													new XYChart.Series<String,Number>();
	    XYChart.Series<String, Number> seriesLvlVocabulary3 = 
													new XYChart.Series<String,Number>();
	    XYChart.Series<String, Number> seriesLvlVocabulary4 = 
													new XYChart.Series<String,Number>();
	    XYChart.Series<String, Number> seriesLvlDefinition1 = 
	    											new XYChart.Series<String,Number>(); 
	    XYChart.Series<String, Number> seriesLvlDefinition2 = 
													new XYChart.Series<String,Number>(); 
	    XYChart.Series<String, Number> seriesLvlDefinition3 = 
													new XYChart.Series<String,Number>(); 
	    XYChart.Series<String, Number> seriesLvlDefinition4 = 
	    											new XYChart.Series<String,Number>(); 
	    

	    //Legend Name
	    vocabularyChart.setLegendVisible(false);
	    definitionChart.setLegendVisible(false);
	    levelVocChart.setLegendVisible(false);
	    levelDefChart.setLegendVisible(false);
	    
    	//Different database commands to filter out the categories
	    
	    ResultSet rs;
	    ResultSet rsCounter; //Counter variable for the statistic values7
	    String language1; //outer-loop
	    String language2; //inner-loop	    

	    try {
	    //For Vocabulary
	    for(int i=0; i<diffLanguages.length;i++) {
	    	language1 = diffLanguages[i];
	    	rs=HSQLDB.getInstance().query("select w1.language from "
	    			+ "words w1 join words w2 on w1.userid= w1.userid where "
	    			+ "((w1.wordid, w2.wordid) in (select wordid1, wordid2 from translate) " 
	    			+ "or (w1.wordid, w2.wordid) in (select wordid2, wordid1 from "
	    			+ "translate)) and (w1.language = '"+language1+"') "
	    			+ "and w1.userid ="+Login.userID+"");
	    	//Is their any used language?
	    	if(rs.next()) {
	    		rs=HSQLDB.getInstance().query("select  w2.language from words w1 join words "
	    				+ "w2 on w1.userid= w1.userid where  (w1.wordid, w2.wordid) in "
	    				+ "(select wordid1, wordid2 from translate) and (w1.language = "
	    				+  "'"+language1+"' )and w1.userid ="+Login.userID+" group by "
	    				+ "w2.language");

	    		while(rs.next()) {
	    			language2=rs.getString(1);
	    			rsCounter = HSQLDB.getInstance().query("select count(*) from words w1 "
	    					+ "join words w2 on w1.userid= w1.userid where "
	    					+ "((w1.wordid, w2.wordid) in (select wordid1, wordid2 from "
	    					+ "translate) or (w1.wordid, w2.wordid) in "
	    					+ "(select wordid2, wordid1 from translate)) and " 
	    					+ "(w1.language = '"+language1+"' "
	    					+ "and w2.language='"+language2+"')and w1.userid = "
	    					+ ""+Login.userID+"");
	    			if(rsCounter.next()) {
	    				if(rsCounter.getInt(1)!=0) {
	    				
	    				//Is there a double language combination? If no, then add the String
	    				doublerCheck(language1, language2,seriesVocabulary1,
	    								 barflowVocabulary,doublerCheck,rsCounter,
	    								 lvlLanguage1,lvlLanguage2);
	    				}
	    				}
	    		}
	    	}}

	    
	    	//For the definition
	    for(int i=0; i<diffLanguages.length;i++) {
	    	language1 = diffLanguages[i];
	    	rsCounter=HSQLDB.getInstance().query("SELECT count(*) FROM Words w NATURAL JOIN "
	    			+ "Definition d WHERE w.UserID = "+Login.userID+" and"
	    			+ " w.language='"+language1+"'");
			if(rsCounter.next()) {
				if(rsCounter.getInt(1)!=0) {
			seriesDefinition1.getData().add(new Data<String, Number>(language1, 
					0));
			barflowDefinition.add(rsCounter.getInt(1));
			
					}
				}
	    }
	    
	    
	    //For the levels of vocabulary
	    
	    /*This for loop runs as long as different language there are.
	     *lvlLanguage1 will be used for wordid1
	     */
	    for(int i=0; i < lvlLanguage1.size(); i++) {
	    	/* ForLoop to count the total Value in the different levels(1-4)
	    	 * and for the different language combinations
	    	 */
	    	for(int x=0; x<=3;x++) {
	    		rsCounter=HSQLDB.getInstance().query("select count(*) from words w1 join "
	    				+"words w2 on w1.userid= w1.userid where (w1.wordid, w2.wordid) in "
	    				+"(select wordid1, wordid2 from translate where Level = "+(x+1)+") "
	    				+"and (w1.language = '"+lvlLanguage1.get(i)+"' and "
	    				+ "w2.language='"+lvlLanguage2.get(i)+"') and "
	    				+ "w1.userid = "+Login.userID+"");
	    		if(rsCounter.next()) {
			//		if(rsCounter.getInt(1)!=0) {
	    			
	    			/*
	    			 *Get all correct language combination which was added before 
	    			 *through the method "doublerCheck"(->lvlLanguage1.add() and
	    			 *lvlLanguage2.add()) in the correct order.
	    			 */
	    			String concatString = new StringBuffer(lvlLanguage2.get(i)).append(" - ").
	    					append(lvlLanguage1.get(i)).toString();
	    			
	    			/*Add the correct language combination (concatString) and the 
	    			 *total count value in the correct level
	    			 */
	    		
	    		switch(x) {
	    		//Level 1
	    		case 0:
	    			seriesLvlVocabulary1.getData().add(new Data<String, Number>(concatString,
	    											0));
	    			/*x represent the level. 0 means 1, 1 means 2 and so on.
	    			 *safe the total Value of the language combination in the corresponding
	    			 *level (it will be safed in an ArrayList).
	    			 */
	    			barflowLvlVoc.get(x).add(rsCounter.getInt(1));
	    			break;
	    		//Level 2
	    		case 1:
	    			seriesLvlVocabulary2.getData().add(new Data<String, Number>(concatString,
							0));
	    			barflowLvlVoc.get(x).add(rsCounter.getInt(1));;
	    			break;
	    		//Level 3	
	    		case 2:
	    			seriesLvlVocabulary3.getData().add(new Data<String, Number>(concatString,
							0));
	    			barflowLvlVoc.get(x).add(rsCounter.getInt(1));
	    			break;
	    		//Level 4
	    		case 3:
	    			seriesLvlVocabulary4.getData().add(new Data<String, Number>(concatString,
							0));
	    			barflowLvlVoc.get(x).add(rsCounter.getInt(1));
	    			break;
	    			
	    		default:
	    			System.out.println("Wrong implementation");
	    		}

				
		//				}
					}
	    		
	    	}
	    }
	    
	
	    } catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    	
	    	vocabularyChart.getData().addAll(seriesVocabulary1);
	    	//Call the function directly to avoid that the default boundary 
	    	//will be depicteted
	    	dynamicChange(barflowVocabulary,vocabularyChart,yAxisVoc);
	    	
	    	definitionChart.getData().addAll(seriesDefinition1);
	    	dynamicChange(barflowDefinition,definitionChart,yAxisDef);
	    	
	    	levelVocChart.getData().addAll(seriesLvlVocabulary1);
	    	dynamicChange(barflowLvlVoc,levelVocChart,yAxislvlVoc,levels[0]);
	    	
	    	
	    	
	    	levelDefChart.getData().addAll(seriesLvlDefinition1);

	    //Tab resp. TabPane handling
	    
	    
	    tabPane.getTabs().addAll(tabVocabulary,tabDefinition,tabLvlVoc,tabLvlDef);
	    tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
	    tabVocabulary.setContent(vocabularyChart);
	    tabDefinition.setContent(definitionChart);
	    tabLvlVoc.setContent(gridForTabVoc);
	    tabLvlDef.setContent(gridForTabDef);
	    
	    //GridPane handling

		gridPane.setHgap(10);
		gridPane.setVgap(10);
		
		GridPane.setConstraints(btnBack,0,8,2,1);
		GridPane.setConstraints(tabPane,1,1,1,7);
		GridPane.setConstraints(levelVocChart,0,0,3,10);
		GridPane.setConstraints(levelDefChart, 0, 0, 3, 10);
		GridPane.setConstraints(cbLvlFilterVoc, 1,0);
		GridPane.setConstraints(cbLvlFilterDef, 1,0);
		
		GridPane.setHalignment(btnBack, HPos.CENTER);
		GridPane.setHalignment(cbLvlFilterVoc, HPos.CENTER);
		GridPane.setHalignment(cbLvlFilterDef, HPos.CENTER);
		
		cbLvlFilterVoc.setPrefWidth(90);
		cbLvlFilterDef.setPrefWidth(90);
		
		gridPane.getChildren().addAll(btnBack,tabPane);
		gridForTabVoc.getChildren().addAll(levelVocChart,cbLvlFilterVoc);
		gridForTabDef.getChildren().addAll(levelDefChart,cbLvlFilterDef);
		//Set the ColumnContraints and add them

		ColumnConstraints column0 = new ColumnConstraints(30,30,30);
		ColumnConstraints column1 = new ColumnConstraints(60,65,Double.MAX_VALUE);
		ColumnConstraints column2 = new ColumnConstraints(30,30,30);
		
		//Both Columns have the same priority
		column0.setHgrow(Priority.ALWAYS);
		column1.setHgrow(Priority.ALWAYS);
		column2.setHgrow(Priority.ALWAYS);
		
		gridPane.getColumnConstraints().addAll(column0,column1,column2);
		gridForTabVoc.getColumnConstraints().addAll(column0,column1,column2);
		gridForTabDef.getColumnConstraints().addAll(column0,column1,column2);
		
		//Set the RowConstraints and add them
		
		//old Values 10,75,75
		RowConstraints row1 = new RowConstraints(40,40,40);
		RowConstraints row2 = new RowConstraints(0,20,Double.MAX_VALUE);
		RowConstraints row3 = new RowConstraints(10,60,60);
		RowConstraints row4 = new RowConstraints(10,60,60);
		RowConstraints row5 = new RowConstraints(10,60,60);
		RowConstraints row6 = new RowConstraints(10,60,60);
		RowConstraints row7 = new RowConstraints(10,60,60);
		RowConstraints row8 = new RowConstraints(10,60,60);
		RowConstraints row9 = new RowConstraints(30,60,60);
		RowConstraints row10 = new RowConstraints();
		
		//Every row have the same priority
		
		row1.setVgrow(Priority.ALWAYS);
		row2.setVgrow(Priority.ALWAYS);
		row3.setVgrow(Priority.ALWAYS);
		row4.setVgrow(Priority.ALWAYS);
		row5.setVgrow(Priority.ALWAYS);
		row6.setVgrow(Priority.ALWAYS);
		row7.setVgrow(Priority.ALWAYS);
		row8.setVgrow(Priority.ALWAYS);
		row9.setVgrow(Priority.ALWAYS);
		
		gridPane.getRowConstraints().addAll(row1,row2,row3,row4,row5,row6,row7,row8,row9,
										row10);		
		gridForTabVoc.getRowConstraints().addAll(row1,row2,row3,row4,row5,row6,row7,row8,row9,
				row10);		
		gridForTabDef.getRowConstraints().addAll(row1,row2,row3,row4,row5,row6,row7,row8,row9,
				row10);		
    
    	Scene cssStyle = new Scene(gridPane,1000,600);
		cssStyle.getStylesheets().addAll(this.getClass().getResource
														("Style.css").toExternalForm());
		primaryStage.setScene(cssStyle);
		
//		gridForTabVoc.setGridLinesVisible(true); //Debugging
//		gridForTabDef.setGridLinesVisible(true); //Debugging
//		gridPane.setGridLinesVisible(true); //Debugging
		
		//Eventhandler
		
		btnBack.setOnAction(e -> {
			
			primaryStage.setScene(uiScene);
				});
		
		cbLvlFilterVoc.setOnAction(e -> {
			
			if(cbLvlFilterVoc.getValue()=="Level 1") {
				
			} else if(cbLvlFilterVoc.getValue()=="Level 2") {
				
			} else if(cbLvlFilterVoc.getValue()=="Level 3") {
				
			} else if(cbLvlFilterVoc.getValue()=="Level 4") {
				
			} 			
			
		});
		
		
		
		
		tabPane.getSelectionModel().selectedItemProperty().addListener
														(new ChangeListener<Tab>()
		    {
		        @Override
		        public void changed(ObservableValue<? extends Tab> arg0, Tab arg1,
		        					Tab mostRecentlySelectedTab)
		        {//Do this firstly to enable a dynamic Chart
		            if (mostRecentlySelectedTab.equals(tabVocabulary))
		            {
		            	
		 	           for (XYChart.Series<String, Number> series :
		 	        	   									vocabularyChart.getData()) {
			               for (XYChart.Data<String, Number> data : series.getData()) {
			            	   	data.setYValue(0);
			               }
			           }
		 	           dynamicChange(barflowVocabulary,vocabularyChart,
		 	        		   yAxisVoc);
		            }
		            if (mostRecentlySelectedTab.equals(tabDefinition))
		            {   
			 	           for (XYChart.Series<String, Number> series : 
			 	       	   									definitionChart.getData()) {
				               for (XYChart.Data<String, Number> data : series.getData()) {
				            	   	data.setYValue(0);
				               }
				           }
		              dynamicChange(barflowDefinition,definitionChart,yAxisDef);
		            	
		            
		            }
		            if (mostRecentlySelectedTab.equals(tabLvlVoc))
		            {   
		     		   for (XYChart.Series<String, Number> series :
		     			   									levelVocChart.getData()) {
		    		       for (XYChart.Data<String, Number> data : series.getData()) {
		    		    	   		data.setYValue(0);
		    		       }
		    		   }
		     		dynamicChange(barflowLvlVoc,levelVocChart,yAxislvlVoc,levels[0]);
		            
		            }
		            
		            
		            
		        }


		    });
		
		}

	
	private void doublerCheck(String language1, String language2,
							 XYChart.Series<String,Number> seriesVocabulary1,
							 ArrayList<Integer> barflow, HashMap<String,String> doublerCheck,
							 ResultSet rsCounter, ArrayList<String> lvlLanguage1,
							 ArrayList<String> lvlLanguage2) {
		
			String concatString = new StringBuffer(language1).append(" - ").
									 		append(language2).toString();
			
			doublerCheck.put(concatString, concatString);
			
			//Build counterpart of the String
			
			concatString = new StringBuffer(language2).append(" - ").
			 							append(language1).toString();
			
			
			
			if(!doublerCheck.containsKey(concatString) || language1.equals(language2))	{
				/*Prepare the data for the barchart (level for Vocabulary) and
				 *inverse it again, because the request in later step(count the total
				 *number for the different levels) will be used the language1(=wordid1)
				 */
				concatString = new StringBuffer(language1).append(" - ").
							append(language2).toString();
				lvlLanguage1.add(language1);
				lvlLanguage2.add(language2);				
				//End of preparation
				seriesVocabulary1.getData().add(new Data<String, Number>(concatString, 
						0));
						try {
							barflowVocabulary.add(rsCounter.getInt(1));
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			}		
	}
	    
	    
	
	
	private void dynamicChange(ArrayList<ArrayList<Integer>> barflow, 
						BarChart<String, Number> barchart, NumberAxis yAxis, int lvl) {
		if(!barflow.isEmpty()) {
		//To avoid the default empty BarChart Y-Axis boundary
		yAxis.setUpperBound(Collections.max(barflow.get(lvl))+5);	
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent actionEvent) {
		   int number = -1;
		   for (XYChart.Series<String, Number> series : barchart.getData()) {
		       for (XYChart.Data<String, Number> data : series.getData()) {
		    	   number++;
		    	   double serieValue = (barflow.get(lvl)).get(number);
		    	   //for a steady and smoothly increase
		           for(double i=0.0; i <= serieValue; i++) {
		        	   for(double x=0.0; x<=i;x=x+0.5) {
		        	   data.setYValue(x);
		        	   }
		           }
		       }
		   }
		}
		}));
		timeline.setCycleCount(1);
		timeline.play();
		}
		}
	
	
	
	

	private void dynamicChange(ArrayList<Integer> barflow, BarChart<String, Number> barchart,
						NumberAxis yAxis) {
		if(!barflow.isEmpty()) {
		//To avoid the default empty BarChart Y-Axis boundary
		yAxis.setUpperBound(Collections.max(barflow)+5);
		Timeline timeline = new Timeline();
	    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
	       @Override
	       public void handle(ActionEvent actionEvent) {
	    	   int number = -1;
	           for (XYChart.Series<String, Number> series : barchart.getData()) {
	               for (XYChart.Data<String, Number> data : series.getData()) {
	            	   number++;
	            	   double serieValue = barflow.get(number);
	            	   //for a steady and smoothly increase
	                   for(double i=0.0; i <= serieValue; i++) {
	                	   for(double x=0.0; x<=i;x=x+0.5) {
	                	   data.setYValue(x);
	                	   }
	                   }
	               }
	           }
	       }
	    }));
	    timeline.setCycleCount(1);
        timeline.play();
	}
	}
	
	}