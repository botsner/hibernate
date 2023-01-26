package ru.botsner.hibernate.hibernate_one_to_many_bi;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.botsner.hibernate.dao.AbstractHibernateDAO;
import ru.botsner.hibernate.hibernate_one_to_many_bi.entity.Department;
import ru.botsner.hibernate.hibernate_one_to_many_bi.entity.Employee;

public class DepartmentDAO extends AbstractHibernateDAO<Department> {

    public DepartmentDAO(SessionFactory factory) {
        super(factory, Department.class);
    }

    public Department getDepartmentByEmpId(int id) {
        Session session = getCurrentSession();
        Employee employee = session.get(Employee.class, id);

        return employee.getDepartment();
    }
}
