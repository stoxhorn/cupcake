package dat.cupcake.model.services;

import dat.cupcake.model.exceptions.DatabaseException;
import dat.cupcake.model.persistence.ConnectionPool;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SQLExecuter {


    public static boolean simpleInsert(ConnectionPool connectionPool, String tableName, String[] coloumns, Object[] parameters) throws SQLException, DatabaseException {

        ArrayList<String> newColoumns = new ArrayList<>(Arrays.asList(coloumns));
        ArrayList<Object> newParameters = new ArrayList<>(Arrays.asList(parameters));

        return simpleInsert(connectionPool, tableName, newColoumns, newParameters);
    }

    public static boolean simpleInsert(ConnectionPool connectionPool, String tableName, ArrayList<String> coloumns, ArrayList<Object> parameters) throws SQLException, DatabaseException {

        boolean isSuccesfull = false;

        String sql = "insert into " + tableName + " (";

        for(String str : coloumns) sql += str + ",";

        sql = sql.substring(0, sql.length()-1) + ") values( ";

        for(Object o : parameters) sql += "?,";

        sql = sql.substring(0, sql.length()-1) + ")";

        try (
                Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                int rowsAffected = finishAndExUpdate(ps, parameters);
                if (rowsAffected == 1)
                {
                    isSuccesfull = true;
                } else
                {
                    String exStr = "";
                    for(Object o : parameters){
                        exStr += o.toString();
                    }
                    throw new DatabaseException("Could not insert into table: " + tableName + "with parameters : " + exStr);
                }
            }
        }
        return isSuccesfull;
    }

    public static boolean simpleDelete(ConnectionPool connectionPool, String tableName, String[] variables, Object[] parameters) throws SQLException, DatabaseException {

        ArrayList<String> newVariables = new ArrayList<>(Arrays.asList(variables));
        ArrayList<Object> newParameters = new ArrayList<>(Arrays.asList(parameters));

        return simpleDelete(connectionPool, tableName, newVariables, newParameters);
    }

    public static boolean simpleDelete(ConnectionPool connectionPool, String tableName, ArrayList<String> variables, ArrayList<Object> parameters) throws SQLException, DatabaseException {

        if(variables.size() != parameters.size()){
            throw new RuntimeException("SimpleDelete throwing exception, because variable array and parameter array is not of equal length");
        }

        boolean result = false;
        String sql = "DELETE FROM " + tableName + " WHERE ";

        for (String str : variables) {
            sql += str + " = ? AND ";
        }

        sql = sql.substring(0, sql.length()-5) ;

        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                int rowsAffected = finishAndExUpdate(ps, parameters);
                if (rowsAffected == 1)
                {
                    result = true;
                } else
                {
                    String[] conditions = ps.toString().split(" WHERE ");
                    throw new DatabaseException("Could not delete row in " + tableName + " using the conditions: " + conditions[1]);
                }
            }
        }
        return result;
    }

    public static boolean simpleUpdate(ConnectionPool connectionPool, String tableName, String[] coloumns, String[] variables, Object[] parameters) throws SQLException, DatabaseException {

        ArrayList<String> newColoumns = new ArrayList<>(Arrays.asList(coloumns));
        ArrayList<String> newVariables = new ArrayList<>(Arrays.asList(variables));
        ArrayList<Object> newParameters = new ArrayList<>(Arrays.asList(parameters));

        return simpleUpdate(connectionPool, tableName, newColoumns, newVariables, newParameters);
    }

    public static boolean simpleUpdate(ConnectionPool connectionPool, String tableName, ArrayList<String> coloumns, ArrayList<String> variables, ArrayList<Object> parameters) throws SQLException, DatabaseException{


        boolean result = false;
        String sql = "UPDATE " + tableName + " SET ";

        for(String str : coloumns){
            sql += str + " = ?,";
        }

        sql = sql.substring(0, sql.length()-1) + " WHERE ";

        for (String str : variables) {
            sql += str + " = ? AND ";
        }

        sql = sql.substring(0, sql.length()-5) ;

        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {

                int rowsAffected = finishAndExUpdate(ps, parameters);

                if (rowsAffected == 1)
                {
                    result = true;
                } else
                {
                    throw new DatabaseException("Could not update " + tableName + " for the ");
                }
            }
        }
        return result;
    }

    public static ResultSet simpleSelect(ConnectionPool connectionPool, String tableName) throws SQLException {
        String sql = "SELECT * FROM " + tableName;

        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ArrayList<Object> tmp = new ArrayList<>();
                ResultSet rs = finishAndExQuery(ps, tmp);

                return rs;

            }
        }
    }

    public static ResultSet conditionalSelect(ConnectionPool connectionPool, String tableName, String[] coloumns, String[] variables, Object[] parameters) throws SQLException, DatabaseException{

        ArrayList<String> newColoumns = new ArrayList<>(Arrays.asList(coloumns));
        ArrayList<String> newVariables = new ArrayList<>(Arrays.asList(variables));
        ArrayList<Object> newParameters = new ArrayList<>(Arrays.asList(parameters));

        return conditionalSelect(connectionPool, tableName, newColoumns, newVariables, newParameters);
    }

    public static ResultSet conditionalSelect(ConnectionPool connectionPool, String tableName, ArrayList<String> coloumns, ArrayList<String> variables, ArrayList<Object> parameters) throws SQLException, DatabaseException{
        String sql = "SELECT ";

        for(Object str : coloumns){
            sql += str + ",";
        }

        sql = sql.substring(0, sql.length()-1) + " FROM " + tableName + " WHERE ";


        for (String str : variables) {
            sql += str + " = ? AND ";
        }

        sql = sql.substring(0, sql.length()-5) ;

        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ResultSet rs = finishAndExQuery(ps, parameters);
                return rs;

            }
        }
    }


    private static int finishAndExUpdate(PreparedStatement ps, ArrayList<Object> objects) throws SQLException {
        int i = 1;
        for(Object o : objects){
            addParamToStatement(ps, o, i);
            i++;
        }
        return ps.executeUpdate();
    }

    private static ResultSet finishAndExQuery(PreparedStatement ps, ArrayList<Object> objects) throws SQLException {
        int i = 1;
        for(Object o : objects){
            addParamToStatement(ps, o, i);
            i++;
        }
        ResultSet rs = ps.executeQuery();
        RowSetFactory factory = RowSetProvider.newFactory();
        CachedRowSet rowSet = factory.createCachedRowSet();
        rowSet.populate(rs);

        return rowSet;
    }

    private static void addParamToStatement(PreparedStatement ps, Object o, int i) throws SQLException {
        if (o instanceof String){
            ps.setString(i, (String) o);
        } else if (o instanceof Float){
            ps.setFloat(i, (Float) o);
        } else if (o instanceof Integer) {
            ps.setInt(i, (Integer) o);
        }
    }

}
