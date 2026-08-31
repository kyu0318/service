package com.example.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "earthquake")
public class EarthquakeShelter {

    // 💡 문자열 ID(예: 'EQ_0_0')와 매핑 완료
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