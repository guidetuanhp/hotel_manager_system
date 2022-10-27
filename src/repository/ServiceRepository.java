package repository;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;

import connect.HibernateConnect;
import entity.Room;
import entity.Services;

public class ServiceRepository {

	public boolean save(Services services) {
		Session session = HibernateConnect.getFactory().openSession();
		session.getTransaction().begin();
		session.save(services);
		session.getTransaction().commit();
		session.close();
		if(services.getId() != null) {
			return true;
		}
		return false;
	}
	
	public void update(Services services) {
		Session session = HibernateConnect.getFactory().openSession();
		session.getTransaction().begin();
		session.update(services);
		session.getTransaction().commit();
		session.close();
	}
	
	public void delete(int id) {
		Session session = HibernateConnect.getFactory().openSession();
		session.getTransaction().begin();
		Services ser = session.get(Services.class, id);
		session.delete(ser);
		session.getTransaction().commit();
		session.close();
	}
	
	public List<Services> findAll(){
		List<Services> list = null;
		Session session = HibernateConnect.getFactory().openSession();
		Query query = session.createQuery("From Services");
		list = query.getResultList();
		session.close();
		return list;
	}
	
	public List<Services> search(String param){
		List<Services> list = null;
		Session session = HibernateConnect.getFactory().openSession();
		Query query = session.createQuery("select s From Services s where s.name like ?1 or s.type like ?1");
		query.setParameter(1, "%"+param+"%");
		list = query.getResultList();
		session.close();
		return list;
	}
	
	
}
