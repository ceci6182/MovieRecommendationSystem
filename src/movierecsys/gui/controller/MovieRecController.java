/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.gui.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;
import movierecsys.dal.MovieDAO;
import movierecsys.gui.model.MovieModel;
import movierecsys.gui.model.RatingModel;
import movierecsys.gui.model.UserModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * @author pgn
 */
public class MovieRecController implements Initializable
{


    MovieModel movieModel = new MovieModel();
    UserModel userModel = new UserModel();
    RatingModel ratingModel = new RatingModel();


    @FXML
    public ListView lstRecommendations;

    @FXML
    public TextField txtMovieTitle_UserRating;

    @FXML
    public TextField txtMovieTitle;

    @FXML
    public TextField txtReleaseYear;

    @FXML
    public ChoiceBox choiceBoxRating;

    /**
     * The TextField containing the URL of the targeted website.
     */
    @FXML
    private TextField txtMovieSearch;

    /**
     * The TextField containing the query word.
     */
    @FXML
    private ListView<Movie> lstMovies;

    /**
     * The TextField containing the URL of the targeted website.
     */
    @FXML
    public TextField txtUserSearch;

    /**
     * The TextField containing the query word.
     */
    @FXML
    public ListView<User> lstUsers;


    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        lstMovies.setItems(movieModel.getMovies());
        setMovieSelection();
        lstUsers.setItems(userModel.getUsers());
        setChoiceBoxRating();
    }

    private void setLstRecommendations() {
        ratingModel.addRatingsToMovies();
        ObservableList<Movie> recommendations = movieModel.getMovies();
        recommendations.sort(Comparator.comparingInt(Movie::getScore).reversed());
        System.out.println(recommendations);
    }

    private void setChoiceBoxRating(){
        choiceBoxRating.getItems().add(-5);
        choiceBoxRating.getItems().add(-3);
        choiceBoxRating.getItems().add(0);
        choiceBoxRating.getItems().add(3);
        choiceBoxRating.getItems().add(5);

    }

    /**
     * Sets up the movie selection of the list of all movers.
     */
    private void setMovieSelection()
    {
        //I do this to receive updates when a new movie is selected from the list of all movies:
        lstMovies.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        lstMovies.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
            {
                txtMovieTitle.setText(newValue.getTitle());
                txtReleaseYear.setText(newValue.getYear() + "");
                txtMovieTitle_UserRating.setText(newValue.getTitle() + "(" + newValue.getYear() +")");
            }
        });

    }

    public void handleMovieSearch() {
        String queue = txtMovieSearch.getText().trim();
        if (queue.isEmpty() || queue.isBlank()) {
            lstMovies.setItems(movieModel.getMovies());
        }
        else {
            movieModel.search(queue);
            lstMovies.setItems(movieModel.getSearchMovies());
        }
    }

    public void btnUpdateMovie() throws Exception {
        Movie selectedMovie = lstMovies.getSelectionModel().getSelectedItem();
        if (selectedMovie != null)
        {
                String title = txtMovieTitle.getText();
                int year = Integer.parseInt(txtReleaseYear.getText());
                selectedMovie.setTitle(title);
                selectedMovie.setYear(year);
                movieModel.update(selectedMovie);

        }
    }

    public void btnDeleteMovie() throws Exception {
        Movie selectedMovie = lstMovies.getSelectionModel().getSelectedItem();
        movieModel.delete(selectedMovie);
    }

    public void btnAddMovie() throws Exception {
        String movieTitle = txtMovieTitle.getText().trim();
        int releaseYear = Integer.parseInt(txtReleaseYear.getText().trim());
        movieModel.add(releaseYear,movieTitle);
    }

    public void btnClear() {
        txtMovieTitle.clear();
        txtReleaseYear.clear();
    }

    public void handleUserSearch() {
        String queue = txtUserSearch.getText().trim();
        if (queue.isEmpty() || queue.isBlank()) {
            lstUsers.setItems(userModel.getUsers());
        }
        else {
            userModel.search(queue);
            lstUsers.setItems(userModel.getSearchUsers());
        }
    }

    public void handleConfirmRating(ActionEvent actionEvent) throws IOException {
        if (!lstMovies.getSelectionModel().isEmpty() && !lstUsers.getSelectionModel().isEmpty() && choiceBoxRating.getValue() != null) {
            Movie selectedMovie = lstMovies.getSelectionModel().getSelectedItem();
            User selectedUser = lstUsers.getSelectionModel().getSelectedItem();
            int selectedRating = (int) choiceBoxRating.getValue();
            Rating ratingToAdd = new Rating(selectedMovie, selectedUser, selectedRating);
            ratingModel.add(selectedMovie, selectedUser, selectedRating);
            System.out.println("rating added");
            setLstRecommendations();
        }
    }
}