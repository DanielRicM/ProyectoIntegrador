package model.Hibernate;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

import model.interfaces.DataHandler;
import model.interfaces.Identifiable;

public class HibernateHandler<T extends Identifiable> implements DataHandler<T>, Closeable {

	private Session session;

	public HibernateHandler() {
		HibernateUtil util = new HibernateUtil();
		session = util.getSession();
	}

	@Override
	public Map<Integer, T> readObjects() {
		Map<Integer, T> map = new HashMap<Integer, T>();
		TypedQuery<T> q = session.createQuery("from Student");
		List<T> results = q.getResultList();
		Iterator<T> iterator = results.iterator();
		while (iterator.hasNext()) {
			T object = (T) iterator.next();
			map.put(object.getId(), object);

		}
		return map;
	}

	@Override
	public T readObject(int id) {
		TypedQuery<T> q = session.createQuery("from Student where id=" + id);
		return q.getSingleResult();
	}

	@Override
	public void writeObjects(Map<Integer, T> map, boolean overwrite) {
		session.beginTransaction();
		for(T object: map.values()) {
			session.save(object);
		}
		session.getTransaction().commit();

	}

	@Override
	public void writeObject(T newObject) {
		Transaction writeTransaction = session.beginTransaction();
		session.save(newObject);
		writeTransaction.commit();
	}

	@Override
	public void deleteObject(int id) {
		Transaction deleteTransaction = session.beginTransaction();
		TypedQuery<T> q = session.createQuery("delete from Student where id=" + id);
		q.executeUpdate();
		deleteTransaction.commit();
	}

	@Override
	public void modifyObject(int id, T newObject) {
		Transaction modifyTransaction = session.beginTransaction();
		deleteObject(id);
		writeObject(newObject);
		modifyTransaction.commit();
	}

	@Override
	public void close() throws IOException {
		session.close();
	}

}
