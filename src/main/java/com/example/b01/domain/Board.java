package com.example.b01.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment를 쓰겠다는 것.
//    Sequence -> DB를 oracle을 쓰면 Sequence를 쓴다(지금은 maria DB이기 때문에 X)
    private Long bno;

    @Column(length = 500, nullable = false) // 컬럼의 길이와 null 허용 여부
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    public void change(String title, String content) {
        this.title =title;
        this.content = content;
    }
}
