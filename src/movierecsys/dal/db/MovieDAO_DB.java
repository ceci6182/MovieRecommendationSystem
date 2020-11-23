package movierecsys.dal.db;

import movierecsys.be.Movie;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO_DB {

    private DatabaseConnector DBConnector;

    public MovieDAO_DB() {
        DBConnector = new DatabaseConnector();

    }

    public List<Movie> getAllMovies() throws SQLException {
        // Create return data structure
        ArrayList<Movie> allMovies = new ArrayList<>();

        //Create a connection
        try (Connection connection = DBConnector.getConnection()) {

            //Create SQL command
            String sql = "SELECT * FROM Movie;";
            //Create SQL statement
            Statement statement = connection.createStatement();
            if(statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    int year = resultSet.getInt("year");

                    Movie movie = new Movie(id, year, title);
                    allMovies.add(movie);

                }
            }

        }
        return allMovies;
    }

    public static void main(String[] args) throws SQLException {
        MovieDAO_DB movieDAO_db = new MovieDAO_DB();
        List<Movie> movies = movieDAO_db.getAllMovies();

        System.out.println(movies);
    }
}
