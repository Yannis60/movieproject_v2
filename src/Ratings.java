public class Ratings {
    private String rating_id;
    private String user_id;
    private String movie_id;
    private int rating;

     // Constructor to initialize all fields of the rating

     public Ratings(String rating_id, String user_id, String movie_id, int rating) {
        this.rating_id = rating_id;
        this.user_id = user_id;
        this.movie_id = movie_id;
        this.rating = rating;
    }

    public String getRating_id() {return rating_id;}
    public String getUser_id() {return user_id;}
    public String getMovie_id() {return movie_id;}
    public int getRating() {return rating;}
}
