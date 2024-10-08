package com.example.coolmate.Services.Product;

import com.example.coolmate.Dtos.ProductDtos.PriceDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.Price;
import com.example.coolmate.Models.Product.ProductDetail;
import com.example.coolmate.Repositories.Product.PriceRepository;
import com.example.coolmate.Repositories.Product.ProductDetailRepository;
import com.example.coolmate.Responses.Product.PriceResponse;
import com.example.coolmate.Services.Impl.Product.IPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PriceService implements IPriceService {


    private final PriceRepository priceRepository;
    private final ProductDetailRepository productDetailRepository;

    @Override
    public PriceResponse createPrice(PriceDTO priceDTO) throws DataNotFoundException {
        // Retrieve the existing product detail from the database
        ProductDetail existingProductDetail = productDetailRepository.findById(priceDTO.getProductDetailId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy chi tiết sản phẩm với ID: " + priceDTO.getProductDetailId()));

        // Create a new price entry without updating the existing price
        Price newPrice = Price.builder()
                .productDetail(existingProductDetail)
                .priceSelling(priceDTO.getPriceSelling())
                .promotionPrice(priceDTO.getPromotionPrice())
                .startDate(priceDTO.getStartDate())
                .endDate(priceDTO.getEndDate())
                .build();


        Price savedPrice = priceRepository.save(newPrice);

        // Construct and return a PriceResponse object with the saved price details
        return PriceResponse.builder()
                .priceId(savedPrice.getId())
                .priceSelling(savedPrice.getPriceSelling())
                .promotionPrice(savedPrice.getPromotionPrice())
                .startDate(savedPrice.getStartDate())
                .endDate(savedPrice.getEndDate())
                .build();
    }


    @Override
    public PriceResponse getPriceById(int id) {
        // Tìm giá dựa trên ID
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Price không tồn tại với ID : " + id));

        // Chuyển đổi Price thành PriceResponse
        return PriceResponse.builder()
                .priceId(price.getId())
                .priceSelling(price.getPriceSelling())
                .promotionPrice(price.getPromotionPrice())
                .startDate(price.getStartDate())
                .endDate(price.getEndDate())
                .productDetail(price.getProductDetail())
                .build();

    }


    // all gia
    @Override
    public Page<PriceResponse> getAllPrices(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "startDate"));

        Page<Price> pricePage = priceRepository.findAll(pageable);

        return pricePage.map(this::convertToPriceResponse);
    }


    private PriceResponse convertToPriceResponse(Price price) {
        return PriceResponse.builder()
                .priceId(price.getId())
                .priceSelling(price.getPriceSelling())
                .promotionPrice(price.getPromotionPrice())
                .startDate(price.getStartDate())
                .endDate(price.getEndDate())
                .productDetail(price.getProductDetail())
                .build();
    }

    @Override
    public void deletePrice(int id) throws DataNotFoundException {
        if (!priceRepository.existsById(id)) {
            throw new DataNotFoundException("Price không tồn tại với ID : " + id);
        }
        priceRepository.deleteById(id);
    }

    @Override
    public PriceResponse updatePriceByProductDetailId(int productDetailId, PriceDTO priceDTO) throws DataNotFoundException {
        // Tìm chi tiết sản phẩm (ProductDetail) bằng productDetailId
        ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy chi tiết sản phẩm với id: " + productDetailId));

        // Lấy danh sách giá của chi tiết sản phẩm này
        List<Price> existingPrices = priceRepository.findByProductDetail(productDetail);

        // Kiểm tra xem có giá nào tồn tại cho chi tiết sản phẩm này không
        if (existingPrices.isEmpty()) {
            throw new DataNotFoundException("Chi tiết sản phẩm chưa có giá. Không thể cập nhật giá.");
        }

        // Với mỗi giá của chi tiết sản phẩm, cập nhật giá bán (priceSelling) và giá khuyến mãi (promotionPrice)
        for (Price existingPrice : existingPrices) {
            existingPrice.setPriceSelling(priceDTO.getPriceSelling());
            existingPrice.setPromotionPrice(priceDTO.getPromotionPrice());

        }
        // Lưu lại danh sách giá đã cập nhật
        List<Price> updatedPrices = priceRepository.saveAll(existingPrices);

        // Trả về PriceResponse của giá mới cập nhật gần đây nhất
        return PriceResponse.fromPriceList(updatedPrices);
    }

    @Override
    public PriceResponse getPricesByProductDetailId(int productDetailId) throws DataNotFoundException {
        ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy chi tiết sản phẩm với ID: " + productDetailId));
        List<Price> prices = priceRepository.findByProductDetail(productDetail);

        return PriceResponse.fromPriceList(prices);
    }

    @Override
    public Page<PriceResponse> getAllDistinctPricesByProductDetailId(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "startDate"));
        List<Price> prices = priceRepository.findAll(); // Fetch all prices first

        // Sử dụng Map để lọc các giá có startDate mới nhất cho mỗi productDetailId
        Map<ProductDetail, Price> latestPrices = prices.stream()
                .collect(Collectors.toMap(
                        Price::getProductDetail,
                        price -> price,
                        (existing, replacement) ->
                                existing.getStartDate().isAfter(replacement.getStartDate()) ? existing : replacement
                ));

        // Chuyển đổi từ Map values (Collection<Price>) sang List<PriceResponse>
        List<PriceResponse> priceResponses = latestPrices.values().stream()
                .map(price -> PriceResponse.fromPriceList(List.of(price)))
                .collect(Collectors.toList());

        // Tính toán vị trí bắt đầu và kết thúc của trang
        int start = (page - 1) * limit;
        int end = Math.min(start + limit, priceResponses.size());

        // Lấy danh sách con cho trang hiện tại
        List<PriceResponse> pageContent = priceResponses.subList(start, end);

        // Tạo đối tượng Page<PriceResponse> và trả về
        return new PageImpl<>(pageContent, pageable, priceResponses.size());
    }

    @Override
    public PriceResponse updatePrice(int id, PriceDTO priceDTO) throws DataNotFoundException {
        // Tìm giá dựa trên priceId
        Price existingPrice = priceRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy giá với id: " + id));

        // Kiểm tra giá trị null hoặc không hợp lệ (nếu cần)
        if (priceDTO.getPriceSelling() < 0 || priceDTO.getPromotionPrice() < 0) {
            throw new IllegalArgumentException("Giá bán và giá khuyến mãi phải lớn hơn hoặc bằng 0.");
        }

        // Cập nhật giá bán (priceSelling) và giá khuyến mãi (promotionPrice)
        existingPrice.setPriceSelling(priceDTO.getPriceSelling());
        existingPrice.setPromotionPrice(priceDTO.getPromotionPrice());

        // Lưu lại giá đã cập nhật
        Price updatedPrice = priceRepository.save(existingPrice);

        // Chuyển đổi Price đã cập nhật thành PriceResponse
        return PriceResponse.builder()
                .priceId(updatedPrice.getId())
                .productDetail(updatedPrice.getProductDetail())
                .priceSelling(updatedPrice.getPriceSelling())
                .promotionPrice(updatedPrice.getPromotionPrice())
                .startDate(updatedPrice.getStartDate())
                .endDate(updatedPrice.getEndDate())
                .build();
    }

    @Override
    public Page<PriceResponse> searchPricesByVersionName(String versionName, Pageable pageable) {
        // Lấy danh sách các đối tượng Price dựa trên versionName với phân trang
        Page<Price> pricePage = priceRepository.findByProductDetailVersionNameContainingIgnoreCase(versionName, pageable);

        // Sử dụng Map để loại bỏ các bản sao dựa trên productDetail.id
        Map<Integer, Price> uniquePricesMap = pricePage.getContent().stream()
                .collect(Collectors.toMap(
                        price -> price.getProductDetail().getId(), // Key là ID của ProductDetail
                        price -> price, // Value là đối tượng Price
                        (existing, replacement) -> existing)); // Nếu trùng lặp, giữ lại bản sao đầu tiên

        // Chuyển đổi các Price thành PriceResponse
        List<PriceResponse> priceResponses = uniquePricesMap.values().stream()
                .map(price -> PriceResponse.builder()
                        .priceId(price.getId())
                        .productDetail(price.getProductDetail())
                        .priceSelling(price.getPriceSelling())
                        .promotionPrice(price.getPromotionPrice())
                        .startDate(price.getStartDate())
                        .endDate(price.getEndDate())
                        .build())
                .collect(Collectors.toList());

        // Trả về Page<PriceResponse>
        return new PageImpl<>(priceResponses, pageable, pricePage.getTotalElements());
    }


}
