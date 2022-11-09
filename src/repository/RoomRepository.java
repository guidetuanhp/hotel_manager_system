package repository;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;

import connect.HibernateConnect;
import entity.Room;


public class RoomRepository{

	public boolean save(Room room) {
		Session session = HibernateConnect.getFactory().openSession();
		session.getTransaction().begin();
		session.save(room);
		session.getTransaction().commit();
		session.close();
		return room.getId() !=null;
	}
	
	public boolean update(Room room) {
		Session session = HibernateConnect.getFactory().openSession();
		session.getTransaction().begin();
		session.update(room);
		session.getTransaction().commit();
		session.close();
		return room.getId() !=null;
	}
	
	public Room findById(Integer id) {
		Room room = null;
		Session session = HibernateConnect.getFactory().openSession();
		room = session.get(Room.class, id);
		session.close();
		return room;
	}
	
	public List<Room> findAll(){
		List<Room> list = null;
		Session session = HibernateConnect.getFactory().openSession();
		Query query = session.createQuery("From Room");
		list = query.getResultList();
		session.close();
		return list;
	}
	
	public List<Room> findEmptyRoom(){
		List<Room> list = findAll();
		List<Room> in = findInhabitedRoom();
		for(int i=0; i<in.size(); i++) {
			for(int j=0; j<list.size(); j++) {
				if(in.get(i).getId() == list.get(j).getId() || list.get(j).getRoomStatus().equalsIgnoreCase("correcting")) {
					list.remove(j);
				}
			}
		}
		return list;
	}
	
	public List<Room> findRepairRoom(){
		List<Room> list = null;
		Session session = HibernateConnect.getFactory().openSession();
		Query query = session.createQuery("select r From Room r where r.roomStatus = ?1");
		query.setParameter(1, "correcting");
		list = query.getResultList();
		session.close();
		return list;
	}
	
	public List<Room> findInhabitedRoom(){
		Timestamp now = new Timestamp(System.currentTimeMillis());
		List<Room> list = null;
		Session session = HibernateConnect.getFactory().openSession();
		Query query = session.createQuery("select r From Room r where r.idCardCustomer != NUll");
		list = query.getResultList();
		System.out.println(list);
		session.close();
		return list;
	}
	
	public void delete(Integer id) {
		Session session = HibernateConnect.getFactory().openSession();
		session.getTransaction().begin();
		Room room = session.get(Room.class, id);
		session.delete(room);
		session.getTransaction().commit();
		session.close();
	}
}
