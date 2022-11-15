package repository;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;

import connect.HibernateConnect;
import entity.Customer;
import entity.Room;

public class CustomerRepository {

	
	public void save(Customer customer) {
		Session session = HibernateConnect.getFactory().openSession();
		session.getTransaction().begin();
		session.save(customer);
		session.getTransaction().commit();
		session.close();
	}
	
	public boolean update(Customer customer) {
		Session session = HibernateConnect.getFactory().openSession();
		session.getTransaction().begin();
		session.update(customer);
		session.getTransaction().commit();
		session.close();
		return customer.getId() !=null;
	}
	
	public Customer findByCardId(String cardid) {
		Session session = HibernateConnect.getFactory().openSession();
		Query query = session.createQuery("select c from Customer c where c.citizenIdentificationNumber = ?1");
		query.setParameter(1, cardid);
		Customer customer = (Customer) query.getSingleResult();
		session.close();
		return customer;
	}
	
	public Customer findById(Integer id) {
		Customer customer = null;
		Session session = HibernateConnect.getFactory().openSession();
		try {
			customer = session.get(Customer.class, id);
		} catch (Exception e) {
			return null;
		}
		session.close();
		return customer;
	}
	
	public List<Customer> findAll(){
		Session session = HibernateConnect.getFactory().openSession();
		List<Customer> list = null;
		Query query = session.createQuery("From Customer");
		list = query.getResultList();
		session.close();
		return list;
	}
	
	public List<Customer> search(String param){
		Session session = HibernateConnect.getFactory().openSession();
		List<Customer> list = null;
		Query query = session.createQuery("Select c From Customer c where c.name like ?1 or c.citizenIdentificationNumber like ?1 or c.nationality like ?1");
		query.setParameter(1, "%"+param+"%");
		list = query.getResultList();
		session.close();
		return list;
	}
	
	public List<Customer> search(int month, int year){
		Session session = HibernateConnect.getFactory().openSession();
		List<Customer> list = null;
		Query query = session.createQuery("Select c From Customer c where MONTH(c.createdDate) = ?1 and YEAR(c.createdDate) = ?2");
		query.setParameter(1, month);
		query.setParameter(2, year);
		list = query.getResultList();
		session.close();
		return list;
	}
}
