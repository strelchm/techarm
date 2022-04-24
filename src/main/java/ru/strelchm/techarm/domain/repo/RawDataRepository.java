package ru.strelchm.techarm.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.strelchm.techarm.domain.RawData;
import ru.strelchm.techarm.domain.RawDataStatus;
import ru.strelchm.techarm.dto.RawDataStatDto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface RawDataRepository extends JpaRepository<RawData, UUID>, JpaSpecificationExecutor<RawData> {
    List<RawData> findByDeviceId(UUID deviceId);

    List<RawData> findByUserId(UUID deviceId);

    List<RawData> findByStatus(RawDataStatus status);

    @Query("SELECT new ru.strelchm.techarm.dto.RawDataStatDto(rd.status, COUNT(rd), rd.device.id) FROM RawData rd " +
            "WHERE (:start IS NULL OR rd.processedTime > :start) " +
            "AND (:end IS NULL OR rd.processedTime < :end) " +
            "AND (:deviceIds IS NULL OR rd.device.id IN :deviceIds) " +
            "GROUP BY rd.status, rd.device.id")
    List<RawDataStatDto> findStatistics(@Param("start") @Temporal Date start, @Param("end") @Temporal Date end, @Param("deviceIds") List<UUID> deviceIds);
}