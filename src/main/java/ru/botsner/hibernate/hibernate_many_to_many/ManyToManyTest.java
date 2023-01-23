package ru.botsner.hibernate.hibernate_many_to_many;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.botsner.hibernate.hibernate_many_to_many.entity.Child;
import ru.botsner.hibernate.hibernate_many_to_many.entity.Section;

public class ManyToManyTest {
    private final SessionFactory factory;

    public ManyToManyTest(SessionFactory factory) {
        this.factory = factory;
    }

    public static void main(String[] args) {
        ManyToManyTest dao = new ManyToManyTest(new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Child.class)
                .addAnnotatedClass(Section.class)
                .buildSessionFactory());

        try {
//            dao.saveSectionWithChildren();
//            dao.saveChildWithSections();
//            dao.getSection();
//            dao.getChild();
//            dao.persistSection();
        } finally {
            dao.factory.close();
        }
    }

    public void saveSectionWithChildren() {
        try (Session session = factory.getCurrentSession()) {
            Section section = new Section("Football");
            Child child1 = new Child("Grisha", 5);
            Child child2 = new Child("Masha", 7);
            Child child3 = new Child("Vasya", 6);

            section.addChildToSection(child1);
            section.addChildToSection(child2);
            section.addChildToSection(child3);

            session.beginTransaction();
            session.save(section);
            session.getTransaction().commit();
        }
    }

    public void saveChildWithSections() {
        try (Session session = factory.getCurrentSession()) {
            Section section1 = new Section("Volleyball");
            Section section2 = new Section("Chess");
            Section section3 = new Section("Math");
            Child child = new Child("Igor", 10);

            child.addSectionToChild(section1);
            child.addSectionToChild(section2);
            child.addSectionToChild(section3);

            session.beginTransaction();
            session.save(child);
            session.getTransaction().commit();
        }
    }

    public void getSection() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            Section section = session.get(Section.class, 1);

            System.out.println(section);
            System.out.println(section.getChildren());

            session.getTransaction().commit();
        }
    }

    public void getChild() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            Child child = session.get(Child.class, 4);

            System.out.println(child);
            System.out.println(child.getSections());

            session.getTransaction().commit();
        }
    }

    public void persistSection() {
        try (Session session = factory.getCurrentSession()) {
            Section section = new Section("Dance");
            Child child1 = new Child("Olya", 12);
            Child child2 = new Child("Vanya", 8);
            Child child3 = new Child("Pavlik", 9);
            section.addChildToSection(child1);
            section.addChildToSection(child2);
            section.addChildToSection(child3);

            session.beginTransaction();
            session.persist(section);
            session.getTransaction().commit();
        }
    }

    public void deleteSectionNoCascade() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            Section section = session.get(Section.class, 7);
            session.delete(section);

            session.getTransaction().commit();
        }
    }

    public void deleteChildNoCascade() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            Child child = session.get(Child.class, 5);
            session.delete(child);

            session.getTransaction().commit();
        }
    }
}
