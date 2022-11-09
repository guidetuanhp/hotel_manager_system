package repository;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;

import connect.HibernateConnect;
import entity.Customer;
import entity.DetailInvoice;
import entity.Invoice;

public class InvoiceRepository {

	public List<Invoice> findByRoomId(Integer roomId){
		List<Invoice> list = null;
		Session session = HibernateConnect.getFactory().openSession();
		Query query = session.createQuery("select i from Invoice i inner join i.room r where r.id = ?1 order by i.id desc");
		query.setParameter(1, roomId);
		list = query.getResultList();
		session.close();
		return list;
	}
	
	public List<Invoice> findByCustomer(Integer customerId){
		List<Invoice> list = null;
		Session session = HibernateConnect.getFactory().openSession();
		Query query = session.createQuery("select i from Invoice i inner join i.customer r where r.id = ?1");
		query.setParameter(1, customerId);
		list = query.getResultList();
		session.close();
		return list;
	}
	public void delete(Integer id) {
		Session session = HibernateConnect.getFactory().openSession();
		Invoice invoice = session.get(Invoice.class, id);
		session.getTransaction().begin();
		session.delete(invoice);
		session.getTransaction().commit();
		session.close();
	}
	public List<Invoice> findAll(){
		List<Invoice> list = null;
		Session session = HibernateConnect.getFactory().openSession();
		Query query = session.createQuery("From Invoice");
		list = query.getResultList();
		session.close();
		return list;
	}
	
	public List<Invoice> findThisMonth(){
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		System.out.println(year);
		int month = c.get(Calendar.MONTH);
		System.out.println(month);
		List<Invoice> list = null;
		Session session = HibernateConnect.getFactory().openSession();
		Query query = session.createQuery("select i From Invoice i where MONTH(i.createdDate) = ?1 and YEAR(i.createdDate) = ?2");
		query.setParameter(1, month+1);
		query.setParameter(2, year);
		list = query.getResultList();
		session.close();
		return list;
	}
	
	public Long overdueThisMonth(){
		Long sum = 0L;
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		Session session = HibernateConnect.getFactory().openSession();
		Query query = session.createQuery("select count(*) From Invoice i where MONTH(i.createdDate) = ?1 and YEAR(i.createdDate) = ?2 and (i.fine is not null and i.fine > ?3)");
		query.setParameter(1, month+1);
		query.setParameter(2, year);
		query.setParameter(3, 0D);
		sum =  (Long) query.getSingleResult();
		session.close();
		return sum;
	}
	
	public List<Invoice> sortByDate(Timestamp date1, Timestamp date2){
		List<Invoice> list = null;
		Session session = HibernateConnect.getFactory().openSession();
		Query query = session.createQuery("select i From Invoice i where i.bookingDate >= ?1 and i.returnDate <= ?2");
		query.setParameter(1, date1);
		query.setParameter(2, date2);
		
		list = query.getResultList();
		session.close();
		return list;
	}
	
	public Invoice findByRoomIdAndCardCustomer(String customerid){
		Invoice invocie = null;
		Session session = HibernateConnect.getFactory().openSession();
		Query query = session.createQuery("select i from Invoice i inner join i.customer c  where c.id = ?1");
		query.setParameter(1, Integer.valueOf(customerid));
		invocie = (Invoice) query.getSingleResult();
		session.close();
		return invocie;
	}
	
	public void save(Invoice invoice) {
		Session session = HibernateConnect.getFactory().openSession();
		session.getTransaction().begin();
		session.save(invoice);
		session.getTransaction().commit();
		session.close();
	}
	
	public void saveDetail(DetailInvoice detailInvoice) {
		Session session = HibernateConnect.getFactory().openSession();
		session.getTransaction().begin();
		session.save(detailInvoice);
		session.getTransaction().commit();
		session.close();
	}
	
	public List<DetailInvoice> findByInvoiceId(Integer id){
		List<DetailInvoice> list = null;
		Session session = HibernateConnect.getFactory().openSession();
		Query query = session.createQuery("select d from DetailInvoice d inner join d.invoice i where i.id = ?1");
		query.setParameter(1, id);
		list = query.getResultList();
		session.close();
		return list;
	}

	public void update(Invoice invoiceG) {
		Session session = HibernateConnect.getFactory().openSession();
		session.getTransaction().begin();
		session.update(invoiceG);
		session.getTransaction().commit();
		session.close();
	}
	
	public Double getAmountByMonth(int month, int year) {
		Session session = HibernateConnect.getFactory().openSession();
		Query query = session.createQuery("select sum(i.totalAmount) from Invoice i where Month(i.createdDate) = ?1 and Year(i.createdDate) =?2");
		query.setParameter(1, month);
		query.setParameter(2, year);
		Double sum = (Double) query.getSingleResult();
		session.close();
		return sum;
	}
}
