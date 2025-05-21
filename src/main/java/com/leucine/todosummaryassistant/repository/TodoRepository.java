package com.leucine.todosummaryassistant.repository;

import com.leucine.todosummaryassistant.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
}
