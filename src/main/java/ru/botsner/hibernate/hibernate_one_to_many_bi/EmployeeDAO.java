package ru.botsner.hibernate.hibernate_one_to_many_bi;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.botsner.hibernate.dao.AbstractHibernateDAO;
import ru.botsner.hibernate.hibernate_one_to_many_bi.entity.Employee;

import java.util.List;

public class EmployeeDAO extends AbstractHibernateDAO<Employee> {

    public EmployeeDAO(SessionFactory factory) {
        super(factory, Employee.class);
    }

    public List<Employee> getAllEmployeesByDeptId(int id) {
        Session session = getCurrentSession();

        List<Employee> emps = session
                .createQuery("from Employee where department.id =: deptId", Employee.class)
                .setParameter("deptId", id)
                .getResultList();

        return emps;
    }

    public void deleteAllEmployees() {
        Session session = getCurrentSession();
        session.createQuery("delete from Employee").executeUpdate();
    }
}
