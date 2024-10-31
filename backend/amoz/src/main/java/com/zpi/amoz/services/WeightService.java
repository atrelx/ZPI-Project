package com.zpi.amoz.services;

import com.zpi.amoz.models.Weight;
import com.zpi.amoz.repository.WeightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WeightService {

    private final WeightRepository weightRepository;

    @Autowired
    public WeightService(WeightRepository weightRepository) {
        this.weightRepository = weightRepository;
    }

    public List<Weight> findAll() {
        return weightRepository.findAll();
    }

    public Optional<Weight> findById(UUID id) {
        return weightRepository.findById(id);
    }

    public Weight save(Weight weight) {
        return weightRepository.save(weight);
    }

    public void deleteById(UUID id) {
        weightRepository.deleteById(id);
    }
}

