package wxrmq;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;


public class RmqDB {
	private static SessionFactory sessionFactory = null;

	public static synchronized SessionFactory getDBFactory() {
		if (sessionFactory == null) {
			Configuration cfg = new Configuration().configure();
			sessionFactory = cfg.buildSessionFactory();
		}
		return sessionFactory;
	}

	public static synchronized void saveOrUpdate(Object object) {
		SessionFactory dbfactory = RmqDB.getDBFactory();
		Session session = dbfactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			session.saveOrUpdate(object);
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
		}
		session.close();
	}

	public static synchronized void save(Object object) {
		SessionFactory dbfactory = RmqDB.getDBFactory();
		Session session = dbfactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			session.save(object);
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
		}
		session.close();
	}

	public static synchronized List<Object[]> query(String hql, Object... params) {

		SessionFactory dbfactory = RmqDB.getDBFactory();
		Session session = dbfactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session.createQuery(hql);
			int i =0;
			for (Object param : params) {
				if (param instanceof Integer){
					query = query.setInteger(i++, (Integer)param);
				}else if (param instanceof String){
					query = query.setString(i++, (String)param);
				}
			}
			List<Object[]> list=  query.list();
			tx.commit();
			return list;
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return null;
	}
}
