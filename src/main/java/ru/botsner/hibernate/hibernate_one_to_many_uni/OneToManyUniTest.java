package ru.botsner.hibernate.hibernate_one_to_many_uni;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.botsner.hibernate.hibernate_one_to_many_uni.entity.Department;
import ru.botsner.hibernate.hibernate_one_to_many_uni.entity.Employee;

public class OneToManyUniTest {

    private final SessionFactory factory;

    public OneToManyUniTest(SessionFactory factory) {
        this.factory = factory;
    }

    public static void main(String[] args) {
        OneToManyUniTest dao = new OneToManyUniTest(new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .addAnnotatedClass(Department.class)
                .buildSessionFactory());

        try {
//            dao.saveDepartment();
//            dao.getDepartment();
//            dao.deleteDepartment();
        } finally {
            dao.factory.close();
        }
    }

    public void saveDepartment() {
        try (Session session = factory.getCurrentSession()) {
            Department dep = new Department("HR"
                    , 500, 1500);
            Employee emp1 = new Employee("Stepan", "Ivanov"
                    , 800);
            Employee emp2 = new Employee("Andrey", "Sidorov"
                    , 1000);
            dep.addEmployeeToDepartment(emp1);
            dep.addEmployeeToDepartment(emp2);

            session.beginTransaction();
            session.save(dep);
            session.getTransaction().commit();
        }
    }

    public void getDepartment() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            Department department = session.get(Department.class, 2);
            System.out.println(department);
            System.out.println(department.getEmps());

            session.getTransaction().commit();
        }
    }

    public void deleteDepartment() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Department department = session.get(Department.class, 3);

            session.delete(department);

            session.getTransaction().commit();
        }
    }
}