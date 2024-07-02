package com.example.coolmate.Services.Product;

import com.example.coolmate.Dtos.ProductDtos.PriceDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.Price;
import com.example.coolmate.Models.Product.ProductDetail;
import com.example.coolmate.Repositories.Product.PriceRepository;
import com.example.coolmate.Repositories.Product.ProductDetailRepository;
import com.example.coolmate.Responses.Product.PriceReponse;
import com.example.coolmate.Services.Impl.Product.IPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PriceService implements IPriceService {
    private final PriceRepository priceRepository;
    private final ProductDetailRepository productDetailRepository;

    private PriceReponse convertToPriceResponse(Price price) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return PriceReponse.builder()
                .id(price.getId())
                .price(price.getPrice())
                .priceSelling(price.getPriceSelling())
                .promotionPrice(price.getPromotionPrice())
                .startDate(price.getStartDate().format(formatter))
                .endDate(price.getEndDate().format(formatter))
                .productDetailId(price.getProductDetail().getId())
                .build();
    }

    @Override
    public PriceReponse createPrice(PriceDTO priceDTO) throws DataNotFoundException {

        ProductDetail existingProductDetail = productDetailRepository.findById(priceDTO.getProductDetailID())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy chi tiết sản phẩm"));

        Price existingPrice = priceRepository.findByProductDetail(existingProductDetail);

        if (existingPrice != null) {
            throw new DataNotFoundException("Sản phẩm đã có giá ");
        } else {
            Price newPrice = Price.builder()
                    .productDetail(existingProductDetail)
                    .price(priceDTO.getPrice())
                    .priceSelling(priceDTO.getPriceSelling())
                    .promotionPrice(priceDTO.getPromotionPrice())
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusMonths(1))
                    .build();
            Price savedPrice = priceRepository.save(newPrice);
            return convertToPriceResponse(savedPrice);
        }
    }


    @Override
    public Price getPriceById(int id) {
        return priceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tồn tại giá có id : " + id));
    }

    @Override
    public List<PriceReponse> getAllPrices() {
        return priceRepository.findAll().stream()
                .map(this::convertToPriceResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePrice(int id) throws DataNotFoundException {
        if (!priceRepository.existsById(id)) {
            throw new DataNotFoundException("Không tồn tại giá có id : " + id);
        }
        priceRepository.deleteById(id);
    }

    @Override
    public Price updatePrice(int priceId, PriceDTO priceDTO) throws DataNotFoundException {
        Price existingPrice = getPriceById(priceId);

        existingPrice.setPrice(priceDTO.getPrice());
        existingPrice.setPriceSelling(priceDTO.getPriceSelling());
        existingPrice.setPromotionPrice(priceDTO.getPromotionPrice());
        // Lưu data mới
        return priceRepository.save(existingPrice);
    }


}
