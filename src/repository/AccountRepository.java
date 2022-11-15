package repository;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;

import connect.HibernateConnect;
import entity.Account;
import entity.Customer;
import entity.Role;

public class AccountRepository {

	public Account checkLogin(String username, String password) {
		Account account = null;
		Session session = HibernateConnect.getFactory().openSession();
		Query query = session
				.createQuery("select a from Account a where a.username = ?1 and a.password = ?2 and a.actived = ?3");
		query.setParameter(1, username);
		query.setParameter(2, password);
		query.setParameter(3, 1);
		try {
			account = (Account) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
		session.close();
		return account;
	}

	public Account findById(int id) {
		Session session = HibernateConnect.getFactory().openSession();
		Account a = session.get(Account.class, id);
		session.close();
		return a;
	}
	
	public void save(Account account) {
		Session session = HibernateConnect.getFactory().openSession();
		session.getTransaction().begin();
		session.save(account);
		session.getTransaction().commit();
		session.close();
	}
	public void saveRole(Role role) {
		Session session = HibernateConnect.getFactory().openSession();
		session.getTransaction().begin();
		session.save(role);
		session.getTransaction().commit();
		session.close();
	}

	public void update(Account account) {
		Session session = HibernateConnect.getFactory().openSession();
		session.getTransaction().begin();
		session.update(account);
		session.getTransaction().commit();
		session.close();
	}
	
	public void updateRole(Role role) {
		Session session = HibernateConnect.getFactory().openSession();
		session.getTransaction().begin();
		session.update(role);
		session.getTransaction().commit();
		session.close();
	}
	
	public void delete(Integer id) {
		Session session = HibernateConnect.getFactory().openSession();
		session.getTransaction().begin();
		Role role = session.get(Role.class, id);
		session.delete(role);
		System.out.println(role);
		Account account = session.get(Account.class, id);
		session.delete(account);
		System.out.println(account);
		session.getTransaction().commit();
		session.close();
	}

	public List<Account> findAll() {
		Session session = HibernateConnect.getFactory().openSession();
		List<Account> list = null;
		Query query = session.createQuery("From Account");
		list = query.getResultList();
		session.close();
		return list;
	}

	public List<Account> search(String param) {
		Session session = HibernateConnect.getFactory().openSession();
		List<Account> list = null;
		Query query = session.createQuery(
				"select a From Account a inner join a.roles r where a.username like ?1 or a.phone like ?1 or a.fullname like ?1 or a.address like ?1 or r.roleName like ?1");
		query.setParameter(1, "%" + param + "%");
		list = query.getResultList();
		session.close();
		return list;
	}
}
