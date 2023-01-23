package ru.botsner.hibernate.hibernate_one_to_many_bi;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.botsner.hibernate.hibernate_one_to_many_bi.entity.Department;
import ru.botsner.hibernate.hibernate_one_to_many_bi.entity.Employee;

public class OneToManyBiTest {

    private final SessionFactory factory;

    public OneToManyBiTest(SessionFactory factory) {
        this.factory = factory;
    }

    public static void main(String[] args) {
        OneToManyBiTest dao = new OneToManyBiTest(new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .addAnnotatedClass(Department.class)
                .buildSessionFactory());

        try {
//            dao.saveDepartment();
//            dao.getDepartment();
//            dao.getEmpsFromDepartment();
//            dao.getDepartmentFromEmployee();
        } finally {
            dao.factory.close();
        }
    }

    public void saveDepartment() {
        try (Session session = factory.getCurrentSession()) {
            Department dept = new Department("Sales"
                    , 300, 1500);
            Employee emp1 = new Employee("Elena", "Smirnova"
                    , 1000);
            Employee emp2 = new Employee("Anton", "Sidorov"
                    , 1200);

            dept.addEmployeeToDepartment(emp1);
            dept.addEmployeeToDepartment(emp2);

            session.beginTransaction();
            session.save(dept);

            session.getTransaction().commit();
        }
    }

    public void getEmpsFromDepartment() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            Department department = session.get(Department.class, 1);

            System.out.println(department);
            System.out.println(department.getEmps());


            session.getTransaction().commit();
        }
    }

    public void getDepartment() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            System.out.println("Get department");
            Department department = session.get(Department.class, 1);

            System.out.println("Show department");
            System.out.println(department);

            System.out.println("Load employees");
            department.getEmps().get(0);

            session.getTransaction().commit();

            System.out.println("Show employees of the department");
            System.out.println(department.getEmps());
            System.out.println("Done!");
        }
    }

    public void getDepartmentFromEmployee() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Employee employee = session.get(Employee.class, 1);

            System.out.println(employee);
            System.out.println(employee.getDepartment());

            session.getTransaction().commit();
        }
    }

    public void deleteEmployee() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Employee employee = session.get(Employee.class, 4);

            session.delete(employee);

            session.getTransaction().commit();
        }
    }
}
