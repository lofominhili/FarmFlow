package com.lofominhili.farmflow.services.ProductService;

import com.lofominhili.farmflow.dto.EntityDTO.HarvestRateDTO;
import com.lofominhili.farmflow.dto.EntityDTO.ProductDTO;
import com.lofominhili.farmflow.entities.HarvestRateEntity;
import com.lofominhili.farmflow.entities.ProductEntity;
import com.lofominhili.farmflow.entities.RecordEntity;
import com.lofominhili.farmflow.entities.UserEntity;
import com.lofominhili.farmflow.exceptions.NotFoundException;
import com.lofominhili.farmflow.exceptions.ProductDuplicateException;
import com.lofominhili.farmflow.exceptions.RequestDataValidationFailedException;
import com.lofominhili.farmflow.mappers.HarvestRateMapper;
import com.lofominhili.farmflow.mappers.ProductMapper;
import com.lofominhili.farmflow.repository.HarvestRateRepository;
import com.lofominhili.farmflow.repository.ProductRepository;
import com.lofominhili.farmflow.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service implementation of {@link ProductService}for product-related operations.
 * This service provides methods for registering new products, adding collected products,
 * and managing harvest rates.
 * <p>
 * This service requires instances of {@link ProductRepository}, {@link ProductMapper},
 * {@link RecordRepository}, {@link HarvestRateRepository}, and {@link HarvestRateMapper}
 * to be injected via constructor.
 *
 * @author daniel
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final RecordRepository recordRepository;
    private final HarvestRateRepository harvestRateRepository;
    private final HarvestRateMapper harvestRateMapper;

    /**
     * Registers a new product with the provided product information.
     * This method checks if a product with the given name already exists in the repository.
     * If not, the product information is converted to a ProductEntity and saved in the repository.
     *
     * @param productDTO The {@link ProductDTO} containing information about the product to be registered.
     *                   It should include the product's name, measure, and amount.
     * @throws ProductDuplicateException If a product with the provided name already exists in the repository.
     *                                   This exception indicates a registration failure due to duplicate product name.
     */
    @Override
    public void registerProduct(ProductDTO productDTO) throws ProductDuplicateException {
        if (productRepository.findByName(productDTO.name()).isPresent()) {
            throw new ProductDuplicateException("This Product already exists!");
        }
        ProductEntity product = productMapper.toEntity(productDTO);
        productRepository.save(product);
    }

    /**
     * Adds collected products to the specified product.
     * This method retrieves the current user from the security context,
     * then retrieves the product from the product repository based on the name provided in the ProductDTO.
     * It validates the product measure and creates a record for the collected products.
     * If a record is created, it also updates the harvest rate accordingly.
     *
     * @param productDTO The {@link ProductDTO} containing information about the collected product.
     *                   It should include the product's name, measure, and amount.
     * @return The {@link HarvestRateDTO} containing information about the updated harvest rate.
     * @throws NotFoundException                    If the product specified in the ProductDTO is not found in the {@link ProductRepository}.
     *                                              This exception indicates that the specified product does not exist.
     * @throws RequestDataValidationFailedException If the measure provided in the ProductDTO does not match the registered one.
     *                                              This exception indicates a validation failure for the product measure.
     */
    public HarvestRateDTO addCollectedProduct(ProductDTO productDTO) throws NotFoundException, RequestDataValidationFailedException {
        UserEntity currentUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProductEntity product = productRepository.findByName(productDTO.name())
                .orElseThrow(() -> new NotFoundException("Product not found!"));
        validateProductAndCreateRecord(product, productDTO, currentUser);
        return updateHarvestRateIfNeeded(product, productDTO);
    }

    private void validateProductAndCreateRecord(ProductEntity product, ProductDTO productDTO, UserEntity currentUser) throws RequestDataValidationFailedException {
        if (!productDTO.measure().equalsIgnoreCase(product.getMeasure().toString())) {
            throw new RequestDataValidationFailedException("Measure does not match the registered one!");
        }
        product.setAmount(product.getAmount() + productDTO.amount());
        productRepository.save(product);
        RecordEntity record = new RecordEntity();
        record.setProduct(product);
        record.setUser(currentUser);
        record.setAmount(productDTO.amount());
        recordRepository.save(record);
    }

    private HarvestRateDTO updateHarvestRateIfNeeded(ProductEntity product, ProductDTO productDTO) {
        Optional<HarvestRateEntity> harvestRate = harvestRateRepository.findByProduct(product);
        if (harvestRate.isPresent()) {
            Integer difference = Math.max(harvestRate.get().getAmount() - productDTO.amount(), 0);
            harvestRate.get().setAmount(difference);
            harvestRateRepository.save(harvestRate.get());
            return harvestRateMapper.toDto(harvestRate.get());
        }
        return new HarvestRateDTO(product.getName(), product.getMeasure().toString(), null, 0);
    }

}

