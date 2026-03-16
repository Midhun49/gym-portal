package com.gymportal.repository;

import com.gymportal.entity.Membership;
import com.gymportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Optional<Membership> findByUserId(long userId);

    @Modifying
    @Transactional
    void deleteByUserId(long userId);

    java.util.List<Membership> findAllByUserIn(java.util.Collection<User> users);
}
