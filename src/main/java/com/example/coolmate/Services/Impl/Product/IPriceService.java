package com.example.coolmate.Services.Impl.Product;

import com.example.coolmate.Dtos.ProductDtos.PriceDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Responses.Product.PriceResponse;
import org.springframework.data.domain.Page;

public interface IPriceService {
    PriceResponse createPrice(PriceDTO priceDTO) throws DataNotFoundException;

    PriceResponse getPriceById(int id);

    Page<PriceResponse> getAllPrices(int page, int limit) throws DataNotFoundException;

    void deletePrice(int id) throws DataNotFoundException;

    PriceResponse updatePriceByProductDetailId(int priceId, PriceDTO priceDTO) throws DataNotFoundException;

    PriceResponse getPricesByProductDetailId(int productDetailId) throws DataNotFoundException;

    Page<PriceResponse> getAllDistinctPricesByProductDetailId(int page, int limit);

    PriceResponse updatePrice(int id, PriceDTO priceDTO) throws DataNotFoundException;
}
