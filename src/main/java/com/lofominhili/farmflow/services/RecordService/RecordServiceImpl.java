package com.lofominhili.farmflow.services.RecordService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lofominhili.farmflow.dto.RequestDTO.StatisticByFarmRequestDTO;
import com.lofominhili.farmflow.dto.RequestDTO.StatisticByUserRequestDTO;
import com.lofominhili.farmflow.dto.ResponseDTO.StatisticByFarmResponseDTO;
import com.lofominhili.farmflow.dto.ResponseDTO.StatisticByUserResponseDTO;
import com.lofominhili.farmflow.entities.RecordEntity;
import com.lofominhili.farmflow.entities.UserEntity;
import com.lofominhili.farmflow.exceptions.NotFoundException;
import com.lofominhili.farmflow.repository.RecordRepository;
import com.lofominhili.farmflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {
    @Value(value = "${record-service.admin-email}")
    private String adminEmail;
    @Value(value = "${spring.mail.username}")
    private String from;

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final ObjectMapper objectMapper;

    @Override
    public List<StatisticByUserResponseDTO> getProductStatisticsByUser(StatisticByUserRequestDTO statisticByUserRequest) throws NotFoundException {
        UserEntity user = userRepository.findByEmail(statisticByUserRequest.email()).orElseThrow(() -> new NotFoundException("User with this email was not found!"));
        List<RecordEntity> records = recordRepository.findAllByUser(user);
        List<RecordEntity> sortedRecordsByDate = records.stream()
                .filter(record -> record.getDate().isAfter(statisticByUserRequest.begin().minusDays(1))
                        && record.getDate().isBefore(statisticByUserRequest.end().plusDays(1)))
                .toList();
        List<String> productNames = sortedRecordsByDate.stream()
                .map(record -> record.getProduct().getName())
                .distinct()
                .toList();

        return productNames.stream()
                .map(name -> new StatisticByUserResponseDTO(name, sortedRecordsByDate.stream()
                        .filter(record -> record.getProduct().getName().equals(name))
                        .mapToInt(record -> record.getAmount())
                        .sum(), sortedRecordsByDate.stream()
                        .filter(record -> record.getProduct().getName().equals(name))
                        .findAny().get().getProduct().getMeasure())).toList();
    }

    @Override
    public List<StatisticByFarmResponseDTO> getProductStatisticsByFarm(StatisticByFarmRequestDTO statisticByFarmRequest) {
        List<RecordEntity> records = recordRepository.findAll();
        List<RecordEntity> sortedRecordsByDate = records.stream()
                .filter(record -> record.getDate().isAfter(statisticByFarmRequest.begin().minusDays(1))
                        && record.getDate().isBefore(statisticByFarmRequest.end().plusDays(1)))
                .toList();
        List<String> productNames = sortedRecordsByDate.stream()
                .map(record -> record.getProduct().getName())
                .distinct()
                .toList();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        sortedRecordsByDate.stream()
                .forEach(record -> map.add(record.getProduct().getName(), record.getUser().getEmail()));
        return productNames.stream()
                .map(name -> new StatisticByFarmResponseDTO(name, map.get(name), sortedRecordsByDate.stream()
                        .filter(record -> record.getProduct().getName().equals(name))
                        .mapToInt(record -> record.getAmount())
                        .sum(), sortedRecordsByDate.stream()
                        .filter(record -> record.getProduct().getName().equals(name))
                        .findAny().get().getProduct().getMeasure())).toList();
    }

    @Scheduled(cron = "0 0 21 ? * *")
    @Override
    public void sendStatisticByEmail() throws JsonProcessingException {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        StatisticByFarmRequestDTO statisticByFarmRequestDTO = new StatisticByFarmRequestDTO(LocalDate.now(), LocalDate.now());
        simpleMailMessage.setTo(adminEmail);
        simpleMailMessage.setSubject("Daily statistic");
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setText(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(getProductStatisticsByFarm(statisticByFarmRequestDTO)));
        javaMailSender.send(simpleMailMessage);
    }
}
