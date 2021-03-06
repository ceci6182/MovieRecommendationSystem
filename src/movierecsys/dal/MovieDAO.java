/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import movierecsys.be.Movie;

/**
 * @author pgn
 */
public class MovieDAO {

    private static final String MOVIE_SOURCE = "data/movie_titles.txt";

    /**
     * Gets a list of all movies in the persistence storage.
     *
     * @return List of movies.
     * @throws java.io.FileNotFoundException
     */
    public List<Movie> getAllMovies() throws IOException {
        List<Movie> allMovies = new ArrayList<>();
        File file = new File(MOVIE_SOURCE);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Movie mov = stringArrayToMovie(line);
                    allMovies.add(mov);
                } catch (Exception ex) {
                    //Do nothing we simply do not accept malformed lines of data.
                    //In a perfect world you should at least log the incident.
                }
            }
        }
        return allMovies;
    }

    /**
     * Reads a movie from a , s
     *
     * @param t
     * @return
     * @throws NumberFormatException
     */
    public Movie stringArrayToMovie(String t) {
        String[] arrMovie = t.split(",");

        int id = Integer.parseInt(arrMovie[0]);
        int year = Integer.parseInt(arrMovie[1]);
        String title = arrMovie[2];
        if (arrMovie.length > 3) {
            for (int i = 3; i < arrMovie.length; i++) {
                title += "," + arrMovie[i];
            }
        }
        return new Movie(id, year, title);
    }

    /**
     * Creates a movie in the persistence storage.
     *
     * @param releaseYear The release year of the movie
     * @param title       The title of the movie
     * @return The object representation of the movie added to the persistence
     * storage.
     */
    public Movie createMovie(int releaseYear, String title) throws Exception {
        Path path = new File(MOVIE_SOURCE).toPath();
        int id;
        try ( BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.SYNC, StandardOpenOption.APPEND, StandardOpenOption.WRITE))
        {
            id = getNextAvailableMovieID();
            bw.newLine();
            bw.write(id + "," + releaseYear + "," + title);
        } catch (IOException ex){
            throw new Exception();
        }
        return new Movie(id, releaseYear, title);
    }

    /**
     * Examines all stored movies and returns the next available highest ID.
     *
     * @return
     * @throws IOException
     */
    public int getNextAvailableMovieID() throws IOException {
        List<Movie> allMovies = getAllMovies();
        if (allMovies == null || allMovies.isEmpty())
        {
            return 1;
        }
        allMovies.sort(Comparator.comparingInt(Movie::getId));
        int id = allMovies.get(0).getId();
        for (Movie allMovie : allMovies) {
            if (allMovie.getId() <= id) {
                id++;
            } else {
                return id;
            }
        }
        return id;
    }

    /**
     * Deletes a movie from the persistence storage.
     *
     * @param movie The movie to delete.
     */
    public void deleteMovie(Movie movie) throws Exception {
        {
            try
            {
                File file = new File(MOVIE_SOURCE);
                if (!file.canWrite())
                {
                    throw new Exception("Can't write to movie storage");
                }
                List<Movie> movies = getAllMovies();
                movies.remove(movie);
                OutputStream os = Files.newOutputStream(file.toPath());
                try ( BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os)))
                {
                    for (Movie mov : movies)
                    {
                        String line = mov.getId() + "," + mov.getYear() + "," + mov.getTitle();
                        bw.write(line);
                        bw.newLine();
                    }
                }
            } catch (IOException ex)
            {
                throw new Exception("Could not delete movie.", ex);
            }
        }
    }

    /**
     * Updates the movie in the persistence storage to reflect the values in the
     * given Movie object.
     *
     * @param movie The updated movie.
     */
    public void updateMovie(Movie movie) throws Exception {
        try
        {
            File tmp = new File(movie.hashCode() + ".txt"); //Creates a temp file for writing to.
            List<Movie> allMovies = getAllMovies();
            allMovies.removeIf((Movie t) -> t.getId() == movie.getId());
            allMovies.add(movie);

            //I'll sort the movies by their ID's
            allMovies.sort(Comparator.comparingInt(Movie::getId));

            try ( BufferedWriter bw = new BufferedWriter(new FileWriter(tmp)))
            {
                for (Movie mov : allMovies)
                {
                    bw.write(mov.getId() + "," + mov.getYear() + "," + mov.getTitle());
                    bw.newLine();
                }
            }
            //Overwrite the movie file wit the tmp one.
            Files.copy(tmp.toPath(), new File(MOVIE_SOURCE).toPath(), StandardCopyOption.REPLACE_EXISTING);
            //Clean up after the operation is done (Remove tmp)
            Files.delete(tmp.toPath());
        } catch (IOException ex)
        {
            throw new Exception("Could not update movie.", ex);
        }
    }

    /**
     * Gets a the movie with the given ID.
     *
     * @param id ID of the movie.
     * @return A Movie object.
     */
    public Movie getMovie(int id) throws IOException {
        List<Movie> all = getAllMovies();

        int index = Collections.binarySearch(all, new Movie(id, 0, ""), Comparator.comparingInt(Movie::getId));

        if (index >= 0)
        {
            return all.get(index);
        } else
        {
            throw new IllegalArgumentException("No movie with ID: " + id + " is found.");
        }
    }

    public void rateMovie (int movieId, int rating) throws IOException {
        Movie tempMovie = getMovie(movieId);
        tempMovie.addScore(rating);
    }

}
