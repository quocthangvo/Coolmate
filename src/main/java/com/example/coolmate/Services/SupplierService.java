package com.example.coolmate.Services;

import com.example.coolmate.Dtos.SupplierDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Supplier;
import com.example.coolmate.Repositories.SupplierRepository;
import com.example.coolmate.Services.Impl.ISupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupplierService implements ISupplierService {

    private final SupplierRepository supplierRepository;

    @Override
    public Supplier createSupplier(SupplierDTO supplierDTO) throws DataNotFoundException {
        Optional<Supplier> existingSupplierName = supplierRepository.findByName(supplierDTO.getName());
        Optional<Supplier> existingSupplierByPhone = supplierRepository.findByPhoneNumber(supplierDTO.getPhoneNumber());
        Optional<Supplier> existingSupplierByAddress = supplierRepository.findByAddress(supplierDTO.getAddress());


        if (existingSupplierName.isPresent()) {
            throw new DataNotFoundException("Nhà cung cấp đã tồn tại với tên: " + supplierDTO.getName());
        } else if (existingSupplierByPhone.isPresent()) {
            throw new DataNotFoundException("Nhà cung cấp đã tồn tại với số điện thoại: " + supplierDTO.getPhoneNumber());
        } else if (existingSupplierByAddress.isPresent()) {
            throw new DataNotFoundException("Nhà cung cấp đã tồn tại với địa chỉ: " + supplierDTO.getAddress());
        } else {

            // Nếu không có nhà cung cấp nào tồn tại, tạo mới và lưu vào cơ sở dữ liệu
            Supplier newSupplier = Supplier.builder()
                    .name(supplierDTO.getName())
                    .phoneNumber(supplierDTO.getPhoneNumber())
                    .address(supplierDTO.getAddress())
                    .build();
            return supplierRepository.save(newSupplier);
        }
    }


    @Override
    public Supplier getSupplierById(int id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier không tồn tại"));
    }

    @Override
    public Page<Supplier> getAllSuppliers(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return supplierRepository.findAll(pageable);
    }

    @Override
    public void deleteSupplier(int id) throws DataNotFoundException {
        if (!supplierRepository.existsById(id)) {
            throw new DataNotFoundException("Không tìm thấy nhà cung cấp id: " + id);
        }
        supplierRepository.deleteById(id);
    }

    @Override
    public Supplier updateSupplier(int supplierId, SupplierDTO supplierDTO) throws DataNotFoundException {
        Supplier existingSupplier = getSupplierById(supplierId);

        // Kiểm tra trùng tên
        if (!existingSupplier.getName().equals(supplierDTO.getName()) &&
                supplierRepository.existsByName(supplierDTO.getName())) {
            throw new DataNotFoundException("Nhà cung cấp có tên '" + supplierDTO.getName() + "' đã tồn tại");
        }

        // Kiểm tra trùng số điện thoại
        if (!existingSupplier.getPhoneNumber().equals(supplierDTO.getPhoneNumber()) &&
                supplierRepository.existsByPhoneNumber(supplierDTO.getPhoneNumber())) {
            throw new DataNotFoundException("Nhà cung cấp có số điện thoại '" + supplierDTO.getPhoneNumber() + "' đã tồn tại");
        }

        // Kiểm tra trùng địa chỉ
        if (!existingSupplier.getAddress().equals(supplierDTO.getAddress()) &&
                supplierRepository.existsByAddress(supplierDTO.getAddress())) {
            throw new DataNotFoundException("Nhà cung cấp có địa chỉ'" + supplierDTO.getAddress() + "' đã tồn tại");
        }

        // Cập nhật thông tin nhà cung cấp
        existingSupplier.setName(supplierDTO.getName());
        existingSupplier.setPhoneNumber(supplierDTO.getPhoneNumber());
        existingSupplier.setAddress(supplierDTO.getAddress());
        supplierRepository.save(existingSupplier); // Lưu dữ liệu đã được cập nhật

        return existingSupplier;
    }


    public List<Supplier> searchSupplierByName(String name) {
        return supplierRepository.findByNameContaining(name);
    }
}
