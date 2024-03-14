package com.lofominhili.farmflow.services.AdminService;

import com.lofominhili.farmflow.dto.HarvestRateDTO;
import com.lofominhili.farmflow.dto.RatingDTO;
import com.lofominhili.farmflow.entities.HarvestRateEntity;
import com.lofominhili.farmflow.entities.ProductEntity;
import com.lofominhili.farmflow.entities.UserEntity;
import com.lofominhili.farmflow.exceptions.NotFoundException;
import com.lofominhili.farmflow.repository.HarvestRateRepository;
import com.lofominhili.farmflow.repository.ProductRepository;
import com.lofominhili.farmflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final HarvestRateRepository harvestRateRepository;

    @Override
    public void rate(RatingDTO rating) throws NotFoundException {
        UserEntity user = userRepository.findByEmail(rating.email())
                .orElseThrow(() -> new NotFoundException("Email not found!"));
        user.setRating(rating.rating());
        userRepository.save(user);
    }

    @Override
    public void blockUser(String email) throws NotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Email not found!"));
        user.setFired(true);
        userRepository.save(user);
    }

    @Override
    public void setHarvestRate(HarvestRateDTO harvestRate) throws NotFoundException {
        ProductEntity product = productRepository.findByName(harvestRate.productName())
                .orElseThrow(() -> new NotFoundException("Product not found!"));
        HarvestRateEntity harvestRateEntity = new HarvestRateEntity();
        harvestRateEntity.setProduct(product);
        harvestRateEntity.setAmount(harvestRate.amount());
        harvestRateRepository.save(harvestRateEntity);
    }
}
