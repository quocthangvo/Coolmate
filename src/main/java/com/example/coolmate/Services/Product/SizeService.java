package com.example.coolmate.Services.Product;

import com.example.coolmate.Dtos.ProductDtos.SizeDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.Size;
import com.example.coolmate.Repositories.Product.SizeRepository;
import com.example.coolmate.Services.Impl.Product.ISizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SizeService implements ISizeService {

    private final SizeRepository sizeRepository;

    @Override
    public Size createSize(SizeDTO sizeDTO) throws DataNotFoundException {
        Optional<Size> existingSize = sizeRepository.findByName(sizeDTO.getName());
        if (existingSize.isPresent()) {
            throw new DataNotFoundException("Kích thước đã tồn tại : " + sizeDTO.getName());
        } else {
            Size newSize = Size.builder()
                    .name(sizeDTO.getName())
                    .build();
            return sizeRepository.save(newSize);
        }
    }

    @Override
    public Size getSizeById(int id) {
        return sizeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kích thước không tồn tại"));
    }

    @Override
    public List<Size> getAllSizes(int page, int limit) {
        return sizeRepository.findAll();
    }

    @Override
    public void deleteSizeById(int id) throws DataNotFoundException {
        if (!sizeRepository.existsById(id)) {
            throw new DataNotFoundException("Không tìm thấy kích thước id: " + id);
        }
        sizeRepository.deleteById(id);
    }

    @Override
    public Size updateSize(int sizeId, SizeDTO sizeDTO) throws DataNotFoundException {
        Size existingSize = getSizeById(sizeId);

        // Kiểm tra nếu tên mới giống với tên hiện tại của danh mục
        if (existingSize.getName().equals(sizeDTO.getName())) {
            throw new DataNotFoundException("Tên kích thước mới " + sizeDTO.getName() + " trùng với tên kích thước hiện tại: ");
        }

        // Kiểm tra trùng tên với các danh mục khác
        Optional<Size> sizeWithSameName = sizeRepository.findByName(sizeDTO.getName());
        if (sizeWithSameName.isPresent()) {
            throw new DataNotFoundException("Kích thước " + sizeDTO.getName() + " đã tồn tại");
        }

        // Cập nhật thông tin danh mục
        existingSize.setName(sizeDTO.getName());
        sizeRepository.save(existingSize); // Lưu dữ liệu đã được cập nhật

        return existingSize;
    }
}
