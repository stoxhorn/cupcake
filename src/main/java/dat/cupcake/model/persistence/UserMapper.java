package dat.cupcake.model.persistence;

import dat.cupcake.model.entities.User;
import dat.cupcake.model.exceptions.DatabaseException;
import dat.cupcake.model.services.SQLExecuter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserMapper implements IUserMapper
{
    ConnectionPool connectionPool;

    public UserMapper(ConnectionPool connectionPool)
    {
        this.connectionPool = connectionPool;
    }

    @Override
    public User fetchUser(String username, String password) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO, "");

        User user = null;

        String sql = "SELECT * FROM user WHERE email = ? AND password = ?";

        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                if (rs.next())
                {
                    String role = rs.getString("role");
                    user = new User(username, password, role);
                } else
                {
                    throw new DatabaseException("Wrong username or password");
                }
            }
        } catch (SQLException ex)
        {
            throw new DatabaseException(ex, "Error logging in. Something went wrong with the database");
        }
        return user;
    }

    @Override
    public User createUser(String email, String password, String role) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO, "");
        User user;
        String sql = "insert into user (email, password, role) values (?,?,?)";
        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setString(1, email);
                ps.setString(2, password);
                ps.setString(3, role);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 1)
                {
                    user = new User(email, password, role);
                } else
                {
                    throw new DatabaseException("The user with email: " + email + " could not be inserted into the database");
                }
            }
        }
        catch (SQLException ex)
        {
            throw new DatabaseException(ex, "Could not insert username into database");
        }
        return user;
    }

    /**
     * Checks if a user with this email exists, and overwrites password and role.
     * @param userUpdate
     */
    public boolean updateUser(User userUpdate) throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "");
        boolean result = false;

        try {
            result = SQLExecuter.simpleUpdate(
                    this.connectionPool,
                    "user",
                    new String[]{"password"},
                    new String[]{"email"},
                    new Object[]{userUpdate.getPassword(), userUpdate.getEmail()}
            );
        } catch (SQLException e) {
            throw new DatabaseException("Error updating user with email: " + userUpdate.getEmail());
        }

        return result;
    }

    /**
     * Deletes a user using a string as email
     * @param email
     */
    public boolean removeUser(String email) throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "");
        boolean result = false;

        try {
            result = SQLExecuter.simpleDelete(
                    this.connectionPool,
                    "user",
                    new String[]{"email"},
                    new Object[]{email}
            );
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting user med email: " + email);
        }

        return result;
    }

    /**
     * deletes a user using an account object
     * @param userUpdate
     */
    public boolean removeUser(User userUpdate) throws DatabaseException {
        return removeUser(userUpdate.getEmail());
    }



    }
