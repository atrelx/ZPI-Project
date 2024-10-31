package com.zpi.amoz.services;

import com.zpi.amoz.models.VariantAttribute;
import com.zpi.amoz.repository.VariantAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VariantAttributeService {

    private final VariantAttributeRepository variantAttributeRepository;

    @Autowired
    public VariantAttributeService(VariantAttributeRepository variantAttributeRepository) {
        this.variantAttributeRepository = variantAttributeRepository;
    }

    public List<VariantAttribute> findAll() {
        return variantAttributeRepository.findAll();
    }

    public Optional<VariantAttribute> findById(UUID id) {
        return variantAttributeRepository.findById(id);
    }

    public VariantAttribute save(VariantAttribute variantAttribute) {
        return variantAttributeRepository.save(variantAttribute);
    }

    public void deleteById(UUID id) {
        variantAttributeRepository.deleteById(id);
    }
}

