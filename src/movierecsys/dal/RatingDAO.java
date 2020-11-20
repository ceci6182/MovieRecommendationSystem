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
import java.util.Comparator;
import java.util.List;

import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;

/**
 *
 * @author pgn
 */
public class RatingDAO
{

    private static final String RATING_SOURCE = "data/ratings.txt";
    MovieDAO movieDAO = new MovieDAO();
    UserDAO userDAO = new UserDAO();
    
    /**
     * Persists the given rating.
     * @param rating the rating to persist.
     */
    public void createRating(Rating rating)
    {
        Path path = new File(RATING_SOURCE).toPath();
        try ( BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.SYNC, StandardOpenOption.APPEND, StandardOpenOption.WRITE))
        {
            bw.newLine();
            bw.write(rating.getMovie().getId() + "," + rating.getUser().getId() + "," + rating.getRating());
        } catch (IOException ex){}
    }
    
    /**
     * Updates the rating to reflect the given object.
     * @param rating The updated rating to persist.
     */
    public void updateRating(Rating rating) throws Exception {
        try
        {
            File tmp = new File(rating.hashCode() + ".txt"); //Creates a temp file for writing to.
            List<Rating> allRatings = getAllRatings();

            for (int i = 0; i<allRatings.size(); i++) {
                Rating t = allRatings.get(i);
                if (t.getMovie() == rating.getMovie()) {
                    if (t.getUser() == rating.getUser()) {
                        if (t.getRating() == rating.getRating()) {
                            allRatings.remove(t);
                            allRatings.add(rating);
                        }
                    }
                }
            }

            //I'll sort the rating by their Movies
            allRatings.sort(Comparator.comparing(Rating::getMovieId));

            try ( BufferedWriter bw = new BufferedWriter(new FileWriter(tmp)))
            {
                for (Rating tempRating : allRatings)
                {
                    bw.write(tempRating.getMovieId() + "," + tempRating.getUser().getId() + "," + tempRating.getRating());
                    bw.newLine();
                }
            }
            //Overwrite the movie file wit the tmp one.
            Files.copy(tmp.toPath(), new File(RATING_SOURCE).toPath(), StandardCopyOption.REPLACE_EXISTING);
            //Clean up after the operation is done (Remove tmp)
            Files.delete(tmp.toPath());
        } catch (IOException ex)
        {
            throw new Exception("Could not update rating.", ex);
        }
    }
    
    /**
     * Removes the given rating.
     * @param rating 
     */
    public void deleteRating(Rating rating) throws Exception {
        try
        {
            File tmp = new File(rating.hashCode() + ".txt"); //Creates a temp file for writing to.
            List<Rating> allRatings = getAllRatings();

            for (int i = 0; i<allRatings.size(); i++) {
                Rating t = allRatings.get(i);
                if (t.getMovie() == rating.getMovie()) {
                    if (t.getUser() == rating.getUser()) {
                        if (t.getRating() == rating.getRating()) {
                            allRatings.remove(t);
                        }
                    }
                }
            }


            //I'll sort the rating by their Movies's
            allRatings.sort(Comparator.comparing(Rating::getMovieId));

            try ( BufferedWriter bw = new BufferedWriter(new FileWriter(tmp)))
            {
                for (Rating tempRating : allRatings)
                {
                    bw.write(tempRating.getMovieId() + "," + tempRating.getUser().getId() + "," + tempRating.getRating());
                    bw.newLine();
                }
            }
            //Overwrite the movie file wit the tmp one.
            Files.copy(tmp.toPath(), new File(RATING_SOURCE).toPath(), StandardCopyOption.REPLACE_EXISTING);
            //Clean up after the operation is done (Remove tmp)
            Files.delete(tmp.toPath());
        } catch (IOException ex)
        {
            throw new Exception("Could not update rating.", ex);
        }
    }
    
    /**
     * Gets all ratings from all users.
     * @return List of all ratings.
     */
    public List<Rating> getAllRatings() throws FileNotFoundException {
        List<Rating> allRatings = new ArrayList<>();
        File file = new File(RATING_SOURCE);

        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        Rating rating = stringArrayToRating(line);
                        allRatings.add(rating);
                    } catch (Exception ex) {
                        //Do nothing we simply do not accept malformed lines of data.
                        //In a perfect world you should at least log the incident.
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allRatings;
    }

    private Rating stringArrayToRating(String t) throws IOException {
        String[] arrRating = t.split(",");

        Movie movie = movieDAO.getMovie(Integer.parseInt(arrRating[0]));
        User user = userDAO.getUser(Integer.parseInt(arrRating[1]));
        int rating = Integer.parseInt(arrRating[2]);
        return new Rating(movie, user, rating);
    }
    
    /**
     * Get all ratings from a specific user.
     * @param user The user 
     * @return The list of ratings.
     */
    public List<Rating> getRatings(User user) throws FileNotFoundException {
        List<Rating> allRatings = getAllRatings();
        List<Rating> resultRatings = new ArrayList<>();

        for (int i = 0; i<allRatings.size(); i++) {
            Rating t = allRatings.get(i);
            if (t.getUser() == user) {
                resultRatings.add(t);
            }
        }
        return resultRatings;
    }
    
}
