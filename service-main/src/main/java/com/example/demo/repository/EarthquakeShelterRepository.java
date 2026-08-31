package com.example.demo.repository;

import com.example.demo.domain.EarthquakeShelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EarthquakeShelterRepository extends JpaRepository<EarthquakeShelter, Long> {

    /**
     * 💡 [지진 대피소 바운딩 박스 범위 검색 최종본]
     * 엔티티의 실제 필드(e.lat, e.lot)를 기준으로 화면 사각 영역 내 대피소만 추출합니다.
     */
    @Query("SELECT e FROM EarthquakeShelter e WHERE e.lat BETWEEN :minLat AND :maxLat AND e.lot BETWEEN :minLng AND :maxLng")
    List<EarthquakeShelter> findSheltersInBounds(
            @Param("minLat") Double minLat,
            @Param("maxLat") Double maxLat,
            @Param("minLng") Double minLng,
            @Param("maxLng") Double maxLng
    );
}