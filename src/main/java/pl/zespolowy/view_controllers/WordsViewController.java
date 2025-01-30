package pl.zespolowy.view_controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import pl.zespolowy.Words.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordsViewController {


    @FXML
    private HBox hBox;
    @FXML
    private TableView<WordSet> wordSetTableView;
    @FXML
    private TableColumn<WordSet, Boolean> wordSetBooleanTableColumn;
    @FXML
    private TableColumn<WordSet, String> wordSetNameTableColumn;
    @FXML
    private TableView<Word> wordTableView;
    @FXML
    private TableColumn<Word, String> wordStringTableColumn;

    private WordSet wordSet;
    private List<WordSet> wordSetList;
    private ObservableList<WordSet> wordSetObservableList;
    private ObservableList<Word> wordObservableList;

    private static final String ROOT_PATH = System.getProperty("user.dir");

    @FXML
    public void initialize(){
        setValues();
    }
    private void setValues(){

        wordStringTableColumn.setCellValueFactory(new PropertyValueFactory<>("text"));


        wordSetBooleanTableColumn.setCellValueFactory(new PropertyValueFactory<>("enabled"));
        wordSetNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        wordSetBooleanTableColumn.setCellFactory(tc->new CheckBoxTableCell<>());
        wordSetBooleanTableColumn.setCellValueFactory(cellData-> cellData.getValue().getEnabled());

        String wordSetPath = ROOT_PATH;
        wordSetList = initWordSets(wordSetPath);
        wordSetObservableList = FXCollections.observableArrayList(wordSetList);

        wordObservableList = FXCollections.observableArrayList();
        wordSetTableView.setItems(wordSetObservableList);

        wordSetTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadWordsForSelectedSet(newSelection);
            }
        });


    }
    private void loadWordsForSelectedSet(WordSet wordSet){

        wordObservableList.clear();

        // Dodaj nowe dane z wybranego zestawu słów
        if (wordSet.getWords() != null) {
            wordObservableList.addAll(wordSet.getWords());
        }

        // Ustaw dane w tabeli słów
        wordTableView.setItems(wordObservableList);
    }
    private List initWordSets(String pathArg) {

        wordSetList = new ArrayList<WordSet>();
        String path = pathArg + "\\src\\main\\resources/wordSets/";
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            String[] fileNames = dir.list();

            if (fileNames != null) {
                for (String fileName : fileNames) {
                    try {
                        String title = fileName.split(".json")[0];
                        String content = Files.readString(Paths.get(path + fileName));

                        SimpleBooleanProperty newBool = new SimpleBooleanProperty(false);
                        WordSet wordSet = new WordSet(title, content);
                        wordSetList.add(wordSet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(fileName);
                }
                return wordSetList;

            } else {
                System.out.println("The current directory is empty or an error occurred.");
            }
        } else {
            System.out.println("The current directory does not exist or is not a directory.");
        }
        return wordSetList;
    }
    
}
