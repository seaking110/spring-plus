package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryQueryImpl implements TodoRepositoryQuery{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<TodoSearchResponse> searchTodos(Pageable pageable, String title, LocalDateTime start, LocalDateTime end, String nickName) {
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;
        QComment comment = QComment.comment;
        BooleanBuilder builder = new BooleanBuilder();

        if (title != null) {
            builder.and(todo.title.eq(title));
        }

        if (start != null) {
            builder.and(todo.createdAt.after(start));
        }

        if (end != null) {
            builder.and(todo.createdAt.before(end));
        }

        if (nickName != null) {
            builder.and(todo.user.nickName.eq(nickName));
        }

        List<Todo> todos = jpaQueryFactory
                .selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin()
                .leftJoin(todo.comments, comment).fetchJoin()
                .where(builder)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        List<TodoSearchResponse> list = new ArrayList<>();
        for (Todo t : todos) {
            list.add(new TodoSearchResponse(
                    t.getId(),
                    t.getTitle(),
                    t.getManagers().size(),
                    t.getComments().size()
            ));
        }

        Long total = jpaQueryFactory
                .select(todo.count())
                .from(todo)
                .where(builder)
                .fetchOne();

        long totalCount = (total != null) ? total : 0L;

        return new PageImpl<>(list, pageable, totalCount);
    }
}
