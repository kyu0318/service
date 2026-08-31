package com.example.demo.service;

import com.example.demo.domain.AirRaidShelter;
import com.example.demo.repository.AirRaidShelterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 1. 비즈니스 로직을 처리하는 서비스 컴포넌트 선언
@Service
// 2. final 필드에 대한 생성자를 자동으로 생성하여 의존성(DI) 주입 (Lombok)
@RequiredArgsConstructor
// 3. 읽기 전용 트랜잭션 설정 (조회 성능 최적화 및 의도치 않은 데이터 변경 방지)
@Transactional(readOnly = true)
public class AirRaidShelterService {

    // DB 접근을 담당하는 민방공 대피소 리포지토리 주입
    private final AirRaidShelterRepository airRaidShelterRepository;

    /**
     * 💡 1. 민방공 대피소 전체 목록 조회
     */
    public List<AirRaidShelter> findAllAirRaidShelters() {
        // JPA의 기본 제공 메서드로 테이블의 전체 행 조회
        return airRaidShelterRepository.findAll();
    }

    /**
     * 💡 2. 지도 화면 사각 영역(위도/경도 범위) 내 민방공 대피소 목록 조회
     */
    public List<AirRaidShelter> findSheltersInBounds(Double minLat, Double maxLat, Double minLng, Double maxLng) {
        // 리포지토리에서 정의한 커스텀 범위 검색(BETWEEN) 메서드 호출
        return airRaidShelterRepository.findSheltersInBounds(minLat, maxLat, minLng, maxLng);
    }

    /**
     * 💡 3. 특정 민방공 대피소 단건 상세 조회
     */
    public AirRaidShelter findShelterById(Long id) {
        // ID로 조회 후, 데이터가 없으면 예외(IllegalArgumentException) 발생
        return airRaidShelterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 대피소가 존재하지 않습니다. id=" + id));
    }
}