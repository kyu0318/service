package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "disaster_tab") // MySQL disaster_tab 테이블과 1:1 매핑
public class DisasterTab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 구분번호 (PK)

    @Column(nullable = false, length = 50)
    private String name; // 탭 표시 명칭 (지진 대피소, 수해 대피소 등)

    @Column(name = "table_name", nullable = false, length = 50)
    private String tableName; // 매핑된 테이블 식별자 (earthquake, flood, airstrike 등)

    @Column(name = "is_active", nullable = false)
    private boolean active; // 활성화 여부 (true: 노출, false: 숨김)
}