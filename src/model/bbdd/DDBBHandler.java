package model.bbdd;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import model.factory.ObjFactory;
import model.interfaces.DataHandler;
import model.interfaces.Identifiable;

public abstract class DDBBHandler<T extends Identifiable> implements DataHandler<Identifiable>, AutoCloseable {

    protected Connection connection;
    protected String table;
    protected ObjFactory<Identifiable> factory;
    protected Statement stm;

    protected DDBBHandler(ObjFactory<Identifiable> factory, String table) {
        this.table = table;
        this.factory = factory;
    }

    protected abstract Connection getConnection() throws ClassNotFoundException, SQLException;

    @Override
    public Map<Integer, Identifiable> readObjects() {
        Map<Integer, Identifiable> map = new HashMap<>();

        try {
            String query = "Select * from " + table;
            ResultSet rs = stm.executeQuery(query);

            ResultSetMetaData metaData = rs.getMetaData();
            int nfields = metaData.getColumnCount();
            String[] fields = new String[nfields];

            for (int i = 0; i < nfields; i++) {
                fields[i] = metaData.getColumnName(i + 1);
            }

            while (rs.next()) {
                String line = "";
                for (String field : fields) {
                    line = line.concat(rs.getString(field) + ";");
                }
                Identifiable object = factory.create(line);
                map.put(object.getId(), object);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return map;
    }

    @Override
    public Identifiable readObject(int id) {

        try {
            String query = "Select * from " + table + " where id=" + String.valueOf(id);
            ResultSet rs = stm.executeQuery(query);
            rs.next();
            ResultSetMetaData metaData = rs.getMetaData();
            int nfields = metaData.getColumnCount();
            String[] fields = new String[nfields];

            for (int i = 0; i < nfields; i++) {
                fields[i] = metaData.getColumnName(i + 1);
            }

            String line = "";
            for (String field : fields) {
                line = line.concat(rs.getString(field) + ";");
            }
            Identifiable object = factory.create(line);

            return object;

        } catch (SQLException e) {
        }
        return null;
    }

    @Override
    public void writeObjects(Map<Integer, Identifiable> map, boolean overwrite) {

        try {
            for (Identifiable Object : map.values()) {
                String query = "Insert into " + table + " values (" + factory.toQuery(Object) + ")";
                stm.executeUpdate(query);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void writeObject(Identifiable newObject) {
        try {
            String query = "Insert into " + table + " values (" + factory.toQuery(newObject) + ") ";
            stm.executeUpdate(query);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteObject(int id) {
        try {
            String query = "Delete from " + table + " where id=" + id;
            stm.executeUpdate(query);
        } catch (SQLException e) {
        }

    }

    @Override
    public void modifyObject(int id, Identifiable newObject) {
        try {
            String query = factory.toUpdateQuery(newObject);
            stm.executeUpdate(query);
        } catch (SQLException e) {
        }

    }

    @Override
    public void close() throws IOException {
        try {
            if (stm != null && !stm.isClosed()) {
                stm.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

}
