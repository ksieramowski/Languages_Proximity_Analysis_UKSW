package pl.zespolowy.graphs;

import lombok.Getter;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import pl.zespolowy.Language.Language;

import java.util.*;

// TODO is mapPreparedForJson the best name for variable? (in this file)

@Getter
public class ProximityGraphs {

    // TODO create graph for overall language proximity
    private final SingleGraph overallLanguageProximityGraph;
    private final SingleGraph themesLanguageProximityGraphs;

    private final String rootPathToFlags = "src/main/resources/flags/";
    private final String level1 = "size: 5px; fill-color: #2eff00;"; // [0.0 - 0.2)
    private final String level2 = "size: 4px; fill-color: #94ff00;"; // [0.2 - 0.4)
    private final String level3 = "size: 3px; fill-color: #f0f500;"; // [0.4 - 0.6)
    private final String level4 = "size: 2px; fill-color: #ff9e00;"; // [0.6 - 0.8)
    private final String level5 = "size: 1px; fill-color: #ff3600;"; // [0.8 - 1.0]

    public ProximityGraphs(final Map<Language, Map<Language, Double>> finalResults,
                           final Map<String, Map<Language, Map<Language, Double>>> themeResults) {
        this.overallLanguageProximityGraph = createOverallGraph(finalResults);
        this.themesLanguageProximityGraphs = createThemeGraphs(themeResults);
    }

    private SingleGraph createOverallGraph(Map<Language, Map<Language, Double>> finalResults) {
        SingleGraph graph = new SingleGraph("Main Graph");
        System.out.println(finalResults);

        graph.setAttribute("ui.stylesheet", "graph { fill-color: #ddcccc; }");

        for (var mainLanguageSet : finalResults.entrySet()) {
            String mainLanguage = mainLanguageSet.getKey().getCode();

            if (graph.getNode(mainLanguage) == null) {
                Node node = graph.addNode(mainLanguage);
                node.setAttribute("ui.label", mainLanguage);
                node.setAttribute("ui.style",
                        "size: 0.5gu; shape: circle; fill-mode: image-scaled; fill-image: url('"
                                + this.rootPathToFlags + mainLanguage.toLowerCase() + ".png');");
            }

            for (var subLanguageSet : mainLanguageSet.getValue().entrySet()) {
                String subLanguage = subLanguageSet.getKey().getCode();
                if (graph.getNode(subLanguage) == null) {
                    Node subNode = graph.addNode(subLanguage);
                    subNode.setAttribute("ui.label", subLanguage);
                    subNode.setAttribute("ui.style",
                            "size: 0.5gu; shape: circle; fill-mode: image-scaled; fill-image: url('"
                                    + this.rootPathToFlags + subLanguage.toLowerCase() + ".png');");
                }

                if (ifEdgeExists("", mainLanguage, subLanguage, graph)) {
                    continue;
                }
                Edge e = graph.addEdge(mainLanguage + "-" + subLanguage, mainLanguage, subLanguage);
                double proximity = subLanguageSet.getValue();
                e.setAttribute("layout.weight", proximity * 10);
                e.setAttribute("proximity", proximity);
                e.setAttribute("ui.label", String.format("%.2f", proximity));
                this.edgeStyleSetter(e);
            }
        }

        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");

        return graph;
    }


    // TODO is this map the best option to retrieve data?
    private SingleGraph createThemeGraphs(final Map<String, Map<Language, Map<Language, Double>>> data) {

        SingleGraph graph = new SingleGraph("Theme Graphs");

        graph.setAttribute("ui.stylesheet", "graph { fill-color: #ddcccc; }");

        SpriteManager spriteManager = new SpriteManager(graph);

        for (var themeSet : data.entrySet()) {
            String theme = themeSet.getKey();
            for (var mainLanguageSet : themeSet.getValue().entrySet()) {
                String mainLanguage = mainLanguageSet.getKey().getCode();
                if (graph.getNode(theme + "_" + mainLanguage) == null) {
                    Node node = graph.addNode(theme + "_" + mainLanguage);
                    node.setAttribute("ui.label", mainLanguage);
                    node.setAttribute("ui.style",
                            "size: 0.5gu; shape: circle; fill-mode: image-scaled; fill-image: url('"
                                    + this.rootPathToFlags + mainLanguage.toLowerCase() + ".png');");
                }
                StringBuilder edgeName = new StringBuilder(theme + "_" + mainLanguage);
                for (var subLanguageSet : mainLanguageSet.getValue().entrySet()) {
                    String subLanguage = subLanguageSet.getKey().getCode();
                    if (graph.getNode(theme + "_" + subLanguage) != null) {
                        if (ifEdgeExists(theme, mainLanguage, subLanguage, graph)) {
                            continue;
                        }
                    } else {
                        Node node = graph.addNode(theme + "_" + subLanguage);
                        node.setAttribute("ui.style",
                                "size: 0.5gu; shape: circle; fill-mode: image-scaled; fill-image: url('"
                                        + this.rootPathToFlags + subLanguage.toLowerCase() + ".png');");
                    }
                    edgeName.append("-").append(subLanguage);
                    Edge e = graph.addEdge(edgeName.toString(), theme + "_" + mainLanguage, theme + "_" + subLanguage);
                    double proximity = subLanguageSet.getValue();
                    e.setAttribute("layout.weight", proximity * 4.5);
                    e.setAttribute("proximity", proximity);
                    e.setAttribute("ui.label", String.format("%.2f", proximity));
                    this.edgeStyleSetter(e);

                    int targetIndex = edgeName.indexOf(String.valueOf('-'));
                    edgeName.delete(targetIndex, edgeName.length());
                }
            }

            Node nodeForSprite = graph.nodes().filter(node -> node.getId().contains(theme))
                    .findAny().orElseThrow(() -> new NoSuchElementException("No node found matching the theme!"));

            Sprite s = spriteManager.addSprite(theme);
            s.attachToNode(nodeForSprite.getId());
            s.setPosition(StyleConstants.Units.GU, 2, 1, 3);
            s.setAttribute("ui.style", "size: 0px; text-size: 15px; text-color: blue; text-style: bold;");
            s.setAttribute("ui.label", s.getId());
        }


        // For better visualization
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");

        return graph;
    }


    private boolean ifEdgeExists(final String theme, final String language1, final String language2, SingleGraph graph) {
        if (theme.isEmpty()) {
            return graph.getEdge(language1 + "-" + language2) != null
                    || graph.getEdge(language2 + "-" + language1) != null;
        } else {
            return graph.getEdge(theme + "_" + language1 + "-" + language2) != null
                    || graph.getEdge(theme + "_" + language2 + "-" + language1) != null;
        }
    }

    private void edgeStyleSetter(final Edge e) {
        double proximity = e.getNumber("proximity");
        if (proximity < 0.2) {
            e.setAttribute("ui.style", level1);
        } else if (proximity < 0.4) {
            e.setAttribute("ui.style", level2);
        } else if (proximity < 0.6) {
            e.setAttribute("ui.style", level3);
        } else if (proximity < 0.8) {
            e.setAttribute("ui.style", level4);
        } else if (proximity <= 1.0) {
            e.setAttribute("ui.style", level5);
        } else {
            throw new RuntimeException("Languages proximity too large!");
        }
    }
}
