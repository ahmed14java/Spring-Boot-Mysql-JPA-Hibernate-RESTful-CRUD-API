package com.demo.controller;

import com.demo.exception.ResourceNotFoundException;
import com.demo.model.Question;
import com.demo.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/questions")
    public Page<Question> getQuestions(Pageable pageable){
        Page<Question> questions = questionRepository.findAll(pageable);
        return questions;
    }

    @PostMapping("/questions")
    public Question createQuestion(@Valid @RequestBody Question question){
        return questionRepository.save(question);
    }

    @PutMapping("/questions/{questionId}")
    public Question updateQuestion(@PathVariable("questionId") Long questionId , @Valid @RequestBody Question questionRequest) {
            return questionRepository.findById(questionId).map(
                    question -> {
                        question.setTitle(questionRequest.getTitle());
                        question.setDescription(questionRequest.getDescription());
                        return questionRepository.save(question);
                    }
            ).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + questionId));
    }
    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable("questionId") Long questionId ){
        return questionRepository.findById(questionId)
                    .map(question -> {
                        questionRepository.delete(question);
                        return ResponseEntity.ok().build();
                    }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + questionId));
    }
}
