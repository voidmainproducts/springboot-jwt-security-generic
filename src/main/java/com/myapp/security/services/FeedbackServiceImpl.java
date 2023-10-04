package com.myapp.security.services;

import com.myapp.security.feedback.Feedback;
import com.myapp.security.feedback.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository repository;

    public FeedbackServiceImpl(FeedbackRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Feedback> getByEmail(String email) {
        var feedbacks = repository.findByEmail(email);
        return feedbacks.orElseGet(ArrayList::new);
    }

    @Override
    public List<Feedback> getAll() {
        var feedbacks = repository.findAll();
        if(feedbacks != null) {
            return feedbacks;
        }
        return new ArrayList<>();
    }

    @Override
    public Feedback create(Feedback feedback) {
        return repository.save(feedback);
    }

    @Override
    public Feedback delete(Feedback feedback) {
        return null;
    }
}
