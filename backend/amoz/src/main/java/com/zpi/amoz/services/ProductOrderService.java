package com.zpi.amoz.services;

import com.zpi.amoz.models.*;
import com.zpi.amoz.repository.*;
import com.zpi.amoz.requests.ProductOrderCreateRequest;
import com.zpi.amoz.requests.ProductVariantCreateRequest;
import com.zpi.amoz.requests.PushRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductOrderService {

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ProductOrderItemService productOrderItemService;

    @Autowired
    private PushService pushService;

    @Autowired
    private ProductOrderItemRepository productOrderItemRepository;


    public List<ProductOrder> findAll() {
        return productOrderRepository.findAll();
    }

    public Optional<ProductOrder> findById(UUID id) {
        return productOrderRepository.findById(id);
    }

    public ProductOrder save(ProductOrder productOrder) {
        return productOrderRepository.save(productOrder);
    }

    public boolean deleteById(UUID id) {
        if (productOrderRepository.existsById(id)) {
            productOrderRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public void deleteProductOrder(UUID productOrderId) {
        ProductOrder existingProductOrder = productOrderRepository.findById(productOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Product Order not found for given ID: " + productOrderId));

        productOrderItemService.removeAllProductOrderItems(existingProductOrder.getOrderItems());
        productOrderRepository.delete(existingProductOrder);
        System.out.println("ProductOrder deleted: " + productOrderId);
    }

    @Transactional
    public ProductOrder createProductOrder(UUID comapnyId, ProductOrderCreateRequest request) {
        ProductOrder initialProductOrder = new ProductOrder();

        initialProductOrder.setStatus(request.status());
        request.trackingNumber().ifPresent(initialProductOrder::setTrackingNumber);
        request.timeOfSending().ifPresent(initialProductOrder::setTimeOfSending);

        if (request.address().isPresent()) {
            Address address = addressService.createAddress(request.address().get());
            initialProductOrder.setAddress(address);
        }

        if (request.customerId().isPresent()) {
            Customer customer = customerRepository.findById(request.customerId().get())
                    .orElseThrow(() -> new EntityNotFoundException("Cannot find customer for given ID: " + request.customerId().get()));
            initialProductOrder.setCustomer(customer);
        }

        ProductOrder productOrder = productOrderRepository.save(initialProductOrder);

        List<ProductOrderItem> productOrderItems = request.productOrderItems().stream()
                .map(item -> productOrderItemService.createProductOrderItem(productOrder, comapnyId, item))
                .collect(Collectors.toList());

        productOrder.setOrderItems(productOrderItems);

        List<Employee> employees = employeeRepository.findAllByCompany(productOrder
                .getOrderItems().get(0)
                .getProductVariant()
                .getProduct()
                .getCompany());

        List<String> pushTokens = employees.stream()
                .map(Employee::getUser)
                .map(User::getPushToken)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        PushRequest pushRequest = new PushRequest(
                "New Order Received! \uD83D\uDE80",
                "A new order has arrived: " + productOrderItems.stream()
                        .map(item -> item.getAmount() + "x " + item.getProductName())
                        .collect(Collectors.joining(", ")) + ". Check the details!",
                Optional.empty()
        );

        pushService.sendBulkPushMessages(pushTokens, pushRequest);

        return productOrderRepository.save(productOrder);
    }

    @Transactional
    public ProductOrder updateProductOrder(UUID productOrderId, UUID comapnyId, ProductOrderCreateRequest request) {
        ProductOrder existingProductOrder = productOrderRepository.findById(productOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Product Order not found for given ID: " + productOrderId));

        existingProductOrder.setStatus(request.status());

        request.trackingNumber().ifPresentOrElse(existingProductOrder::setTrackingNumber, () -> existingProductOrder.setTrackingNumber(null));
        request.timeOfSending().ifPresentOrElse(existingProductOrder::setTimeOfSending, () -> existingProductOrder.setTimeOfSending(null));

        if (request.address().isPresent()) {
            Address updatedAddress = addressService.createAddress(request.address().get());
            existingProductOrder.setAddress(updatedAddress);
        } else {
            existingProductOrder.setAddress(null);
        }

        if (request.customerId().isPresent()) {
            Customer customer = customerRepository.findById(request.customerId().get())
                    .orElseThrow(() -> new EntityNotFoundException("Cannot find customer for given ID: " + request.customerId().get()));
            existingProductOrder.setCustomer(customer);
        } else {
            existingProductOrder.setCustomer(null);
        }

        this.removeAllProductOrderItemsTransactional(existingProductOrder);

        List<ProductOrderItem> updatedOrderItems = request.productOrderItems().stream()
                .map(item -> productOrderItemService.createProductOrderItem(existingProductOrder, comapnyId, item))
                .collect(Collectors.toList());

        existingProductOrder.setOrderItems(updatedOrderItems);

        return productOrderRepository.save(existingProductOrder);
    }

    @Transactional
    public void removeAllProductOrderItemsTransactional(ProductOrder productOrder) {
        List<ProductOrderItem> productOrderItems = productOrder.getOrderItems();
        productOrderItems.forEach(item -> {
            ProductVariant productVariant = productVariantRepository.findByProductVariantId(item.getProductVariant().getProductVariantId())
                    .orElseThrow(() -> new EntityNotFoundException("ProductVariant not found for the given ID: " + item.getProductVariant().getProductVariantId()));
            Stock stock = productVariant.getStock();
            stock.increaseStock(item.getAmount());
        });
        productOrderItemRepository.deleteAllInBatch(productOrderItems);
    }

    public List<ProductOrder> findByCompanyId(UUID companyId) {
        return productOrderRepository.findByCompanyId(companyId);
    }

}

