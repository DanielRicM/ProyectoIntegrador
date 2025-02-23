package model.oodb;

import resources.ConfigManager;
import model.interfaces.DataHandler;
import model.interfaces.Identifiable;

import javax.persistence.*;
import java.io.Closeable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OODBHandler <T extends Identifiable> implements DataHandler<Identifiable>, Closeable {
    private final EntityManagerFactory emf;
    private final EntityManager em;
    private final String clazz;

    public OODBHandler(String clazz) {
        emf = Persistence.createEntityManagerFactory(ConfigManager.getProperty("odb.database"));
        em = emf.createEntityManager();
        this.clazz = clazz;
    }

    @Override
    public Map<Integer, Identifiable> readObjects() {
        Map<Integer, Identifiable> map = new HashMap<>();
        TypedQuery<Identifiable> q = em.createQuery("Select c from " + clazz + " c ", Identifiable.class);
        List<Identifiable> results = q.getResultList();
        for (Identifiable object : results) {
            map.put(object.getId(), object);
        }
        return map;
    }

    @Override
    public Identifiable readObject(int id) {
        TypedQuery<Identifiable> q = em.createQuery("Select c from " + clazz + " c where id=" + id, Identifiable.class);
        return q.getSingleResult();
    }

    @Override
    public void writeObjects(Map<Integer, Identifiable> map, boolean overwrite) {
        em.getTransaction().begin();
        for (Identifiable object : map.values()) {
            em.merge(object);
        }
        em.getTransaction().commit();
    }

    @Override
    public void writeObject(Identifiable newObject) {
        em.getTransaction().begin();
        em.persist(newObject);
        em.getTransaction().commit();
    }

    @Override
    public void deleteObject(int id) {
        em.getTransaction().begin();
        em.createQuery("delete from " + clazz + " where id=" + id).executeUpdate();
        em.getTransaction().commit();
    }

    @Override
    public void modifyObject(int id, Identifiable newObject) {
        em.getTransaction().begin();
        em.merge(newObject);
        em.getTransaction().commit();
    }

    @Override
    public void close() {
        em.close();
        emf.close();
    }
}
