package com.lofominhili.farmflow.controllers;

import com.lofominhili.farmflow.dto.BasicDTO.SuccessDTO;
import com.lofominhili.farmflow.dto.EntityDTO.HarvestRateDTO;
import com.lofominhili.farmflow.dto.EntityDTO.ProductDTO;
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

/**
 * REST controller for handling product-related operations.
 * This controller provides endpoints for registering products and adding collected products.
 * <p>
 * This controller is mapped to "/api/product" base path.
 * It requires an instance of {@link ProductService} to be injected via constructor.
 *
 * @author daniel
 */
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Endpoint for registering a new product.
     * This method validates the incoming {@link ProductDTO} using {@link Valid} annotation.
     * If validation fails, it throws a {@link RequestDataValidationFailedException}.
     * Otherwise, it delegates the registration operation to {@link ProductService#registerProduct(ProductDTO)}.
     *
     * @param productDTO       The {@link ProductDTO} containing information about the product to be registered.
     * @param validationResult The result of validation performed by Spring's {@link BindingResult}.
     * @return A {@link ResponseEntity} containing a success message if the registration operation is successful.
     * @throws RequestDataValidationFailedException If the incoming data fails validation.
     * @throws ProductDuplicateException            If the product already exists.
     */
    @PostMapping("/register-product")
    public ResponseEntity<SuccessDTO<String>> registerProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult validationResult
    ) throws RequestDataValidationFailedException, ProductDuplicateException {
        if (validationResult.hasErrors()) {
            throw new RequestDataValidationFailedException(GlobalExceptionHandler.handleValidationResults(validationResult));
        }
        productService.registerProduct(productDTO);
        return new ResponseEntity<>(
                new SuccessDTO<>(
                        HttpStatus.CREATED.value(),
                        "register",
                        "Successfully registered!"
                ), HttpStatus.CREATED);
    }

    /**
     * Endpoint for adding collected products.
     * This method validates the incoming {@link ProductDTO} using {@link Valid} annotation.
     * If validation fails, it throws a {@link RequestDataValidationFailedException}.
     * Otherwise, it delegates the operation to {@link ProductService#addCollectedProduct(ProductDTO)}.
     *
     * @param productDTO       The {@link ProductDTO} containing information about the collected product.
     * @param validationResult The result of validation performed by Spring's {@link BindingResult}.
     * @return A {@link ResponseEntity} containing a success message and updated harvest rate if the operation is successful.
     * @throws RequestDataValidationFailedException If the incoming data fails validation.
     * @throws NotFoundException                    If the product specified in the productDTO is not found.
     */
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