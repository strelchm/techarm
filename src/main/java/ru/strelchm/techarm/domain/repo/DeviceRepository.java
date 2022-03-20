package ru.strelchm.techarm.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.strelchm.techarm.domain.Device;
import ru.strelchm.techarm.domain.User;
import ru.strelchm.techarm.domain.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {
    Optional<Device> findByName(String login);
}