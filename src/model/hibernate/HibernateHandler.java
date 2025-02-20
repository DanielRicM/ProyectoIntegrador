package model.hibernate;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import model.interfaces.DataHandler;
import model.interfaces.Identifiable;

public class HibernateHandler<T extends Identifiable> implements DataHandler<Identifiable>, Closeable {

	private Session session;
	private String clazz;

	public HibernateHandler(String clazz) {
		this.clazz = clazz;
		HibernateUtil util = new HibernateUtil();
		session = util.getSession();
	}

	@Override
	public Map<Integer, Identifiable> readObjects() {
		Map<Integer, Identifiable> map = new HashMap<Integer, Identifiable>();
		TypedQuery<Identifiable> q = session.createQuery("from " + clazz);
		List<Identifiable> results = q.getResultList();
		Iterator<Identifiable> iterator = results.iterator();
		while (iterator.hasNext()) {
			Identifiable object = (Identifiable) iterator.next();
			map.put(object.getId(), object);

		}
		return map;
	}

	@Override
	public Identifiable readObject(int id) {
		TypedQuery<Identifiable> q = session.createQuery("from " + clazz + " where id=" + id);
		return q.getSingleResult();
	}

	@Override
	public void writeObjects(Map<Integer, Identifiable> map, boolean overwrite)  {
		Transaction writeTransaction = session.beginTransaction();
		for (Identifiable object : map.values()) {
			session.save(object);
		}
		writeTransaction.commit();
	}

	@Override
	public void writeObject(Identifiable newObject) throws ConstraintViolationException {
		Transaction writeTransaction = session.beginTransaction();
	        session.save(newObject);
	        writeTransaction.commit();
	}

	@Override
	public void deleteObject(int id) {
		Transaction deleteTransaction = session.beginTransaction();
		TypedQuery<Identifiable> q = session.createQuery("delete from " + clazz + " where id=" + id);
		q.executeUpdate();
		deleteTransaction.commit();
	}

	@Override
	public void modifyObject(int id, Identifiable newObject) {
		Transaction modifyTransaction = session.beginTransaction();
		session.update(newObject);
		modifyTransaction.commit();
	}

	@Override
	public void close() throws IOException {
		session.close();
	}

}
