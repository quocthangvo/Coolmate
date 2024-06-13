package com.example.coolmate.Services.Impl.Product;

import com.example.coolmate.Dtos.ProductDtos.ColorDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.Color;

import java.util.List;

public interface IColorService {
    Color createColor(ColorDTO colorDTO) throws DataNotFoundException;

    Color getColorById(int id);

    List<Color> getAllColors(int page, int limit);

    void deleteColorById(int id) throws DataNotFoundException;

    Color updateColor(int colorId, ColorDTO colorDTO) throws DataNotFoundException;
}
