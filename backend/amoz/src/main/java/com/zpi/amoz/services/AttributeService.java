package com.zpi.amoz.services;

import com.zpi.amoz.models.Attribute;
import com.zpi.amoz.repository.AttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AttributeService {

    private final AttributeRepository attributeRepository;

    @Autowired
    public AttributeService(AttributeRepository attributeRepository) {
        this.attributeRepository = attributeRepository;
    }

    public List<Attribute> findAll() {
        return attributeRepository.findAll();
    }

    public Optional<Attribute> findById(UUID id) {
        return attributeRepository.findById(id);
    }

    public Attribute save(Attribute attribute) {
        return attributeRepository.save(attribute);
    }

    public boolean deleteById(UUID id) {
        if (attributeRepository.existsById(id)) {
            attributeRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}

