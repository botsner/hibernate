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

class SectionDAOTest {

    private static SessionFactory factory;
    private Session session;

    private static SectionDAO sectionDAO;
    private static ChildDAO childDAO;

    @BeforeAll
    static void setUp() {
        factory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .addAnnotatedClass(Section.class)
                .addAnnotatedClass(Child.class)
                .buildSessionFactory();

        sectionDAO = new SectionDAO(factory);
        childDAO = new ChildDAO(factory);

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
    void sectionDaoLoads() {
        assertNotNull(sectionDAO);
    }

    @Test
    void childDaoLoads() {
        assertNotNull(childDAO);
    }

    @Test
    void saveSectionWithChildrenCascade() {
        Section section1 = new Section("Hockey");

        Child child1 = new Child("Vanya", 5);
        Child child2 = new Child("Katya", 7);

        section1.addChildToSection(child1);
        section1.addChildToSection(child2);

        sectionDAO.save(section1);

        assertTrue(section1.getId() > 0);
        assertTrue(child1.getId() > 0);
        assertTrue(child2.getId() > 0);
    }

    @Test
    void getSectionWithChildrenCascade() {
        List<Section> sections = sectionDAO.findAll();

        assertFalse(sections.isEmpty());
        assertEquals(2, sections.size());

        Section section = sections.get(0);
        List<Child> children = section.getChildren();

        assertEquals("Volleyball", section.getName());

        assertFalse(children.isEmpty());
        assertEquals("Masha", children.get(0).getFirstName());
        assertEquals("Igor", children.get(1).getFirstName());
        assertEquals(7, children.get(0).getAge());
        assertEquals(10, children.get(1).getAge());
    }

    @Test
    void deleteSectionWithoutChildrenNoCascade() {
        Section section = sectionDAO.findAll().get(0);

        assertNotNull(section);

        List<Section> sections = sectionDAO.findAll();
        List<Child> children = childDAO.findAll();

        assertEquals(2, sections.size());
        assertEquals(2, children.size());

        sectionDAO.delete(section);

        sections = sectionDAO.findAll();
        children = childDAO.findAll();

        assertFalse(sections.isEmpty());
        assertEquals(1, sections.size());
        assertFalse(children.isEmpty());
        assertEquals(2, children.size());
    }

    @Test
    void getAllSectionsByChildId() {
        List<Section> sections = sectionDAO.getAllSectionsByChildId(2);

        assertNotNull(sections);
        assertFalse(sections.isEmpty());
        assertEquals(2, sections.size());

        Section section1 = sections.get(0);
        Section section2 = sections.get(1);

        assertEquals("Volleyball", section1.getName());
        assertEquals("Chess", section2.getName());
    }
}