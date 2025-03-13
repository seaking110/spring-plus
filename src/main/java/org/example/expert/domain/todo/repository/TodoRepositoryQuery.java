package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface TodoRepositoryQuery {
    Page<TodoSearchResponse> searchTodos(Pageable pageable, String title, LocalDateTime start, LocalDateTime end, String nickName);
}
