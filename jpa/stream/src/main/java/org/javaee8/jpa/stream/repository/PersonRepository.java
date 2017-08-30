package org.javaee8.jpa.stream.repository;

import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import org.javaee8.jpa.stream.domain.Person;

/**
 * 
 * @author Gaurav Gupta
 *
 */
public class PersonRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Stream<Person> findAll() {
        CriteriaQuery criteriaQuery = entityManager.getCriteriaBuilder().createQuery();
        criteriaQuery.select(criteriaQuery.from(Person.class));
        return entityManager.createQuery(criteriaQuery).getResultStream();
    }

}
