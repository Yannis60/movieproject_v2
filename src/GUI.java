import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application {

    private MovieAnalysis analysis = new MovieAnalysis();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Movie Ratings Analysis");

        // Output area
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(500);

        // Buttons
        Button btnTopMovies = new Button("Top Rated Movies");
        Button btnGenre = new Button("Ratings by Genre");
        Button btnAge = new Button("Ratings by Age Group");
        Button btnRecommend = new Button("Recommended Movies");

        // Button Actions
        btnTopMovies.setOnAction(e -> {
            outputArea.clear();
            outputArea.appendText("🎬 TOP RATED MOVIES\n\n");
            outputArea.appendText(analysis.getTopMoviesText());
        });

        btnGenre.setOnAction(e -> {
            outputArea.clear();
            outputArea.appendText("📊 RATING BY GENRE\n\n");
            outputArea.appendText(analysis.getGenreText());
        });

        btnAge.setOnAction(e -> {
            outputArea.clear();
            outputArea.appendText("👥 RATING BY AGE GROUP\n\n");
            outputArea.appendText(analysis.getAgeGroupText());
        });

        btnRecommend.setOnAction(e -> {
            outputArea.clear();
            outputArea.appendText("⭐ RECOMMENDED MOVIES\n\n");
            outputArea.appendText(analysis.getRecommendedText());
        });

        // Layout
        VBox root = new VBox(10, btnTopMovies, btnGenre, btnAge, btnRecommend, outputArea);
        root.setPadding(new Insets(15));

        Scene scene = new Scene(root, 700, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
