package pl.zespolowy;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.javafx.FxGraphRenderer;
import pl.zespolowy.Business.Algorithm.*;
import pl.zespolowy.Controllers.MainSceneController;
import pl.zespolowy.Translation.DeepLTranslator;
import pl.zespolowy.Translation.Translator;
import pl.zespolowy.graphs.ProximityGraphs;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException, NoSuchFieldException {

//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main-scene.fxml"));
//        Parent root = fxmlLoader.load();
//        MainSceneController controller = fxmlLoader.getController();
//
//        Translator translator = new DeepLTranslator();
//        controller.setTranslator(translator);
//
//        Scene scene = new Scene(root, 800, 600);

        WordSetsTranslation wst = new WordSetsTranslation();
        WordSetsRegrouper wordSetsRegroup = new WordSetsRegrouper(wst);
        LanguageSimilarityCalculator languageProximity = new LanguageSimilarityCalculator(wordSetsRegroup);
        languageProximity.countProximityAndFillLPRClasses();
        WordsProximityNormalizer wordsProximityNormalizer = new WordsProximityNormalizer(languageProximity, wst);

        ProximityGraphs proximityGraphs = new ProximityGraphs(wordsProximityNormalizer.getFinalResult(),
                                                              wordsProximityNormalizer.getResultByTopic());

        SingleGraph mainGraph = proximityGraphs.getOverallLanguageProximityGraph();
        SingleGraph themeGraph = proximityGraphs.getThemesLanguageProximityGraphs();

        HBox hBox = new HBox();

        // Overall
        FxViewer view1 = new FxViewer(mainGraph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        view1.enableAutoLayout();
        FxViewPanel mainPanel = (FxViewPanel) view1.addView(FxViewer.DEFAULT_VIEW_ID, new FxGraphRenderer());
        //FxDefaultView panel = (FxDefaultView) view.addDefaultView(false);
        hBox.getChildren().addAll(mainPanel);

        mainPanel.setOnMouseClicked(mouseEvent -> {
            System.out.println("Main Panel view center: " + mainPanel.getCamera().getViewCenter());
        });


        // Themes
        FxViewer view2 = new FxViewer(themeGraph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        view2.enableAutoLayout();
        FxViewPanel themePanel = (FxViewPanel) view2.addView(FxViewer.DEFAULT_VIEW_ID, new FxGraphRenderer());
        //FxDefaultView panel = (FxDefaultView) view.addDefaultView(false);
        hBox.getChildren().addAll(themePanel);

        themePanel.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                // Reset the camera to the default view
                themePanel.getCamera().resetView();
                return;
            }


            double x = mouseEvent.getX();
            double y = mouseEvent.getY();


            // Convert the adjusted coordinates to graph-space coordinates
            Point3 graphCoordinates = themePanel.getCamera().transformPxToGu(x, y);
            Point3 graphCenter = themePanel.getCamera().getViewCenter();
            System.out.println(themePanel.getCamera().transformGuToPx(graphCenter.x, graphCenter.y, 0));


            // Center the view on the clicked graph-space point
            //themePanel.getCamera().setViewCenter(graphCoordinates.x, graphCoordinates.y, 0);


            // Zoom in
            themePanel.getCamera().setViewPercent(0.9);
        });


        Scene scene = new Scene(hBox, 1280, 720);

        stage.setTitle("Windows");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {



        launch();
    }
}