package com.example.coolmate.Services.Product;

import com.example.coolmate.Dtos.ProductDtos.ColorDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.Color;
import com.example.coolmate.Repositories.Product.ColorRepository;
import com.example.coolmate.Services.Impl.Product.IColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ColorService implements IColorService {
    private final ColorRepository colorRepository;
    @Override
    public Color createColor(ColorDTO colorDTO) throws DataNotFoundException {
        Optional<Color> existingColor = colorRepository.findByName(colorDTO.getName());
        if(existingColor.isPresent()){
            throw new DataNotFoundException("Màu sắc đã tồn tại : "+colorDTO.getName());
        }else{
            Color newColor = Color.builder()
                    .name(colorDTO.getName())
                    .build();
            return colorRepository.save(newColor);
        }

    }

    @Override
    public Color getColorById(int id) {
        return colorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Màu sắc không tồn tại"));
    }

    @Override
    public List<Color> getAllColors() {
        return colorRepository.findAll();
    }

    @Override
    public void deleteColorById(int id) throws DataNotFoundException {
        if (!colorRepository.existsById(id)) {
            throw new DataNotFoundException("Không tìm thấy màu sắc id: " + id);
        }
        colorRepository.deleteById(id);
    }

    @Override
    public Color updateColor(int colorId, ColorDTO colorDTO) throws DataNotFoundException {
        Color existingColor = getColorById(colorId);

        // Kiểm tra nếu tên mới giống với tên hiện tại của danh mục
        if (existingColor.getName().equals(colorDTO.getName())) {
            throw new DataNotFoundException("Tên màu sắc mới " + colorDTO.getName() + " trùng với tên màu sắc hiện tại: ");
        }

        // Kiểm tra trùng tên với các danh mục khác
        Optional<Color> colorWithSameName = colorRepository.findByName(colorDTO.getName());
        if (colorWithSameName.isPresent()) {
            throw new DataNotFoundException("Màu sắc " + colorDTO.getName() + " đã tồn tại");
        }

        // Cập nhật thông tin danh mục
        existingColor.setName(colorDTO.getName());
        colorRepository.save(existingColor); // Lưu dữ liệu đã được cập nhật

        return existingColor;
    }
}
