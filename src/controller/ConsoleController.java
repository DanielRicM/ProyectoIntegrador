package controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import model.mongodb.MongoDBHandler;
import model.oodb.OODBHandler;
import model.basex.BaseXHandler;

import view.ConsoleView;
import model.hibernate.HibernateHandler;
import model.jsonphp.JSONPHPHandler;
import model.bbdd.DDBBHandler;
import model.bbdd.MySQLHandler;
import model.bbdd.SQLiteHandler;
import model.fileio.BinaryFileHandler;
import model.fileio.FileHandler;
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
                    table = "students";
                    clazz = "Student";
                    return;
                case 2:
                    inputHandler = new SongInputHandler();
                    factory = new SongFactory();
                    table = "songs";
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
                    return createDDBBHandler(view.askDatabase());
                case 2:
                    return createFileHandler(view.askFilePath());
                case 3:
                    return createHibernateHandler();
                case 4:
                    return createJSONPHPHandler();
                case 5:
                    return createOODBHandler();
                case 6:
                    return createBaseXHandler();
                case 7:
                    return createMongoDBHandler();
                default:
                    view.optionNotValid();
            }
        }
    }

    public void handleDataActions() {
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

    private void viewAllObjects() {
        Map<Integer, Identifiable> map = dataHandler.readObjects();
        view.displayAllObjects(map);
    }

    private void viewOneObject() {
        Identifiable object = dataHandler.readObject(view.askId());
        view.displayOneObject(object);
    }

    private void writeOneObject() {
        Identifiable object = inputHandler.getDetails(null);
        try {
            dataHandler.writeObject(object);
        } catch (Exception e) {
            view.displayMessage("Error creating the object: " + e.getMessage());
        }
    }

    private void modifySingleObject() {
        int id = view.askId();

        try {
            Identifiable existingObject = dataHandler.readObject(id);
            Identifiable updatedObject = inputHandler.getDetails(existingObject);
            dataHandler.modifyObject(id, updatedObject);
            view.displayMessage("Update successful.");
        } catch (IllegalArgumentException e) {
            view.displayMessage("Object not found.");
            view.displayMessage(e.toString());
        }
    }

    private void deleteOneObject() {
        dataHandler.deleteObject(view.askId());
    }

    private void transferObjects() {
        Map<Integer, Identifiable> map = dataHandler.readObjects();

        DataHandler<Identifiable> secondDataHandler = selectDataAccess();
        // TODO Handle nullPointerException better
        if (secondDataHandler == null) {
            return;
        }
        secondDataHandler.writeObjects(map, false);
    }

    public String getExtension(String filePath) {
        String[] parts = filePath.split("\\.");
        return parts[parts.length - 1];
    }

    private FileHandler<Identifiable> createFileHandler(String filePath) {
        FileHandler<Identifiable> access;
        String extension = getExtension(filePath);
        try {
            switch (extension) {
                case "txt":
                    access = new TextFileHandler<>(new File(filePath), factory);
                    return access;
                case "dat", "bin":
                    access = new BinaryFileHandler<>(new File(filePath));
                    return access;
                case "xml":
                    access = new XMLFileHandler<>(new File(filePath), factory);
                    return access;
                default:
                    //TODO: create custom exception
                    throw new IOException("File type not supported: " + extension);
            }
        } catch (IOException ex) {
            view.displayMessage("Error instantiating FileHandler: " + ex.getMessage());
        }
        return null;
    }

    private DDBBHandler<Identifiable> createDDBBHandler(String database) {
        String databaseType = getExtension(database);
        try {
            if (databaseType.equals(database)) {
                view.displayMessage("MySQLHandler Created");
                return new MySQLHandler<>(database, factory, table);
            } else if (databaseType.equals("db")) {
                view.displayMessage("SQLiteHandler Created");
                return new SQLiteHandler<>(database, factory, table);
            } else {
                throw new IOException("Database type not supported: " + databaseType);
            }
        } catch (ClassNotFoundException cnfe) {
            view.displayMessage("Class error: " + cnfe.getMessage());
        } catch (SQLException sqle) {
            view.displayMessage("SQL error: " + sqle.getMessage());
        } catch (IOException ex) {
            view.displayMessage("Error instantiating DDBBHandler: " + ex.getMessage());
        }
        return null;
    }

    private HibernateHandler<Identifiable> createHibernateHandler() {
        view.displayMessage("HibernateHandler Created");
        return new HibernateHandler<>(clazz);
    }

    private JSONPHPHandler<Identifiable> createJSONPHPHandler() {
        view.displayMessage("JSONPHPHandler Created");
        return new JSONPHPHandler<>(table, factory);
    }

    private OODBHandler<Identifiable> createOODBHandler() {
        view.displayMessage("OODBHandler Created");
        return new OODBHandler<>(clazz);
    }

    private BaseXHandler<Identifiable> createBaseXHandler() {
        view.displayMessage("BaseXHandler Created");
        return new BaseXHandler<>(clazz, factory);
    }

    private MongoDBHandler<Identifiable> createMongoDBHandler() {
        view.displayMessage("MongoDBHandler Created");
        return new MongoDBHandler<>(table, factory);
    }
}
