package com.example.coolmate.Services;

import com.example.coolmate.Dtos.ProductDtos.PriceDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.Price;
import com.example.coolmate.Models.Product.Product;
import com.example.coolmate.Repositories.Product.PriceRepository;
import com.example.coolmate.Repositories.Product.ProductRepository;
import com.example.coolmate.Services.Impl.IPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PriceService implements IPriceService {
    private final PriceRepository priceRepository;
    private final ProductRepository productRepository;

    @Override
    public Price createPrice(PriceDTO priceDTO) throws DataNotFoundException {
        Product existingProduct = productRepository.findById(priceDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find price with id: " + priceDTO.getProductId()));

        // Create a new Category entity
        Price newPrice = Price.builder()
                .price(priceDTO.getPrice())
                .product(existingProduct)
                .build();
        return priceRepository.save(newPrice);

    }

    @Override
    public Price getPriceById(int id) {
        return priceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Price không tồn tại"));
    }

    @Override
    public List<Price> getAllPrices() {
        return priceRepository.findAll();
    }

    @Override
    public void deletePrice(int id) throws DataNotFoundException {
        if (!priceRepository.existsById(id)) {
            throw new DataNotFoundException("Price not found with id: " + id);
        }
        priceRepository.deleteById(id);
    }

    @Override
    public Price updatePrice(int priceId, PriceDTO priceDTO) throws DataNotFoundException {
        Price existingPrice = getPriceById(priceId);

        existingPrice.setPrice(priceDTO.getPrice());
        priceRepository.save(existingPrice); //lưu data mới
        return existingPrice;
    }


}
