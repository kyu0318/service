package com.example.demo.controller;

import com.example.demo.domain.DisasterTab;
import com.example.demo.repository.DisasterTabRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disaster-tabs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DisasterTabController {

    private final DisasterTabRepository disasterTabRepository;

    /**
     * 1. [사용자 화면용] 현재 활성화(is_active = 1)된 탭 목록만 반환
     * 호출: GET /api/disaster-tabs/active
     */
    @GetMapping("/active")
    public ResponseEntity<List<DisasterTab>> getActiveTabs() {
        return ResponseEntity.ok(disasterTabRepository.findByActiveTrueOrderByIdAsc());
    }

    /**
     * 2. [관리자용] 전체 탭 목록 반환
     * 호출: GET /api/disaster-tabs/all
     */
    @GetMapping("/all")
    public ResponseEntity<List<DisasterTab>> getAllTabs() {
        return ResponseEntity.ok(disasterTabRepository.findAll());
    }

    /**
     * 3. [관리자용] 탭 활성화/비활성화 상태 변경
     * 호출: PATCH /api/disaster-tabs/{id}/status?active=false
     */
    @PatchMapping("/{id}/status")
    @Transactional // 💡 변경 감지(Dirty Checking)로 안전하게 DB 커밋
    public ResponseEntity<String> updateTabStatus(
            @PathVariable("id") Long id,
            @RequestParam("active") boolean active
    ) {
        DisasterTab tab = disasterTabRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 탭이 존재하지 않습니다. id=" + id));

        tab.setActive(active);
        // @Transactional 안에서는 dirty checking으로 자동 UPDATE되지만 명시적 save도 무방합니다.
        disasterTabRepository.save(tab);

        return ResponseEntity.ok("상태 변경 완료 (ID: " + id + ", active: " + active + ")");
    }
}