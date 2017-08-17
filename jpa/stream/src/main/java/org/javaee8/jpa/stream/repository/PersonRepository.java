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

    @PersistenceContext(unitName = "DEFAULT_PU")
    private EntityManager em;

    public Stream<Person> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Person.class));
        return em.createQuery(cq).getResultStream();
    }

}
