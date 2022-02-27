package ru.strelchm.techarm.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.strelchm.techarm.domain.User;
import ru.strelchm.techarm.domain.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByLogin(String login);

    List<User> findAllByStatus(UserStatus status);
}