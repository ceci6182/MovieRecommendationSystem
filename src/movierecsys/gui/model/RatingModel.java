package movierecsys.gui.model;

import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;
import movierecsys.dal.RatingDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RatingModel {

    RatingDAO ratingDAO = new RatingDAO();

    public RatingModel() {
    }

    public void add(Movie movie, User user, int rating) throws IOException {
        Rating tempRating = new Rating(movie,user,rating);
        ratingDAO.createRating(tempRating);
    }

    public boolean checkIfDuplicate(Rating rating) throws IOException {
        List<Rating> ratingsByUser = ratingDAO.getRatings(rating.getUser());
        for (Rating tempRating : ratingsByUser) {
            if (tempRating.getMovieId() == rating.getMovieId()) {
                return true;
            }
        }
        return false;
    }

    public void addRatingsToMovies() {
        try {
            ratingDAO.addRatingsToMovies();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
