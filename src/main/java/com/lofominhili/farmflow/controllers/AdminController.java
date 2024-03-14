package com.lofominhili.farmflow.controllers;

import com.lofominhili.farmflow.dto.BasicDTO.SuccessDTO;
import com.lofominhili.farmflow.dto.HarvestRateDTO;
import com.lofominhili.farmflow.dto.RatingDTO;
import com.lofominhili.farmflow.dto.RequestDTO.StatisticByFarmRequestDTO;
import com.lofominhili.farmflow.dto.RequestDTO.StatisticByUserRequestDTO;
import com.lofominhili.farmflow.dto.ResponseDTO.StatisticByFarmResponseDTO;
import com.lofominhili.farmflow.dto.ResponseDTO.StatisticByUserResponseDTO;
import com.lofominhili.farmflow.exceptions.NotFoundException;
import com.lofominhili.farmflow.exceptions.RequestDataValidationFailedException;
import com.lofominhili.farmflow.services.AdminService.AdminService;
import com.lofominhili.farmflow.services.RecordService.RecordService;
import com.lofominhili.farmflow.utils.GlobalExceptionHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final RecordService recordService;

    @PostMapping("/rate")
    public ResponseEntity<SuccessDTO<String>> rateUser(
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

    @PostMapping("/get-statistics-by-user")
    public ResponseEntity<SuccessDTO<List<StatisticByUserResponseDTO>>> getProductStatisticsByUser(
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

    @PostMapping("/get-statistics-by-farm")
    public ResponseEntity<SuccessDTO<List<StatisticByFarmResponseDTO>>> getProductStatisticsByFarm(
            @Valid @RequestBody StatisticByFarmRequestDTO statisticByFarmRequestDTO,
            BindingResult validationResult
    ) throws RequestDataValidationFailedException {
        if (validationResult.hasErrors()) {
            throw new RequestDataValidationFailedException(GlobalExceptionHandler.handleValidationResults(validationResult));
        }
        return new ResponseEntity<>(
                new SuccessDTO<>(
                        HttpStatus.OK.value(),
                        "get statistics",
                        recordService.getProductStatisticsByFarm(statisticByFarmRequestDTO)
                ), HttpStatus.OK);
    }

    @PostMapping("/block-user/{email}")
    public ResponseEntity<SuccessDTO<String>> blockUser(@PathVariable String email) throws NotFoundException {
        adminService.blockUser(email);
        return new ResponseEntity<>(
                new SuccessDTO<>(
                        HttpStatus.OK.value(),
                        "block",
                        "Successfully blocked!"
                ), HttpStatus.OK);
    }

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
