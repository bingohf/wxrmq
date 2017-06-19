package wxrmq;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.w3c.dom.css.ElementCSSInlineStyle;

import com.google.gson.Gson;

import wxrmq.data.remote.WxUserInfo;
import wxrmq.domain.TagValue;
import wxrmq.domain.WxUser;
import wxrmq.domain.WxUser_Tag;
import wxrmq.utils.TextUtils;


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

	public static synchronized void delete(String hql) {
		SessionFactory dbfactory = RmqDB.getDBFactory();
		Session session = dbfactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			 Query query=session.createQuery(hql);
			 query.executeUpdate();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
		}
		session.close();
	}
	
	public static synchronized List<Object[]> sqlQuery(String sql, Object... params) {

		SessionFactory dbfactory = RmqDB.getDBFactory();
		Session session = dbfactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session.createSQLQuery(sql);
			int i =0;
			for (Object param : params) {
				if (param instanceof Integer){
					query = query.setInteger(i++, (Integer)param);
				}else if (param instanceof String){
					query = query.setString(i++, (String)param);
				}else if (param instanceof Long){
					query = query.setLong(i++, (Long)param);
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
				}else if (param instanceof Long){
					query = query.setLong(i++, (Long)param);
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
	
	public static <T> T getById(Class<T> cls, Serializable id) {
		SessionFactory dbfactory = RmqDB.getDBFactory();
		Session session = dbfactory.openSession();
		try {
			return session.get(cls, id);
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}
	
	
	public static void saveWxUserInfo(WxUserInfo wxUserInfo, WxUser wxUser){
		wxUser.setUin(wxUserInfo.getUin());
		wxUser.setNickName(wxUserInfo.getNickName());
		wxUser.setFriendsCount(wxUserInfo.getFriendInfo().getFriendsCount());
		wxUser.setAge(wxUserInfo.getAge());
		wxUser.setQuota(wxUserInfo.getQuota());
		wxUser.setWxId(wxUserInfo.getWxId());
		wxUser.setProvince(wxUserInfo.getProvince());
		wxUser.setCity(wxUserInfo.getCity());
		wxUser.setSex(wxUserInfo.getSex());
		wxUser.setIndustry(TextUtils.concat(wxUserInfo.getIndustry(),"/"));
		wxUser.setInfoJson(new Gson().toJson(wxUserInfo));
		int maleCount = wxUserInfo.getFriendInfo().getSexs().get(0).getY();
		int femaleCount = wxUserInfo.getFriendInfo().getSexs().get(1).getY();
		if(maleCount + femaleCount >0){
			wxUser.setMalePercent( (maleCount * 100f / (maleCount + femaleCount)));
		}
		
		SessionFactory dbfactory = RmqDB.getDBFactory();
		Session session = dbfactory.openSession();
		Transaction tx = session.beginTransaction();
		try{		
			Query query=session.createQuery("delete WxUser_Tag s where s.uin='" + wxUser.getUin() +"'");
			query.executeUpdate();
			session.saveOrUpdate(wxUser);
			List<TagValue> citys = wxUserInfo.getFriendInfo().getCitys();
			for(TagValue tagValue : citys){
				if(tagValue.getY() >=50){
					WxUser_Tag tag = new WxUser_Tag();
					tag.setLabel(tagValue.getTag());
					tag.setCount(tagValue.getY());
					tag.setSubLabel(tagValue.getSubTag());
					tag.setUin(wxUser.getUin());
					tag.setType(WxUser_Tag.TYPE_CITY);
					session.saveOrUpdate(tag);
				}
			}
			for(String industry :wxUserInfo.getIndustry()){
				WxUser_Tag tag = new WxUser_Tag();
				tag.setLabel(industry);
				tag.setCount(1);
				tag.setSubLabel("");
				tag.setUin(wxUser.getUin());
				tag.setType(WxUser_Tag.TYPE_INDUSTRY);
				session.saveOrUpdate(tag);
			}
		tx.commit();
		}catch(HibernateException e){
			e.printStackTrace();
			tx.rollback();
		}
		session.close();

		
	}
	
}
