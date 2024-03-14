package com.lofominhili.farmflow.repository;

import com.lofominhili.farmflow.entities.RecordEntity;
import com.lofominhili.farmflow.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<RecordEntity, Long> {
    List<RecordEntity> findAllByUser(UserEntity user);

}
