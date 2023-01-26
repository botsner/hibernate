package ru.botsner.hibernate.hibernate_one_to_one_bi;

import org.hibernate.SessionFactory;
import ru.botsner.hibernate.dao.AbstractHibernateDAO;
import ru.botsner.hibernate.hibernate_one_to_one_bi.entity.Detail;

public class DetailDAO extends AbstractHibernateDAO<Detail> {

    public DetailDAO(SessionFactory factory) {
        super(factory, Detail.class);
    }
}
