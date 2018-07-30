package org.javaee8.validation;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.time.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertThat;

/**
 * @author mertcaliskan
 */
@RunWith(Arquillian.class)
public class ConstraintViolationTest {

    private Validator validator;

    static {
        // prevent to use translated messages which we compare in test;
        // translation language depends on the locale.
        Locale.setDefault(Locale.US);
    }


    @Deployment
    public static WebArchive deploy() {
        return create(WebArchive.class)
                .addAsLibraries(
                        create(JavaArchive.class).addClasses(Person.class,
                                Admin.class,
                                Country.class,
                                Address.class));
    }

    @Before
    public void setUpValidator() {
        validator = Validation
                .byDefaultProvider()
                .configure()
                .clockProvider(
                        () -> Clock.fixed(
                            Instant.parse("2017-01-01T00:00:00.00Z"),
                                ZoneId.systemDefault()))
                .buildValidatorFactory()
                .getValidator();
    }

    @Test
    public void validatingBirthDateFailsWithViolation() {
        Person person = new Person();
        person.setYearOfBirth(Year.of(2018));

        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        assertThat(violations, is(not(nullValue())));
        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(),
                is("must be a past date"));
    }

    public void validatingMarriageAnniversaryDateFailsWithViolation() {
        Person person = new Person();
        person.setMarriageAnniversary(Optional.of(LocalDate.MAX));

        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        assertThat(violations, is(not(nullValue())));
        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(),
                is("must be in the past"));
    }

    @Test
    public void validatingEmailsFailsWithViolation() {
        Person person = new Person();
        person.setEmails(Arrays.asList("mert.caliskan@payara.fish", "invalid_mail"));

        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        assertThat(violations, is(not(nullValue())));
        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(),
                is("must be a well-formed email address"));
    }

    @Test
    public void validatingPassswordWithDefaultGroupFailsWithViolation() {
        Person person = new Person();
        person.setPassword("1234567");

        Set<ConstraintViolation<Person>> violations = validator.validate(person, Default.class);

        assertThat(violations, is(not(nullValue())));
        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(),
                is("size must be between 8 and 2147483647"));
    }

    @Test
    public void validatingPassswordWithAdminGroupFailsWithViolation() {
        Person person = new Person();
        person.setPassword("1234567");

        Set<ConstraintViolation<Person>> violations = validator.validate(person, Admin.class);

        assertThat(violations, is(not(nullValue())));
        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(),
                is("size must be between 12 and 2147483647"));
    }

    @Test
    public void validatingAddressFailsWithViolation() {
        Person person = new Person();
        Country country = new Country();
        country.setCountryCode("ABC");
        Address address = new Address();
        address.setDetail("");
        person.getAddressMap().put(country, address);

        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        assertThat(violations, is(not(nullValue())));
        assertThat(violations.size(), is(2));
        assertThat(violations.iterator().next().getMessage(),
                anyOf(is("must not be empty"), is("size must be between 2 and 2")));
        assertThat(violations.iterator().next().getMessage(),
                anyOf(is("must not be empty"), is("size must be between 2 and 2")));
    }
}
