package ru.botsner.hibernate.hibernate_one_to_many_uni;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import ru.botsner.hibernate.hibernate_one_to_many_uni.entity.Department;
import ru.botsner.hibernate.hibernate_one_to_many_uni.entity.Employee;
import ru.botsner.hibernate.util.SqlScriptRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDAOTest {

    private static SessionFactory factory;
    private Session session;

    private static EmployeeDAO empDAO;
    private static DepartmentDAO deptDAO;

    @BeforeAll
    static void setUp() {
        factory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .addAnnotatedClass(Department.class)
                .buildSessionFactory();

        empDAO = new EmployeeDAO(factory);
        deptDAO = new DepartmentDAO(factory);

        SqlScriptRunner.runSqlScriptFile("/one_to_many/test-data.sql", factory);
    }
    @BeforeEach
    public void beforeEach() {
        SqlScriptRunner.runSqlScriptFile("/one_to_many/reset-autoincrement.sql", factory);

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
    void departmentDaoLoads() {
        assertNotNull(deptDAO);
    }

    @Test
    void deleteEmployeeByIdNoCascade() {
        empDAO.deleteById(1);

        List<Department> depts = deptDAO.findAll();
        List<Employee> emps = depts.get(0).getEmps();

        assertEquals(1, depts.size());
        assertEquals(1, emps.size());
    }
}