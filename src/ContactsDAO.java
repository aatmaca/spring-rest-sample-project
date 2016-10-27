package iris.contacts;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ContactsDAO {
	@Autowired
	private SessionFactory sessionFactory;

	public Contact getById(int id) {
		return (Contact) sessionFactory.getCurrentSession().get(Contact.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Contact> searchContacts(String name) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Contact.class);
		criteria.add(Restrictions.ilike("ad", "%" + name + "%"));
//		criteria.setMaxResults(20);
		return criteria.list();
	}

	public List<Contact> randevular(Date basTarih, Date bitTarih) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Contact.class);
		criteria.add(Restrictions.between("sonrakirandevu", basTarih, bitTarih));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Contact> getAllContacts() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Contact.class);
		criteria.setMaxResults(20);
		return criteria.list();
	}

	public int save(Contact contact) {
		return (Integer) sessionFactory.getCurrentSession().save(contact);
	}

	public void update(Contact contact) {
		sessionFactory.getCurrentSession().merge(contact);
	}

	public void delete(int id) {
		Contact c = getById(id);
		sessionFactory.getCurrentSession().delete(c);
	}
}
