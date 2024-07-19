package com.example.coolmate.Services.Impl;

import com.example.coolmate.Dtos.SupplierDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Supplier;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ISupplierService {
    Supplier createSupplier(SupplierDTO supplierDTO) throws DataNotFoundException;

    Supplier getSupplierById(int id);

    Page<Supplier> getAllSuppliers(int page, int limit);

    void deleteSupplier(int id) throws DataNotFoundException;

    Supplier updateSupplier(int supplierId, SupplierDTO supplierDTO) throws DataNotFoundException;

    List<Supplier> searchSupplierByName(String name);
}


