package com.lofominhili.farmflow.controllers;

import com.lofominhili.farmflow.dto.BasicDTO.SuccessDTO;
import com.lofominhili.farmflow.dto.EntityDTO.HarvestRateDTO;
import com.lofominhili.farmflow.dto.EntityDTO.RatingDTO;
import com.lofominhili.farmflow.dto.RequestDTO.StatisticByUserRequestDTO;
import com.lofominhili.farmflow.dto.RequestDTO.StatisticsByFarmRequestDTO;
import com.lofominhili.farmflow.dto.ResponseDTO.StatisticByUserResponseDTO;
import com.lofominhili.farmflow.dto.ResponseDTO.StatisticsByFarmResponseDTO;
import com.lofominhili.farmflow.exceptions.NotFoundException;
import com.lofominhili.farmflow.exceptions.RequestDataValidationFailedException;
import com.lofominhili.farmflow.services.AdminService.AdminService;
import com.lofominhili.farmflow.services.RecordService.RecordService;
import com.lofominhili.farmflow.utils.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling administrative operations.
 * This controller provides endpoints for rating users, retrieving product statistics by user and farm,
 * blocking users, and setting harvest rates.
 * This controller is mapped to "/api/admin" base path.
 * It requires instances of {@link AdminService} and {@link RecordService} to be injected via constructor.
 *
 * @author daniel
 */
