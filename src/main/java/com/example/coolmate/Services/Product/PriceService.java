package com.example.coolmate.Services.Product;

import com.example.coolmate.Dtos.ProductDtos.PriceDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.Price;
import com.example.coolmate.Models.Product.Product;
import com.example.coolmate.Models.Product.ProductDetail;
import com.example.coolmate.Repositories.Product.PriceRepository;
import com.example.coolmate.Repositories.Product.ProductDetailRepository;
import com.example.coolmate.Services.Impl.Product.IPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PriceService implements IPriceService {
    private final PriceRepository priceRepository;
    private final ProductDetailRepository productDetailRepository;

    @Override
    public Price createPrice(PriceDTO priceDTO) throws DataNotFoundException {

        ProductDetail existingProductDetail = productDetailRepository.findById(priceDTO.getProductDetailID())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy chi tiết sản phẩm"));

        // Kiểm tra xem có giá nào tồn tại cho sản phẩm này không
        Price existingPrice = priceRepository.findByProductDetail(existingProductDetail);

        if (existingPrice != null) {
            // Nếu đã có giá cho sản phẩm, chỉ cần trả về giá hiện có
            throw new DataNotFoundException("Sản phẩm đã có giá ");
        } else {
            // Nếu chưa có giá, tạo một bản ghi giá mới
            Price newPrice = Price.builder()
                    .productDetail(existingProductDetail)
                    .price(priceDTO.getPrice())
                    .priceSelling(priceDTO.getPriceSelling())
                    .promotionPrice(priceDTO.getPromotionPrice())
                    .startDate(LocalDateTime.now()) // Giả sử bắt đầu từ thời điểm hiện tại
                    .endDate(LocalDateTime.now().plusMonths(1)) // Giả sử kết thúc sau 1 tháng
                    .build();
            return priceRepository.save(newPrice);
        }
    }


    @Override
    public Price getPriceById(int id) {
        return priceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Price không tồn tại với ID : " + id));
    }

    @Override
    public List<Price> getAllPrices() {
        return priceRepository.findAll();
    }

    @Override
    public void deletePrice(int id) throws DataNotFoundException {
        if (!priceRepository.existsById(id)) {
            throw new DataNotFoundException("Price không tồn tại với ID : " + id);
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

    @Override
    public List<Price> findByProduct(Product product) {
        return priceRepository.findByProductDetail_Product(product);
    }


}
