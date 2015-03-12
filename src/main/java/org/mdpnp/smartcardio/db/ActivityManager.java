package org.mdpnp.smartcardio.db;

import java.util.Date;

import org.hibernate.HibernateException;
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

		Session session = null;
		Transaction tx = null;

		try {
			loggerManager.info("creating new activity log " + logger);
			// session = HibernateUtil.getSessionFactory().openSession();
			session = HibernateUtil.getSessionFactory().getCurrentSession();

			tx = session.beginTransaction();

			logger.setDate(new Date());
			session.save(logger);

			tx.commit();

		} catch (HibernateException e) {
			if (null != tx) {
				tx.rollback();
			}
			if (null != session) {
				session.close();
			}
			loggerManager.error(e.getMessage());
			System.out.println(e.getMessage());

		} catch (Exception e) {
			if (null != tx) {
				tx.rollback();
			}
			if (null != session) {
				session.close();
			}
			loggerManager.error(e.getMessage());
			System.out.println(e.getMessage());

		} finally {
			if (null != session && session.isOpen())
				session.close();
		}

		return logger;
	}
}
