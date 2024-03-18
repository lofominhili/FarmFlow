package com.lofominhili.farmflow.services.AdminService;

import com.lofominhili.farmflow.dto.EntityDTO.HarvestRateDTO;
import com.lofominhili.farmflow.dto.EntityDTO.RatingDTO;
import com.lofominhili.farmflow.entities.HarvestRateEntity;
import com.lofominhili.farmflow.entities.ProductEntity;
import com.lofominhili.farmflow.entities.UserEntity;
import com.lofominhili.farmflow.exceptions.NotFoundException;
import com.lofominhili.farmflow.repository.HarvestRateRepository;
import com.lofominhili.farmflow.repository.ProductRepository;
import com.lofominhili.farmflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service implementation of {@link AdminService} for administrative operations.
 * This service provides methods for managing user ratings, blocking user accounts,
 * and setting harvest rates for products.
 * <p>
 * This service requires instances of {@link UserRepository}, {@link ProductRepository},
 * and {@link HarvestRateRepository} to be injected via constructor.
 *
 * @author daniel
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final HarvestRateRepository harvestRateRepository;

    /**
     * Sets the rating for a user based on the provided {@link RatingDTO}.
     * This method retrieves the user from the user repository based on the email
     * provided in the RatingDTO. If the user is found, their rating is updated
     * to the value specified in the RatingDTO. The updated user entity is then saved
     * back to the user repository.
     *
     * @param rating The {@link RatingDTO} containing information about the user's email address and rating value.
     *               It should include the user's email and the new rating value.
     * @throws NotFoundException If the user specified by the email address is not found in the {@link UserRepository}.
     *                           This exception indicates that the specified user does not exist.
     */
    @Override
    public void rate(RatingDTO rating) throws NotFoundException {
        UserEntity user = userRepository.findByEmail(rating.email())
                .orElseThrow(() -> new NotFoundException("Email not found!"));
        user.setRating(rating.rating());
        userRepository.save(user);
    }

    /**
     * Blocks a user account associated with the provided email address.
     * This method retrieves the user from the user repository based on the provided email.
     * If the user is found, their account status is updated to indicate that they are fired or blocked.
     * The updated user entity is then saved back to the user repository.
     *
     * @param email The email address of the user whose account is to be blocked.
     *              It should be a valid email address associated with a user account.
     * @throws NotFoundException If the user specified by the email address is not found in the {@link UserRepository}.
     *                           This exception indicates that the specified user does not exist.
     */
    @Override
    public void block(String email) throws NotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Email not found!"));
        user.setFired(true);
        userRepository.save(user);
    }

    /**
     * Sets the harvest rate for a product based on the provided {@link HarvestRateDTO}.
     * This method retrieves the product from the product repository based on the product name
     * provided in the HarvestRateDTO. If the product is found, a new HarvestRateEntity is created
     * and populated with the product and harvest rate amount from the provided HarvestRateDTO.
     * The created HarvestRateEntity is then saved to the harvest rate repository.
     *
     * @param harvestRate The {@link HarvestRateDTO} containing information about the harvest rate to be set.
     *                    It should include the name of the product and the amount of harvest rate.
     * @throws NotFoundException If the product specified in the {@link HarvestRateDTO} is not found in the {@link ProductRepository}.
     *                           This exception indicates that the specified product does not exist.
     */
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
