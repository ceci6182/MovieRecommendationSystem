package movierecsys.gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import movierecsys.be.Movie;
import movierecsys.bll.util.MovieSearcher;
import movierecsys.dal.MovieDAO;

import java.io.IOException;

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

    public void delete (Movie movie) {
        movies.remove(movie);
    }

    public void add (Movie movie) {
        movies.add(movie);
    }

}


