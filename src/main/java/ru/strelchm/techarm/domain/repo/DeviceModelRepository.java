package ru.strelchm.techarm.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.strelchm.techarm.domain.DeviceModel;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceModelRepository extends JpaRepository<DeviceModel, UUID>, JpaSpecificationExecutor<DeviceModel> {
    Optional<DeviceModel> findByName(String name);
}