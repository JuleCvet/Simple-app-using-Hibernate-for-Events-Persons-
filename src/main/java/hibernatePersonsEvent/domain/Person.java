package hibernatePersonsEvent.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "person")
public class Person {

	@Id
	@Column(name = "person_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private int age;
    private String firstname;
    private String lastname;

    private Set events = new HashSet();
    private Set emailAddresses = new HashSet();
    
    public Person() {}

	public Long getId() {
		return id;
	}

	private void setId(Long id) {
		this.id = id;
	}
	
	protected Set getEvents() {
        return events;
    }

	protected void setEvents(Set events) {
        this.events = events;
    }
    
    public void addToEvent(Event event) {
        this.getEvents().add(event);
        event.getParticipants().add(this);
    }

    public void removeFromEvent(Event event) {
        this.getEvents().remove(event);
        event.getParticipants().remove(this);
    }

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Set getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(Set emailAddresses) {
        this.emailAddresses = emailAddresses;
    }
}
//all bi-directional associations need one side as inverse. In a one-to-many association it has 
//to be the many-side, and in many-to-many association you can select either side.