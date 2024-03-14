package com.lofominhili.farmflow.services.AdminService;

import com.lofominhili.farmflow.dto.HarvestRateDTO;
import com.lofominhili.farmflow.dto.RatingDTO;
import com.lofominhili.farmflow.exceptions.NotFoundException;

public interface AdminService {
    void rate(RatingDTO rating) throws NotFoundException;

    void blockUser(String email) throws NotFoundException;

    void setHarvestRate(HarvestRateDTO harvestRate) throws NotFoundException;
}
