package org.example.expert.domain.comment.repository;

import org.example.expert.domain.comment.entity.Comment;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepositoryQuery {
    List<Comment> findByTodoIdWithUser(@Param("todoId") Long todoId);
}