public class Movies {
    private String movie_id;
    private String title;
    private String genre;
    private int year;

    public Movies(String movie_id, String title, String genre, int year) {
        this.movie_id = movie_id;
        this.title = title;
        this.genre = genre;
        this.year = year;
    }

    public String getMovie_id() {return movie_id;}
    public String getTitle() {return title;}
    public String getGenre() {return genre;}
    public int getYear() {return year;}
}
