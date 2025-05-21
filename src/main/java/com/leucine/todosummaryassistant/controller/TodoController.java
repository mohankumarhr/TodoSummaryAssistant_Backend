package com.leucine.todosummaryassistant.controller;


import com.leucine.todosummaryassistant.model.Todo;
import com.leucine.todosummaryassistant.repository.TodoRepository;
import com.leucine.todosummaryassistant.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class TodoController {

  @Autowired
  TodoRepository todoRepository;

  @Autowired
  SummaryService summaryService;

  @GetMapping("/todos")
  public List<Todo> getTodos() {
    return todoRepository.findAll();
  }

  @PostMapping("/todos")
  public Todo createTodo(@RequestBody Todo todo) {
    return todoRepository.save(todo);
  }

  @DeleteMapping("/todos/{id}")
  public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
    todoRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }


  @GetMapping("/summary")
  public ResponseEntity<?> getSummary() {
    Map<String, Object> result = summaryService.summarizeAndSendToSlack();
    return ResponseEntity.ok(result);
  }
}

