package com.zpi.amoz.services;

import com.zpi.amoz.models.Dimensions;
import com.zpi.amoz.repository.DimensionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DimensionsService {

    private final DimensionsRepository dimensionsRepository;

    @Autowired
    public DimensionsService(DimensionsRepository dimensionsRepository) {
        this.dimensionsRepository = dimensionsRepository;
    }

    public List<Dimensions> findAll() {
        return dimensionsRepository.findAll();
    }

    public Optional<Dimensions> findById(UUID id) {
        return dimensionsRepository.findById(id);
    }

    public Dimensions save(Dimensions dimensions) {
        return dimensionsRepository.save(dimensions);
    }

    public void deleteById(UUID id) {
        dimensionsRepository.deleteById(id);
    }
}
