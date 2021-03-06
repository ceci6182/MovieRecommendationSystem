package movierecsys.gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import movierecsys.be.User;
import movierecsys.bll.util.UserSearcher;
import movierecsys.dal.UserDAO;

import java.io.IOException;
import java.util.Comparator;

public class UserModel {
    ObservableList<User> users;
    ObservableList<User> searchUsers;
    UserDAO userDAO = new UserDAO();
    UserSearcher searcher = new UserSearcher();

    public UserModel() {
        users = FXCollections.observableArrayList();
        try {
            users.addAll(userDAO.getAllUsers());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<User> getUsers() {
        return users;
    }

    public void search (String searchWords) {
        searchUsers = FXCollections.observableArrayList();
        searchUsers.clear();
        searchUsers.addAll(searcher.search(users, searchWords));
    }

    public ObservableList<User> getSearchUsers() {
        return searchUsers;
    }

    public void update (User selectedUser) throws Exception {
        userDAO.updateUser(selectedUser);
        if (users.remove(selectedUser))
        {
            users.add(selectedUser);
            users.sort(Comparator.comparingInt(User::getId));
        }

    }

}


