package org.mdpnp.smartcardio.db;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mdpnp.smartcardio.dto.CardDTO;
import org.mdpnp.smartcardio.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class just has a couple methods to perform CRUD operations.
 * 
 * @author diego@mdpnp.org
 * 
 */

public class EmployeeManager {

	// Logger
	private static Logger loggerManager = LoggerFactory
			.getLogger(EmployeeManager.class);

	public CardDTO create(CardDTO cardDto) {

		Session session = null;
		Transaction tx = null;

		try {
			loggerManager.info("creating new card " + cardDto);
			session = HibernateUtil.getSessionFactory().openSession();// .getCurrentSession();
			// Is this an anti-pattern of opening and closing a Session for each
			// database call in a single thread
			// or is it OK because we are opening/closing (w/ the commit) the
			// connection for each request (session per request)
			// session is not thread safe, so maybe we should open close each
			// connection per request.
			// (see the nested exception problem)
			// session = HibernateUtil.getSession();

			// note 2: openSession() --> commit does NOT close session;
			// getCurrentSession()--> commit closes session

			tx = session.beginTransaction();

			cardDto.setCreationDate(new Date());
			cardDto.setModificationDate(cardDto.getCreationDate());
			// System.out.println(cardDto);
			session.save(cardDto);

			// /TODO This could be a good candidate for saveOrUpdate, to create
			// the card if it doesn't exit or update it if it does
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

		return cardDto;
	}

	public boolean delete(CardDTO cardDto) {
											
		Session session = null;
		Transaction tx = null;

		try {
			loggerManager.info("deleting card " + cardDto);
			session = HibernateUtil.getSessionFactory().openSession();// .getCurrentSession();
			tx = session.beginTransaction();
			System.out.println("Trying to delete card with UID "
					+ cardDto.getCardNumber());
			session.delete(cardDto);

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
			return false;

		} catch (Exception e) {
			if (null != tx) {
				tx.rollback();
			}
			if (null != session) {
				session.close();
			}
			loggerManager.error(e.getMessage());
			System.out.println(e.getMessage());
			return false;

		} finally {
			if (null != session && session.isOpen())
				session.close();
		}

		return true;
	}

	public CardDTO update(CardDTO CardDto) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();// .getCurrentSession();

			session.beginTransaction();
			CardDto.setModificationDate(new Date());
			session.update(CardDto);
			session.getTransaction().commit();// commit should close session
			return CardDto;
		} catch (HibernateException e) {
			if (null != session && session.getTransaction() != null) {
				session.getTransaction().rollback();
				session.close();
			}
			e.printStackTrace();
			loggerManager.error(e.getMessage());
			return CardDto;
		}
	}

	public CardDTO findByName(String username) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from CardDTO where username = :username ");
			query.setParameter("username", username);
			@SuppressWarnings("unchecked")
			List<CardDTO> cardList = query.list();
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

	public CardDTO findByUID(String cardnumber) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from CardDTO where cardnumber = :cardnumber ");
			query.setParameter("cardnumber", cardnumber);
			@SuppressWarnings("unchecked")
			List<CardDTO> cardList = query.list();
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

	public CardDTO findByAccess(String clinicalaccess) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from CardDTO where clinicalaccess = :clinicalaccess ");
			query.setParameter("clinicalaccess", clinicalaccess);
			@SuppressWarnings("unchecked")
			List<CardDTO> cardList = query.list();
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

	public List<CardDTO> findByAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from CardDTO");
			// NOTE: that for Hibernate the name of this table is not the name
			// on the DB
			// but the name of the DTO that we are associating via the *.hbm.xml
			// file
			@SuppressWarnings("unchecked")
			List<CardDTO> cardList = query.list();
			if (cardList.size() > 0)
				return cardList;
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
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}
	}

}