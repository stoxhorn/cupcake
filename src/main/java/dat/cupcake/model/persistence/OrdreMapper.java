package dat.cupcake.model.persistence;

import dat.cupcake.model.entities.DBOrdre;
import dat.cupcake.model.entities.Ordre;
import dat.cupcake.model.exceptions.DatabaseException;
import dat.cupcake.model.services.OrdreBuilder;
import dat.cupcake.model.services.OrdreEntityConverter;
import dat.cupcake.model.services.SQLExecuter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrdreMapper {
    ConnectionPool connectionPool;

    public OrdreMapper(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
    }

    public boolean createDBOrdre(DBOrdre dbo) throws DatabaseException {
        return createDBOrdre(dbo.getOrdreId(), dbo.getKvitteringsId(), dbo.getBottom(), dbo.getTop());
    }
    public boolean createDBOrdre(int ordreId, int kvitteringsId, String bottom, String top) throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "");
        boolean result = false;
        String table = "ordre";

        try {
            result = SQLExecuter.simpleInsert(
                    this.connectionPool,
                    table,
                    new String[]{"ordreId","kvitteringsId","top", "bottom"},
                    new Object[]{ordreId, kvitteringsId, bottom, top}
            );
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting ordre: " + ordreId + ", " + kvitteringsId + ", " + bottom + ", " + top
                    );
        }

        return result;
    }

    public boolean removeDBOrdre(DBOrdre order) throws DatabaseException {
        return removeDBOrdre(order.getOrdreId());
    }

    public boolean removeDBOrdre(int id) throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "");
        boolean result = false;

        try {
            result = SQLExecuter.simpleDelete(
                    this.connectionPool,
                    "ordre",
                    new String[]{"ordreId"},
                    new Object[]{id}
            );
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting Ordre: " + id);
        }

        return result;

    }

    public boolean updateDBOrdre(DBOrdre  order) throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "");
        boolean result = false;
        String table = "ordre";

        try {
            result = SQLExecuter.simpleUpdate(
                    this.connectionPool,
                    table,
                    new String[]{"kvitteringsId", "top", "bottom"},
                    new String[]{"ordreId"},
                    new Object[]{order.getKvitteringsId(), order.getTop(), order.getBottom(), order.getOrdreId()}
            );
        } catch (SQLException e) {
            throw new DatabaseException("Error updating ordre: " + order.getOrdreId());
        }

        return result;
    }

    public DBOrdre fetchDBOrdre(int id) throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "");

        DBOrdre res = null;

        try {
            ResultSet rs = SQLExecuter.conditionalSelect(
                    this.connectionPool,
                    "ordre",
                    new String[]{"*"},
                    new String[]{"ordreId"},
                    new Object[]{id}
            );
            rs.first();
            res = new DBOrdre(rs.getInt("ordreId"), rs.getInt("kvitteringsId"), rs.getString("bottom"), rs.getString("top"));

        } catch (SQLException e) {
            throw new DatabaseException("Could not find an ordre with the id: " + id);
        }

        return res;
    }

    public ArrayList<DBOrdre> getAllDBOrdreFromKvitteringId(int id) throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "");

        ArrayList<DBOrdre> res = null;

        try {
            ResultSet rs = SQLExecuter.conditionalSelect(
                    this.connectionPool,
                    "ordre",
                    new String[]{"*"},
                    new String[]{"kvitteringsId"},
                    new Object[]{id}
            );
            res = new ArrayList<>();
            while(rs.next()){
                res.add(new DBOrdre(rs.getInt("ordreId"), rs.getInt("kvitteringsId"), rs.getString("bottom"), rs.getString("top")));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Could not find an ordre with the id: " + id);
        }

        return res;
    }

    public Ordre fetchOrdre(ConnectionPool cp, int ordreId) throws DatabaseException {
        OrdreBuilder ob = new OrdreBuilder(cp);
        DBOrdre dbo = fetchDBOrdre(ordreId);

        ob  .addOrdreId(dbo.getOrdreId())
                .addKvitteringsId(dbo.getKvitteringsId())
                .fetchBottom(dbo.getBottom())
                .fetchTop(dbo.getTop());

        return ob.buildOrdre();
    }
    public ArrayList<Ordre> fetchAllKvitteringsOrdre(int id) throws DatabaseException {
        ArrayList<DBOrdre> dboL = getAllDBOrdreFromKvitteringId(id);
        ArrayList<Ordre> oL = new ArrayList<>();
        if (dboL != null){
            for(DBOrdre dbo : dboL){

                OrdreBuilder ob = new OrdreBuilder(this.connectionPool);
                Ordre tmpOrdre = OrdreEntityConverter.dbOrdreFetchesOrdre(this.connectionPool, dbo);

                oL.add(tmpOrdre);
            }
        }
        return oL;
    }

    public ArrayList<DBOrdre> fetchAllDBOrdre() throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "");

        ArrayList<DBOrdre> res = null;

        try {
            ResultSet rs = SQLExecuter.simpleSelect(
                    this.connectionPool,
                    "ordre"
            );
            res = new ArrayList<>();
            while(rs.next()){
                res.add(
                        new DBOrdre(
                                rs.getInt("ordreId"),
                                rs.getInt("kvitteringsId"),
                                rs.getString("bottom"),
                                rs.getString("top")
                        )
                );
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error getting all DBOrdre");
        }
        return res;
    }
}

