package com.example.coolmate.Services.Impl.Product;

import com.example.coolmate.Dtos.ProductDtos.PriceDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.Price;
import com.example.coolmate.Responses.Product.PriceResponse;

import java.util.List;

public interface IPriceService {
    PriceResponse createPrice(PriceDTO priceDTO) throws DataNotFoundException;

    PriceResponse getPriceById(int id);

    List<Price> getAllPrices(int page, int limit);

    void deletePrice(int id) throws DataNotFoundException;

    PriceResponse updatePriceByProductDetailId(int priceId, PriceDTO priceDTO) throws DataNotFoundException;

    PriceResponse getPricesByProductDetailId(int productDetailId) throws DataNotFoundException;

    List<PriceResponse> getAllDistinctPricesByProductDetailId();

    PriceResponse updatePrice(int id, PriceDTO priceDTO) throws DataNotFoundException;
}
