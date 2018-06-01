import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class is used to represent the data of a tableview and process the user
 * input to the control and show the output again in the view.
 * 
 * @author Erik
 *
 */
public class EditFileCardsLayout {

	// The instance of the control Class
	EditFileCards control;
	// Tableview - visualizes the data
	private TableView<FileCardsDB> table = new TableView<FileCardsDB>();
	// Layout stage
	private Stage editStage = new Stage();
	private Stage uiStage;
	// Arangement of Objects in the stage
	private GridPane root = new GridPane();
	private HBox hboxTextFields = new HBox();
	private HBox hboxButtons = new HBox();
	private HBox hboxTitle = new HBox();
	private HBox hboxSubcategory = new HBox();
	private HBox hboxImportExportButtons = new HBox();
	private TextField addSideA = new TextField(); // col1
	private TextField addSideB = new TextField(); // col2
	private Button addButton = new Button("Add"); // addEntry
	private Button deleteButton = new Button("Delete"); // delete focused entry
	private ComboBox<String> filterCategory; // Filter Category tableview
	private ComboBox<String> filterSubCategoryA; // Filter SubCategory tableview
	private ComboBox<String> filterSubCategoryB; // Filter SubCategory tableview
	private Button exportButton = new Button("Export");
	private Button importButton = new Button("Import");
	private Button closeButton = new Button("Quit");
	// Set up the column sideA of the tableView
	private TableColumn<FileCardsDB, String> sideA = new TableColumn<FileCardsDB, String>("SideA");
	private TableColumn<FileCardsDB, String> sideB = new TableColumn<FileCardsDB, String>("SideB");
	// Lists for combobox to filter for Category
	private ObservableList<String> optionsCategory = FXCollections.observableArrayList("Translation", "Definition");
	// Lists for combobox to filter for SubCategory when Category is Definition
	private ObservableList<String> optionsSubCategoryLanguage = FXCollections.observableArrayList("English", "German",
			"French", "Spanish");
	private ObservableList<String> optionsSubCategoryDefintion = FXCollections.observableArrayList("Defintion");

	/**
	 * This constructor is the default one for generating the general functions of editing
	 * 
	 * @param control the control class of the view
	 * @param uiStage the Stage of the previous window
	 */
	public EditFileCardsLayout(EditFileCards control, Stage uiStage) {
		this.control = control;
		this.uiStage = uiStage;
		// Name the new stage (window)
		editStage.setTitle("MyFileCards");
		// Set MinMax of the buttons, textfields ect.
		setElements();
		// Arrange Elements in the window
		arrangeElements();
	}

	/**
	 * This constructor is called when editing while the testmode. 
	 * This will only display the corresponding content of the tested filecard.
	 * 
	 * @param control is the controlClass of the view
	 * @param uiStage is the stage of the previous window
	 * @param individualTesting this specifies that the right constructor gets called
	 */
	public EditFileCardsLayout(EditFileCards control, Stage uiStage, Boolean individualTesting) {
		this.control = control;
		this.uiStage = uiStage;
		// Made modal, so the parentwindow cannot be activated as long as editing isn`t closed
		editStage.initModality(Modality.APPLICATION_MODAL);
		// Name the new stage (window)
		editStage.setTitle("MyFileCards");
		// Set MinMax of the buttons, textfields ect.
		setElements();
		// Arrange Elements in the window
		arrangeElements();
	}

	/**
	 * This methods deligates to the control Class to implement the behaviour of the
	 * different Listener of the category filter, subcategory filter for the first
	 * column and subcategory filter for the second column
	 * 
	 * 
	 */
	public void setFilterListener() {
		filterCategory.valueProperty().addListener((obs, oldValue, newValue) -> {
			control.addFilterCategoryListener(filterCategory, filterSubCategoryA, filterSubCategoryB, sideA, sideB,
					optionsCategory, optionsSubCategoryDefintion, optionsSubCategoryLanguage);
		});
		filterSubCategoryA.valueProperty().addListener((obs, oldValue, newValue) -> {
			control.addFilterSubCategoryAListener(filterCategory, filterSubCategoryA, filterSubCategoryB, sideA, sideB,
					optionsCategory, optionsSubCategoryDefintion, optionsSubCategoryLanguage);
		});
		filterSubCategoryB.valueProperty().addListener((obs, oldValue, newValue) -> {
			control.addFilterSubCategoryBListener(filterCategory, filterSubCategoryA, filterSubCategoryB, sideA, sideB,
					optionsCategory, optionsSubCategoryDefintion, optionsSubCategoryLanguage);

		});

	}

