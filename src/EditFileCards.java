import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * This class is used to control and process data in a tableview and give the
 * user the ability to filter for specific categories and to manipulate(adding,
 * deleting, importing, exporting) data
 * 
 * 
 * @author Erik
 *
 */
public class EditFileCards {

	// Observable List to feed tableview with data with the object type FileCardsDB
	private final ObservableList<FileCardsDB> data = FXCollections.observableArrayList();
	// Result of a sql query
	private ResultSet rs = null;
	// ArrayList for importing new data
	ArrayList<FileCardsDB> dataNew = new ArrayList<>();
	// FileChooser
	FileChooser filechooser = new FileChooser();
	// The instance of the view class
	EditFileCardsLayout view;
	//Parent View User Interface
	UserInterface ui;
	Stage uiStage;
	
	public EditFileCards(Stage uiStage,UserInterface ui) {
		//Reference of userInterface
		this.ui = ui;
		this.uiStage = uiStage;
		//Hide userInterface
		uiStage.hide();
		// Construct the View
		view = new EditFileCardsLayout(this);
		// Prepare the columns of the TableView
		view.setColumns();
		// Feed the tableview with initial data
		initialValues(view.getFilterCategory().getValue(), view.getFilterSubCategoryA().getValue(),
				view.getFilterSubCategoryB().getValue(), view.getTable());
		// add ActionListener to buttons ect.
		view.setActionListener();
		// add Key Listener to different Elements
		view.setKeyListener();
		// addFilterListener to refresh tableview depending on chosen categories
		view.setFilterListener();
		view.setWindowListener();
	}

	/**
	 * This method checks if a word already is present in a DB Table by a select
	 * query. If the Resultset is not empty, the word already exists for the user
	 * and the language.
	 * 
	 * @param wordNew,
	 *            the word to check
	 * @param language,
	 *            the language of the word
	 * @param userID,
	 *            the userID of the user
	 * @return the ResultSet of the query
	 */
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

	/**
	 * This method inserts a word in a DB Table and returns the ResultSet with the
	 * new generated wordID
	 * 
	 * @param wordNew,
	 *            the word to insert
	 * @param language,
	 *            the language of the word
	 * @param userID,
	 *            the userID of the User @return, The ResultSet with the worID of
	 *            the inserted word
	 */
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

	/**
	 * This method checks if the translate entity already exists and returns the
	 * ResultSet
	 * 
	 * @param wordID1,
	 *            the wordID1 of the first word
	 * @param wordID2,
	 *            the wordID2 of the second word
	 * @return the ResultSet, if empty, the entity doesn`t exist
	 */
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

	/**
	 * This method inserts a new DB Translate or Definition Table entry if it does
	 * not already exists and returns the ResultSet of the new entry or null.
	 * 
	 * @param word1,
	 *            the word of the first column to be insert
	 * @param word2,
	 *            the word of the second column to be insert
	 * @param language1,
	 *            the language(SubCategory) of the first word to be insert
	 * @param language2,
	 *            the language(SubCategory) of the second word to be insert
	 * @param userID,
	 *            the userID of the User
	 * @param category,
	 *            the category of the Entry
	 * @return the ResultSet of the new inserted Translate entry
	 */
	public ResultSet insertEntry(String word1, String word2, String language1, String language2, String category,
			int userID) {
		ResultSet rs = null;
		int wordID1;
		int wordID2;
		try {
			if (category.equals("Translation")) {
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
					HSQLDB.getInstance().update(
							"INSERT INTO Translate (wordid1, wordid2)" + "values(" + wordID1 + "," + wordID2 + ")");
					data.add(new FileCardsDB(wordID1, wordID2, word1, word2, category, language1, language2));
				} else {
					System.out.println("Already existing translate");
				}
				rs = HSQLDB.getInstance()
						.query("SELECT * " + "FROM Translate " + "WHERE (WordID1 =" + wordID1 + " " + "AND WordID2 ="
								+ wordID2 + ") " + "OR (WordID1 =" + wordID2 + " " + "AND WordID2 =" + wordID1 + ")");
			} else if (category.equals("Definition")) {
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
					data.add(new FileCardsDB(wordID1, wordID2, word1, word2, category, language1, language2));
				} else {
					System.out.println("Already existing definition");
				}
				rs = HSQLDB.getInstance().query("SELECT * " + "FROM Definition " + "WHERE WordID =" + wordID1 + " "
						+ "AND definition ='" + word2 + "'");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;

	}

