package ru.botsner.hibernate.hibernate_crud;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.botsner.hibernate.dao.AbstractHibernateDAO;
import ru.botsner.hibernate.hibernate_crud.entity.Employee;

import java.util.List;

public class EmployeeDAO extends AbstractHibernateDAO<Employee> {

    public EmployeeDAO(SessionFactory factory) {
        super(factory, Employee.class);
    }

    public List<Employee> getEmployeesByName(String empName) {
        Session session = getCurrentSession();

        List<Employee> emps = session
                .createQuery("from Employee where name =: employeeName", Employee.class)
                .setParameter("employeeName", empName)
                .getResultList();

        return emps;
    }
}
