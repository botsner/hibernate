package ru.botsner.hibernate.hibernate_one_to_many_uni;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.botsner.hibernate.dao.AbstractHibernateDAO;
import ru.botsner.hibernate.hibernate_one_to_many_uni.entity.Department;
import ru.botsner.hibernate.hibernate_one_to_many_uni.entity.Employee;

public class DepartmentDAO extends AbstractHibernateDAO<Department> {

    public DepartmentDAO(SessionFactory factory) {
        super(factory, Department.class);
    }

    public void addEmployeeToDepartmentByDeptId(Employee employee, int departmentId) {
        Session session = getCurrentSession();

        Department department = session.get(Department.class, departmentId);
        department.addEmployeeToDepartment(employee);
    }
}
