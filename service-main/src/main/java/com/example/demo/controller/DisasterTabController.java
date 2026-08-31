package com.example.demo.controller;

import com.example.demo.domain.DisasterTab;
import com.example.demo.repository.DisasterTabRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disaster-tabs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // 프론트엔드 연동 CORS 허용
public class DisasterTabController {

    private final DisasterTabRepository disasterTabRepository;

    /**
     * 1. 💡 [사용자 화면용] 현재 활성화(is_active = 1)된 탭 목록만 반환
     * 호출: GET http://localhost:8080/api/disaster-tabs/active
     */
    @GetMapping("/active")
    public ResponseEntity<List<DisasterTab>> getActiveTabs() {
        return ResponseEntity.ok(disasterTabRepository.findByActiveTrueOrderByIdAsc());
    }

    /**
     * 2. 💡 [관리자 Admin용] 전체 탭 목록 반환 (활성/비활성 스위치 표시용)
     * 호출: GET http://localhost:8080/api/disaster-tabs/all
     */
    @GetMapping("/all")
    public ResponseEntity<List<DisasterTab>> getAllTabs() {
        return ResponseEntity.ok(disasterTabRepository.findAll());
    }

    /**
     * 3. 💡 [관리자 Admin용] 탭 활성화/비활성화 상태 즉시 변경
     * 호출: PATCH http://localhost:8080/api/disaster-tabs/{id}/status?active=false
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateTabStatus(
            @PathVariable("id") Long id,
            @RequestParam("active") boolean active
    ) {
        DisasterTab tab = disasterTabRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 탭이 존재하지 않습니다. id=" + id));

        tab.setActive(active);
        disasterTabRepository.save(tab); // DB 상태 즉시 반영

        return ResponseEntity.ok("상태 변경 완료: " + active);
    }
}