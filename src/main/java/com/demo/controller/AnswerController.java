package com.demo.controller;

import com.demo.exception.ResourceNotFoundException;
import com.demo.model.Answer;
import com.demo.repository.AnswerRepository;
import com.demo.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AnswerController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @GetMapping("/question/{questionId}/answers")
    public ResponseEntity<List<Answer>> getAnswerByQuestionId(@PathVariable Long questionId ) {
        List<Answer> answers = answerRepository.findByQuestionId(questionId);
        return new ResponseEntity<List<Answer>>(answers , HttpStatus.OK);
    }

    @PostMapping("/question/{questionId}/answers")
    public Answer addAnswer(@PathVariable Long questionId , @Valid @RequestBody Answer answer) {
        return questionRepository.findById(questionId)
                            .map(question -> {
                                answer.setQuestion(question);
                                return answerRepository.save(answer);
                            }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + questionId));
    }

    @PutMapping("/questions/{questionId}/answers/{answerId}")
    public Answer updateAnswer(@PathVariable Long questionId ,@PathVariable Long answerId ,@Valid @RequestBody Answer answerRequest) {
        if (!questionRepository.existsById(questionId)){
            throw new ResourceNotFoundException("Question not found with id: " + questionId);
        }
        return answerRepository.findById(answerId)
                    .map(answer -> {
                        answer.setText(answerRequest.getText());
                        return answerRepository.save(answer);
                    }).orElseThrow(() -> new ResourceNotFoundException("Answer not found with id:" + answerId));
    }

    @DeleteMapping("/questions/{questionId}/answers/{answerId}")
    public ResponseEntity<?> deleteAnswer(@PathVariable Long questionId ,@PathVariable Long answerId) {
        if (!questionRepository.existsById(questionId)){
            throw new ResourceNotFoundException("Question not found with id: " + questionId);
        }
       return answerRepository.findById(answerId)
                .map(answer -> {
                    answerRepository.delete(answer);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Answer not found with id:" + answerId));
    }

}
