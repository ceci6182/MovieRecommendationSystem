/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal;

import movierecsys.be.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author pgn
 */
public class UserDAO
{
    private static final String USER_SOURCE = "data/users.txt";
    
    /**
     * Gets a list of all known users.
     * @return List of users.
     */
    public List<User> getAllUsers() throws IOException {
        List<User> users = new ArrayList<>();
        File file = new File(USER_SOURCE);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    User user = stringArrayToUser(line);
                    users.add(user);
                } catch (Exception ex) {
                    //Do nothing we simply do not accept malformed lines of data.
                    //In a perfect world you should at least log the incident.
                }
            }
        }
        return users;
    }


    private User stringArrayToUser(String t) {
        String[] arrUser = t.split(",");

        int id = Integer.parseInt(arrUser[0]);
        String name = arrUser[1];
        return new User(id, name);
    }
    
    /**
     * Gets a single User by its ID.
     * @param id The ID of the user.
     * @return The User with the ID.
     */
    public User getUser(int id) throws IOException {
        List<User> all = getAllUsers();

        int index = Collections.binarySearch(all, new User(id, ""), Comparator.comparingInt(User::getId));

        if (index >= 0)
        {
            return all.get(index);
        } else
        {
            throw new IllegalArgumentException("No movie with ID: " + id + " is found.");
        }
    }
    
    /**
     * Updates a user so the persistence storage reflects the given User object.
     * @param user The updated user.
     */
    public void updateUser(User user) throws Exception {
        try
        {
            File tmp = new File(user.hashCode() + ".txt"); //Creates a temp file for writing to.
            List<User> allUsers = getAllUsers();
            allUsers.removeIf((User t) -> t.getId() == user.getId());
            allUsers.add(user);

            //I'll sort the users by their ID's
            allUsers.sort(Comparator.comparingInt(User::getId));

            try ( BufferedWriter bw = new BufferedWriter(new FileWriter(tmp)))
            {
                for (User tempUser: allUsers)
                {
                    bw.write(tempUser.getId() + "," + tempUser.getName());
                    bw.newLine();
                }
            }
            //Overwrite the movie file wit the tmp one.
            Files.copy(tmp.toPath(), new File(USER_SOURCE).toPath(), StandardCopyOption.REPLACE_EXISTING);
            //Clean up after the operation is done (Remove tmp)
            Files.delete(tmp.toPath());
        } catch (IOException ex)
        {
            throw new Exception("Could not update user.", ex);
        }
    }
    
}
