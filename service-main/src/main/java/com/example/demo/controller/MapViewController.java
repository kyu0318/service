package com.example.demo.controller; // 본인의 실제 패키지명에 맞게 유지

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody; // 데이터(JSON) 반환용 어노테이션
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MapViewController {

    // application.properties 에 적힌 kakao.maps.api-key 값을 가져옵니다.
    @Value("${kakao.maps.api-key}")
    private String kakaoApiKey;

    /**
     * 브라우저나 iframe이 '/map' 주소로 요청할 때 동작하는 메서드
     */
    @GetMapping("/map")
    public String mapPage(Model model) {
        // 1. "kakaoKey"라는 이름의 바구니에 서버가 가진 실제 카카오 API 키를 담습니다.
        model.addAttribute("kakaoKey", kakaoApiKey);

        // 2. src/main/resources/templates/map.html 파일을 찾아 타임리프로 렌더링하여 화면을 보여줍니다.
        return "map";
    }

    /**
     * [추가] 프론트엔드의 CORS 에러를 우회하기 위한 백엔드 프록시 API
     */
    @GetMapping("/api/kakao/search")
    @ResponseBody // HTML 화면이 아니라 순수 데이터(JSON)를 브라우저로 바로 쏴주겠다는 뜻!
    public ResponseEntity<?> searchKeyword(@RequestParam("query") String query) {
        // application.properties에 등록된 api-key를 여기서 안전하게 사용하여 카카오 API 호출
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + query;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            Map<String, Object> body = response.getBody();

            if (body != null && body.get("documents") instanceof java.util.List) {
                java.util.List<Map<String, Object>> documents = (java.util.List<Map<String, Object>>) body.get("documents");
                if (!documents.isEmpty()) {
                    Map<String, Object> first = documents.get(0);
                    Map<String, Object> result = new HashMap<>();
                    result.put("lat", first.get("y")); // 위도
                    result.put("lng", first.get("x")); // 경도
                    result.put("placeName", first.get("place_name"));
                    return ResponseEntity.ok(result);
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("결과 없음");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("API 호출 에러: " + e.getMessage());
        }
    }
}