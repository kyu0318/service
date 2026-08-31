package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "disaster_tab") // MySQL의 disaster_tab 테이블과 1:1 매핑
public class DisasterTab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 구분번호

    @Column(nullable = false, length = 50)
    private String name; // 이름 (지진 대피소 등)

    @Column(name = "table_name", nullable = false, length = 50)
    private String tableName; // 연결된 테이블 이름 (earthquake 등)

    @Column(name = "is_active", nullable = false)
    private boolean active; // 사용여부 (true / false)
}