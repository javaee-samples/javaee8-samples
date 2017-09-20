package org.javaee8.jpa.dynamic.tx;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class EmployeeService {

    @Inject
    private EntityManager entityManager;

    public void persist(Employee employee) {
        entityManager.persist(employee);
    }

    public Employee getById(int id) {
        return entityManager.find(Employee.class, id);
    }
}
