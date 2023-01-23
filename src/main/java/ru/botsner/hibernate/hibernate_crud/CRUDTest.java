package ru.botsner.hibernate.hibernate_crud;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.botsner.hibernate.hibernate_crud.entity.Employee;

import java.util.List;

public class CRUDTest {
    private final SessionFactory factory;

    public CRUDTest(SessionFactory factory) {
        this.factory = factory;
    }

    public static void main(String[] args) {
        CRUDTest dao = new CRUDTest(new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .buildSessionFactory());

        try {
//            dao.saveEmployee();
//            dao.getEmployee();
//            dao.getAllEmployeesByName();
//            dao.deleteEmployee();
        } finally {
            dao.factory.close();
        }
    }

    public void saveEmployee() {
        try (Session session = factory.getCurrentSession()) {
            Employee emp = new Employee("Aleksandr", "Ivanov",
                    "IT", 600);

            session.beginTransaction();
            session.save(emp);
            session.getTransaction().commit();

            System.out.println(emp);
        }
    }

    public void getEmployee() {
        try (Session session = factory.getCurrentSession()) {
            Employee emp = new Employee("Ivan", "Sidorov",
                    "HR", 700);

            session.beginTransaction();
            session.save(emp);
            int empID = emp.getId();
            Employee employee = session.get(Employee.class, empID);
            session.getTransaction().commit();

            System.out.println(employee);
        }
    }

    public void getAllEmployees() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            List<Employee> emps = session.createQuery("from Employee", Employee.class)
                    .getResultList();

            session.getTransaction().commit();

            for (Employee e : emps)
                System.out.println(e);
        }
    }

    public void getAllEmployeesByName() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            List<Employee> emps = session.createQuery("from Employee " +
                    "where name = 'Aleksandr' AND salary > 500", Employee.class)
                    .getResultList();

            session.getTransaction().commit();

            for (Employee e : emps)
                System.out.println(e);
        }
    }

    public void updateEmployee() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
//            Employee emp = session.get(Employee.class, 1);
//            emp.setSalary(1500);

            session.createQuery("update Employee set salary = 1000 " +
                    "where name = 'Aleksandr'").executeUpdate();

            session.getTransaction().commit();
        }
    }

    public void deleteEmployee() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
//            Employee emp = session.get(Employee.class, 1);
//            session.delete(emp);

            session.createQuery("delete Employee " +
                    "where firstName = 'Aleksandr'").executeUpdate();

            session.getTransaction().commit();
        }
    }
}
