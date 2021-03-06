package engine.controller;

import engine.QuizAnswer;
import engine.QuizResult;
import engine.entity.Quiz;
import engine.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;

    @GetMapping(path = "/api/quizzes/{id}")
    public Quiz getQuestion(@PathVariable int id) {
        return quizRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/api/quizzes")
    public List<Quiz> getAllQuestions() {
        return quizRepository.findAll();
    }

    @PostMapping(path = "/api/quizzes/{id}/solve")
    public QuizResult checkAnswer(@RequestBody QuizAnswer guess, @PathVariable int id) {
        Quiz question = quizRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (question.isCorrect(guess.getAnswer())) {
            return QuizResult.CORRECT_ANSWER;
        } else {
            return QuizResult.WRONG_ANSWER;
        }
    }

    @PostMapping(path = "/api/quizzes")
    public Quiz addQuestion(@RequestBody @Valid Quiz quiz, Principal principal) {
        quiz.setAuthor(principal.getName());
        return quizRepository.save(quiz);
    }

    @DeleteMapping(path = "/api/quizzes/{id}")
    public ResponseEntity<String> deleteQuiz(@PathVariable int id, Principal principal) {
//        Quiz quiz = quizRepository.findById(id).orElseThrow(QuizNotFoundException::new);
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!quiz.getAuthor().equals(principal.getName())) {
            return new ResponseEntity<>("User is not the author of the quiz", HttpStatus.FORBIDDEN);
        }
        quizRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}