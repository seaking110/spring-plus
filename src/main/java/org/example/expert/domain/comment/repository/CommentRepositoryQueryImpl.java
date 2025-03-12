package org.example.expert.domain.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CommentRepositoryQueryImpl implements CommentRepositoryQuery{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Comment> findByTodoIdWithUser(Long todoId) {
        QComment comment = QComment.comment;
        QUser user = QUser.user;

        return jpaQueryFactory
                .selectFrom(comment)
                .join(comment.user, user).fetchJoin()
                .where(comment.todo.id.eq(todoId))
                .fetch();
    }
}