	/**
	 * This method disables all buttons in the view
	 * 
	 */
	public void disableButtons() {
		addButton.setDisable(true);
		deleteButton.setDisable(true);
		importButton.setDisable(true);
		exportButton.setDisable(true);
	}

	/**
	 * This method enables all buttons in the view
	 * 
	 */
	public void enableButtons() {
		addButton.setDisable(false);
		deleteButton.setDisable(false);
		importButton.setDisable(false);
		exportButton.setDisable(false);
	}

	/**
	 * This method sets the different (MinMaxPref) sizes of the specific elements
	 * like buttons, testfields, comboboxes and the tableview
	 */
	public void setElements() {
		// Fill Comboboxes with options
		setFilterCategory(new ComboBox<String>(optionsCategory));
		setFilterSubCategoryA(new ComboBox<String>());
		setFilterSubCategoryB(new ComboBox<String>());

		// Set default text of Comboboxes
		filterCategory.setPromptText("Please Choose");
		filterSubCategoryA.setPromptText("Please Choose");
		filterSubCategoryA.setDisable(true);
		filterSubCategoryB.setPromptText("Please Choose");
		filterSubCategoryB.setDisable(true);
		// Set width of the columns
		sideA.setPrefWidth(150);
		sideA.setMaxWidth(700);
		sideA.setMinWidth(50);
		sideB.setPrefWidth(150);
		sideB.setMaxWidth(700);
		sideB.setMinWidth(50);
		// Set columns to grow equally in width
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// Height of the table
		table.setPrefHeight(3000);
		table.setMinHeight(100);
		table.setMaxHeight(3000);

		// Set IDs for css style

		exportButton.setId("buttonEditFileCards");
		importButton.setId("buttonEditFileCards");
		addButton.setId("buttonEditFileCards");
		deleteButton.setId("buttonEditFileCards");
		closeButton.setId("buttonEditFileCards");

		// Set size constraints of the textfields, buttons and label
		addSideA.setMaxWidth(sideA.getMaxWidth());
		addSideA.setPrefWidth(sideA.getPrefWidth());
		addSideA.setMinWidth(sideA.getMinWidth());
		addSideA.setPromptText("SideA");
		addSideB.setMaxWidth(sideB.getMaxWidth());
		addSideB.setPrefWidth(sideB.getPrefWidth());
		addSideB.setPromptText("SideB");
		addButton.setMaxWidth(sideA.getMaxWidth());
		addButton.setMinWidth(sideA.getMinWidth());
		addButton.setPrefWidth(sideA.getPrefWidth());
		addButton.setDisable(true);
		deleteButton.setMaxWidth(sideB.getMaxWidth());
		deleteButton.setMinWidth(sideB.getMinWidth());
		deleteButton.setPrefWidth(sideB.getPrefWidth());
		deleteButton.setDisable(true);
		filterSubCategoryA.setMaxWidth(sideA.getMaxWidth());
		filterSubCategoryA.setMinWidth(sideA.getMinWidth());
		filterSubCategoryA.setPrefWidth(sideA.getPrefWidth());
		filterSubCategoryB.setMaxWidth(sideB.getMaxWidth());
		filterSubCategoryB.setMinWidth(sideB.getMinWidth());
		filterSubCategoryB.setPrefWidth(sideB.getPrefWidth());
		filterCategory.setMaxWidth(sideA.getMaxWidth() + sideB.getMaxWidth());
		filterCategory.setMinWidth(sideA.getMinWidth());
		filterCategory.setPrefWidth(sideA.getPrefWidth());
		importButton.setMaxWidth(sideA.getMaxWidth());
		importButton.setMinWidth(sideA.getMinWidth());
		importButton.setPrefWidth(sideA.getPrefWidth());
		importButton.setDisable(true);
		exportButton.setMaxWidth(sideB.getMaxWidth());
		exportButton.setMinWidth(sideB.getMinWidth());
		exportButton.setPrefWidth(sideB.getPrefWidth());
		exportButton.setDisable(true);
		closeButton.setMaxWidth(filterCategory.getMaxWidth());
		closeButton.setMinWidth(filterCategory.getMinWidth());
		closeButton.setPrefWidth(filterCategory.getPrefWidth());

	}

