package wxrmq;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class WxRmqDB {
	private static SessionFactory sessionFactory = null;
	public static synchronized SessionFactory getDBFactory() {
		if(sessionFactory == null){
			 Configuration cfg = new Configuration().configure();
			 sessionFactory = cfg.buildSessionFactory();
		}
		return sessionFactory;
	}
}
