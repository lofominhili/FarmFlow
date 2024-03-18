package com.lofominhili.farmflow.services.ProductService;

import com.lofominhili.farmflow.dto.EntityDTO.HarvestRateDTO;
import com.lofominhili.farmflow.dto.EntityDTO.ProductDTO;
import com.lofominhili.farmflow.exceptions.NotFoundException;
import com.lofominhili.farmflow.exceptions.ProductDuplicateException;
import com.lofominhili.farmflow.exceptions.RequestDataValidationFailedException;

public interface ProductService {
    void registerProduct(ProductDTO product) throws ProductDuplicateException;

    HarvestRateDTO addCollectedProduct(ProductDTO product) throws NotFoundException, RequestDataValidationFailedException;
}
