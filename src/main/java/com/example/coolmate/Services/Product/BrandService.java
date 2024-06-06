package com.example.coolmate.Services.Product;

import com.example.coolmate.Dtos.ProductDtos.BrandDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.Brand;
import com.example.coolmate.Repositories.Product.BrandRepository;
import com.example.coolmate.Services.Impl.Product.IBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandService implements IBrandService {

    private final BrandRepository brandRepository;

    @Override
    public Brand createBrand(BrandDTO brandDTO) throws DataNotFoundException {
        Optional<Brand> existingBrand = brandRepository.findByName(brandDTO.getName());

        if (existingBrand.isPresent()) {
            throw new DataNotFoundException("Thương hiệu đã tồn tại: " + brandDTO.getName());
        } else {
            // Create a new Category entity
            Brand newBrand = Brand.builder()
                    .name(brandDTO.getName())
                    .build();
            return brandRepository.save(newBrand);
        }
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Brand getBrandById(int id) {
        return brandRepository.findById(id).orElseThrow(()->new RuntimeException("Brand không tồn tại"));
    }

    @Override
    public void deleteBrandById(int id) throws DataNotFoundException {
        if(!brandRepository.existsById(id)){
            throw new DataNotFoundException("Không tìm thấy thương hiệu có ID : "+id);
        }
        brandRepository.deleteById(id);
    }

    @Override
    public Brand updateBrand(int id, BrandDTO brandDTO) throws DataNotFoundException {
       Brand existingBrand = getBrandById(id);
       if(existingBrand.getName().equals(brandDTO.getName())){
           throw new DataNotFoundException("Tên thương hiệu mới " + brandDTO.getName()
                   + " trùng với tên thương hiệu hiện tại: ");
       }

        Optional<Brand> brandWithSameName = brandRepository.findByName(brandDTO.getName());
        if (brandWithSameName.isPresent()) {
            throw new DataNotFoundException("Thương hiệu" + brandDTO.getName() + " đã tồn tại");
        }

        // Cập nhật thông tin thương heiu56
        existingBrand.setName(brandDTO.getName());
        brandRepository.save(existingBrand); // Lưu dữ liệu đã được cập nhật

        return existingBrand;
    }
}
