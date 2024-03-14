package com.lofominhili.farmflow.services.ProductService;

import com.lofominhili.farmflow.dto.HarvestRateDTO;
import com.lofominhili.farmflow.dto.ProductDTO;
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

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final RecordRepository recordRepository;
    private final HarvestRateRepository harvestRateRepository;
    private final HarvestRateMapper harvestRateMapper;

    @Override
    public void registrateProduct(ProductDTO productDTO) throws ProductDuplicateException {
        if (productRepository.findByName(productDTO.name()).isPresent()) {
            throw new ProductDuplicateException("This Product already exists!");
        }
        ProductEntity product = productMapper.toEntity(productDTO);
        productRepository.save(product);
    }

    @Override
    public HarvestRateDTO addCollectedProduct(ProductDTO productDTO) throws NotFoundException, RequestDataValidationFailedException {
        UserEntity currentUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProductEntity product = productRepository.findByName(productDTO.name())
                .orElseThrow(() -> new NotFoundException("Product not found!"));
        if (!productDTO.measure().toUpperCase().equals(product.getMeasure().toString()))
            throw new RequestDataValidationFailedException("measure does not match the registered one!");
        product.setAmount(product.getAmount() + productDTO.amount());
        Optional<HarvestRateEntity> harvestRate = harvestRateRepository.findByProduct(product);
        RecordEntity record = new RecordEntity();
        record.setProduct(product);
        record.setUser(currentUser);
        record.setAmount(productDTO.amount());
        if (harvestRate.isPresent()) {
            Integer difference = harvestRate.map(harvestRateEntity -> Math.max(harvestRateEntity.getAmount() - productDTO.amount(), 0)).orElse(0);
            harvestRate.get().setAmount(difference);
            HarvestRateDTO harvestRateDTO = harvestRateMapper.toDto(harvestRate.get());
            harvestRateRepository.save(harvestRate.get());
            return harvestRateDTO;
        }
        HarvestRateDTO harvestRateDTO = new HarvestRateDTO(product.getName(), product.getMeasure().toString(), null, 0);
        productRepository.save(product);
        recordRepository.save(record);
        return harvestRateDTO;
    }
}

