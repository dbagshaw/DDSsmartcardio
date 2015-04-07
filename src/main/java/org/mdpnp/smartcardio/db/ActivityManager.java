package org.mdpnp.smartcardio.db;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mdpnp.smartcardio.activity.ActivityLogger;
import org.mdpnp.smartcardio.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivityManager {

	private static Logger loggerManager = LoggerFactory
			.getLogger(ActivityManager.class);

	public ActivityLogger create(ActivityLogger logger) {

		Session sessionTwo = null;
		Transaction tx = null;
		Long rowID = null;

		try {
			// loggerManager.info("creating new activity log " + logger);
			// sessionTwo = HibernateUtil.getSessionFactory().openSession();
			sessionTwo = HibernateUtil.getSessionFactory().getCurrentSession();

			tx = sessionTwo.beginTransaction();

			logger.setDate(new Date());

			// sessionTwo.saveOrUpdate(logger);
			rowID = (Long) sessionTwo.save(logger);

			tx.commit();

			// session.close();

		} catch (HibernateException e) {
			if (null != tx) {
				tx.rollback();
				rowID = null;
			}
			loggerManager.error(e.getMessage());
		} catch (Exception e) {
			if (null != tx) {
				tx.rollback();
				rowID = null;
			}
			loggerManager.error(e.getMessage());
		}

		logger.setId(rowID);
		// sessionTwo.close();
		return logger;
	}
	
	public ActivityLogger findByName(String username) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from ActivityLogger where username = :username ");
			query.setParameter("username", username);
			@SuppressWarnings("unchecked")
			List<ActivityLogger> cardList = query.list();
			if (cardList.size() > 0)
				return cardList.get(0);
			else
				return null;
		} catch (HibernateException e) {
			if (session.getTransaction() != null) {
				session.getTransaction().rollback();
				session.close();
			}
			e.printStackTrace();
			loggerManager.error(e.getMessage());
			return null;
		}
	}
}
