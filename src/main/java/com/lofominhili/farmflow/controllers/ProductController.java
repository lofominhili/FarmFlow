package com.lofominhili.farmflow.controllers;

import com.lofominhili.farmflow.dto.BasicDTO.SuccessDTO;
import com.lofominhili.farmflow.dto.HarvestRateDTO;
import com.lofominhili.farmflow.dto.ProductDTO;
import com.lofominhili.farmflow.exceptions.NotFoundException;
import com.lofominhili.farmflow.exceptions.ProductDuplicateException;
import com.lofominhili.farmflow.exceptions.RequestDataValidationFailedException;
import com.lofominhili.farmflow.services.ProductService.ProductService;
import com.lofominhili.farmflow.utils.GlobalExceptionHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/registrate-product")
    public ResponseEntity<SuccessDTO<String>> registrateProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult validationResult
    ) throws RequestDataValidationFailedException, ProductDuplicateException {
        if (validationResult.hasErrors()) {
            throw new RequestDataValidationFailedException(GlobalExceptionHandler.handleValidationResults(validationResult));
        }
        productService.registrateProduct(productDTO);
        return new ResponseEntity<>(
                new SuccessDTO<>(
                        HttpStatus.CREATED.value(),
                        "registration",
                        "Successfully registered!"
                ), HttpStatus.CREATED);
    }

    @PostMapping("/add-collected-product")
    public ResponseEntity<SuccessDTO<HarvestRateDTO>> addCollectedProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult validationResult
    ) throws RequestDataValidationFailedException, NotFoundException {
        if (validationResult.hasErrors()) {
            throw new RequestDataValidationFailedException(GlobalExceptionHandler.handleValidationResults(validationResult));
        }
        return new ResponseEntity<>(
                new SuccessDTO<>(
                        HttpStatus.OK.value(),
                        "add",
                        productService.addCollectedProduct(productDTO)
                ), HttpStatus.OK);
    }

}
