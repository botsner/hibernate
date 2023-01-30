package ru.botsner.hibernate.hibernate_one_to_many_bi;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import ru.botsner.hibernate.hibernate_one_to_many_bi.entity.Department;
import ru.botsner.hibernate.hibernate_one_to_many_bi.entity.Employee;
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
                .addAnnotatedClass(Department.class)
                .addAnnotatedClass(Employee.class)
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
    void saveEmployeeWithDepartmentCascade() {
        Department dept = new Department("HR", 1500, 300);
        Employee emp = new Employee("Pavel", "Ivanov", 1000);

        dept.addEmployeeToDepartment(emp);

        empDAO.save(emp);

        assertTrue(dept.getId() > 0);
        assertTrue(emp.getId() > 0);
    }

    @Test
    void getEmployeeWithDepartmentCascade() {
        List<Employee> emps = empDAO.findAll();

        assertFalse(emps.isEmpty());

        Employee emp = emps.get(0);
        Department dept = emp.getDepartment();

        assertEquals("Pavel", emp.getFirstName());
        assertEquals("Ivanov", emp.getSurname());
        assertEquals(1000, emp.getSalary());

        assertNotNull(dept);
        assertEquals("IT", dept.getDepartmentName());
        assertEquals(500, dept.getMinSalary());
        assertEquals(1700, dept.getMaxSalary());
    }

    @Test
    void deleteEmployeeWithoutDeletingDepartmentNoCascade() {
        Employee emp = empDAO.findAll().get(0);

        assertNotNull(emp);

        empDAO.delete(emp);

        List<Department> depts = deptDAO.findAll();
        List<Employee> emps = empDAO.findAll();

        assertEquals(1, depts.size());
        assertEquals(1, emps.size());
    }

    @Test
    void getAllEmployeesByDeptId() {
        List<Employee> emps = empDAO.getAllEmployeesByDeptId(1);

        assertFalse(emps.isEmpty());
        assertEquals(2, emps.size());

        Employee emp1 = emps.get(0);
        Employee emp2 = emps.get(1);

        assertEquals("Pavel", emp1.getFirstName());
        assertEquals("Ivanov", emp1.getSurname());
        assertEquals(1000, emp1.getSalary());

        assertEquals("Elena", emp2.getFirstName());
        assertEquals("Petrova", emp2.getSurname());
        assertEquals(1200, emp2.getSalary());
    }

    @Test
    void deleteAllEmployeesWithoutDeletingDepartments() {
        List<Employee> emps = empDAO.findAll();
        List<Department> depts = deptDAO.findAll();

        assertFalse(emps.isEmpty());
        assertEquals(2, emps.size());

        assertFalse(depts.isEmpty());
        assertEquals(1, depts.size());

        empDAO.deleteAllEmployees();

        emps = empDAO.findAll();
        depts = deptDAO.findAll();

        assertTrue(emps.isEmpty());

        assertFalse(depts.isEmpty());
        assertEquals(1, depts.size());
    }
}