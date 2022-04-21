package dat.cupcake.model.persistence;

import dat.cupcake.model.entities.DBWallet;
import dat.cupcake.model.exceptions.DatabaseException;
import dat.cupcake.model.services.SQLExecuter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WalletMapper {
    ConnectionPool connectionPool;


    public WalletMapper(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
    }
    /**
     * Does not check if a user exists with the given email
     * If no such user exists an error will be thrown that is not handled properly
     * "could not fulfill a requirement/rule" or something
     * Tested positive
     */
    public boolean createDBWallet(String email) throws DatabaseException {
        return createDBWallet(email, 0.0F);
    }

    public boolean createDBWallet(String email, float balance) throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "");
        boolean result = false;

        ArrayList<String> coloumns = new ArrayList<>(); coloumns.add("email"); coloumns.add("kroner");
        ArrayList<Object> parameters = new ArrayList<>(); parameters.add(email); parameters.add(balance);

        try {
            result = SQLExecuter.simpleInsert(
                    this.connectionPool,
                    "wallet",
                    coloumns,
                    parameters);
        } catch (SQLException e) {
            throw new DatabaseException(e, "Could not insert wallet into database");
        }

        return result;
    }

    /**
     * tested positive
     * @param wallet
     * @return
     * @throws DatabaseException
     */
    public boolean removeDBWallet(DBWallet wallet) throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "");
        String email = wallet.getEmail();
        boolean result = false;
        try {
            result = SQLExecuter.simpleDelete(
                    connectionPool,
                    "wallet",
                    new String[]{"email"},
                    new Object[]{wallet.getEmail()}
            );
        }catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not delete wallet for user with email: " + wallet.getEmail() + " into database");
        }
        return result;
    }


    public boolean updateDBWallet(DBWallet  wallet) throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "");
        String email = wallet.getEmail();
        float newBalance = wallet.getBalance();
        boolean result = false;

        try {
            result = SQLExecuter.simpleUpdate(
                    connectionPool,
                    "wallet",
                    new String[]{"kroner"},
                    new String[]{"email"},
                    new Object[]{newBalance, email}
            );
        } catch (SQLException e) {
            throw new DatabaseException(e, "Could not update wallet for user with email: " + email);
        }


        return result;
    }


    public DBWallet getDBWallet(String email) throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "");

        DBWallet res = null;

        try {
            ResultSet rs = SQLExecuter.conditionalSelect(
                    this.connectionPool,
                    "wallet",
                    new String[]{"*"},
                    new String[]{"email"},
                    new Object[]{email}
            );
            try{
                rs.first();
                res = new DBWallet(rs.getString("email"), rs.getFloat("kroner"));
            }
            catch (Exception e){
                // no results
                return null;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Could not find a wallet with the email: " + email);
        }

        return res;

    }
}
