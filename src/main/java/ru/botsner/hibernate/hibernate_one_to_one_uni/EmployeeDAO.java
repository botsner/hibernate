package ru.botsner.hibernate.hibernate_one_to_one_uni;

import org.hibernate.SessionFactory;
import ru.botsner.hibernate.dao.AbstractHibernateDAO;
import ru.botsner.hibernate.hibernate_one_to_one_uni.entity.Employee;

public class EmployeeDAO extends AbstractHibernateDAO<Employee> {

    public EmployeeDAO(SessionFactory factory) {
        super(factory, Employee.class);
    }
}
