/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import movierecsys.be.Movie;
import movierecsys.gui.model.MovieModel;
import movierecsys.gui.model.UserModel;

/**
 *
 * @author pgn
 */
public class MovieRecController implements Initializable
{
    public TextField txtMovieTitle;
    public TextField txtReleaseYear;
    MovieModel movieModel = new MovieModel();
    UserModel userModel = new UserModel();

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
    public ListView lstUsers;


    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
       lstMovies.setItems(movieModel.getMovies());
        setMovieSelection();
        lstUsers.setItems(userModel.getUsers());
    }

    /**
     * Sets up the movie selection of the list of all movers.
     */
    private void setMovieSelection()
    {
        //I do this to receive updates when a new movie is selected from the list of all movies:
        lstMovies.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        lstMovies.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Movie>()
        {

            @Override
            public void changed(ObservableValue<? extends Movie> observable, Movie oldValue, Movie newValue)
            {
                if (newValue != null)
                {
                    txtMovieTitle.setText(newValue.getTitle());
                    txtReleaseYear.setText(newValue.getYear() + "");
                }
            }

        });

    }

    public void handleMovieSearch(KeyEvent keyEvent) {
        String queue = txtMovieSearch.getText().trim();
        if (queue.isEmpty() || queue.isBlank()) {
            lstMovies.setItems(movieModel.getMovies());
        }
        else {
            movieModel.search(queue);
            lstMovies.setItems(movieModel.getSearchMovies());
        }
    }

    public void btnUpdateMovie(ActionEvent actionEvent) throws Exception {
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

    public void btnDeleteMovie(ActionEvent actionEvent) throws Exception {
        Movie selectedMovie = lstMovies.getSelectionModel().getSelectedItem();
        movieModel.delete(selectedMovie);
    }

    public void btnAddMovie(ActionEvent actionEvent) throws Exception {
        String movieTitle = txtMovieTitle.getText().trim();
        int releaseYear = Integer.parseInt(txtReleaseYear.getText().trim());
        movieModel.add(releaseYear,movieTitle);
    }

    public void btnClear(ActionEvent actionEvent) {
        txtMovieTitle.clear();
        txtReleaseYear.clear();
    }

    public void handleUserSearch(KeyEvent keyEvent) {
        String queue = txtUserSearch.getText().trim();
        if (queue.isEmpty() || queue.isBlank()) {
            lstUsers.setItems(userModel.getUsers());
        }
        else {
            userModel.search(queue);
            lstUsers.setItems(userModel.getSearchUsers());
        }
    }
}