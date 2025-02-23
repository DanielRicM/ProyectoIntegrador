package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import model.mongodb.MongoDBHandler;
import model.oodb.OODBHandler;
import model.basex.BaseXHandler;

import view.ConsoleView;
import model.hibernate.HibernateHandler;
import model.jsonphp.JSONPHPHandler;
import model.bbdd.MySQLHandler;
import model.bbdd.SQLiteHandler;
import model.fileio.BinaryFileHandler;
import model.fileio.TextFileHandler;
import model.fileio.XMLFileHandler;
import model.interfaces.DataHandler;
import model.interfaces.Identifiable;
import model.factory.ObjFactory;
import model.factory.StudentFactory;
import model.factory.SongFactory;

public class ConsoleController {

    private final ConsoleView view;
    private DataHandler<Identifiable> dataHandler;
    private ObjFactory<Identifiable> factory;
    private InputHandler inputHandler;
    private String table;
    private String clazz;

    public ConsoleController(ConsoleView view) {
        this.view = view;
    }

    public void run() {
        while (startMenu()) {
            selectObjectType();
            dataHandler = selectDataAccess();
            handleDataActions();
        }
    }

    private boolean startMenu() {
        return view.startMenu();
    }

    private void selectObjectType() {
        while (true) {
            int option = view.askObjectType();
            switch (option) {
                case 1:
                    inputHandler = new StudentInputHandler();
                    factory = new StudentFactory();
                    table = "student";
                    clazz = "Student";
                    return;
                case 2:
                    inputHandler = new SongInputHandler();
                    factory = new SongFactory();
                    table = "song";
                    clazz = "Song";
                    return;
                default:
                    view.optionNotValid();
            }
        }
    }

    private DataHandler<Identifiable> selectDataAccess() {
        while (true) {
            switch (view.askDataAccessType()) {
                case 1:
                    return createMySQLHandler();
                case 2:
                    return createSQLiteHandler();
                case 3:
                    return createTextFileHandler();
                case 4:
                    return createXMLFileHandler();
                case 5:
                    return createBinaryFileHandler();
                case 6:
                    return createHibernateHandler();
                case 7:
                    return createJSONPHPHandler();
                case 8:
                    return createOODBHandler();
                case 9:
                    return createBaseXHandler();
                case 10:
                    return createMongoDBHandler();
                default:
                    view.optionNotValid();
            }
        }
    }

    public void handleDataActions() {
        while (true) {
            switch (view.dataActions()) {
                case 1:
                    viewAllObjects();
                    break;
                case 2:
                    viewOneObject();
                    break;
                case 3:
                    writeOneObject();
                    break;
                case 4:
                    modifySingleObject();
                    break;
                case 5:
                    deleteOneObject();
                    break;
                case 6:
                    transferObjects();
                    break;
                case 7:
                    try {
                        dataHandler.close();
                    } catch (IOException e) {
                        view.displayMessage("Error closing the access: " + e.getMessage());
                    }
                    view.displayMessage("Exiting...");
                    return;
                default:
                    view.optionNotValid();
                    break;
            }
        }
    }

    private void viewAllObjects() {
        Map<Integer, Identifiable> map = dataHandler.readObjects();
        view.displayAllObjects(map);
    }

    private void viewOneObject() {
        Identifiable object = dataHandler.readObject(view.askId());
        if (object == null) {
            view.displayMessage("Object does not exist.");
            return;
        }
        view.displayOneObject(object);
    }

    private void writeOneObject() {
        Identifiable object = inputHandler.getDetails(null);
        try {
            dataHandler.writeObject(object);
        } catch (Exception e) {
            view.displayMessage("Error creating the object.");
        }
    }

    private void modifySingleObject() {
        try {
            int id = view.askId();
            Identifiable existingObject = dataHandler.readObject(id);
            Identifiable updatedObject = inputHandler.getDetails(existingObject);
            dataHandler.modifyObject(id, updatedObject);
            view.displayMessage("Update successful.");
        } catch (NumberFormatException e) {
            view.displayMessage("Id not valid.");
        } catch (IllegalArgumentException e) {
            view.displayMessage("Object not found.");
        }
    }

    private void deleteOneObject() {
        try {
            dataHandler.deleteObject(view.askId());
            view.displayMessage("Delete successful.");
        } catch (NumberFormatException e) {
            view.displayMessage("Id not valid.");
        }


    }

    private void transferObjects() {
        Map<Integer, Identifiable> map = dataHandler.readObjects();

        DataHandler<Identifiable> secondDataHandler = selectDataAccess();
        if (secondDataHandler == null) {
            return;
        }
        secondDataHandler.writeObjects(map, false);
        view.displayMessage("Transfer successful.");
        try {
            secondDataHandler.close();
        } catch (IOException e) {
            view.displayMessage("Error closing the second access.");
        }
    }

    private MySQLHandler<Identifiable> createMySQLHandler() {
        try {
            view.displayMessage("Creating MySQLHandler...");
            return new MySQLHandler<>(factory, table);
        } catch (ClassNotFoundException cnfe) {
            view.displayMessage("Class error: " + cnfe.getMessage());
        } catch (SQLException sqle) {
            view.displayMessage("SQL error: " + sqle.getMessage());
        }
        return null;
    }

    private SQLiteHandler<Identifiable> createSQLiteHandler() {
        try {
            view.displayMessage("Creating SQLiteHandler...");
            return new SQLiteHandler<>(factory, table);
        } catch (SQLException sqle) {
            view.displayMessage("SQL error: " + sqle.getMessage());
        }
        return null;
    }

    private TextFileHandler<Identifiable> createTextFileHandler() {
        try {
            view.displayMessage("Creating TextFileHandler...");
            return new TextFileHandler<>(clazz, factory);
        } catch (IOException ex) {
            view.displayMessage("Error instantiating TextFileHandler");
        }
        return null;
    }

    private XMLFileHandler<Identifiable> createXMLFileHandler() {
        try {
            view.displayMessage("Creating XMLFileHandler...");
            return new XMLFileHandler<>(clazz, factory);
        } catch (IOException ex) {
            view.displayMessage("Error instantiating XMLFileHandler");
        }
        return null;
    }

    private BinaryFileHandler<Identifiable> createBinaryFileHandler() {
        try {
            view.displayMessage("Creating BinaryFileHandler...");
            return new BinaryFileHandler<>(clazz);
        } catch (IOException ex) {
            view.displayMessage("Error instantiating BinaryFileHandler");
        }
        return null;
    }

    private HibernateHandler<Identifiable> createHibernateHandler() {
        view.displayMessage("Creating HibernateHandler...");
        return new HibernateHandler<>(clazz);
    }

    private JSONPHPHandler<Identifiable> createJSONPHPHandler() {
        view.displayMessage("Creating JSONPHPHandler...");
        return new JSONPHPHandler<>(table, factory);
    }

    private OODBHandler<Identifiable> createOODBHandler() {
        view.displayMessage("Creating OODBHandler...");
        return new OODBHandler<>(clazz);
    }

    private BaseXHandler<Identifiable> createBaseXHandler() {
        view.displayMessage("Creating BaseXHandler...");
        return new BaseXHandler<>(clazz, factory);
    }

    private MongoDBHandler<Identifiable> createMongoDBHandler() {
        view.displayMessage("Creating MongoDBHandler...");
        return new MongoDBHandler<>(table, factory);
    }
}
