package com.lofominhili.farmflow.services.RecordService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lofominhili.farmflow.dto.RequestDTO.StatisticByUserRequestDTO;
import com.lofominhili.farmflow.dto.RequestDTO.StatisticsByFarmRequestDTO;
import com.lofominhili.farmflow.dto.ResponseDTO.StatisticByUserResponseDTO;
import com.lofominhili.farmflow.dto.ResponseDTO.StatisticsByFarmResponseDTO;
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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Service implementation of {@link RecordService} for managing records and statistics related to products and users.
 * This service provides methods for retrieving product statistics by user, product statistics by farm,
 * and sending daily statistics via email.
 * This service requires instances of {@link RecordRepository}, {@link UserRepository}, {@link JavaMailSender},
 * and {@link ObjectMapper} to be injected via constructor.
 *
 * @author daniel
 */
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

    /**
     * Retrieves product statistics by user within the specified date range.
     * This method first retrieves the user from the user repository based on the provided email.
     * Then, it retrieves all records associated with that user within the specified date range.
     * It calculates the sum of product amounts for each product and creates response DTOs
     * containing product statistics for the user.
     *
     * @param statisticByUserRequest The {@link StatisticByUserRequestDTO} containing information about the user's email and date range.
     * @return A list of {@link StatisticByUserResponseDTO} objects representing product statistics for the user.
     * @throws NotFoundException If the user specified by the email is not found in the {@link UserRepository}.
     *                           This exception indicates that the specified user does not exist.
     */
    @Override
    public List<StatisticByUserResponseDTO> getProductStatisticsByUser(StatisticByUserRequestDTO statisticByUserRequest) throws NotFoundException {
        UserEntity user = userRepository.findByEmail(statisticByUserRequest.email())
                .orElseThrow(() -> new NotFoundException("User with this email was not found!"));
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
                        .mapToInt(RecordEntity::getAmount)
                        .sum(), sortedRecordsByDate.stream()
                        .filter(record -> record.getProduct().getName().equals(name))
                        .findAny().get().getProduct().getMeasure())).toList();
    }

    /**
     * Retrieves product statistics by farm within the specified date range.
     * This method retrieves all records within the specified date range.
     * It calculates the sum of product amounts for each product, grouping them by product name.
     * The method creates response DTOs containing product statistics for each product and farm.
     *
     * @param statisticByFarmRequest The {@link StatisticsByFarmRequestDTO} containing the date range for retrieving statistics.
     * @return A list of {@link StatisticsByFarmResponseDTO} objects representing product statistics for each product and farm.
     */
    public List<StatisticsByFarmResponseDTO> getProductStatisticsByFarm(StatisticsByFarmRequestDTO statisticByFarmRequest) {
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
                .map(name -> {
                    Set<String> uniqueUsers = new HashSet<>(Objects.requireNonNull(map.get(name)));
                    return new StatisticsByFarmResponseDTO(name, uniqueUsers.stream().toList(), sortedRecordsByDate.stream()
                            .filter(record -> record.getProduct().getName().equals(name))
                            .mapToInt(RecordEntity::getAmount)
                            .sum(), sortedRecordsByDate.stream()
                            .filter(record -> record.getProduct().getName().equals(name))
                            .findAny().get().getProduct().getMeasure());
                }).toList();
    }

    /**
     * Sends daily product statistics via email to the admin.
     * This method is scheduled to run daily at 9:00 PM.
     * It retrieves product statistics by farm for the current date
     * and sends them via email to the admin specified in the application properties.
     *
     * @throws JsonProcessingException If there is an error while serializing the product statistics data to JSON format.
     */
    @Scheduled(cron = "0 0 21 ? * *")
    @Override
    public void sendStatisticByEmail() throws JsonProcessingException {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        StatisticsByFarmRequestDTO statisticsByFarmRequestDTO = new StatisticsByFarmRequestDTO(LocalDate.now(), LocalDate.now());
        simpleMailMessage.setTo(adminEmail);
        simpleMailMessage.setSubject("Daily statistic");
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setText(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(getProductStatisticsByFarm(statisticsByFarmRequestDTO)));
        javaMailSender.send(simpleMailMessage);
    }
}
