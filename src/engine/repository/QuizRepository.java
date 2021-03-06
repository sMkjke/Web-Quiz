package engine.repository;

import engine.entity.Quiz;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends CrudRepository<Quiz, Integer> {

    Optional<Quiz> findById(Integer id);

    List<Quiz> findAll();

    <T extends Quiz> T save(T question);
}