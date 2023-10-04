package com.myapp.security.services;

import com.myapp.security.feedback.Feedback;

import java.util.List;

public interface FeedbackService {
    List<Feedback> getByEmail(String email);
    List<Feedback> getAll();
    Feedback create(Feedback feedback);
    Feedback delete(Feedback feedback);
}
