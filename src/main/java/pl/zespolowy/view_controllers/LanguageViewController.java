package pl.zespolowy.view_controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.javafx.FxGraphRenderer;
import pl.zespolowy.Business.Algorithm.LanguageSimilarityCalculator;
import pl.zespolowy.Business.Algorithm.WordSetsRegrouper;
import pl.zespolowy.Business.Algorithm.WordSetsTranslation;
import pl.zespolowy.Business.Algorithm.WordsProximityNormalizer;
import pl.zespolowy.Language.*;
import pl.zespolowy.Words.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.zespolowy.graphs.ProximityGraphs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LanguageViewController {
    //language 'set' or language class?
    @FXML
    private VBox vBox;
    @FXML
    private HBox hBox;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TableView<Language> languageTableView;
    @FXML
    private TableView<WordSet> themeTableView;
    @FXML
    private Button button;
    @FXML
    private TableColumn<WordSet, String> themeNameTableColumn;
    @FXML
    private TableColumn<WordSet, Boolean> themeEnableTableColumn;
    @FXML
    private TableColumn<Language, String> languageNameTableColumn;
    @FXML
    private TableColumn<Language, Boolean> languageEnableTableColumn;

    private LanguageSet languageSet;

    private ObservableList<Language> languages;

    private List<WordSet> wordSetList;

    private ObservableList<WordSet> themes;

    private static final String ROOT_PATH = System.getProperty("user.dir");
    private MainViewController mainViewController;

    @FXML
    public void initialize(){
        VBox.setVgrow(hBox, Priority.ALWAYS);
        HBox.setHgrow(themeTableView, Priority.ALWAYS);
        setThemeValues();
        setLanguageValues();
    }
    private void setThemeValues(){
//        //theme table
        themeTableView.setEditable(true);

        themeNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        themeEnableTableColumn.setCellValueFactory(new PropertyValueFactory<>("enabled"));
        themeEnableTableColumn.setCellFactory(tc-> new CheckBoxTableCell<>());
        themeEnableTableColumn.setCellValueFactory(cellData -> cellData.getValue().getEnabled());


        initWordSets(ROOT_PATH);
        themes = FXCollections.observableArrayList(wordSetList);

        themeTableView.setItems(themes);
    }
    private void setLanguageValues(){

        languageTableView.setEditable(true);

        languageNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        languageEnableTableColumn.setCellValueFactory(new PropertyValueFactory<>("enabled"));
        languageEnableTableColumn.setCellFactory(tc -> new CheckBoxTableCell<>());
        languageEnableTableColumn.setCellValueFactory(cellData -> cellData.getValue().getEnabled());


        String languagesPath = ROOT_PATH + "\\src\\main\\resources/languages.json";
        languageSet = initLanguages(languagesPath);

        if (languageSet == null || languageSet.getLanguages() == null) {
            throw new IllegalStateException("LanguageSet or its list is not initialized!");
        }
        languageSet.print();
        languages = FXCollections.observableArrayList(languageSet.getLanguages());

        //System.out.println(languages.getFirst());
        languageTableView.setItems(languages);
    }

    private LanguageSet initLanguages(String path) {

        try {
            String content = Files.readString(Paths.get(path));
            System.out.println(content + "!!!");
            if (Files.notExists(Paths.get(path))) {
                System.err.println("JSON file not found: " + path);
                return null;
            }
            if (content.isEmpty()) {
                System.err.println("JSON file is empty: " + path);
                return null;
            }

            languageSet = new LanguageSet(content);
            System.out.println(languageSet.getLanguages().size());
            return languageSet;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return languageSet;
    }
    @FXML
    public void onButtonPress(){
        //Serializing Languages and Themes to save updated info to .json
        serializeLanguages();
        serializeWordSets();
        if(!isAtLeastOneWordSetChecked()){
            Alert a = new Alert(Alert.AlertType.NONE);
            EventHandler<ActionEvent> event = new EventHandler<ActionEvent>()
            {
                        public void handle(ActionEvent e)
                        {
                            // set alert type
                            a.setAlertType(Alert.AlertType.ERROR);

                            // show the dialog
                            a.show();
                        }
                    };
        }
        openGraphWindow();
    }
    public void serializeLanguages(){
        languageSet.Serialize();

    }
    public void serializeWordSets() {
            wordSetList.forEach(wordSet -> {
                wordSet.Serialize();
            });

    }
    private boolean isAtLeastOneWordSetChecked(){
        for (WordSet theme : themes) {
            BooleanProperty booleanProperty = new SimpleBooleanProperty(false);
            booleanProperty = theme.getEnabled();
            if(booleanProperty.getValue()) {
                System.out.println("Some value was selected");
                return true;
            }
        }
        System.out.println("No value was selected!");
        return false;
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

    private void openGraphWindow() {
        try {
            // Tworzenie głównych obiektów do grafów
            WordSetsTranslation wst = new WordSetsTranslation();
            WordSetsRegrouper wordSetsRegroup = new WordSetsRegrouper(wst);
            LanguageSimilarityCalculator languageProximity = new LanguageSimilarityCalculator(wordSetsRegroup);
            languageProximity.countProximityAndFillLPRClasses();
            WordsProximityNormalizer wordsProximityNormalizer = new WordsProximityNormalizer(languageProximity, wst);

            ProximityGraphs proximityGraphs = new ProximityGraphs(wordsProximityNormalizer.getFinalResult(),
                    wordsProximityNormalizer.getResultByTopic());

            SingleGraph mainGraph = proximityGraphs.getOverallLanguageProximityGraph();
            SingleGraph themeGraph = proximityGraphs.getThemesLanguageProximityGraphs();

            // Tworzenie widoków dla grafów
            FxViewer view1 = new FxViewer(mainGraph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
            view1.enableAutoLayout();
            FxViewPanel mainPanel = (FxViewPanel) view1.addView(FxViewer.DEFAULT_VIEW_ID, new FxGraphRenderer());

            FxViewer view2 = new FxViewer(themeGraph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
            view2.enableAutoLayout();
            FxViewPanel themePanel = (FxViewPanel) view2.addView(FxViewer.DEFAULT_VIEW_ID, new FxGraphRenderer());

            // Ustawienie układu
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(themePanel);

            HBox hBox = new HBox();
            hBox.setStyle("-fx-background-color: lightblue;");
            hBox.getChildren().addAll(mainPanel);
            HBox.setHgrow(mainPanel, Priority.ALWAYS);
            hBox.getChildren().addAll(stackPane);
            HBox.setHgrow(stackPane, Priority.ALWAYS);

            // Dodanie obsługi kliknięcia dla themePanel
            themePanel.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    themePanel.getCamera().resetView();
                    return;
                }

                double x = mouseEvent.getX();
                double y = mouseEvent.getY();

                Point3 graphCoordinates = themePanel.getCamera().transformPxToGu(x, y);

                themePanel.getCamera().setViewCenter(graphCoordinates.x, graphCoordinates.y, 0);
                themePanel.getCamera().setViewPercent(0.3); // Zoom in
            });

            // Tworzenie nowego okna
            Stage graphWindow = new Stage();
            graphWindow.setTitle("Graphs Window");
            graphWindow.initModality(Modality.WINDOW_MODAL); // Opcjonalnie blokuj interakcję z głównym oknem
            graphWindow.setScene(new Scene(hBox, 1280, 720));
            graphWindow.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
