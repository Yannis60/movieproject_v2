public class Main {
    public static void main(String[] args) {

        MovieAnalysis analysis = new MovieAnalysis();

        System.out.println("=== MOVIE ANALYSIS REPORT ===");

        analysis.topRatedMovies();
        analysis.ratingDistributionByGenre();
        analysis.ratingByAgeGroup();
        analysis.recommendedMovies();

        System.out.println("\n=== END OF REPORT ===");
    }
}
