//package com.example.coolmate.Controllers;
//
//import com.example.coolmate.Dtos.InventoryDTO;
//import com.example.coolmate.Exceptions.DataNotFoundException;
//import com.example.coolmate.Exceptions.Message.ErrorMessage;
//import com.example.coolmate.Models.Inventory;
//import com.example.coolmate.Responses.ApiResponses.ApiResponse;
//import com.example.coolmate.Services.Impl.IInventoryService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("api/v1/inventories")
//@RequiredArgsConstructor
//public class InventoryController {
//    private final IInventoryService iInventoryService;
//
//    @PostMapping("")
//    public ResponseEntity<?> createCategory(
//            @Valid @RequestBody InventoryDTO inventoryDTO, BindingResult result) {
//        try {
//            Inventory createdInventory = iInventoryService.createInventory(inventoryDTO);
//            ApiResponse<Inventory> response = new ApiResponse<>(
//                    "Inventory created successfully", createdInventory);
//            return ResponseEntity.ok(response);
//        } catch (DataNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(e.getMessage()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage(
//                    "Đã xảy ra lỗi không xác định"));
//        }
//    }
//}
