import java.sql.*;
import java.util.*;

/**
 * MovieAnalysis - combined analysis utilities:
 * - console-print methods (old)
 * - text-return methods (for GUI)
 * - data-return methods (for charts)
 *
 * Relies on DatabaseConnection.getConnection()
 */
public class MovieAnalysis {

    // Simple record to hold top-movie stats
    public record MovieStat(String title, double avgRating, int votes) {}

    // -------------------------
    // Console / original methods
    // -------------------------
    public void topRatedMovies() {
        String query = """
                SELECT m.title, AVG(r.rating) AS avg_rating, COUNT(r.rating) AS rating_count
                FROM ratings r
                JOIN movies m ON r.movie_id = m.movie_id
                GROUP BY m.movie_id, m.title
                HAVING COUNT(r.rating) >= 5
                ORDER BY avg_rating DESC
                LIMIT 10;
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n🎬 TOP RATED MOVIES");
            while (rs.next()) {
                System.out.printf("%-40s ⭐ %.2f (%d ratings)%n",
                        rs.getString("title"),
                        rs.getDouble("avg_rating"),
                        rs.getInt("rating_count"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ratingDistributionByGenre() {
        String query = """
                SELECT m.genre, r.rating, COUNT(*) AS count
                FROM ratings r
                JOIN movies m ON r.movie_id = m.movie_id
                GROUP BY m.genre, r.rating
                ORDER BY m.genre, r.rating;
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n📊 RATING BY GENRE");
            while (rs.next()) {
                System.out.printf("%-15s Rating %.1f → %d votes%n",
                        rs.getString("genre"),
                        rs.getDouble("rating"),
                        rs.getInt("count"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ratingByAgeGroup() {
        String query = """
                SELECT CASE
                    WHEN u.age < 18 THEN 'Under 18'
                    WHEN u.age BETWEEN 18 AND 35 THEN '18-35'
                    WHEN u.age BETWEEN 36 AND 50 THEN '36-50'
                    ELSE '50+'
                END AS age_group,
                r.rating,
                COUNT(*) AS count
                FROM ratings r
                JOIN users u ON r.user_id = u.user_id
                GROUP BY age_group, r.rating
                ORDER BY age_group, r.rating;
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n👥 RATING BY AGE GROUP");
            while (rs.next()) {
                System.out.printf("%-10s Rating %.1f → %d users%n",
                        rs.getString("age_group"),
                        rs.getDouble("rating"),
                        rs.getInt("count"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void recommendedMovies() {
        String query = """
                SELECT m.title, AVG(r.rating) AS avg_rating, COUNT(r.rating) AS votes
                FROM ratings r
                JOIN movies m ON r.movie_id = m.movie_id
                GROUP BY m.movie_id, m.title
                HAVING COUNT(r.rating) >= 5
                ORDER BY avg_rating DESC
                LIMIT 10;
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n⭐ RECOMMENDED MOVIES");
            while (rs.next()) {
                System.out.printf("%-40s ⭐ %.2f (%d votes)%n",
                        rs.getString("title"),
                        rs.getDouble("avg_rating"),
                        rs.getInt("votes"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // -------------------------
    // Text-return methods for GUI
    // -------------------------
    public String getTopMoviesText() {
        StringBuilder sb = new StringBuilder();
        String query = """
            SELECT m.title, AVG(r.rating) AS avg_rating, COUNT(r.rating) AS rating_count
            FROM ratings r
            JOIN movies m ON r.movie_id = m.movie_id
            GROUP BY m.movie_id, m.title
            HAVING COUNT(r.rating) >= 5
            ORDER BY avg_rating DESC
            LIMIT 10;
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                sb.append(String.format("%s — ⭐ %.2f (%d ratings)%n",
                        rs.getString("title"),
                        rs.getDouble("avg_rating"),
                        rs.getInt("rating_count")));
            }

        } catch (Exception e) {
            sb.append("Error: ").append(e.getMessage()).append("\n");
        }

        if (sb.length() == 0) sb.append("No top movies found.\n");
        return sb.toString();
    }

    public String getGenreText() {
        StringBuilder sb = new StringBuilder();
        String query = """
            SELECT m.genre, r.rating, COUNT(*) AS count
            FROM ratings r
            JOIN movies m ON r.movie_id = m.movie_id
            GROUP BY m.genre, r.rating
            ORDER BY m.genre, r.rating;
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                sb.append(String.format("%-15s Rating %.1f → %d votes%n",
                        rs.getString("genre"),
                        rs.getDouble("rating"),
                        rs.getInt("count")));
            }

        } catch (Exception e) {
            sb.append("Error: ").append(e.getMessage()).append("\n");
        }

        if (sb.length() == 0) sb.append("No genre data found.\n");
        return sb.toString();
    }

    public String getAgeGroupText() {
        StringBuilder sb = new StringBuilder();
        String query = """
            SELECT 
                CASE
                    WHEN u.age < 18 THEN 'Under 18'
                    WHEN u.age BETWEEN 18 AND 35 THEN '18-35'
                    WHEN u.age BETWEEN 36 AND 50 THEN '36-50'
                    ELSE '50+'
                END AS age_group,
                r.rating,
                COUNT(*) AS count
            FROM ratings r
            JOIN users u ON r.user_id = u.user_id
            GROUP BY age_group, r.rating
            ORDER BY age_group, r.rating;
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                sb.append(String.format("%-10s Rating %.1f → %d users%n",
                        rs.getString("age_group"),
                        rs.getDouble("rating"),
                        rs.getInt("count")));
            }

        } catch (Exception e) {
            sb.append("Error: ").append(e.getMessage()).append("\n");
        }

        if (sb.length() == 0) sb.append("No age-group data found.\n");
        return sb.toString();
    }

    public String getRecommendedText() {
        StringBuilder sb = new StringBuilder();
        String query = """
            SELECT m.title, AVG(r.rating) AS avg_rating, COUNT(r.rating) AS votes
            FROM ratings r
            JOIN movies m ON r.movie_id = m.movie_id
            GROUP BY m.movie_id, m.title
            HAVING COUNT(r.rating) >= 5
            ORDER BY avg_rating DESC
            LIMIT 10;
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                sb.append(String.format("%s — ⭐ %.2f (%d votes)%n",
                        rs.getString("title"),
                        rs.getDouble("avg_rating"),
                        rs.getInt("votes")));
            }

        } catch (Exception e) {
            sb.append("Error: ").append(e.getMessage()).append("\n");
        }

        if (sb.length() == 0) sb.append("No recommended movies found.\n");
        return sb.toString();
    }

    public String searchMovie(String title) {
        StringBuilder sb = new StringBuilder();

        String query = """
        SELECT m.title, m.genre, AVG(r.rating) AS avg_rating, COUNT(r.rating) AS votes
        FROM movies m
        LEFT JOIN ratings r ON m.movie_id = r.movie_id
        WHERE m.title LIKE ?
        GROUP BY m.movie_id, m.title, m.genre
        ORDER BY avg_rating DESC
        LIMIT 50;
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + title + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sb.append("🎬 ").append(rs.getString("title")).append("\n");
                    sb.append("Genre: ").append(rs.getString("genre")).append("\n");
                    double avg = rs.getDouble("avg_rating");
                    if (rs.wasNull()) sb.append("⭐ Rating: N/A\n"); else sb.append("⭐ Rating: ").append(String.format("%.2f", avg)).append("\n");
                    sb.append("🗳 Votes: ").append(rs.getInt("votes")).append("\n\n");
                }
            }

            if (sb.length() == 0) {
                return "❌ No movie found with name: " + title;
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }

        return sb.toString();
    }

    // -------------------------
    // Data methods for charts
    // -------------------------

    /**
     * Returns list of MovieStat for top movies (by average rating).
     * @param limit max number of movies
     * @param minVotes minimum number of votes to include a movie
     */
    public List<MovieStat> getTopMoviesData(int limit, int minVotes) {
        List<MovieStat> list = new ArrayList<>();
        String sql = """
            SELECT m.title, AVG(r.rating) AS avg_rating, COUNT(r.rating) AS votes
            FROM ratings r
            JOIN movies m ON r.movie_id = m.movie_id
            GROUP BY m.movie_id, m.title
            HAVING COUNT(r.rating) >= ?
            ORDER BY avg_rating DESC
            LIMIT ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, minVotes);
            ps.setInt(2, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("title");
                    double avg = rs.getDouble("avg_rating");
                    int votes = rs.getInt("votes");
                    list.add(new MovieStat(title, avg, votes));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Return histogram data: rating -> count (ensures keys 1..10 exist)
     */
    public Map<Integer, Integer> getRatingHistogramData() {
        Map<Integer, Integer> map = new TreeMap<>();
        String sql = "SELECT rating, COUNT(*) AS cnt FROM ratings GROUP BY rating ORDER BY rating";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int rating = rs.getInt("rating");
                int cnt = rs.getInt("cnt");
                map.put(rating, cnt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int i = 1; i <= 10; i++) map.putIfAbsent(i, 0);
        return map;
    }

    /**
     * Return for each genre the five-number summary: [min, Q1, median, Q3, max]
     */
    public Map<String, double[]> getGenreFiveNumberSummary() {
        Map<String, List<Integer>> genreRatings = new HashMap<>();
        String sql = """
            SELECT m.genre, r.rating
            FROM ratings r
            JOIN movies m ON r.movie_id = m.movie_id
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String genre = rs.getString("genre");
                int rating = rs.getInt("rating");
                genreRatings.computeIfAbsent(genre, k -> new ArrayList<>()).add(rating);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Map<String, double[]> result = new LinkedHashMap<>();
        for (var entry : genreRatings.entrySet()) {
            List<Integer> vals = entry.getValue();
            Collections.sort(vals);
            double[] summary = fiveNumberSummary(vals);
            result.put(entry.getKey(), summary);
        }
        return result;
    }

    // Helper: compute five-number summary
    private double[] fiveNumberSummary(List<Integer> sorted) {
        if (sorted == null || sorted.isEmpty()) return new double[]{0,0,0,0,0};
        int n = sorted.size();
        double min = sorted.get(0);
        double max = sorted.get(n-1);
        double median = medianOfList(sorted, 0, n - 1);
        int loStart = 0;
        int loEnd = (n % 2 == 0) ? (n/2 - 1) : (n/2 - 1);
        int hiStart = (n % 2 == 0) ? (n/2) : (n/2 + 1);
        int hiEnd = n - 1;
        double q1 = medianOfList(sorted, loStart, Math.max(loEnd, loStart));
        double q3 = medianOfList(sorted, Math.max(hiStart, loStart), hiEnd);
        return new double[]{min, q1, median, q3, max};
    }

    // Helper: median for sorted[from..to] inclusive
    private double medianOfList(List<Integer> sorted, int from, int to) {
        if (from > to) return 0;
        int len = to - from + 1;
        int mid = from + (len - 1) / 2;
        if (len % 2 == 1) {
            return sorted.get(mid);
        } else {
            return (sorted.get(mid) + sorted.get(mid + 1)) / 2.0;
        }
    }
}