	/**
	 * This method arranges the different elements in containers in the Scene, how
	 * they should grow and the size of the boxes. It also configures the stage.
	 * 
	 */
	public void arrangeElements() {

		// Let elements grow equally in horizontal direction
		HBox.setHgrow(addButton, Priority.ALWAYS);
		HBox.setHgrow(deleteButton, Priority.ALWAYS);
		HBox.setHgrow(addSideA, Priority.ALWAYS);
		HBox.setHgrow(addSideB, Priority.ALWAYS);
		HBox.setHgrow(filterSubCategoryA, Priority.ALWAYS);
		HBox.setHgrow(filterSubCategoryB, Priority.ALWAYS);
		HBox.setHgrow(filterCategory, Priority.ALWAYS);
		HBox.setHgrow(importButton, Priority.ALWAYS);
		HBox.setHgrow(exportButton, Priority.ALWAYS);

		// Arrange different elements in boxes and bind them together in grid
		hboxTextFields.getChildren().addAll(addSideA, addSideB);
		hboxButtons.getChildren().addAll(addButton, deleteButton);
		hboxSubcategory.getChildren().addAll(filterSubCategoryA, filterSubCategoryB);
		hboxTitle.getChildren().addAll(filterCategory);
		hboxImportExportButtons.getChildren().addAll(importButton, exportButton);

		root.getChildren().addAll(hboxTitle, hboxSubcategory, table, hboxTextFields, hboxButtons,
				hboxImportExportButtons, closeButton);
		// Set location of different boxes
		GridPane.setConstraints(hboxTitle, 0, 0);
		GridPane.setConstraints(hboxSubcategory, 0, 1);
		GridPane.setConstraints(table, 0, 2);
		GridPane.setConstraints(hboxTextFields, 0, 3);
		GridPane.setConstraints(hboxButtons, 0, 4);
		GridPane.setConstraints(hboxImportExportButtons, 0, 5);
		GridPane.setConstraints(closeButton, 0, 6);

		// Set size properties of the stage
		editStage.setMaxWidth(addSideA.getMaxWidth() + addSideB.getMaxWidth());
		// Open Stage on same size and extend as previous Stage
		editStage.setWidth(700);
		editStage.setHeight(400);
		editStage.setX((uiStage.getX() + uiStage.getWidth() / 2) - editStage.getWidth() / 2);
		editStage.setY(uiStage.getY() + uiStage.getHeight() / 2 - editStage.getHeight() / 2);
		// Bin the width of the table(and following also the other elements) to the
		// width of the stage
		table.prefWidthProperty().bind(editStage.widthProperty());

		// The scene is characterized by the gridlayout
		root.setId("pane2");
		Scene scene = new Scene(root);
		scene.getStylesheets().addAll(this.getClass().getResource("Style.css").toExternalForm());
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setPrefSize(editStage.getWidth(), editStage.getHeight());
		root.setMinSize(editStage.getWidth(), editStage.getHeight());
		editStage.setScene(scene);

		editStage.show();

	}

	/**
	 * This method characterizes the different columns and how they should behave
	 * while editing
	 * 
	 */
	public void setColumns() {
		/*
		 * Set the cells of the column to the propertytype string from the object
		 * fileCardsDB(attribute sideA) // By being a property, the table gets informed
		 * if a value changes and updates automatically Defines how to display data on
		 * the cell
		 */
		sideA.setCellValueFactory(new PropertyValueFactory<FileCardsDB, String>("sideA"));

		// Set the cell to a textfield when editing
		sideA.setCellFactory(TextFieldTableCell.forTableColumn());

		// Tell what should happen when editing
		// Implement anonymous class (Eventhandler)
		sideA.setOnEditCommit(new EventHandler<CellEditEvent<FileCardsDB, String>>() {
			@Override
			public void handle(CellEditEvent<FileCardsDB, String> edit) {
				// Get the new value of the cell after editing
				String wordNew = edit.getNewValue();
				// Update Translate
				control.updateEntry(wordNew, edit.getRowValue().getSideB(), edit.getRowValue().getIdSideA(),
						edit.getRowValue().getIdSideB(), getFilterSubCategoryA().getValue(),
						getFilterSubCategoryB().getValue(), filterCategory.getValue(), table, Login.userID);
				// Refresh tableview
				edit.getTableView().getColumns().get(0).setVisible(false);
				edit.getTableView().getColumns().get(0).setVisible(true);
			}
		});

		sideB.setCellValueFactory(new PropertyValueFactory<FileCardsDB, String>("sideB"));
		// Set the cell to a textfield when editing
		sideB.setCellFactory(TextFieldTableCell.forTableColumn());

		// Tell what should happen when editing
		// Implement anonymous class
		sideB.setOnEditCommit(new EventHandler<CellEditEvent<FileCardsDB, String>>() {
			// Add necessary mehtod
			@Override
			public void handle(CellEditEvent<FileCardsDB, String> edit) {
				// Get the new value of the cell after editing
				String wordNew = edit.getNewValue();
				// Update
				control.updateEntry(edit.getRowValue().getSideA(), wordNew, edit.getRowValue().getIdSideA(),
						edit.getRowValue().getIdSideB(), getFilterSubCategoryA().getValue(),
						getFilterSubCategoryB().getValue(), filterCategory.getValue(), table, Login.userID);
				// Refresh tableview
				edit.getTableView().getColumns().get(0).setVisible(false);
				edit.getTableView().getColumns().get(0).setVisible(true);
			}
		});

		// Set the tableView editable
		table.setEditable(true);
		// Add columns to the tableView
		table.getColumns().addAll(sideA, sideB);
	}

