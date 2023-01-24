package ru.botsner.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public abstract class AbstractHibernateDAO<T> {

    private final Class<T> entityClass;
    private final SessionFactory sessionFactory;

    public AbstractHibernateDAO(SessionFactory sessionFactory, Class<T> entityClass)   {
        this.sessionFactory = sessionFactory;
        this.entityClass = entityClass;
    }

    public void save(final T entity) {
        Session session = getCurrentSession();
        session.persist(entity);
    }

    public T findById(final int id) {
        Session session = getCurrentSession();
        return session.get(entityClass, id);
    }

    public List<T> findAll() {
        Session session = sessionFactory.getCurrentSession();

        List<T> entities = session
                .createQuery("from " + entityClass.getName(), entityClass)
                .getResultList();

        return entities;
    }

    public void update(final T entity) {
        Session session = getCurrentSession();
        session.merge(entity);
    }

    public void delete(final T entity) {
        Session session = getCurrentSession();
        session.delete(entity);
    }

    public void deleteById(final int id) {
        Session session = getCurrentSession();

        session.createQuery("delete from " + entityClass.getName() + " where id =: entityId")
                .setParameter("entityId", id)
                .executeUpdate();
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
