package com.example.coolmate.Services.Impl.Product;

import com.example.coolmate.Dtos.ProductDtos.PriceDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.Price;
import com.example.coolmate.Models.Product.Product;

import java.util.List;

public interface IPriceService {
    Price createPrice(PriceDTO priceDTO) throws DataNotFoundException;

    Price getPriceById(int id);

    List<Price> getAllPrices();

    void deletePrice(int id) throws DataNotFoundException;

    Price updatePrice(int priceId, PriceDTO priceDTO) throws DataNotFoundException;
    List<Price> findByProduct(Product product);
}
