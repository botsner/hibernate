package ru.botsner.hibernate.hibernate_one_to_one_uni;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.botsner.hibernate.hibernate_one_to_one_uni.entity.Detail;
import ru.botsner.hibernate.hibernate_one_to_one_uni.entity.Employee;

public class OneToOneUniTest {
    private final SessionFactory factory;

    public OneToOneUniTest(SessionFactory factory) {
        this.factory = factory;
    }

    public static void main(String[] args) {
        OneToOneUniTest dao = new OneToOneUniTest(new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .addAnnotatedClass(Detail.class)
                .buildSessionFactory());

        try {
//            dao.saveEmployee();
//            dao.getEmployee();
//            dao.deleteEmployee();
        } finally {
            dao.factory.close();
        }
    }

    public void saveEmployee() {
        try (Session session = factory.getCurrentSession()) {
            Employee employee = new Employee("Egor", "Smirnov",
                    "Sales", 700);
            Detail detail = new Detail("Moscow", "985621856",
                    "egorka@gmail.com");

            employee.setEmpDetail(detail);

            session.beginTransaction();
            session.save(employee);
            session.getTransaction().commit();
        }
    }

    public void getEmployee() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            Employee emp = session.get(Employee.class, 10);
            System.out.println(emp.getEmpDetail());

            session.getTransaction().commit();
        }
    }

    public void deleteEmployee() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            Employee emp = session.get(Employee.class, 2);
            session.delete(emp);

            session.getTransaction().commit();
        }
    }
}
