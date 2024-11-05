package com.zpi.amoz.services;

import com.zpi.amoz.models.Stock;
import com.zpi.amoz.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StockService {

    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

    public Optional<Stock> findById(UUID id) {
        return stockRepository.findById(id);
    }

    public Stock save(Stock stock) {
        return stockRepository.save(stock);
    }

    public boolean deleteById(UUID id) {
        if (stockRepository.existsById(id)) {
            stockRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}

