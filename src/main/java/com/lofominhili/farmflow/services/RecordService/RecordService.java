package com.lofominhili.farmflow.services.RecordService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lofominhili.farmflow.dto.RequestDTO.StatisticByUserRequestDTO;
import com.lofominhili.farmflow.dto.RequestDTO.StatisticsByFarmRequestDTO;
import com.lofominhili.farmflow.dto.ResponseDTO.StatisticByUserResponseDTO;
import com.lofominhili.farmflow.dto.ResponseDTO.StatisticsByFarmResponseDTO;
import com.lofominhili.farmflow.exceptions.NotFoundException;

import java.util.List;

public interface RecordService {
    List<StatisticByUserResponseDTO> getProductStatisticsByUser(StatisticByUserRequestDTO statisticByUserRequest) throws NotFoundException;

    List<StatisticsByFarmResponseDTO> getProductStatisticsByFarm(StatisticsByFarmRequestDTO statisticByFarmRequest);

    void sendStatisticByEmail() throws JsonProcessingException;

}
