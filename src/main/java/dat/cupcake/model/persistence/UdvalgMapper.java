package dat.cupcake.model.persistence;

import dat.cupcake.model.entities.DBUdvalg;
import dat.cupcake.model.entities.Udvalg;
import dat.cupcake.model.exceptions.DatabaseException;
import dat.cupcake.model.services.SQLExecuter;
import dat.cupcake.model.services.UdvalgEntityConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UdvalgMapper {

    ConnectionPool connectionPool;

    public UdvalgMapper(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
    }

    public boolean createDBUdvalg(String choice, String type, float price) throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "");
        boolean result = false;
        String table = "udvalg";

        try {
            result = SQLExecuter.simpleInsert(
                    this.connectionPool,
                    table,
                    new String[]{"valg","pris","type"},
                    new Object[]{choice, price, type}
            );
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting Udvalg: " + choice + ", " + type + ", " + price);
        }

        return result;
    }

    public boolean removeDBUdvalg(String valg) throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "");
        boolean result = false;

        try {
            result = SQLExecuter.simpleDelete(
                    this.connectionPool,
                    "udvalg",
                    new String[]{"valg"},
                    new Object[]{valg}
            );
        } catch (SQLException e) {
            throw new DatabaseException("Error delete Udvalg: " + valg);
        }

        return result;
    }

    public boolean updateDBUdvalg(DBUdvalg  choice) throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "");
        boolean result = false;
        String table = "udvalg";

        String valg = choice.getValg();
        float pris = choice.getPrice();

        try {
            result = SQLExecuter.simpleUpdate(
                    this.connectionPool,
                    table,
                    new String[]{"pris"},
                    new String[]{"valg"},
                    new Object[]{pris, valg}
            );
        } catch (SQLException e) {
            throw new DatabaseException("Error updating Udvalg: " + choice);
        }

        return result;
    }

    public DBUdvalg getDBUdvalg(String valg) throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "");

        DBUdvalg res = null;

        try {
            ResultSet rs = SQLExecuter.conditionalSelect(
                    this.connectionPool,
                    "udvalg",
                    new String[]{"*"},
                    new String[]{"valg"},
                    new Object[]{valg}
            );
            rs.first();
            res = new DBUdvalg(
                    rs.getString("valg"),
                    rs.getString("type"),
                    rs.getFloat("pris")
            );

        } catch (SQLException e) {
            throw new DatabaseException("Could not find an udvalg with the valg: " + valg);
        }

        return res;

    }

    public Udvalg getUdvalg(String valg) throws DatabaseException {
        DBUdvalg dbu = this.getDBUdvalg(valg);

        if (dbu != null){
            return UdvalgEntityConverter.dbUdvalgToUdvalg(dbu);
        }
        return null;
    }

    public ArrayList<Udvalg> getAllUdvalg() throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "");
        ArrayList<Udvalg> res = null;

        try {
            ResultSet rs = SQLExecuter.simpleSelect(
                    this.connectionPool,
                    "udvalg"
            );
            res = new ArrayList<>();
            while(rs.next()){
                res.add(
                        new Udvalg(
                                rs.getString("valg"),
                                rs.getString("type"),
                                rs.getFloat("pris")
                        )
                );
            }
            String test = "asd";
        } catch (SQLException e) {
            throw new DatabaseException("Error getting all Udvalg");
        }
        return res;
    }


}
