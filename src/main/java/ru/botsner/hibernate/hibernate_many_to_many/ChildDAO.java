package ru.botsner.hibernate.hibernate_many_to_many;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.botsner.hibernate.dao.AbstractHibernateDAO;
import ru.botsner.hibernate.hibernate_many_to_many.entity.Child;
import ru.botsner.hibernate.hibernate_many_to_many.entity.Section;

import java.util.List;

public class ChildDAO extends AbstractHibernateDAO<Child> {

    public ChildDAO(SessionFactory factory) {
        super(factory, Child.class);
    }

    public List<Child> getAllChildrenBySectionId(int id) {
        Session session = getCurrentSession();

        Section section = session.get(Section.class, id);
        List<Child> children = section.getChildren();

        return children;
    }
}
