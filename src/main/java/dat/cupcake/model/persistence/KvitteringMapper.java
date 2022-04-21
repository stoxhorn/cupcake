package dat.cupcake.model.persistence;

import dat.cupcake.model.entities.DBKvittering;
import dat.cupcake.model.entities.Kvittering;
import dat.cupcake.model.exceptions.DatabaseException;
import dat.cupcake.model.services.KvitteringBuilder;
import dat.cupcake.model.services.KvitteringEntityConverter;
import dat.cupcake.model.services.SQLExecuter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KvitteringMapper {

    ConnectionPool connectionPool;

    public KvitteringMapper(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
    }

    public boolean createDBKvittering(DBKvittering kvit) throws DatabaseException {

        return createDBKvittering(kvit.getKvitteringsId(), kvit.getEmail(), kvit.getStatus(), kvit.getDatoOprettet());
    }

    public boolean createDBKvittering(int kvitteringsId, String email, String status, String datoOprettet) throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "");
        boolean result = false;

        try {
            result = SQLExecuter.simpleInsert(
                    this.connectionPool,
                    "kvittering",
                    new String[]{"kvitteringsId","email","status", "datoOprettet"},
                    new Object[]{kvitteringsId, email, status, datoOprettet}
            );
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting Kvittering: " + kvitteringsId + ", " + email + ", " + status + ", " + datoOprettet
            );
        }

        return result;
    }

    public boolean removeDBKvittering(DBKvittering receipt) throws DatabaseException {
        return removeDBKvittering(receipt.getKvitteringsId());
    }

    public boolean removeDBKvittering(int id) throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "");
        boolean result = false;

        try {
            result = SQLExecuter.simpleDelete(
                    this.connectionPool,
                    "kvittering",
                    new String[]{"kvitteringsId"},
                    new Object[]{id}
            );
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting Kvittering med ID: " + id);
        }

        return result;

    }

    public boolean updateDBKvittering(DBKvittering receiptUpdate) throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "");
        boolean result = false;

        try {
            result = SQLExecuter.simpleUpdate(
                    this.connectionPool,
                    "kvittering",
                    new String[]{"status", "datoOprettet"},
                    new String[]{"kvitteringsId"},
                    new Object[]{receiptUpdate.getStatus(), receiptUpdate.getDatoOprettet(), receiptUpdate.getKvitteringsId()}
            );
        } catch (SQLException e) {
            throw new DatabaseException("Error updating kvittering med ID: " + receiptUpdate.getKvitteringsId());
        }

        return result;
    }


    public DBKvittering getDBKvittering(int receiptId) throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "");

        DBKvittering res = null;

        try {
            ResultSet rs = SQLExecuter.conditionalSelect(
                    this.connectionPool,
                    "kvittering",
                    new String[]{"kvitteringsId", "email", "status", "datoOprettet"},
                    new String[]{"kvitteringsId"},
                    new Object[]{receiptId}
            );
            rs.first();
            String tmp = rs.toString();
            int tmpint = rs.getInt("kvitteringsId");
            String tmpStr = rs.getString("email");
            String tmpstr1 = rs.getString("status");
            String tmpStr2 = rs.getString("datoOprettet");

            res = new DBKvittering(
                    rs.getInt("kvitteringsId"),
                    rs.getString("email"),
                    rs.getString("status"),
                    rs.getString("datoOprettet")
            );

        } catch (SQLException e) {
            throw new DatabaseException("Could not find a kvittering with the ID: " + receiptId);
        }

        return res;

    }

    public ArrayList<DBKvittering> getAllDBKvitteringsFromEmail(String email) throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "");

        ArrayList<DBKvittering> res = null;

        try {
            ResultSet rs = SQLExecuter.conditionalSelect(
                    this.connectionPool,
                    "kvittering",
                    new String[]{"*"},
                    new String[]{"email"},
                    new Object[]{email}
            );
            res = new ArrayList<>();
            while(rs.next()){
                res.add(new DBKvittering(
                        rs.getInt("kvitteringsId"),
                        rs.getString("email"),
                        rs.getString("status"),
                        rs.getString("datoOprettet")));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Could not find a kvittering with the ID: " + email
                    );
        }

        return res;

    }
    public ArrayList<Kvittering> fetchAllKvitteringFromEmail(String email) throws DatabaseException {
        ArrayList<DBKvittering> dbkL = getAllDBKvitteringsFromEmail(email);
        ArrayList<Kvittering> kL = new ArrayList<>();

        if (dbkL != null){
            for(DBKvittering dbk : dbkL){

                KvitteringBuilder kb = new KvitteringBuilder(this.connectionPool);
                Kvittering tmpKvit = KvitteringEntityConverter.DBKvitteringFetchesKvittering(this.connectionPool, dbk);

                kL.add(tmpKvit);
            }
        }
        return kL;
    }

    public ArrayList<DBKvittering> fetchAllDBKvittering() throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "");

        ArrayList<DBKvittering> res = null;

        try {
            ResultSet rs = SQLExecuter.simpleSelect(
                    this.connectionPool,
                    "kvittering"
            );
            res = new ArrayList<>();
            while(rs.next()){
                res.add(
                        new DBKvittering(
                                rs.getInt("kvitteringsId"),
                                rs.getString("email"),
                                rs.getString("status"),
                                rs.getString("datoOprettet")
                        )
                );
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error getting all DBKvitterings");
        }
        return res;
    }


    public ArrayList<Kvittering> fetchAllKvittering() throws DatabaseException {
        ArrayList<DBKvittering> dbkL = fetchAllDBKvittering();

        ArrayList<Kvittering> kL = new ArrayList<>();

        for(DBKvittering dbk : dbkL){
            kL.add(KvitteringEntityConverter.DBKvitteringFetchesKvittering(connectionPool, dbk));
        }
        return kL;
    }
}
