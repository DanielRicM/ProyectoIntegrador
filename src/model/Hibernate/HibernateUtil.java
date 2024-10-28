package model.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	private SessionFactory sessionFactory;

	private Session session;

	public HibernateUtil() {
		sessionFactory = new Configuration().configure().buildSessionFactory();
		session = sessionFactory.openSession();

	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public Session getSession() {
		return session;
	}
}
