package org.example.expert.domain.log.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "logs")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String todoTitle;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Log(String userName, String todoTitle) {
        this.userName = userName;
        this.todoTitle = todoTitle;
    }
}