	/**
	 * This method checks if an entry of the DB Definition Table already exists. If
	 * the return ResultSet is empty the entry does not already exist.
	 * 
	 * @param wordID1,
	 *            the wordID of the word of the definition entry to be checked
	 * @param definition,
	 *            the definition of the definition entry to be checked
	 * @return the Resultset, empty if no duplicate
	 */
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

	/**
	 * This method updates the DB defintion or translate table entity and checks if
	 * the new entity and the new words already exist and returns the new ResultSet.
	 * If the entity already exists, it deletes the current entity
	 * 
	 * @param word1New,
	 *            the new word of the first column
	 * @param word2New,
	 *            the new word of the secon column
	 * @param wordID1old,
	 *            the wordID of the first old entry
	 * @param wordID2old,
	 *            the wordID of the second old entry
	 * @param language1,
	 *            the language(SubCategory) of the first entry
	 * @param language2,
	 *            the language(SubCategory) of the second entry
	 * @param category,
	 *            the category of the entry
	 * @param table,
	 *            the instance of the table representing the data
	 * @param userID,
	 *            the UserID of the User
	 * @return the ResultSet of the new entry
	 */
	public ResultSet updateEntry(String word1New, String word2New, int wordID1old, int wordID2old, String language1,
			String language2, String category, TableView<FileCardsDB> table, int userID) {
		ResultSet rs = null;
		int wordID1;
		int wordID2;
		try {
			if (category.equals("Translation")) {
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
					deleteCurrentEntry(view.getFilterCategory().getValue(), view.getTable());
				}
			} else if (category.equals("Definition")) {
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
					deleteCurrentEntry(view.getFilterCategory().getValue(), view.getTable());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * This method deletes the highlighted row in the tableview. It distinguishes
	 * between the categories translation and definition and also deletes the words
	 * from the DB words table if the words don`t point on ther entries to keep
	 * entries clean.
	 * 
	 * @param category,
	 *            the category of the entry to be deleted
	 * @param table,
	 *            the instance of the table representing the data
	 */
	public void deleteCurrentEntry(String category, TableView<FileCardsDB> table) {

		if (table.getSelectionModel().getSelectedItem() != null
				&& table.getSelectionModel().getSelectedItem() != null) {
			int wordID1 = table.getSelectionModel().getSelectedItem().getIdSideA();
			int wordID2 = table.getSelectionModel().getSelectedItem().getIdSideB();
			// Remove the current entry (with the old values) from the observable list
			data.remove(table.getSelectionModel().getSelectedItem());
			// Remove the current entry (with the old values) from the database
			try {
				if (category.equals("Translation")) {
					HSQLDB.getInstance()
							.update("DELETE FROM Translate " + "where (WordID1 = " + wordID1 + " " + " AND WordID2 = "
									+ wordID2 + ") " + " OR (WordID1 = " + wordID2 + " " + " AND WordID2 = " + wordID1
									+ ")");
					deleteWords(wordID1);
					deleteWords(wordID2);

				} else if (category.equals("Definition")) {
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

	/**
	 * Deletes an entry identified by two words and the chosen category translation
	 * or definition. It also deletes the words when they are not pointing on other
	 * entries.
	 * 
	 * @param word1,
	 *            the word of the first column
	 * @param word2,
	 *            the word of the secon column
	 * @param category,
	 *            the category of the entry to be deleted
	 */
	public void deleteCurrentEntry(String word1, String word2, String category) {

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
				if (category.equals("Translation")) {
					HSQLDB.getInstance()
							.update("DELETE FROM Translate " + "where (WordID1 = " + wordID1 + " " + " AND WordID2 = "
									+ wordID2 + ") " + " OR (WordID1 = " + wordID2 + " " + " AND WordID2 = " + wordID1
									+ ")");
					deleteWords(wordID1);
					deleteWords(wordID2);

				} else if (category.equals("Definition")) {
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

	/**
	 * This method deletes a given word, identified by the wordID, if it does not
	 * point on a translate or definition entry.
	 * 
	 * @param wordID
	 *            of the word to be deleted
	 */
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

	/**
	 * This method disables all buttons in the view
	 */
	public void disableButtons() {
		view.disableButtons();
	}

	/**
	 * This method enables all buttons in the view
	 */
	public void enableButtons() {
		view.enableButtons();
	}

	/**
	 * This method writes all entries of the current view to an object file which
	 * the user can choose with a filechooser
	 * 
	 * @param category
	 *            is the category of the entries to be written
	 * @param subCategoryA
	 *            is the subCategory of the first column
	 * @param subCategoryB
	 *            is the subCategory of the second column
	 * @param editStage
	 *            is the current Stage
	 */
	public void exportData(String category, String subCategoryA, String subCategoryB, Stage editStage) {
		if ((category != null) && (subCategoryA != null) && (subCategoryA != null)) {
			try {
				ArrayList<FileCardsDB> dataWrite = new ArrayList<>();

				for (int i = 0; i < data.size(); i++) {
					dataWrite.add(data.get(i));
				}
				File f = filechooser.showSaveDialog(editStage);
				if (f != null) {
					ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
					oos.writeObject(dataWrite);
					oos.close();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * This method imports a object file when the arrangement of chosen category,
	 * subCategoryA and subCategoryB matches. When successfully imported it saves
	 * the data in the DB.
	 * 
	 * @param category
	 *            is the category of the entries to be read have to have
	 * @param subCategoryA
	 *            is the subCategory of the first column to be read has to have
	 * @param subCategoryB
	 *            is the subCategory of the second column to be read has to have
	 * @param editStage
	 *            is the current Stage
	 */
	public void importData(String category, String subCategoryA, String subCategoryB, Stage editStage) {
		if ((category != null) && (subCategoryA != null) && (subCategoryB != null)) {
			try {

				File f = filechooser.showOpenDialog(editStage);
				if (f != null) {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
					dataNew = (ArrayList<FileCardsDB>) ois.readObject();
					ois.close();
					for (int i = 0; i < dataNew.size(); i++) {
						if (category.equals(dataNew.get(i).getCat()) && subCategoryA.equals(dataNew.get(i).getSubCatA())
								&& subCategoryB.equals(dataNew.get(i).getSubCatB())) {
							insertEntry(dataNew.get(i).sideA, dataNew.get(i).sideB, dataNew.get(i).subCatA, dataNew.get(i).subCatB,
									dataNew.get(i).cat, Login.userID);
						}
					}
				}
			} catch (FileNotFoundException e) {
				System.out.println("File not found");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * This method speciefies the observable List of the Tableview when the editin
	 * mode is started.
	 * 
	 * 
	 * @param category
	 *            is the category of the entries
	 * @param subCategoryA
	 *            is the subCategory of the first column
	 * @param subCategoryB
	 *            is the subCategory of the second column
	 * @param table
	 *            is the instance of the tableView representing the data
	 */
	public void initialValues(String category, String subCategoryA, String subCategoryB, TableView<FileCardsDB> table) {
		// Read entries from database for Start
		try {
			rs = HSQLDB.getInstance().query("SELECT w1.wordID, w2.wordID, w1.word, w2.word  "
					+ "FROM (Words w1 join Words w2 on w1.UserID = w2.UserID) "
					+ "join Translate t on t.wordID1=w1.wordID and t.wordID2=w2.wordID " + "where w1.userid = -1");
			// Iterate through the ResultSet and add each row to the Observable List
			while (rs.next()) {
				data.add(new FileCardsDB(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), category,
						subCategoryA, subCategoryB));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Feed the tableview with the data of the observable list
		table.setItems(data);
	}

	// Filter Functionality for database

	// Add ChangeListener to the Combobox Category
	/**
	 * This method listens to the choice of the category of the user. It disables
	 * the different Buttons in the view and enables the user to choose the possible
	 * subcategories after the user chose the category.
	 * 
	 * @param category
	 *            is the category of the entries
	 * @param subCategoryA
	 *            is the subCategory of the first column
	 * @param subCategoryB
	 *            is the subCategory of the second column
	 * @param sideA
	 *            is the first column
	 * @param sideB
	 *            is the second column
	 * @param optionsCategory
	 *            is the ObservableList of possible Categories
	 * @param optionsSubCategoryDefintion
	 *            is the ObservableList of possible DefinitionLanguages
	 * @param optionsSubCategoryLanguage
	 *            is the ObservableList of possible Languages
	 */
	public void addFilterCategoryListener(ComboBox<String> category, ComboBox<String> subCategoryA,
			ComboBox<String> subCategoryB, TableColumn<FileCardsDB, String> sideA,
			TableColumn<FileCardsDB, String> sideB, ObservableList<String> optionsCategory,
			ObservableList<String> optionsSubCategoryDefintion, ObservableList<String> optionsSubCategoryLanguage) {
		subCategoryA.setValue(null);
		subCategoryB.setValue(null);
		sideA.setText("SideA");
		sideB.setText("SideB");
		subCategoryA.setPromptText("Please Choose");
		subCategoryB.setPromptText("Please Choose");
		if (category.getValue().equals("Definition")) {
			subCategoryB.setItems(optionsSubCategoryDefintion);
			subCategoryB.setValue("Definition");
			subCategoryB.setDisable(true);
			sideB.setText(subCategoryB.getValue().toString());
			subCategoryA.setItems(optionsSubCategoryLanguage);
			subCategoryA.setDisable(false);
			disableButtons();

		} else if (category.getValue().equals("Translation")) {
			subCategoryA.setItems(optionsSubCategoryLanguage);
			subCategoryA.setDisable(false);
			subCategoryB.setItems(optionsSubCategoryLanguage);
			subCategoryB.setDisable(false);
			disableButtons();
		}

		data.clear();
	}

	// Add ChangeListener to the Combobox SubCategoryA
	/**
	 * This method listens to changes in the choice of the subcategory of the first
	 * row. It distincts in its behaviour between the categories translate and
	 * definition. It chechks the values in the category list and subCategory of the
	 * second row.
	 * 
	 * @param category
	 *            is the category of the entries
	 * @param subCategoryA
	 *            is the subCategory of the first column
	 * @param subCategoryB
	 *            is the subCategory of the second column
	 * @param sideA
	 *            is the first column
	 * @param sideB
	 *            is the second column
	 * @param optionsCategory
	 *            is the ObservableList of possible Categories
	 * @param optionsSubCategoryDefintion
	 *            is the ObservableList of possible DefinitionLanguages
	 * @param optionsSubCategoryLanguage
	 *            is the ObservableList of possible Languages
	 */
	public void addFilterSubCategoryAListener(ComboBox<String> category, ComboBox<String> subCategoryA,
			ComboBox<String> subCategoryB, TableColumn<FileCardsDB, String> sideA,
			TableColumn<FileCardsDB, String> sideB, ObservableList<String> optionsCategory,
			ObservableList<String> optionsSubCategoryDefintion, ObservableList<String> optionsSubCategoryLanguage) {
		sideA.setText(subCategoryA.getValue());
		try {
			if (category.getValue().equals("Translation") && (subCategoryA.getValue() != null)
					&& (subCategoryB.getValue() != null)) {

				rs = HSQLDB.getInstance()
						.query("select w1.wordid, w2.wordid, w1.word, w2.word "
								+ "from words w1 join words w2 on w1.userid= w1.userid "
								+ "where  ((w1.wordid, w2.wordid) in (select wordid1, wordid2 from translate) "
								+ "or (w1.wordid, w2.wordid) in (select wordid2, wordid1 from translate)) "
								+ "and (w1.language = '" + subCategoryA.getValue() + "'  and w2.language='"
								+ subCategoryB.getValue() + "') " + "and w1.userid = " + Login.userID + " ");

				enableButtons();
			} else if (category.getValue().equals("Definition") && (subCategoryA.getValue() != null)) {
				rs = HSQLDB.getInstance()
						.query("SELECT w.wordID, d.definitionID, w.word, d.Definition  "
								+ "FROM Words w NATURAL JOIN Definition d " + "WHERE w.UserID = " + Login.userID
								+ "AND w.Language = '" + subCategoryA.getValue() + "' ");
				sideB.setText(subCategoryB.getValue());
				enableButtons();
			}
			data.clear();
			while (rs.next()) {
				data.add(new FileCardsDB(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
						category.getValue(), subCategoryA.getValue(), subCategoryB.getValue()));
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// Add ChangeListener to the Combobox SubCategoryB
	/**
	 * This method listens to changes in the choice of the subcategory of the second
	 * row. It distincts in its behaviour between the categories translate and
	 * definition. It checks the values in the category list and subCategory of the
	 * first row.
	 * 
	 * @param category
	 *            is the category of the entries
	 * @param subCategoryA
	 *            is the subCategory of the first column
	 * @param subCategoryB
	 *            is the subCategory of the second column
	 * @param sideA
	 *            is the first column
	 * @param sideB
	 *            is the second column
	 * @param optionsCategory
	 *            is the ObservableList of possible Categories
	 * @param optionsSubCategoryDefintion
	 *            is the ObservableList of possible DefinitionLanguages
	 * @param optionsSubCategoryLanguage
	 *            is the ObservableList of possible Languages
	 */
	public void addFilterSubCategoryBListener(ComboBox<String> category, ComboBox<String> subCategoryA,
			ComboBox<String> subCategoryB, TableColumn<FileCardsDB, String> sideA,
			TableColumn<FileCardsDB, String> sideB, ObservableList<String> optionsCategory,
			ObservableList<String> optionsSubCategoryDefintion, ObservableList<String> optionsSubCategoryLanguage) {

		sideB.setText(subCategoryB.getValue());
		try {
			if (category.getValue().equals("Translation") && (subCategoryA.getValue() != null)
					&& (subCategoryB.getValue() != null)) {

				rs = HSQLDB.getInstance()
						.query("select w1.wordid, w2.wordid, w1.word, w2.word "
								+ "from words w1 join words w2 on w1.userid= w1.userid "
								+ "where  ((w1.wordid, w2.wordid) in (select wordid1, wordid2 from translate) "
								+ "or (w1.wordid, w2.wordid) in (select wordid2, wordid1 from translate)) "
								+ "and (w1.language = '" + subCategoryA.getValue() + "'  and w2.language='"
								+ subCategoryB.getValue() + "') " + "and w1.userid = " + Login.userID + " ");

				enableButtons();

			}
			data.clear();
			while (rs.next()) {
				data.add(new FileCardsDB(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
						category.getValue(), subCategoryA.getValue(), subCategoryB.getValue()));
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * This method specifies what should happen when the ENTER Key is pressed. It
	 * makes the addButton to fire action when the focus is in the second textfield
	 * and if the textfields are filled.
	 * 
	 * @param addSideA
	 *            the TextFielf of the Textfield of the first row
	 * @param addButton
	 *            the addButton to add an Entry
	 * @param e
	 *            the KeyEvent
	 */
	public void addKeyListener(TextField addSideA, Button addButton, KeyEvent e) {

		// ActionListener for key pressed (Enter) when adding entry and focusing on
		// textfieldSideA again

		if (!(addSideA.getText().isEmpty()) && e.getCode().equals(KeyCode.ENTER)) {
			addButton.fire();
			addSideA.requestFocus();
		}
	}

	/**
	 * This method is called when focused on the tableview, the EventHandler to
	 * delete the selected row with delete and Back_Space Key when editing mode of
	 * cell is not entered (table.editingCellProperty().get() == null)
	 * 
	 * @param table
	 *            is the instance of the tableview representing the data
	 * @param e
	 *            the KeyEvent
	 */
	public void deleteKeyListener(TableView<FileCardsDB> table, KeyEvent e) {

		if ((table.editingCellProperty().getValue() == null)
				&& (e.getCode().equals(KeyCode.DELETE) || e.getCode().equals(KeyCode.BACK_SPACE))) {
			deleteCurrentEntry(view.getFilterCategory().getValue(), view.getTable());
		}

	}
	
	public void closeEditStage(Stage editStage) {
		editStage.close();
		uiStage.show();
	}
	
}
