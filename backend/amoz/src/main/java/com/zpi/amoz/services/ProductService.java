package com.zpi.amoz.services;

import com.zpi.amoz.models.*;
import com.zpi.amoz.repository.*;
import com.zpi.amoz.requests.ProductCreateRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private AttributeService attributeService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductAttributeRepository productAttributeRepository;

    @Autowired
    private CompanyRepository companyRepository;


    public List<Product> findAll() {
        return productRepository.findAll().stream().filter(Product::isActive).collect(Collectors.toList());
    }

    public Optional<Product> findById(UUID id) {
        return productRepository.findById(id);
    }

    public List<Product> findAllByCompanyId(UUID companyId) {
        return productRepository.findAllByCompanyId(companyId).stream().filter(Product::isActive).collect(Collectors.toList());
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }


    @Transactional
    public boolean deactivateProduct(UUID productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            for (ProductVariant variant : product.getProductVariants()) {
                productVariantRepository.deactivateProductVariant(variant.getProductVariantId());
            }

            productRepository.deactivateProduct(productId);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Product createProduct(UUID companyId, ProductCreateRequest productDetails) {
        Product initialProduct = new Product();
        initialProduct.setName(productDetails.name());
        initialProduct.setPrice(productDetails.price());
        initialProduct.setDescription(productDetails.description().orElse(null));
        initialProduct.setBrand(productDetails.brand().orElse(null));

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find company for given id"));
        Category category = categoryRepository.findById(productDetails.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("Could not find category for given id"));
        if (categoryRepository.hasChild(category.getCategoryId())) {
            throw new IllegalArgumentException("Given category is not at the bottom level");
        }
        initialProduct.setCategory(category);
        initialProduct.setCompany(company);

        Product product = productRepository.save(initialProduct);

        List<ProductAttribute> productAttributes = productDetails.productAttributes().stream()
                .map(attribute -> attributeService.createProductAttribute(attribute, product.getProductId()))
                .collect(Collectors.toList());

        List<ProductVariant> productVariants = productVariantRepository.findAllById(productDetails.productVariantIds());

        product.setProductAttributes(productAttributes);
        product.setProductVariants(productVariants);

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(UUID productId, ProductCreateRequest productDetails) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        product.setName(productDetails.name());
        product.setPrice(productDetails.price());
        product.setDescription(productDetails.description().orElse(null));
        product.setBrand(productDetails.brand().orElse(null));

        Category category = categoryRepository.findById(productDetails.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        if (categoryRepository.hasChild(category.getCategoryId())) {
            throw new IllegalArgumentException("Given category is not at the bottom level");
        }

        productAttributeRepository.deleteAllByProductId(productId);

        List<ProductAttribute> productAttributes = productDetails.productAttributes().stream()
                .map(attribute -> attributeService.createProductAttribute(attribute, product.getProductId()))
                .collect(Collectors.toList());
        List<ProductVariant> productVariants = productVariantRepository.findAllById(productDetails.productVariantIds());

        product.setCategory(category);
        product.setProductAttributes(productAttributes);
        product.setProductVariants(productVariants);

        return productRepository.save(product);
    }

    @Transactional
    public Product setMainVariant(UUID productId, UUID mainVariantId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        ProductVariant mainProductVariant = productVariantRepository.findById(mainVariantId)
                .orElseThrow(() -> new EntityNotFoundException("Main variant not found"));

        product.setMainProductVariant(mainProductVariant);

        return productRepository.save(product);
    }

    @Transactional
    public void deactivateProductById(UUID productId) throws EntityNotFoundException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find product for given id: " + productId));

        for (ProductVariant productVariant : product.getProductVariants()) {
            productVariant.setActive(false);
            productVariantRepository.save(productVariant);
        }

        productRepository.deactivateProduct(productId);
    }
}
