package ru.botsner.hibernate.hibernate_one_to_one_bi;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.botsner.hibernate.hibernate_one_to_one_bi.entity.Detail;
import ru.botsner.hibernate.hibernate_one_to_one_bi.entity.Employee;

public class OneToOneBiTest {
    private final SessionFactory factory;

    public OneToOneBiTest(SessionFactory factory) {
        this.factory = factory;
    }

    public static void main(String[] args) {
        OneToOneBiTest dao = new OneToOneBiTest(new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .addAnnotatedClass(Detail.class)
                .buildSessionFactory());

        try {
//            dao.saveDetail();
//            dao.getDetail();
//            dao.deleteDetail();
        } finally {
            dao.factory.close();
        }
    }

    public void saveDetail() {
        try (Session session = factory.getCurrentSession()) {
            Employee employee = new Employee("Nikolay", "Ivanov",
                    "HR", 850);
            Detail detail = new Detail("New-York", "3215984656",
                    "nikolay@gmail.com");

            employee.setEmpDetail(detail);
            detail.setEmployee(employee);

            session.beginTransaction();
            session.save(detail);
            session.getTransaction().commit();
        }
    }

    public void getDetail() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            Detail detail = session.get(Detail.class, 4);
            System.out.println(detail.getEmployee());

            session.getTransaction().commit();
        }
    }

    public void deleteDetail() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            Detail detail = session.get(Detail.class, 1);
            detail.getEmployee().setEmpDetail(null);
            session.delete(detail);

            session.getTransaction().commit();
        }
    }
}
