package ru.botsner.hibernate.hibernate_one_to_one_bi;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import ru.botsner.hibernate.hibernate_one_to_one_bi.entity.Detail;
import ru.botsner.hibernate.hibernate_one_to_one_bi.entity.Employee;
import ru.botsner.hibernate.util.SqlScriptRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDAOTest {

    private static SessionFactory factory;
    private Session session;

    private static EmployeeDAO empDAO;
    private static DetailDAO detDAO;

    @BeforeAll
    static void setUp() {
        factory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .addAnnotatedClass(Detail.class)
                .buildSessionFactory();

        empDAO = new EmployeeDAO(factory);
        detDAO = new DetailDAO(factory);

        SqlScriptRunner.runSqlScriptFile("/one_to_one/test-data.sql", factory);
    }

    @BeforeEach
    public void beforeEach() {
        SqlScriptRunner.runSqlScriptFile("/one_to_one/reset-autoincrement.sql", factory);

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
    void employeeDaoLoads() {
        assertNotNull(empDAO);
    }

    @Test
    void detailDaoLoads() {
        assertNotNull(detDAO);
    }

    @Test
    void saveEmployeeWithDetailCascade() {
        Employee employee = new Employee("Elena", "Petrova", "HR", 1200);
        Detail detail = new Detail("London", "456789", "elena@gmail.com");
        employee.setEmpDetail(detail);

        empDAO.save(employee);

        assertTrue(employee.getId() > 0);
        assertTrue(detail.getId() > 0);
    }

    @Test
    void getEmployeeWithDetailCascade() {
        List<Employee> emps = empDAO.findAll();

        assertFalse(emps.isEmpty());

        Employee emp = emps.get(0);
        Detail det = emp.getEmpDetail();

        assertNotNull(det);
        assertEquals("Pavel", emp.getFirstName());
        assertEquals("IT", emp.getDepartment());
        assertEquals("Moscow", det.getCity());
        assertEquals("pavel@mail.com", det.getEmail());
    }

    @Test
    void deleteEmployeeWithDetailCascade() {
        Employee emp = empDAO.findAll().get(0);
        empDAO.delete(emp);

        List<Employee> emps = empDAO.findAll();
        List<Detail> details = detDAO.findAll();

        assertTrue(emps.isEmpty());
        assertTrue(details.isEmpty());
    }
}