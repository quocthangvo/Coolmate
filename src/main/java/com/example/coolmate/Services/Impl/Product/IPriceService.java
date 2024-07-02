package com.example.coolmate.Services.Impl.Product;

import com.example.coolmate.Dtos.ProductDtos.PriceDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.Price;
import com.example.coolmate.Responses.Product.PriceReponse;

import java.util.List;

public interface IPriceService {
    PriceReponse createPrice(PriceDTO priceDTO) throws DataNotFoundException;

    Price getPriceById(int id);

    List<PriceReponse> getAllPrices();

    void deletePrice(int id) throws DataNotFoundException;

    Price updatePrice(int priceId, PriceDTO priceDTO) throws DataNotFoundException;

}
