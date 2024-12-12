package com.zpi.amoz.services;

import com.zpi.amoz.models.*;
import com.zpi.amoz.repository.ProductOrderItemRepository;
import com.zpi.amoz.repository.ProductVariantRepository;
import com.zpi.amoz.requests.ProductOrderItemCreateRequest;
import com.zpi.amoz.requests.PushRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductOrderItemService {


    @Autowired
    private ProductOrderItemRepository productOrderItemRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ProductVariantService productVariantService;

    @Autowired
    private PushService pushService;

    @Autowired
    private EmployeeService employeeService;

    public List<ProductOrderItem> findAll() {
        return productOrderItemRepository.findAll();
    }

    public Optional<ProductOrderItem> findById(UUID id) {
        return productOrderItemRepository.findById(id);
    }

    public ProductOrderItem save(ProductOrderItem productOrderItem) {
        return productOrderItemRepository.save(productOrderItem);
    }

    public boolean deleteById(UUID id) {
        if (productOrderItemRepository.existsById(id)) {
            productOrderItemRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public ProductOrderItem createProductOrderItem(ProductOrder productOrder, UUID companyId, ProductOrderItemCreateRequest request) {
        ProductOrderItem productOrderItem = new ProductOrderItem();
        productOrderItem.setProductOrder(productOrder);

        ProductVariant productVariant = productVariantRepository.findByProductVariantId(request.productVariantId())
                .orElseThrow(() -> new EntityNotFoundException("Could not find product variant for given id: " + request.productVariantId()));

        productOrderItem.setProductVariant(productVariant);

        productOrderItem.setProductName(productVariantService.getFullProductName(productVariant));
        productOrderItem.setUnitPrice(productVariant.getVariantPrice());

        Stock stock = productVariant.getStock();
        if (stock.getAmountAvailable() < request.amount()) {
            throw new IllegalArgumentException("Requested amount is bigger than amount available in stock");
        }
        stock.decreaseStock(request.amount());

        if (stock.isAlarmTriggered()) {
            List<Employee> employees = employeeService.getEmployeesByCompanyId(companyId);

            List<String> pushTokens = employees.stream()
                    .map(Employee::getUser)
                    .map(User::getPushToken)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            PushRequest pushRequest = new PushRequest(
                    "Alert: Stan produktu na magazynie osiągnął stan krytyczny",
                    "Zostały tylko " + stock.getAmountAvailable() + " sztuki produktu: " + productOrderItem.getProductName(),
                    Optional.empty());

            pushService.sendBulkPushMessages(pushTokens, pushRequest);
        }

        productOrderItem.setAmount(request.amount());
        return productOrderItemRepository.save(productOrderItem);
    }


    public void removeAllProductOrderItems(List<ProductOrderItem> productOrderItems) {
        productOrderItems.forEach(item -> {
            ProductVariant productVariant = productVariantRepository.findByProductVariantId(item.getProductVariant().getProductVariantId())
                    .orElseThrow(() -> new EntityNotFoundException("ProductVariant not found for the given ID: " + item.getProductVariant().getProductVariantId()));
            Stock stock = productVariant.getStock();
            stock.increaseStock(item.getAmount());
        });
        productOrderItemRepository.deleteAll(productOrderItems);
    }


}

