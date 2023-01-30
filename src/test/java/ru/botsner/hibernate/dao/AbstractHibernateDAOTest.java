package ru.botsner.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import ru.botsner.hibernate.hibernate_crud.entity.Employee;
import ru.botsner.hibernate.util.SqlScriptRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbstractHibernateDAOTest {

    private static SessionFactory factory;
    private Session session;

    private static AbstractHibernateDAO<Employee> abstractDAO;
    private Employee employee;

    @BeforeAll
    static void setUp() {
        factory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .buildSessionFactory();

        abstractDAO = new AbstractHibernateDAO<>(factory, Employee.class) {};

        SqlScriptRunner.runSqlScriptFile("/crud/test-data.sql", factory);
    }

    @BeforeEach
    public void beforeEach() {
        SqlScriptRunner.runSqlScriptFile("/crud/reset-autoincrement.sql", factory);
        session = factory.getCurrentSession();
        session.beginTransaction();

        employee = new Employee("Pavel", "Ivanov", "IT", 1000);
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
    void daoLoads() {
        assertNotNull(abstractDAO);
    }

    @Test
    void save() {
        assertEquals(0, employee.getId());
        abstractDAO.save(employee);
        assertTrue(employee.getId() > 0);
    }

    @Test
    void findById() {
        Employee emp = abstractDAO.findById(1);
        assertNotNull(emp);
        assertEquals("Ivan", emp.getFirstName());
        assertEquals("Petrov", emp.getSurname());
        assertEquals("IT", emp.getDepartment());
        assertEquals(1500, emp.getSalary());
    }

    @Test
    void findAll() {
        List<Employee> emps = abstractDAO.findAll();
        assertNotNull(emps);
        assertFalse(emps.isEmpty());
        assertEquals(3, emps.size());
    }

    @Test
    void update() {
        employee.setId(1);
        abstractDAO.update(employee);
        Employee updatedEmp = abstractDAO.findById(1);
        assertNotNull(updatedEmp);
        assertEquals("Pavel", updatedEmp.getFirstName());
        assertEquals("Ivanov", updatedEmp.getSurname());
        assertEquals("IT", updatedEmp.getDepartment());
        assertEquals(1000, updatedEmp.getSalary());
    }

    @Test
    void delete() {
        employee.setId(1);
        abstractDAO.delete(employee);
        List<Employee> emps = abstractDAO.findAll();
        assertEquals(2, emps.size());
    }

    @Test
    void deleteById() {
        List<Employee> emps = abstractDAO.findAll();
        assertEquals(3, emps.size());
        abstractDAO.deleteById(1);
        emps = abstractDAO.findAll();
        assertEquals(2, emps.size());
    }
}