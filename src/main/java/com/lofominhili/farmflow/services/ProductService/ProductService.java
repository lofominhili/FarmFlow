package com.lofominhili.farmflow.services.ProductService;

import com.lofominhili.farmflow.dto.HarvestRateDTO;
import com.lofominhili.farmflow.dto.ProductDTO;
import com.lofominhili.farmflow.exceptions.NotFoundException;
import com.lofominhili.farmflow.exceptions.ProductDuplicateException;
import com.lofominhili.farmflow.exceptions.RequestDataValidationFailedException;

public interface ProductService {
    void registrateProduct(ProductDTO product) throws ProductDuplicateException;

    HarvestRateDTO addCollectedProduct(ProductDTO product) throws NotFoundException, RequestDataValidationFailedException;
}
