package dat.cupcake.model.persistence;

import dat.cupcake.model.entities.User;
import dat.cupcake.model.exceptions.DatabaseException;

public interface IUserMapper
{
    public User login(String email, String kodeord) throws DatabaseException;
    public User createUser(String username, String password, String role) throws DatabaseException;
}
