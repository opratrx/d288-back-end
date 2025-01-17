package com.aaron.d288.bootstrap;

import com.aaron.d288.dao.CustomerRepository;
import com.aaron.d288.dao.DivisionRepository;
import com.aaron.d288.entities.Customer;
import com.aaron.d288.entities.Division;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BootStrapData implements CommandLineRunner {
    private final CustomerRepository customerRepository;
    private final DivisionRepository divisionRepository;

    public BootStrapData(CustomerRepository customerRepository, DivisionRepository divisionRepository) {
        this.customerRepository = customerRepository;
        this.divisionRepository = divisionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // Do nothing if the customer count is more than one.
        if(customerRepository.count() > 1) {
            return;
        }

        // Creating and saving a new Division instance
        Division newDivision = new Division();
        newDivision.setCountry_id(1L);
        newDivision.setDivision_name("Test Division");
        divisionRepository.save(newDivision);

        // Creating and saving Customer instances
        createNewCustomer("Sonny", "Moore", "123 Fake Street", "12345", "1234567891", newDivision);
        createNewCustomer("Fred", "Again", "112 Somewhere Road", "12346", "1235435432", newDivision);
        createNewCustomer("Pink", "Pantherress", "144 Fake Avenue", "54323", "6542454563", newDivision);
        createNewCustomer("John", "Summit", "154 Nowhere Avenue", "76545", "5676544567", newDivision);
        createNewCustomer("Arctic", "Monkeys", "146 Fun Avenue", "98453", "4545653456", newDivision);
    }

    private void createNewCustomer(String firstName, String lastName, String address, String postalCode, String phone, Division division) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setAddress(address);
        customer.setPostal_code(postalCode);
        customer.setPhone(phone);
        customer.setDivision(division);
        customerRepository.save(customer);
    }
}
