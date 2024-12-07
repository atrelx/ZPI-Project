package com.zpi.amoz.services;

import com.zpi.amoz.models.CustomerId;
import com.zpi.amoz.models.*;
import com.zpi.amoz.repository.CustomerB2BRepository;
import com.zpi.amoz.repository.CustomerB2CRepository;
import com.zpi.amoz.repository.CustomerRepository;
import com.zpi.amoz.requests.CustomerB2BCreateRequest;
import com.zpi.amoz.requests.CustomerB2CCreateRequest;
import com.zpi.amoz.requests.CustomerCreateRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerB2BRepository customerB2BRepository;


    @Autowired
    private CustomerB2CRepository customerB2CRepository;


    @Autowired
    private ContactPersonService contactPersonService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private PersonService personService;

    @Autowired
    private CompanyService companyService;

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findById(UUID id) {
        return customerRepository.findById(id);
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public boolean deleteById(UUID id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public CustomerB2B createCustomerB2B(String sub, CustomerB2BCreateRequest request) {
        Company company = companyService.getCompanyByUserId(sub)
                .orElseThrow(() -> new EntityNotFoundException("Could not find company for given sub:" + sub));


        Customer customer = this.createCustomer(company, request.customer());

        CustomerB2B customerB2B = new CustomerB2B();
        CustomerId customerId = new CustomerId(customer);
        customerB2B.setId(customerId);

        customerB2B.setCustomer(customer);

        Address address = addressService.createAddress(request.address());
        customerB2B.setAddress(address);

        customerB2B.setNameOnInvoice(request.nameOnInvoice());
        customerB2B.setCompanyNumber(request.companyNumber());

        return customerB2BRepository.save(customerB2B);
    }

    @Transactional
    public CustomerB2B updateCustomerB2B(UUID customerId, CustomerB2BCreateRequest request) {
        CustomerB2B customerB2B = customerB2BRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find customer for given id: " + customerId));

        this.updateCustomer(customerId, request.customer());

        addressService.updateAddress(customerB2B.getAddress().getAddressId(), request.address());

        customerB2B.setNameOnInvoice(request.nameOnInvoice());
        customerB2B.setCompanyNumber(request.companyNumber());

        return customerB2BRepository.save(customerB2B);
    }

    @Transactional
    public CustomerB2C createCustomerB2C(String sub, CustomerB2CCreateRequest request) {
        Company company = companyService.getCompanyByUserId(sub)
                .orElseThrow(() -> new EntityNotFoundException("Could not find company for given sub:" + sub));


        Customer customer = this.createCustomer(company, request.customer());
        CustomerB2C customerB2C = new CustomerB2C();
        CustomerId customerId = new CustomerId(customer);
        customerB2C.setId(customerId);
        customerB2C.setCustomer(customer);

        Person person = personService.createPerson(request.person());
        customerB2C.setPerson(person);

        return customerB2CRepository.save(customerB2C);
    }

    @Transactional
    public CustomerB2C updateCustomerB2C(UUID customerId, CustomerB2CCreateRequest request) {
        CustomerB2C customerB2C = customerB2CRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find customer for given id: " + customerId));

        this.updateCustomer(customerId, request.customer());

        personService.updatePerson(customerB2C.getPerson().getPersonId(), request.person());

        return customerB2CRepository.save(customerB2C);
    }

    @Transactional
    Customer createCustomer(Company company, CustomerCreateRequest request) {
        Customer customer = new Customer();

        ContactPerson contactPerson = contactPersonService.createContactPerson(request.contactPerson());
        customer.setContactPerson(contactPerson);

        if (request.defaultDeliveryAddress().isPresent()) {
            Address address = addressService.createAddress(request.defaultDeliveryAddress().get());
            customer.setDefaultDeliveryAddress(address);
        }

        customer.setCompany(company);
        return customerRepository.save(customer);
    }

    @Transactional
    Customer updateCustomer(UUID customerId, CustomerCreateRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find customer for given id: " + customerId));

        contactPersonService.updateContactPerson(customer.getContactPerson().getContactPersonId(), request.contactPerson());

        if (request.defaultDeliveryAddress().isPresent()) {
            if (customer.getDefaultDeliveryAddress() == null) {
                Address address = addressService.createAddress(request.defaultDeliveryAddress().get());
                customer.setDefaultDeliveryAddress(address);
            } else {
                addressService.updateAddress(customer.getDefaultDeliveryAddress().getAddressId(), request.defaultDeliveryAddress().get());
            }
        } else if (customer.getDefaultDeliveryAddress() != null) {
            customer.setDefaultDeliveryAddress(null);
        }

        return customerRepository.save(customer);
    }

    public Boolean isCustomerB2B(UUID customerId) {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find customer for given id: " + customerId));
        return customerB2BRepository.findByCustomerId(customerId).isPresent();
    }

    public List<CustomerB2B> findAllCustomersB2BBySub(String sub) {
        UUID companyId = companyService.getCompanyByUserId(sub)
                .orElseThrow(() -> new EntityNotFoundException("Could not find company for given sub: " + sub))
                .getCompanyId();
        return customerB2BRepository.findAllByCompanyId(companyId);
    }

    public List<CustomerB2C> findAllCustomersB2CBySub(String sub) {
        UUID companyId = companyService.getCompanyByUserId(sub)
                .orElseThrow(() -> new EntityNotFoundException("Could not find company for given sub: " + sub))
                .getCompanyId();
        return customerB2CRepository.findAllByCompanyId(companyId);
    }

    public CustomerB2B findCustomersB2BById(UUID customerId) {
        return customerB2BRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find customer B2B for given ID: " + customerId));
    }

    public CustomerB2C findCustomersB2CById(UUID customerId) {
        return customerB2CRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find customer B2C for given ID: " + customerId));
    }
}
