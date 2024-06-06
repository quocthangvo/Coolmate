package com.example.coolmate.Services.Impl.Product;

import com.example.coolmate.Dtos.ProductDtos.BrandDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.Brand;

import java.util.List;

public interface IBrandService {
    Brand createBrand(BrandDTO brandDTO) throws DataNotFoundException;
    List<Brand> getAllBrands();
    Brand getBrandById(int id);
    void deleteBrandById(int id) throws DataNotFoundException;
    Brand updateBrand(int id, BrandDTO brandDTO) throws DataNotFoundException;
}
