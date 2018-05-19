import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class EditFileCards {

	// Tableview - visualizes the data
	private TableView<FileCardsDB> table = new TableView<FileCardsDB>();
	// Observable List to feed tableview with data with the object type FileCardsDB
	private final ObservableList<FileCardsDB> data = FXCollections.observableArrayList();
	// Layout stage
	private Stage editStage = new Stage();
	// Arangement of Objects in the stage
	private GridPane root = new GridPane();
	private HBox hboxTextFields = new HBox();
	private HBox hboxButtons = new HBox();
	private HBox hboxTitle = new HBox();
	private HBox hboxSubcategory = new HBox();
	// Result of a sql query
	private ResultSet rs = null;
	// Layout
	private TextField addSideA = new TextField(); // col1
	private TextField addSideB = new TextField(); // col2
	private Button addButton = new Button("Add"); // addEntry
	private Button deleteButton = new Button("Delete"); // delete focused entry
	private ComboBox<String> filterCategory; // Filter Category tableview
	private ComboBox<String> filterSubCategoryA; // Filter SubCategory tableview
	private ComboBox<String> filterSubCategoryB; // Filter SubCategory tableview

	// Lists for combobox to filter for Category
	ObservableList<String> optionsCategory = FXCollections.observableArrayList("Translation", "Definition");
	// Lists for combobox to filter for SubCategory when Category is Definition
	ObservableList<String> optionsSubCategoryLanguage = FXCollections.observableArrayList("English", "German", "French",
			"Spanish");
	ObservableList<String> optionsSubCategoryDefintion = FXCollections.observableArrayList("Defintion");

	public ResultSet isDuplicateWord(String wordNew, String language, int userID) {
		ResultSet rs = null;
		try {
			rs = HSQLDB.getInstance().query("SELECT wordID " + "FROM words " + "WHERE word = '" + wordNew + "' "
					+ "AND Language = '" + language + "' " + "AND UserID = " + userID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public ResultSet insertWord(String wordNew, String language, int userID) {
		ResultSet rs = null;
		try {
			System.out.println("Updating word");
			HSQLDB.getInstance().update("insert into Words (Word, Language, UserID)" + "values('" + wordNew + "','"
					+ language + "', " + userID + ")");

			// Get the Resultset of the inserted new word
			rs = HSQLDB.getInstance().query("SELECT wordID " + "FROM words " + "WHERE word = '" + wordNew
					+ "' AND Language = '" + language + "' AND UserID = " + userID);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public ResultSet isDuplicateTranslate(int wordID1, int wordID2) {
		ResultSet rs = null;
		try {
			rs = HSQLDB.getInstance()
					.query("Select * " + "from translate " + "where (wordid1 =" + wordID1 + " " + "and wordid2 = "
							+ wordID2 + ") " + "or (wordid1 = " + wordID2 + " " + "and wordid2 = " + wordID1 + ") ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public ResultSet updateTranslate(String word1New, String word2New, int wordID1old, int wordID2old, String language1,
			String language2, int userID) {
		ResultSet rs = null;
		int wordID1;
		int wordID2;
		try {
			rs = isDuplicateWord(word1New, language1, userID);
			if (!rs.next()) {
				System.out.println("WordDup1");
				rs = insertWord(word1New, language1, userID);
				rs.next();
			}
			wordID1 = rs.getInt(1);
			rs = isDuplicateWord(word2New, language2, userID);
			if (!rs.next()) {
				System.out.println("WordDup2");
				rs = insertWord(word2New, language2, userID);
				rs.next();
			}
			wordID2 = rs.getInt(1);

			rs = isDuplicateTranslate(wordID1, wordID2);
			if (!rs.next()) {
				System.out.println("Updating tranlslate");
				HSQLDB.getInstance()
						.update("UPDATE Translate " + "SET WordID1 =" + wordID1 + ", WordID2 = " + wordID2 + " "
								+ " WHERE (WordID1 = " + wordID1old + "" + " AND WordID2 =" + wordID2old + ")"
								+ " OR (WordID1 =" + wordID2old + "" + " AND WordID2 =" + wordID1old + ")");
				// Insert the new value + wordID in the Observable List (update tableView)
				table.getSelectionModel().getSelectedItem().setSideA(word1New);
				table.getSelectionModel().getSelectedItem().setIdSideA(wordID1);
				table.getSelectionModel().getSelectedItem().setSideB(word2New);
				table.getSelectionModel().getSelectedItem().setIdSideB(wordID2);
				deleteWords(wordID1old);
				deleteWords(wordID2old);
			} else {
				System.out.println("DeleteRow");
				deleteCurrentEntry();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public ResultSet insertTranslate(String word1, String word2, String language1, String language2, int userID) {
		ResultSet rs = null;
		int wordID1;
		int wordID2;
		try {
			rs = isDuplicateWord(word1, language1, userID);
			if (!rs.next()) {
				rs = insertWord(word1, language1, userID);
				rs.next();
			}
			wordID1 = rs.getInt(1);
			rs = isDuplicateWord(word2, language2, userID);
			if (!rs.next()) {
				rs = insertWord(word2, language2, userID);
				rs.next();
			}
			wordID2 = rs.getInt(1);
			rs = isDuplicateTranslate(wordID1, wordID2);
			if (!rs.next()) {
				System.out.println("InsertTranslate");
				HSQLDB.getInstance()
						.update("INSERT INTO Translate (wordid1, wordid2)" + "values(" + wordID1 + "," + wordID2 + ")");
				data.add(new FileCardsDB(wordID1, wordID2, word1, word2));
			} else {
				System.out.println("Already existing translate");
			}
			rs = HSQLDB.getInstance()
					.query("SELECT * " + "FROM Translate " + "WHERE (WordID1 ='" + wordID1 + "' " + "AND WordID2 ='"
							+ wordID2 + "') " + "OR (WordID1 ='" + wordID2 + "' " + "AND WordID2 ='" + wordID1 + "')");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;

	}

	public ResultSet isDuplicateDefinition(int wordID1, String definition) {
		ResultSet rs = null;
		try {
			rs = HSQLDB.getInstance().query("Select definitionid " + "from definition " + "where wordid =" + wordID1
					+ " " + "and definition = '" + definition + "' ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public ResultSet insertDefinition(String word1, String word2, String language1, String language2, int userID) {
		ResultSet rs = null;
		int wordID1;
		int wordID2;
		try {
			rs = isDuplicateWord(word1, language1, userID);
			if (!rs.next()) {
				rs = insertWord(word1, language1, userID);
				rs.next();
			}
			wordID1 = rs.getInt(1);
			rs = isDuplicateDefinition(wordID1, word2);
			if (!rs.next()) {
				System.out.println("InsertDefinition");
				HSQLDB.getInstance().update(
						"INSERT INTO Definition (wordid, definition)" + "values(" + wordID1 + ",'" + word2 + "')");
				rs = HSQLDB.getInstance().query("SELECT definitionid FROM Definition where wordid = " + wordID1
						+ " AND definition ='" + word2 + "'");
				rs.next();
				wordID2 = rs.getInt(1);
				data.add(new FileCardsDB(wordID1, wordID2, word1, word2));
			} else {
				System.out.println("Already existing definition");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public ResultSet updateDefinition(String word1New, String word2New, int wordID1old, int wordID2old,
			String language1, String language2, int userID) {
		ResultSet rs = null;
		int wordID1;
		int wordID2;
		try {
			rs = isDuplicateWord(word1New, language1, userID);
			if (!rs.next()) {
				System.out.println("WordDup1");
				rs = insertWord(word1New, language1, userID);
				rs.next();
			}
			wordID1 = rs.getInt(1);

			rs = isDuplicateDefinition(wordID1, word2New);
			if (!rs.next()) {
				System.out.println("Updating Definition");
				HSQLDB.getInstance().update("UPDATE Definition " + "SET WordID =" + wordID1 + ", definition = '"
						+ word2New + "' " + " WHERE definitionID = " + wordID2old + "");
				// Insert the new value + wordID in the Observable List (update tableView)
				table.getSelectionModel().getSelectedItem().setSideA(word1New);
				table.getSelectionModel().getSelectedItem().setIdSideA(wordID1);
				table.getSelectionModel().getSelectedItem().setSideB(word2New);
				table.getSelectionModel().getSelectedItem().setIdSideB(wordID2old);
				deleteWords(wordID1old);
			} else {
				System.out.println("DeleteRow");
				deleteCurrentEntry();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public void deleteCurrentEntry() {

		if (table.getSelectionModel().getSelectedItem() != null
				&& table.getSelectionModel().getSelectedItem() != null) {
			int wordID1 = table.getSelectionModel().getSelectedItem().getIdSideA();
			int wordID2 = table.getSelectionModel().getSelectedItem().getIdSideB();
			// Remove the current entry (with the old values) from the observable list
			data.remove(table.getSelectionModel().getSelectedItem());
			// Remove the current entry (with the old values) from the database
			try {
				if (filterCategory.getValue().equals("Translation")) {
					HSQLDB.getInstance()
							.update("DELETE FROM Translate " + "where (WordID1 = " + wordID1 + " " + " AND WordID2 = "
									+ wordID2 + ") " + " OR (WordID1 = " + wordID2 + " " + " AND WordID2 = " + wordID1
									+ ")");
					deleteWords(wordID1);
					deleteWords(wordID2);

				} else if (filterCategory.getValue().equals("Definition")) {
					System.out.println("WordID2: " + wordID2);
					HSQLDB.getInstance().update("DELETE FROM Definition where DefinitionID = " + wordID2 + "");
					deleteWords(wordID1);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void deleteCurrentEntry(String word1, String word2) {

		int wordID1 = -1;
		int wordID2 = -1;
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).sideA.equals(word1) && data.get(i).sideB.equals(word2)) {
				wordID1 = data.get(i).idSideA;
				wordID2 = data.get(i).idSideB;
				data.remove(i);
				i = data.size();
			}

			// Remove the current entry (with the old values) from the database
			try {
				if (filterCategory.getValue().equals("Translation")) {
					HSQLDB.getInstance()
							.update("DELETE FROM Translate " + "where (WordID1 = " + wordID1 + " " + " AND WordID2 = "
									+ wordID2 + ") " + " OR (WordID1 = " + wordID2 + " " + " AND WordID2 = " + wordID1
									+ ")");
					deleteWords(wordID1);
					deleteWords(wordID2);

				} else if (filterCategory.getValue().equals("Definition")) {
					System.out.println("WordID2: " + wordID2);
					HSQLDB.getInstance().update("DELETE FROM Definition where DefinitionID = " + wordID2 + "");
					deleteWords(wordID1);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void deleteWords(int wordID) {
		try {
			HSQLDB.getInstance()
					.update("DELETE FROM words " + "where WordID = " + wordID + " " + " AND " + wordID
							+ "  not in(SELECT wordid1 " + "from translate " + "UNION " + "SELECT wordid2 "
							+ "from translate)" + " AND WordID  not in(SELECT wordid " + "from definition )");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public EditFileCards() {
		// Name the new stage (window)
		editStage.setTitle("MyFileCards");

		// Set up the column sideA of the tableView
		TableColumn<FileCardsDB, String> sideA = new TableColumn<FileCardsDB, String>("SideA");

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
				int wordID1;
				/*
				 * Because the different categories are characterized by different primary
				 * keys("Definition" has one ID as primary key, "Translation has a combination
				 * of two different IDs as primary key) we hav to handle the different
				 * categroies in a different way *
				 */

				// Case we are in the category "Translation"
				if (filterCategory.getValue().equals("Translation")) {

					try {
						updateTranslate(wordNew, edit.getRowValue().getSideB(), edit.getRowValue().getIdSideA(),
								edit.getRowValue().getIdSideB(), filterSubCategoryA.getValue(),
								filterSubCategoryB.getValue(), Login.userID);
						// Refresh tableview
						edit.getTableView().getColumns().get(0).setVisible(false);
						edit.getTableView().getColumns().get(0).setVisible(true);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else if (filterCategory.getValue().equals("Definition")) {

					try {
						// We need to update the table Definition with the new entity
						updateDefinition(wordNew, edit.getRowValue().getSideB(), edit.getRowValue().getIdSideA(),
								edit.getRowValue().getIdSideB(), filterSubCategoryA.getValue(),
								filterSubCategoryB.getValue(), Login.userID);
						edit.getTableView().getColumns().get(0).setVisible(false);
						edit.getTableView().getColumns().get(0).setVisible(true);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		});

		// Set up the columns
		TableColumn<FileCardsDB, String> sideB = new TableColumn<FileCardsDB, String>("SideB");

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
				int wordID2;

				// We have to delete the old entries and create new entries
				// Get the IDs of the current entries
				if (filterCategory.getValue().equals("Translation")) {

					try {
						updateTranslate(edit.getRowValue().getSideA(), wordNew, edit.getRowValue().getIdSideA(),
								edit.getRowValue().getIdSideB(), filterSubCategoryA.getValue(),
								filterSubCategoryB.getValue(), Login.userID);
						// Refresh tableview
						edit.getTableView().getColumns().get(0).setVisible(false);
						edit.getTableView().getColumns().get(0).setVisible(true);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else if (filterCategory.getValue().equals("Definition")) {

					try {
						// We need to update the table Definition with the new entity
						updateDefinition(edit.getRowValue().getSideA(), wordNew, edit.getRowValue().getIdSideA(),
								edit.getRowValue().getIdSideB(), filterSubCategoryA.getValue(),
								filterSubCategoryB.getValue(), Login.userID);
						edit.getTableView().getColumns().get(0).setVisible(false);
						edit.getTableView().getColumns().get(0).setVisible(true);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		});

		// Set the tableView editable
		table.setEditable(true);

		// Add columns to the tableView
		table.getColumns().addAll(sideA, sideB);

		// Read entries from database for Start
		try {
			rs = HSQLDB.getInstance().query("SELECT w1.wordID, w2.wordID, w1.word, w2.word  "
					+ "FROM (Words w1 join Words w2 on w1.UserID = w2.UserID) "
					+ "join Translate t on t.wordID1=w1.wordID and t.wordID2=w2.wordID " + "where w1.userid = -1");
			// Iterate through the ResultSet and add each row to the Observable List
			while (rs.next()) {
				data.add(new FileCardsDB(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4)));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Feed the tableview with the data of the observable list
		table.setItems(data);

		// Fill Comboboxes with options
		filterCategory = new ComboBox<String>(optionsCategory);
		filterSubCategoryA = new ComboBox<String>();
		filterSubCategoryB = new ComboBox<String>();

		// Set default text of Comboboxes
		filterCategory.setPromptText("Please Choose");
		filterSubCategoryA.setPromptText("Please Choose");
		filterSubCategoryA.setDisable(true);
		filterSubCategoryB.setPromptText("Please Choose");
		filterSubCategoryB.setDisable(true);
		// Set size of the columns
		sideA.setPrefWidth(150);
		sideA.setMaxWidth(700);
		sideA.setMinWidth(50);
		sideB.setPrefWidth(150);
		sideB.setMaxWidth(700);
		sideB.setMinWidth(50);
		// Set columns to grow equally in width
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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
		filterCategory.setMaxWidth(sideA.getMaxWidth());
		filterCategory.setMinWidth(sideA.getMinWidth());
		filterCategory.setPrefWidth(sideA.getPrefWidth());
		// Let elements grow equally in horizontal direction
		HBox.setHgrow(addButton, Priority.ALWAYS);
		HBox.setHgrow(deleteButton, Priority.ALWAYS);
		HBox.setHgrow(addSideA, Priority.ALWAYS);
		HBox.setHgrow(addSideB, Priority.ALWAYS);
		HBox.setHgrow(filterSubCategoryA, Priority.ALWAYS);
		HBox.setHgrow(filterSubCategoryB, Priority.ALWAYS);
		HBox.setHgrow(filterCategory, Priority.ALWAYS);

		// ActionListener for key pressed (Enter) when adding entry and focusing on
		// textfieldSideA again
		addSideB.setOnKeyReleased(e -> {
			if (!(addSideA.getText().isEmpty()) && e.getCode().equals(KeyCode.ENTER)) {
				addButton.fire();
				addSideA.requestFocus();
			}
		});

		/*
		 * EventHandler to delete selected row with delete Button when editing mode of
		 * cell is not entered (table.editingCellProperty().get() == null)
		 */
		table.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if ((table.editingCellProperty().getValue() == null)
						&& (e.getCode().equals(KeyCode.DELETE) || e.getCode().equals(KeyCode.BACK_SPACE))) {
					deleteCurrentEntry();
				}
			}
		});

		// Actionlistener for removing an entry
		deleteButton.setOnAction(edit -> {
			if ((filterCategory.getValue() != null) && (filterSubCategoryA.getValue() != null)
					&& (filterSubCategoryB.getValue() != null)) {
				deleteCurrentEntry(addSideA.getText(), addSideB.getText());
			}
			addSideA.clear();
			addSideB.clear();
			addSideA.requestFocus();

		});

		// Actionlistener for Button to add new entries
		addButton.setOnAction(e -> {
			// Insert new entry in database
			Integer wordID1;
			Integer wordID2;

			try {
				if ((filterCategory.getValue() != null) && (filterSubCategoryA.getValue() != null)
						&& (filterSubCategoryB.getValue() != null)) {

					if (filterCategory.getValue().equals("Translation")) {
						rs = insertTranslate(addSideA.getText(), addSideB.getText(), filterSubCategoryA.getValue(),
								filterSubCategoryB.getValue(), Login.userID);

					} else if (filterCategory.getValue().equals("Definition")) {
						rs = insertDefinition(addSideA.getText(), addSideB.getText(), filterSubCategoryA.getValue(),
								filterSubCategoryB.getValue(), Login.userID);
					}
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// Set textfields to placeholder
			addSideA.clear();
			addSideB.clear();
		});

		// Filter Functionality for database

		// Add ChangeListener to the Combobox Category
		filterCategory.valueProperty().addListener((obs, oldValue, newValue) -> {
			filterSubCategoryA.setValue(null);
			filterSubCategoryB.setValue(null);
			sideA.setText("SideA");
			sideB.setText("SideB");
			filterSubCategoryA.setPromptText("Please Choose");
			filterSubCategoryB.setPromptText("Please Choose");
			if (filterCategory.getValue().equals("Definition")) {
				filterSubCategoryB.setItems(optionsSubCategoryDefintion);
				filterSubCategoryB.setValue("Definition");
				filterSubCategoryB.setDisable(true);
				sideB.setText(filterSubCategoryB.getValue().toString());
				filterSubCategoryA.setItems(optionsSubCategoryLanguage);
				filterSubCategoryA.setDisable(false);
				addButton.setDisable(true);
				deleteButton.setDisable(true);
			} else if (filterCategory.getValue().equals("Translation")) {
				filterSubCategoryA.setItems(optionsSubCategoryLanguage);
				filterSubCategoryA.setDisable(false);
				filterSubCategoryB.setItems(optionsSubCategoryLanguage);
				filterSubCategoryB.setDisable(false);
				addButton.setDisable(true);
				deleteButton.setDisable(true);
			}

			data.clear();
		});
		// Add ChangeListener to the Combobox SubCategoryA
		filterSubCategoryA.valueProperty().addListener((obs, oldValue, newValue) -> {
			sideA.setText(filterSubCategoryA.getValue());
			try {
				if (filterCategory.getValue().equals("Translation") && (filterSubCategoryA.getValue() != null)
						&& (filterSubCategoryB.getValue() != null)) {

					rs = HSQLDB.getInstance()
							.query("select w1.wordid, w2.wordid, w1.word, w2.word "
									+ "from words w1 join words w2 on w1.userid= w1.userid "
									+ "where  ((w1.wordid, w2.wordid) in (select wordid1, wordid2 from translate) "
									+ "or (w1.wordid, w2.wordid) in (select wordid2, wordid1 from translate)) "
									+ "and (w1.language = '" + filterSubCategoryA.getValue() + "'  and w2.language='"
									+ filterSubCategoryB.getValue() + "') " + "and w1.userid = " + Login.userID + " ");

					addButton.setDisable(false);
					deleteButton.setDisable(false);
				} else if (filterCategory.getValue().equals("Definition") && (filterSubCategoryA.getValue() != null)) {
					rs = HSQLDB.getInstance()
							.query("SELECT w.wordID, d.definitionID, w.word, d.Definition  "
									+ "FROM Words w NATURAL JOIN Definition d " + "WHERE w.UserID = " + Login.userID
									+ "AND w.Language = '" + filterSubCategoryA.getValue() + "' ");
					sideB.setText(filterSubCategoryB.getValue());
					addButton.setDisable(false);
					deleteButton.setDisable(false);
				}
				data.clear();
				while (rs.next()) {
					data.add(new FileCardsDB(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4)));
				}

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		// Add ChangeListener to the Combobox SubCategoryB
		filterSubCategoryB.valueProperty().addListener((obs, oldValue, newValue) -> {
			sideB.setText(filterSubCategoryB.getValue());
			try {
				if (filterCategory.getValue().equals("Translation") && (filterSubCategoryA.getValue() != null)
						&& (filterSubCategoryB.getValue() != null)) {

					rs = HSQLDB.getInstance()
							.query("select w1.wordid, w2.wordid, w1.word, w2.word "
									+ "from words w1 join words w2 on w1.userid= w1.userid "
									+ "where  ((w1.wordid, w2.wordid) in (select wordid1, wordid2 from translate) "
									+ "or (w1.wordid, w2.wordid) in (select wordid2, wordid1 from translate)) "
									+ "and (w1.language = '" + filterSubCategoryA.getValue() + "'  and w2.language='"
									+ filterSubCategoryB.getValue() + "') " + "and w1.userid = " + Login.userID + " ");

					addButton.setDisable(false);
					deleteButton.setDisable(false);

				}
				data.clear();
				while (rs.next()) {
					data.add(new FileCardsDB(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4)));
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		// Arrange different elements in boxes and bind them together in grid
		hboxTextFields.getChildren().addAll(addSideA, addSideB);
		hboxButtons.getChildren().addAll(addButton, deleteButton);
		hboxSubcategory.getChildren().addAll(filterSubCategoryA, filterSubCategoryB);
		hboxTitle.getChildren().addAll(filterCategory);

		root.getChildren().addAll(hboxTitle, hboxSubcategory, table, hboxTextFields, hboxButtons);

		// Set location of different boxes
		GridPane.setConstraints(hboxTitle, 0, 0);
		GridPane.setConstraints(hboxSubcategory, 0, 1);
		GridPane.setConstraints(table, 0, 2);
		GridPane.setConstraints(hboxTextFields, 0, 3);
		GridPane.setConstraints(hboxButtons, 0, 4);

		// Set size properties of the stage
		editStage.setWidth(400);
		editStage.setHeight(450);
		editStage.setMaxWidth(addSideA.getMaxWidth() + addSideB.getMaxWidth());

		// Bin the width of the table(and following also the other elements) to the
		// width of the stage
		table.prefWidthProperty().bind(editStage.widthProperty());

		// The scene is characterized by the gridlayout
		Scene scene = new Scene(root);
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setPrefSize(editStage.getWidth(), editStage.getHeight());
		root.setMinSize(editStage.getWidth(), editStage.getHeight());
		editStage.setScene(scene);
		editStage.show();
	}
}
