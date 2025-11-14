import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MovieAnalysis {

    public void topRatedMovies() {
        String query = """
                SELECT m.title, AVG(r.rating) AS avg_rating, COUNT(r.rating) AS rating_count
                FROM ratings r
                JOIN movies m ON r.movie_id = m.movie_id
                GROUP BY m.title
                HAVING COUNT(r.rating) >= 5
                ORDER BY avg_rating DESC
                LIMIT 10;
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n🎬 TOP RATED MOVIES");
            while (rs.next()) {
                System.out.printf("%-40s ⭐ %.2f (%d ratings)\n",
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
                System.out.printf("%-15s Rating %.1f → %d votes\n",
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
                System.out.printf("%-10s Rating %.1f → %d users\n",
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
                GROUP BY m.title
                HAVING COUNT(r.rating) >= 5
                ORDER BY avg_rating DESC
                LIMIT 10;
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n⭐ RECOMMENDED MOVIES");
            while (rs.next()) {
                System.out.printf("%-40s ⭐ %.2f (%d votes)\n",
                        rs.getString("title"),
                        rs.getDouble("avg_rating"),
                        rs.getInt("votes"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getTopMoviesText() {
        StringBuilder sb = new StringBuilder();
        String query = """
            SELECT m.title, AVG(r.rating) AS avg_rating, COUNT(r.rating) AS rating_count
            FROM ratings r
            JOIN movies m ON r.movie_Id = m.movie_Id
            GROUP BY m.title
            HAVING COUNT(r.rating) >= 5
            ORDER BY avg_rating DESC
            LIMIT 10;
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                sb.append(String.format("%-30s ⭐ %.2f (%d ratings)\n",
                        rs.getString("title"),
                        rs.getDouble("avg_rating"),
                        rs.getInt("rating_count")));
            }

        } catch (Exception e) {
            sb.append("Error: ").append(e.getMessage());
        }

        return sb.toString();
    }

    public String getGenreText() {
        StringBuilder sb = new StringBuilder();
        String query = """
            SELECT m.genre, r.rating, COUNT(*) AS count
            FROM ratings r
            JOIN movies m ON r.movie_Id = m.movie_Id
            GROUP BY m.genre, r.rating
            ORDER BY m.genre, r.rating;
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                sb.append(String.format("%-15s Rating %.1f → %d votes\n",
                        rs.getString("genre"),
                        rs.getDouble("rating"),
                        rs.getInt("count")));
            }

        } catch (Exception e) {
            sb.append("Error: ").append(e.getMessage());
        }

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
            JOIN users u ON r.user_Id = u.user_Id
            GROUP BY age_group, r.rating
            ORDER BY age_group, r.rating;
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                sb.append(String.format("%-10s Rating %.1f → %d users\n",
                        rs.getString("age_group"),
                        rs.getDouble("rating"),
                        rs.getInt("count")));
            }

        } catch (Exception e) {
            sb.append("Error: ").append(e.getMessage());
        }

        return sb.toString();
    }

    public String getRecommendedText() {
        StringBuilder sb = new StringBuilder();
        String query = """
            SELECT m.title, AVG(r.rating) AS avg_rating, COUNT(r.rating) AS votes
            FROM ratings r
            JOIN movies m ON r.movie_Id = m.movie_Id
            GROUP BY m.title
            HAVING COUNT(r.rating) >= 5
            ORDER BY avg_rating DESC
            LIMIT 10;
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                sb.append(String.format("%-30s ⭐ %.2f (%d votes)\n",
                        rs.getString("title"),
                        rs.getDouble("avg_rating"),
                        rs.getInt("votes")));
            }

        } catch (Exception e) {
            sb.append("Error: ").append(e.getMessage());
        }

        return sb.toString();
    }

}
