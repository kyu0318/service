package com.example.demo.repository;

import com.example.demo.domain.DisasterTab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisasterTabRepository extends JpaRepository<DisasterTab, Long> {

    // 💡 사용여부가 true인 항목만 id 순서대로 조회하는 쿼리 메서드
    List<DisasterTab> findByActiveTrueOrderByIdAsc();
}
