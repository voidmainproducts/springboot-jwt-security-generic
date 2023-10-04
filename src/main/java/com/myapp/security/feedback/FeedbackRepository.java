package com.myapp.security.feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    Optional<List<Feedback>> findByEmail(String email);
    List<Feedback> findAll();
    Feedback save(Feedback feedback);
}
