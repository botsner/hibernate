package ru.botsner.hibernate.hibernate_one_to_one_uni;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.botsner.hibernate.dao.AbstractHibernateDAO;
import ru.botsner.hibernate.hibernate_one_to_one_uni.entity.Detail;
import ru.botsner.hibernate.hibernate_one_to_one_uni.entity.Employee;

public class DetailDAO extends AbstractHibernateDAO<Detail> {

    public DetailDAO(SessionFactory factory) {
        super(factory, Detail.class);
    }

    // delete details without deleting employee
    public void deleteDetailByEmpId(int id) {
        Session session = getCurrentSession();

        Employee emp = session.get(Employee.class, id);
        Detail detail = emp.getEmpDetail();
        emp.setEmpDetail(null);
        session.delete(detail);
    }
}
