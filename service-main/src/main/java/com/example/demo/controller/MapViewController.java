package com.example.demo.controller; //  본인의 실제 패키지명에 맞게 유지

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller; //  화면(HTML)을 반환하므로 @Controller 사용
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapViewController {

    //  application.properties 에 적힌 kakao.maps.api-key 값을 가져옵니다.
    @Value("${kakao.maps.api-key}")
    private String kakaoApiKey;

    /**

     브라우저나 iframe이 '/map' 주소로 요청할 때 동작하는 메서드*/@GetMapping("/map")
    public String mapPage(Model model) {// 1. "kakaoKey"라는 이름의 바구니에 서버가 가진 실제 카카오 API 키를 담습니다.
        model.addAttribute("kakaoKey", kakaoApiKey);

        // 2. src/main/resources/templates/map.html 파일을 찾아 타임리프로 렌더링하여 화면을 보여줍니다.
        return "map";
    }
}