	/**
	 * This method specifies the different ActionListener on the Buttons.
	 * 
	 * If the deleteButton is clicked, the the corresponding entry the the text in
	 * the two testfields will be deleted.
	 * 
	 * If the addButton is clicked, the he corresponding entry the the text in the
	 * two testfields will be added.
	 * 
	 * If the importButton is clicked, the user can choose with the FileChooser a
	 * file to be imported, which has to be an Arraylist of FileCardsDB-Objects. The
	 * Category and SubCategories have to match the chosen ones when the
	 * importButton is clicked.
	 * 
	 * If the exportButton is clicked, the current viewed data can be saved as
	 * FileCardsDBObjects. Therefore, a filechooser is opened and the user can
	 * specify a name.
	 * @param all if true it will only the "Quit" Button be decorated with an actionlistener,
	 * which is the case when just one filecard should be edited (in testing mode)
	 */
	public void setActionListener(Boolean all) {
		if (all) {
			// Actionlistener for removing an entry
			deleteButton.setOnAction(edit -> {
				if ((filterCategory.getValue() != null) && (filterSubCategoryA.getValue() != null)
						&& (filterSubCategoryB.getValue() != null)) {
				//	control.deleteCurrentEntry(addSideA.getText(), addSideB.getText(), filterCategory.getValue());
					control.deleteCurrentEntry(filterCategory.getValue(), table.getSelectionModel().getTableView());
				}
				addSideA.clear();
				addSideB.clear();
				addSideA.requestFocus();

			});

			// Actionlistener for Button to add new entries
			addButton.setOnAction(e -> {

				if ((filterCategory.getValue() != null) && (filterSubCategoryA.getValue() != null)
						&& (filterSubCategoryB.getValue() != null)) {
					control.insertEntry(addSideA.getText(), addSideB.getText(), filterSubCategoryA.getValue(),
							filterSubCategoryB.getValue(), filterCategory.getValue(), Login.userID);
				}

				// Set textfields to placeholder
				addSideA.clear();
				addSideB.clear();
			});
			// Export data
			exportButton.setOnAction(edit -> {
				control.exportData(filterCategory.getValue(), filterSubCategoryA.getValue(),
						filterSubCategoryB.getValue(), editStage);
			});
			// Import data
			importButton.setOnAction(edit -> {
				control.importData(filterCategory.getValue(), filterSubCategoryA.getValue(),
						filterSubCategoryB.getValue(), editStage);
			});
			// Quit editing
			closeButton.setOnAction(edit -> {
				control.closeEditStage(editStage);
			});
		} else {
			closeButton.setOnAction(edit -> {
				editStage.close();
			});
		}

	}

	/**
	 * This method deligates the implementation of the behaviour when a specific
	 * button is pressed to the control class. It defines the ENTER Key when focused
	 * in the textfield for the second column It defines the DELETE and BACK_SPACE
	 * Key when focused in the tableview
	 */
	public void setKeyListener() {
		addSideB.setOnKeyReleased(e -> {
			control.addKeyListener(addSideA, addButton, e);
		});
		table.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				control.deleteKeyListener(table, e);
			}

		});
	}

	public void setWindowListener() {
		control.closeProgram(editStage);
	}

	/*
	 * Getter and Setter
	 */

	public TableView<FileCardsDB> getTable() {
		return table;
	}

	public void setTable(TableView<FileCardsDB> table) {
		this.table = table;
	}

	public ComboBox<String> getFilterSubCategoryA() {
		return filterSubCategoryA;
	}

	public void setFilterSubCategoryA(ComboBox<String> filterSubCategoryA) {
		this.filterSubCategoryA = filterSubCategoryA;
	}

	public ComboBox<String> getFilterSubCategoryB() {
		return filterSubCategoryB;
	}

	public void setFilterSubCategoryB(ComboBox<String> filterSubCategoryB) {
		this.filterSubCategoryB = filterSubCategoryB;
	}

	public ComboBox<String> getFilterCategory() {
		return filterCategory;
	}

	public void setFilterCategory(ComboBox<String> filterCategory) {
		this.filterCategory = filterCategory;
	}

	public Stage getEditStage() {
		return editStage;
	}

}
