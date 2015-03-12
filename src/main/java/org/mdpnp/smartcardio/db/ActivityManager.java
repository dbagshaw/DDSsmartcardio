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

		// } catch (HibernateException e) {
		// if (null != tx) {
		// tx.rollback();
		// rowID = null;
		// }
		// if (null != sessionTwo) {
		// sessionTwo.close();
		// }
		// loggerManager.error(e.getMessage());
		// // System.out.println(e.getMessage());
		//
		// } catch (Exception e) {
		// if (null != tx) {
		// tx.rollback();
		// rowID = null;
		// }
		// if (null != sessionTwo) {
		// sessionTwo.close();
		// }
		// loggerManager.error(e.getMessage());
		// // System.out.println(e.getMessage());
		//
		// } finally {
		// if (null != sessionTwo && sessionTwo.isOpen())
		// sessionTwo.close();
		// }

		logger.setId(rowID);
		// sessionTwo.close();
		return logger;
	}
}
