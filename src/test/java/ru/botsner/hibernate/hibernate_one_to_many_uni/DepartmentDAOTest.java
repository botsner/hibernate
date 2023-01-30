package ru.botsner.hibernate.hibernate_one_to_many_uni;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import ru.botsner.hibernate.hibernate_one_to_many_uni.entity.Department;
import ru.botsner.hibernate.hibernate_one_to_many_uni.entity.Employee;
import ru.botsner.hibernate.util.SqlScriptRunner;

import javax.persistence.PersistenceException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentDAOTest {

    private static SessionFactory factory;
    private Session session;

    private static DepartmentDAO deptDAO;
    private static EmployeeDAO empDAO;

    @BeforeAll
    static void setUp() {
        factory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .addAnnotatedClass(Department.class)
                .addAnnotatedClass(Employee.class)
                .buildSessionFactory();

        deptDAO = new DepartmentDAO(factory);
        empDAO = new EmployeeDAO(factory);

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
    void departmentDaoLoads() {
        assertNotNull(deptDAO);
    }

    @Test
    void employeeDaoLoads() {
        assertNotNull(empDAO);
    }

    @Test
    void saveDepartmentWithEmployeesCascade() {
        Department dept = new Department("HR", 1500, 300);
        Employee emp1 = new Employee("Pavel", "Ivanov", 1000);
        Employee emp2 = new Employee("Elena", "Petrova", 1200);

        dept.addEmployeeToDepartment(emp1);
        dept.addEmployeeToDepartment(emp2);

        deptDAO.save(dept);

        assertTrue(dept.getId() > 0);
        assertTrue(emp1.getId() > 0);
        assertTrue(emp2.getId() > 0);
    }

    @Test
    void getDepartmentWithEmployeesCascade() {
        List<Department> depts = deptDAO.findAll();

        assertFalse(depts.isEmpty());

        Department dept = depts.get(0);
        List<Employee> emps = dept.getEmps();

        assertEquals("IT", dept.getDepartmentName());
        assertEquals(500, dept.getMinSalary());
        assertEquals(1700, dept.getMaxSalary());

        assertFalse(emps.isEmpty());
        assertEquals(2, emps.size());
        assertEquals("Pavel", emps.get(0).getFirstName());
        assertEquals("Elena", emps.get(1).getFirstName());
    }

    @Test
    void deleteDepartmentWithEmployeesCascade() {
        Department dept = deptDAO.findAll().get(0);

        assertFalse(dept.getEmps().isEmpty());
        assertEquals(2, dept.getEmps().size());

        deptDAO.delete(dept);

        List<Department> depts = deptDAO.findAll();
        List<Employee> emps = empDAO.findAll();

        assertTrue(depts.isEmpty());
        assertTrue(emps.isEmpty());
    }

    @Test
    void expectedExceptionConstraintViolationWithDeleteDepartmentById() throws PersistenceException {
        PersistenceException thrown = assertThrows(PersistenceException.class,
                () -> deptDAO.deleteById(1));

        assertNotNull(thrown.getMessage());
    }

    @Test
    void addEmployeeToDepartmentByDeptId() {
        Employee emp = new Employee("empName", "empSurname", 900);
        deptDAO.addEmployeeToDepartmentByDeptId(emp, 1);

        List<Department> depts = deptDAO.findAll();
        List<Employee> emps = depts.get(0).getEmps();

        assertTrue(emp.getId() > 0);
        assertEquals(1, depts.size());
        assertEquals(3, emps.size());
    }
}