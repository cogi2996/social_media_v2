package com.example.social_media.controller;

import com.example.social_media.entity.User;
import com.example.social_media.security.IAuthenticationFacade;
import com.example.social_media.service.FollowService;
import com.example.social_media.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {
    private final IAuthenticationFacade authenticationFacade;
    private final UserService userService;
    private final FollowService followService;


    @GetMapping("/search/top")
    public String getIntoSearch(Model model, @RequestParam("name") String encodedName) {
        User currentUser = authenticationFacade.getUser();
        model.addAttribute("user", currentUser);

        try {
            String decodedName = URLDecoder.decode(encodedName, StandardCharsets.UTF_8.toString());
            System.out.println("decodedName: " + decodedName);
            model.addAttribute("keyword", decodedName);
            List<User> results = userService.searchUserByName(decodedName, 0, 3, null);
            if (!results.isEmpty()) {
                model.addAttribute("results", results);
                List<Integer> followState = results
                        .stream()
                        .map(user ->
                                followService.existsFollowBySourceIdAndTargetId(currentUser.getUserId(), user.getUserId()))
                        .toList();
                model.addAttribute("followStates", followState);
            } else {
                model.addAttribute("message", "Không có kết quả tìm kiếm liên quan!");
            }
        } catch (UnsupportedEncodingException e) {
            // Xử lý ngoại lệ nếu không thể giải mã
        }

        return "web/search";
    }
}
