package com.example.coolmate.Services.Impl.Product;

import com.example.coolmate.Dtos.ProductDtos.SizeDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.Size;

import java.util.List;

public interface ISizeService {
    Size createSize(SizeDTO sizeDTO) throws DataNotFoundException;
    Size getSizeById(int id);
    List<Size> getAllSizes();
    void deleteSizeById(int id) throws DataNotFoundException;
    Size updateSize(int sizeId,SizeDTO sizeDTO) throws  DataNotFoundException;
}
