package org.example.repository;

import org.example.entity.Groups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupsRepository extends JpaRepository<Groups, Long> {
    Optional<Groups> findByTitle(String title);
}
