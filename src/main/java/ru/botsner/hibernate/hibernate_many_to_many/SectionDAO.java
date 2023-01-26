package ru.botsner.hibernate.hibernate_many_to_many;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.botsner.hibernate.dao.AbstractHibernateDAO;
import ru.botsner.hibernate.hibernate_many_to_many.entity.Child;
import ru.botsner.hibernate.hibernate_many_to_many.entity.Section;

import java.util.List;

public class SectionDAO extends AbstractHibernateDAO<Section> {

    public SectionDAO(SessionFactory factory) {
        super(factory, Section.class);
    }

    public List<Section> getAllSectionsByChildId(int id) {
        Session session = getCurrentSession();

        Child child = session.get(Child.class, id);
        List<Section> sections = child.getSections();

        return sections;
    }
}
