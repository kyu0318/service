package com.example.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "flood")
public class FloodShelter {

    // 💡 문자열 ID('FLD_0_0') 수용을 위해 String으로 변경 및 @GeneratedValue 제거
    @Id
    @Column(name = "shlt_id", length = 50)
    private String shlt_id;

    private String ctpv_nm;
    private String sgg_nm;
    private String fclt_nm;
    private String daddr;

    @Column(name = "lot", precision = 10, scale = 7)
    private BigDecimal lot;

    @Column(name = "lat", precision = 10, scale = 7)
    private BigDecimal lat;

    private String mng_dept_nm;
}