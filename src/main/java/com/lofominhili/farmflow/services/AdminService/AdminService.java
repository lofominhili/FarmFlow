package com.lofominhili.farmflow.services.AdminService;

import com.lofominhili.farmflow.dto.EntityDTO.HarvestRateDTO;
import com.lofominhili.farmflow.dto.EntityDTO.RatingDTO;
import com.lofominhili.farmflow.exceptions.NotFoundException;

public interface AdminService {
    void rate(RatingDTO rating) throws NotFoundException;

    void block(String email) throws NotFoundException;

    void setHarvestRate(HarvestRateDTO harvestRate) throws NotFoundException;
}
