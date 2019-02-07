package org.javaee8.jpa.dynamic.tx;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.File;

import javax.inject.Inject;

import org.hamcrest.Matchers;
import org.javaee8.jpa.dynamic.tx.ApplicationInit;
import org.javaee8.jpa.dynamic.tx.Employee;
import org.javaee8.jpa.dynamic.tx.EmployeeService;
import org.javaee8.jpa.dynamic.tx.util.EmployeeBeanWrapper;
import org.javaee8.jpa.dynamic.tx.util.TransactionLiteral;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(Arquillian.class)
public class DynamicTXTest {

    @Inject
    private EmployeeService employeeService;

    @Deployment
    public static WebArchive createDeployment() {
        return create(WebArchive.class)
                .addClasses(
                    ApplicationInit.class,
                    Employee.class,
                    EmployeeBeanWrapper.class,
                    EmployeeService.class)
                .addPackage(
                    TransactionLiteral.class.getPackage())
                .addAsResource(
                    "META-INF/persistence.xml")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/jboss-deployment-structure.xml"));
    }

    @Test
    public void testPersist() throws Exception {
        Employee employee = new Employee();
        employee.setName("reza");

        assertThat("employeeService", employeeService, Matchers.notNullValue());
        employeeService.persist(employee);

        Employee persistedEmployee = employeeService.getById(employee.getId());

        assertEquals("reza", persistedEmployee.getName());
    }

}
