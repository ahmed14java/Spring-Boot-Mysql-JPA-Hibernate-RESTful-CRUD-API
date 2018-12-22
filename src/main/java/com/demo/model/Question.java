package com.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question extends DateAudit{

    @Id
    @GeneratedValue(generator = "question_generator")
    @SequenceGenerator(name = "question_generator" , sequenceName = "question_sequence" , initialValue = 1000)
    private Long id;

    @NotBlank
    @Size(min = 3 , max = 100)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @OneToMany(mappedBy = "question")
    @JsonIgnore
    private List<Answer> answers ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
