package com.myapp.security.controllers;

import com.myapp.security.auth.AuthenticationService;
import com.myapp.security.feedback.Feedback;
import com.myapp.security.services.FeedbackService;
import com.myapp.security.user.User;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/feedback")
@Hidden
public class FeedbackController {

  private final FeedbackService feedbackService;
  private final AuthenticationService authenticationService;

  public FeedbackController(FeedbackService feedbackService, AuthenticationService authenticationService) {
    this.feedbackService = feedbackService;
    this.authenticationService = authenticationService;
  }

  @GetMapping("/hello")
  public ResponseEntity<String> sayHello() {
    return ResponseEntity.ok("Hello from secured endpoint");
  }

  @GetMapping()
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  public ResponseEntity<List<Feedback>> getFeedbacksForCurrentLoggedInUser(HttpServletRequest request) {
    List<Feedback> feedbacks = new ArrayList<>();
    User user = authenticationService.getUserFromRequest(request);
    if(user!= null) {
      feedbacks = feedbackService.getByEmail(user.getEmail());
    }
    return ResponseEntity.ok(feedbacks);
  }
  @GetMapping(value = "/all")
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<List<Feedback>> getAllFeedback() {
    List<Feedback> feedbacks = feedbackService.getAll();
    return ResponseEntity.ok(feedbacks);
  }

  @PostMapping(value = "/create")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public ResponseEntity<Object> create(HttpServletRequest request, @RequestBody Feedback feedback) {
    if(isValidFeedback(feedback)){
      feedback.setTimestamp(LocalDateTime.now());
      User user = authenticationService.getUserFromRequest(request);
      if(user != null) {
        feedback.setEmail(user.getEmail());
        Feedback saved = feedbackService.create(feedback);
        return ResponseEntity.ok(saved);
      }
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("At least one field Rating or comment required");
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong");
  }

  private boolean isValidFeedback(Feedback feedback) {
    return feedback != null && (feedback.getRating() > 0 || (feedback.getComment() != null && !feedback.getComment().isEmpty()));
  }
}