@Tag(name = "AdminController", description = "Controller class for admin operations")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final RecordService recordService;

    /**
     * Endpoint for rating a user.
     * This method validates the incoming {@link RatingDTO} using {@link Valid} annotation.
     * If validation fails, it throws a {@link RequestDataValidationFailedException}.
     * Otherwise, it delegates the rating operation to {@link AdminService#rate(RatingDTO)}.
     *
     * @param ratingDTO        The {@link RatingDTO} containing information about the user's email and rating.
     * @param validationResult The result of validation performed by Spring's {@link BindingResult}.
     * @return A {@link ResponseEntity} containing a success message if the rating operation is successful.
     * @throws RequestDataValidationFailedException If the incoming data fails validation.
     * @throws NotFoundException                    If the user specified in the ratingDTO is not found.
     */
    @Operation(summary = "Rates user")
    @PostMapping("/rate")
    public ResponseEntity<SuccessDTO<String>> rate(
            @Valid @RequestBody RatingDTO ratingDTO,
            BindingResult validationResult
    ) throws RequestDataValidationFailedException, NotFoundException {
        if (validationResult.hasErrors()) {
            throw new RequestDataValidationFailedException(GlobalExceptionHandler.handleValidationResults(validationResult));
        }
        adminService.rate(ratingDTO);
        return new ResponseEntity<>(
                new SuccessDTO<>(
                        HttpStatus.OK.value(),
                        "rate",
                        "Successfully rated!"
                ), HttpStatus.OK);
    }

    /**
     * Endpoint for retrieving product statistics by user.
     * This method validates the incoming {@link StatisticByUserRequestDTO} using {@link Valid} annotation.
     * If validation fails, it throws a {@link RequestDataValidationFailedException}.
     * Otherwise, it delegates the retrieval operation to {@link RecordService#getProductStatisticsByUser(StatisticByUserRequestDTO)}.
     *
     * @param statisticByUserRequestDTO The {@link StatisticByUserRequestDTO} containing information about the user's email and date range.
     * @param validationResult          The result of validation performed by Spring's {@link BindingResult}.
     * @return A {@link ResponseEntity} containing a list of {@link StatisticByUserResponseDTO} if the retrieval operation is successful.
     * @throws RequestDataValidationFailedException If the incoming data fails validation.
     * @throws NotFoundException                    If the user specified in the statisticByUserRequestDTO is not found.
     */
    @Operation(summary = "Gets products statistic by user")
    @GetMapping("/get-statistics-by-user")
    public ResponseEntity<SuccessDTO<List<StatisticByUserResponseDTO>>> getProductStatisticByUser(
            @Valid @RequestBody StatisticByUserRequestDTO statisticByUserRequestDTO,
            BindingResult validationResult
    ) throws RequestDataValidationFailedException, NotFoundException {
        if (validationResult.hasErrors()) {
            throw new RequestDataValidationFailedException(GlobalExceptionHandler.handleValidationResults(validationResult));
        }
        return new ResponseEntity<>(
                new SuccessDTO<>(
                        HttpStatus.OK.value(),
                        "get statistics",
                        recordService.getProductStatisticsByUser(statisticByUserRequestDTO)
                ), HttpStatus.OK);
    }

    /**
     * Endpoint for retrieving product statistics by farm.
     * This method validates the incoming {@link StatisticsByFarmRequestDTO} using {@link Valid} annotation.
     * If validation fails, it throws a {@link RequestDataValidationFailedException}.
     * Otherwise, it delegates the retrieval operation to {@link RecordService#getProductStatisticsByFarm(StatisticsByFarmRequestDTO)}.
     *
     * @param statisticsByFarmRequestDTO The {@link StatisticsByFarmRequestDTO} containing information about the date range for retrieving statistics.
     * @param validationResult           The result of validation performed by Spring's {@link BindingResult}.
     * @return A {@link ResponseEntity} containing a list of {@link StatisticsByFarmResponseDTO} if the retrieval operation is successful.
     * @throws RequestDataValidationFailedException If the incoming data fails validation.
     */
    @Operation(summary = "Gets products statistics by farm")
    @GetMapping("/get-statistics-by-farm")
    public ResponseEntity<SuccessDTO<List<StatisticsByFarmResponseDTO>>> getProductStatisticsByFarm(
            @Valid @RequestBody StatisticsByFarmRequestDTO statisticsByFarmRequestDTO,
            BindingResult validationResult
    ) throws RequestDataValidationFailedException {
        if (validationResult.hasErrors()) {
            throw new RequestDataValidationFailedException(GlobalExceptionHandler.handleValidationResults(validationResult));
        }
        return new ResponseEntity<>(
                new SuccessDTO<>(
                        HttpStatus.OK.value(),
                        "get statistics",
                        recordService.getProductStatisticsByFarm(statisticsByFarmRequestDTO)
                ), HttpStatus.OK);
    }

    /**
     * Endpoint for blocking a user.
     * This method delegates the blocking operation to {@link AdminService#block(String)}.
     *
     * @param email The email address of the user to be blocked.
     * @return A {@link ResponseEntity} containing a success message if the blocking operation is successful.
     * @throws NotFoundException If the user specified by the email is not found.
     */
    @Operation(summary = "Blocks user by email")
    @PostMapping("/block/{email}")
    public ResponseEntity<SuccessDTO<String>> block(@PathVariable @Parameter(description = "The email address of the user to be blocked") String email) throws NotFoundException {
        adminService.block(email);
        return new ResponseEntity<>(
                new SuccessDTO<>(
                        HttpStatus.OK.value(),
                        "block",
                        "Successfully blocked!"
                ), HttpStatus.OK);
    }

    /**
     * Endpoint for setting the harvest rate for a product.
     * This method validates the incoming {@link HarvestRateDTO} using {@link Valid} annotation.
     * If validation fails, it throws a {@link RequestDataValidationFailedException}.
     * Otherwise, it delegates the setting operation to {@link AdminService#setHarvestRate(HarvestRateDTO)}.
     *
     * @param harvestRateDTO   The {@link HarvestRateDTO} containing information about the product name and harvest rate amount.
     * @param validationResult The result of validation performed by Spring's {@link BindingResult}.
     * @return A {@link ResponseEntity} containing a success message if the setting operation is successful.
     * @throws RequestDataValidationFailedException If the incoming data fails validation.
     * @throws NotFoundException                    If the product specified in the harvestRateDTO is not found.
     */
    @Operation(summary = "Sets harvest rate for product")
    @PostMapping("/set-harvest-rate")
    public ResponseEntity<SuccessDTO<String>> setHarvestRate(
            @Valid @RequestBody HarvestRateDTO harvestRateDTO,
            BindingResult validationResult
    ) throws RequestDataValidationFailedException, NotFoundException {
        if (validationResult.hasErrors()) {
            throw new RequestDataValidationFailedException(GlobalExceptionHandler.handleValidationResults(validationResult));
        }
        adminService.setHarvestRate(harvestRateDTO);
        return new ResponseEntity<>(
                new SuccessDTO<>(
                        HttpStatus.OK.value(),
                        "set harvest rate",
                        "Successfully set!"
                ), HttpStatus.OK);
    }
}
