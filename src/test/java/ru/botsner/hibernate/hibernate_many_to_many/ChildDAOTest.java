package ru.botsner.hibernate.hibernate_many_to_many;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import ru.botsner.hibernate.hibernate_many_to_many.entity.Child;
import ru.botsner.hibernate.hibernate_many_to_many.entity.Section;
import ru.botsner.hibernate.util.SqlScriptRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChildDAOTest {
    private static SessionFactory factory;
    private Session session;

    private static ChildDAO childDAO;
    private static SectionDAO sectionDAO;

    @BeforeAll
    static void setUp() {
        factory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .addAnnotatedClass(Child.class)
                .addAnnotatedClass(Section.class)
                .buildSessionFactory();

        childDAO = new ChildDAO(factory);
        sectionDAO = new SectionDAO(factory);

        SqlScriptRunner.runSqlScriptFile("/many_to_many/test-data.sql", factory);
    }

    @BeforeEach
    public void beforeEach() {
        SqlScriptRunner.runSqlScriptFile("/many_to_many/reset-autoincrement.sql", factory);

        session = factory.getCurrentSession();
        session.beginTransaction();
    }

    @AfterEach
    public void afterEach() {
        if (session != null) {
            session.getTransaction().rollback();
            session.close();
        }
    }

    @AfterAll
    static void tearDown() {
        if (factory != null) {
            factory.close();
        }
    }

    @Test
    void childDaoLoads() {
        assertNotNull(childDAO);
    }

    @Test
    void sectionDaoLoads() {
        assertNotNull(sectionDAO);
    }

    @Test
    void saveChildWithSectionsCascade() {
        Section section1 = new Section("Volleyball");
        Section section2 = new Section("Chess");
        Section section3 = new Section("Math");
        Child child = new Child("Igor", 10);

        child.addSectionToChild(section1);
        child.addSectionToChild(section2);
        child.addSectionToChild(section3);

        childDAO.save(child);

        assertTrue(child.getId() > 0);
        assertTrue(section1.getId() > 0);
        assertTrue(section2.getId() > 0);
        assertTrue(section3.getId() > 0);
    }

    @Test
    void getChildWithSectionsCascade() {
        List<Child> children = childDAO.findAll();

        assertFalse(children.isEmpty());
        assertEquals(2, children.size());

        Child child = children.get(0);
        List<Section> sections = child.getSections();

        assertEquals("Masha", child.getFirstName());
        assertEquals(7, child.getAge());

        assertFalse(sections.isEmpty());
        assertEquals("Volleyball", sections.get(0).getName());
    }

    @Test
    void deleteChildWithoutSectionsNoCascade() {
        Child child = childDAO.findAll().get(0);

        assertNotNull(child);

        childDAO.delete(child);

        List<Child> children = childDAO.findAll();
        List<Section> sections = sectionDAO.findAll();

        assertFalse(children.isEmpty());
        assertEquals(1, children.size());
        assertFalse(sections.isEmpty());
        assertEquals(2, sections.size());
    }

    @Test
    void getAllChildrenBySectionId() {
        List<Child> children = childDAO.getAllChildrenBySectionId(1);

        assertNotNull(children);
        assertFalse(children.isEmpty());
        assertEquals(2, children.size());

        Child child1 = children.get(0);
        Child child2 = children.get(1);

        assertEquals("Masha", child1.getFirstName());
        assertEquals(7, child1.getAge());

        assertEquals("Igor", child2.getFirstName());
        assertEquals(10, child2.getAge());
    }
}