package movierecsys.gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import movierecsys.be.Movie;
import movierecsys.bll.util.MovieSearcher;
import movierecsys.dal.MovieDAO;

import java.io.IOException;
import java.util.Comparator;

public class MovieModel {
    ObservableList<Movie> movies;
    ObservableList<Movie> searchMovies;
    MovieDAO movieDAO = new MovieDAO();
    MovieSearcher searcher = new MovieSearcher();

public MovieModel() {
    movies = FXCollections.observableArrayList();
    try {
        movies.addAll(movieDAO.getAllMovies());
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    public ObservableList<Movie> getMovies() {
        return movies;
    }

    public void search (String searchWords) {
        searchMovies = FXCollections.observableArrayList();
        searchMovies.clear();
        searchMovies.addAll(searcher.search(movies, searchWords));
    }

    public ObservableList<Movie> getSearchMovies() {
        return searchMovies;
    }

    public void update (Movie selectedMovie) throws Exception {
        movieDAO.updateMovie(selectedMovie);
        if (movies.remove(selectedMovie))
        {
            movies.add(selectedMovie);
            movies.sort(Comparator.comparingInt(Movie::getId));
        }

    }

    public void delete (Movie movie) throws Exception {
        movieDAO.deleteMovie(movie);
        movies.remove(movie);
    }

    public void add (int year, String title) throws Exception {
        Movie movie = movieDAO.createMovie(year, title);
        movies.add(movie);
        movies.sort(Comparator.comparingInt(Movie::getId));
    }

}


