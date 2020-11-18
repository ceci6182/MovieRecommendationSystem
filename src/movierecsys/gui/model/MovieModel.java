package movierecsys.gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import movierecsys.be.Movie;
import movierecsys.bll.util.MovieSearcher;
import movierecsys.dal.MovieDAO;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

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

    public void update (Movie movie) {
        if (movies.remove(movie))
        {
            movies.add(movie);
            movies.sort(new Comparator<Movie>()
            {
                @Override
                public int compare(Movie arg0, Movie arg1)
                {
                    return arg0.getId() - arg1.getId();
                }

            });
        }
    }

    public void delete (Movie movie) {
        movies.remove(movie);
    }

    public void add (int year, String title) throws IOException {
        movies.add(new Movie(getNextAvailableMovieID(), year, title));
        movies.sort(Comparator.comparingInt(Movie::getId));
    }

    /**
     * Examines all stored movies and returns the next available highest ID.
     *
     * @return
     * @throws IOException
     */
    public int getNextAvailableMovieID() throws IOException {
        if (movies == null || movies.isEmpty())
        {
            return 1;
        }
        movies.sort(Comparator.comparingInt(Movie::getId));
        int id = movies.get(0).getId();
        for (Movie allMovie : movies) {
            if (allMovie.getId() <= id) {
                id++;
            } else {
                return id;
            }
        }
        return id;
    }
}


