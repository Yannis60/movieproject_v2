import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class GUI extends Application {

    private final MovieAnalysis analysis = new MovieAnalysis();

    private VBox outputTextArea;     // Text section
    private VBox chartsArea;         // Charts section

    @Override
    public void start(Stage stage) {

        /* ------------------ SIDEBAR ------------------ */
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(260);
        sidebar.getStyleClass().add("sidebar");

        Label menuLabel = new Label("MENU");
        menuLabel.getStyleClass().add("sidebar-title");

        Button btnTopMovies = createSidebarButton("⭐ Top Rated Movies");
        Button btnGenre = createSidebarButton("📊 Rating by Genre");
        Button btnHistogram = createSidebarButton("📉 Rating Histogram");
        Button btnAgeGroup = createSidebarButton("👥 Rating by Age Group");
        Button btnRecommended = createSidebarButton("🔮 Recommendations");

        TextField searchField = new TextField();
        searchField.setPromptText("Search movie...");
        searchField.getStyleClass().add("search-box");

        Button btnSearch = createSidebarButton("🔍 Search");

        sidebar.getChildren().addAll(
                menuLabel,
                btnTopMovies,
                btnGenre,
                btnHistogram,     // << added
                btnAgeGroup,
                btnRecommended,
                new Separator(),
                searchField,
                btnSearch
        );

        /* ------------------ CENTER CONTENT ------------------ */
        outputTextArea = new VBox(10);
        outputTextArea.setPadding(new Insets(15));

        chartsArea = new VBox(20);
        chartsArea.setPadding(new Insets(15));

        VBox mainContent = new VBox(20, outputTextArea, chartsArea);
        mainContent.setPadding(new Insets(20));
        mainContent.getStyleClass().add("main-content");

        /* ------------------ ROOT ------------------ */
        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(mainContent);

        root.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());

        /* ---------------- BUTTON ACTIONS ---------------- */
        btnTopMovies.setOnAction(e -> {
            clearOutput();
            outputTextArea.getChildren().add(new Label("⭐ TOP RATED MOVIES"));
            outputTextArea.getChildren().add(new Label(analysis.getTopMoviesText()));
            chartsArea.getChildren().add(generateBarChart());
        });

        btnGenre.setOnAction(e -> {
            clearOutput();
            outputTextArea.getChildren().add(new Label("📊 RATING DISTRIBUTION BY GENRE"));
            outputTextArea.getChildren().add(new Label(analysis.getGenreText()));
            chartsArea.getChildren().add(generateBoxPlot());
        });

        btnHistogram.setOnAction(e -> {
            clearOutput();
            outputTextArea.getChildren().add(new Label("📉 RATING DISTRIBUTION"));
            Map<Integer, Integer> hist = analysis.getRatingHistogramData();

            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= 10; i++) {
                sb.append("Rating ").append(i).append(" → ")
                        .append(hist.get(i)).append(" votes\n");
            }

            outputTextArea.getChildren().add(new Label(sb.toString()));
            chartsArea.getChildren().add(generateHistogram());
        });


        btnAgeGroup.setOnAction(e -> {
            clearOutput();
            outputTextArea.getChildren().add(new Label("👥 RATING BY AGE GROUP"));
            outputTextArea.getChildren().add(new Label(analysis.getAgeGroupText()));
        });

        btnRecommended.setOnAction(e -> {
            clearOutput();
            outputTextArea.getChildren().add(new Label("🔮 RECOMMENDED MOVIES"));
            outputTextArea.getChildren().add(new Label(analysis.getRecommendedText()));
        });

        btnSearch.setOnAction(e -> {
            clearOutput();
            String query = searchField.getText();
            if (query.isEmpty()) {
                outputTextArea.getChildren().add(new Label("❌ Enter a movie to search"));
            } else {
                outputTextArea.getChildren().add(new Label("🔍 SEARCH RESULTS"));
                outputTextArea.getChildren().add(new Label(analysis.searchMovie(query)));
            }
        });

        /* ------------------ SCENE ------------------ */
        Scene scene = new Scene(root, 1200, 720);
        stage.setScene(scene);
        stage.setTitle("Movie Ratings Dashboard");
        stage.show();
    }

    private Button createSidebarButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.getStyleClass().add("sidebar-button");
        return btn;
    }

    private void clearOutput() {
        outputTextArea.getChildren().clear();
        chartsArea.getChildren().clear();
    }

    /* ===========================================================
                       CHARTS (REAL DATA)
       =========================================================== */

    /** BAR CHART — Top Movies */
    private BarChart<String, Number> generateBarChart() {
        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis(0, 10, 1);
        BarChart<String, Number> chart = new BarChart<>(x, y);
        chart.setTitle("Top Movies (Avg Rating)");
        x.setLabel("Movie");
        y.setLabel("Average Rating");

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        List<MovieAnalysis.MovieStat> data = analysis.getTopMoviesData(10, 5);
        for (var item : data) {
            series.getData().add(new XYChart.Data<>(item.title(), item.avgRating()));
        }

        chart.getData().add(series);
        chart.setLegendVisible(false);
        chart.setPrefHeight(300);
        return chart;
    }

    /** HISTOGRAM — Rating Distribution */
    private BarChart<String, Number> generateHistogram() {
        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(x, y);
        chart.setTitle("Rating Distribution");
        x.setLabel("Rating");
        y.setLabel("Count");

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        Map<Integer, Integer> map = analysis.getRatingHistogramData();
        for (int i = 1; i <= 10; i++)
            series.getData().add(new XYChart.Data<>(String.valueOf(i), map.get(i)));

        chart.getData().add(series);
        chart.setLegendVisible(false);
        chart.setPrefHeight(250);
        return chart;
    }

    /** BOXPLOT (Simplified using Min, Median, Max) */
    private BarChart<String, Number> generateBoxPlot() {
        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis(0, 10, 1);

        BarChart<String, Number> chart = new BarChart<>(x, y);
        chart.setTitle("Genre Rating Spread (Min, Median, Max)");

        XYChart.Series<String, Number> minS = new XYChart.Series<>();
        XYChart.Series<String, Number> medS = new XYChart.Series<>();
        XYChart.Series<String, Number> maxS = new XYChart.Series<>();

        minS.setName("Min");
        medS.setName("Median");
        maxS.setName("Max");

        Map<String, double[]> bx = analysis.getGenreFiveNumberSummary();

        for (var entry : bx.entrySet()) {
            String genre = entry.getKey();
            double[] v = entry.getValue();

            minS.getData().add(new XYChart.Data<>(genre, v[0]));
            medS.getData().add(new XYChart.Data<>(genre, v[2]));
            maxS.getData().add(new XYChart.Data<>(genre, v[4]));
        }

        chart.getData().addAll(minS, medS, maxS);
        chart.setPrefHeight(350);
        return chart;
    }

    public static void main(String[] args) {
        launch();
    }
}
