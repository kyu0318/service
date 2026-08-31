package com.example.demo.repository;



import com.example.demo.domain.AirRaidShelter;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;



import java.math.BigDecimal;

import java.util.List;



// 1. Spring Data JPA의 Repository 인터페이스임을 명시합니다.

@Repository

// 2. JpaRepository<엔티티 타입, 기본키 타입>을 상속받습니다.

public interface AirRaidShelterRepository extends JpaRepository<AirRaidShelter, Long> {



    /**

     * 💡 [공습 대피소 바운딩 박스 범위 검색 쿼리 메서드]

     * AirRaidShelter 엔티티의 lat(위도)과 lot(경도) 필드를 기준으로 사각 영역 조회를 수행합니다.

     *

     * SQL 변환:

     * SELECT * FROM airstrike

     * WHERE lat BETWEEN :minLat AND :maxLat

     *   AND lot BETWEEN :minLot AND :maxLot

     *

     * @param minLat 최소 위도 (남쪽 경계)

     * @param maxLat 최대 위도 (북쪽 경계)

     * @param minLot 최소 경도 (서쪽 경계)

     * @param maxLot 최대 경도 (동쪽 경계)

     * @return 현재 화면 영역 안에 포함된 공습 대피소 리스트

     */

    List<AirRaidShelter> findByLatBetweenAndLotBetween(

            BigDecimal minLat, BigDecimal maxLat,

            BigDecimal minLot, BigDecimal maxLot

    );

}

