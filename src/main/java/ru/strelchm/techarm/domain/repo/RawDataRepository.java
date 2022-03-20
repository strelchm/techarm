package ru.strelchm.techarm.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.strelchm.techarm.domain.RawData;
import ru.strelchm.techarm.domain.RawDataStatus;
import ru.strelchm.techarm.domain.User;
import ru.strelchm.techarm.domain.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RawDataRepository extends JpaRepository<RawData, UUID> {
    List<RawData> findByDeviceId(UUID deviceId);

    List<RawData> findByUserId(UUID deviceId);

    List<RawData> findByStatus(RawDataStatus status);
}