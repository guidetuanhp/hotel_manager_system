package connect;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import entity.Account;
import entity.Customer;
import entity.DetailInvoice;
import entity.Invoice;
import entity.Role;
import entity.Room;
import entity.Services;


public class HibernateConnect {
	
	private final static SessionFactory FACTORY;

	static {
		Configuration conf = new Configuration();
		Properties props = new Properties();
		
		props.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
		props.put(Environment.URL, "jdbc:mysql://localhost:3306/hotelmanager");
		props.put(Environment.USER, "root");
		props.put(Environment.PASS, "");
		props.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
		props.put(Environment.SHOW_SQL, "true");
		props.put(Environment.HBM2DDL_AUTO, "update");
		conf.addAnnotatedClass(Account.class);
		conf.addAnnotatedClass(Customer.class);
		conf.addAnnotatedClass(DetailInvoice.class);
		conf.addAnnotatedClass(Invoice.class);
		conf.addAnnotatedClass(Role.class);
		conf.addAnnotatedClass(Room.class);
		conf.addAnnotatedClass(Services.class);

		conf.setProperties(props);
		
		ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
		FACTORY = conf.buildSessionFactory(registry);
	}
	public static SessionFactory getFactory() {
		return FACTORY;
	}
	
}
