package hibernatePersonsEvent.domain;

import org.hibernate.Session;
import java.util.*;

public class EventManager {

    public static void main(String[] args) {
    	
        EventManager mgr = new EventManager();

        if (args[0].equals("store")) {
            mgr.createAndStoreEvent("My Event", new Date());
        }
        else if (args[0].equals("list")) {
            List events = mgr.listEvents();
            for (int i = 0; i < events.size(); i++) {
                Event theEvent = (Event) events.get(i);
                System.out.println(
                        "Event: " + theEvent.getTitle() + " Time: " + theEvent.getDate());
            }
        }
        /*else if (args[0].equals("addpersontoevent")) {
            Long eventId = mgr.createAndStoreEvent("My Event", new Date());
            Long personId = mgr.createAndStorePerson("Foo", "Bar");
            mgr.addPersonToEvent(personId, eventId);
            System.out.println("Added person " + personId + " to event " + eventId);
        }*/  

        HibernateUtil.getSessionFactory().close();
    }

    
    private List listEvents() {
    	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    	
        session.beginTransaction();
        List result = session.createQuery("from Event").list();
        session.getTransaction().commit();
        return result;
	}
//we are using a Hibernate Query Language (HQL) query to load all existing Event objects from the database. 
//Hibernate will generate the appropriate SQL, send it to the database and populate Event objects with the data

	private void createAndStoreEvent(String title, Date theDate) {
    	
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Event theEvent = new Event();
        theEvent.setTitle(title);
        theEvent.setDate(theDate);
        
        session.save(theEvent);

        session.getTransaction().commit();
    }

//In createAndStoreEvent() we created a new Event object and handed it over to Hibernate. At that point, 
//Hibernate takes care of the SQL and executes an INSERT on the database

//What does sessionFactory.getCurrentSession() do? First, you can call it as many times and anywhere you like 
//once you get hold of your org.hibernate.SessionFactory. The getCurrentSession() method always returns the "current" unit of work. 
//Remember that we switched the configuration option for this mechanism to "thread" in our src/main/resources/hibernate.cfg.xml? 
//Due to that setting, the context of a current unit of work is bound to the current Java thread that executes the application

	private void addPersonToEvent(Long personId, Long eventId) {
		
	    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	    
	    session.beginTransaction();
	    
	    Person aPerson = (Person) session
                .createQuery("select p from Person p left join fetch p.events where p.id = :pid")
                .setParameter("pid", personId)
                .uniqueResult(); // Eager fetch the collection so we can use it detached
        
	    Event anEvent = (Event) session.load(Event.class, eventId);
	
	    /*Person aPerson = (Person) session.load(Person.class, personId);
	    Event anEvent = (Event) session.load(Event.class, eventId);
	    aPerson.getEvents().add(anEvent);//bring some people and events together 
*/	
	    
	    // End of first unit of work
        aPerson.getEvents().add(anEvent); // aPerson (and its collection) is detached

        
        // Begin second unit of work
        Session session2 = HibernateUtil.getSessionFactory().getCurrentSession();
        session2.beginTransaction();
        session2.update(aPerson); // Reattachment of aPerson

	    session.getTransaction().commit();
	}
	
	
	 private void addEmailToPerson(Long personId, String emailAddress) {
	        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	        session.beginTransaction();

	        Person aPerson = (Person) session.load(Person.class, personId);
	        // adding to the emailAddress collection might trigger a lazy load of the collection
	        aPerson.getEmailAddresses().add(emailAddress);

	        session.getTransaction().commit();
	        
	}
}

