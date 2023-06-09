package com.ll.basic1.boundedContext.member.controller;

import com.ll.basic1.base.rq.Rq;
import com.ll.basic1.base.RsData;
import com.ll.basic1.boundedContext.member.entity.Member;
import com.ll.basic1.boundedContext.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final Rq rq;

//    // 생성자 주입
//    @Autowired
//    public MemberController(MemberService memberService) {
//        this.memberService = memberService;
//    }


    @GetMapping("/member/login")
//    @ResponseBody
    public String showLogin() {
//        if (rq.isLogined()) {
//            return """
//                    <h1>이미 로그인 되었습니다.</h1>
//                    """.stripIndent();
//        }


        return "usr/member/login";
    }

    @PostMapping("/member/login")
    @ResponseBody
    public RsData login(String username, String password) {

        if (username == null || username.trim().length() == 0) {
            return RsData.of("F-3", "username(을)를 입력해주세요.");
        }

        if (password == null || password.trim().length() == 0) {
            return RsData.of("F-4", "password(을)를 입력해주세요.");
        }

        RsData rsData = memberService.tryLogin(username, password);

        if (rsData.isSuccess()) {
            Member member = (Member) rsData.getData();
//            rq.setCookie("loginedMemberId",member.getId());
            rq.setSession("loginedMemberId", member.getId());
        }


        return rsData;
    }

//    @GetMapping("/member/doLogin")
//    @ResponseBody
//    public RsData login(String username, String password) {
//
//        if (username == null || username.trim().length() == 0) {
//            return RsData.of("F-3", "username(을)를 입력해주세요.");
//        }
//
//        if (password == null || password.trim().length() == 0) {
//            return RsData.of("F-4", "password(을)를 입력해주세요.");
//        }
//
//        RsData rsData = memberService.tryLogin(username, password);
//
//        if (rsData.isSuccess()) {
//            Member member = (Member) rsData.getData();
//            rq.setCookie("loginedMemberId",member.getId());
//        }
//
//        return rsData;
//    }

    @GetMapping("/member/logout")
    @ResponseBody
    public RsData logout() {

//        boolean cookieRemoved = rq.removeCookie("loginedMemberId");
        boolean cookieRemoved = rq.removeSession("loginedMemberId");

        if (cookieRemoved == false) {
            return RsData.of("S-2", "이미 로그아웃 상태입니다.");
        }

        return RsData.of("S-1", "로그아웃 되었습니다.");
    }

    @GetMapping("/member/me")
    public String showMe(Model model) {
        long loginedMemberId = rq.getLoginedMemberId();

        Member member = memberService.findById(loginedMemberId);

        model.addAttribute("member", member);

        return "usr/member/me";
    }

//    @GetMapping("/member/me")
//    @ResponseBody
//    public RsData showMe() {
//
////        long loginedMemberId = rq.getCookieAsLong("loginedMemberId", 0);
//
//        long loginedMemberId = rq.getSessionAsLong("loginedMemberId", 0);
//        boolean isLogined = loginedMemberId > 0;
//
//        if (isLogined == false)
//            return RsData.of("F-1", "로그인 후 이용해주세요.");
//
//        Member member = memberService.findById(loginedMemberId);
//
//        return RsData.of("S-1", "당신의 username(은)는 %s 입니다.".formatted(member.getUsername()));
//    }

    // 디버깅용 함수
    @GetMapping("/member/session")
    @ResponseBody
    public String showSession() {
        return rq.getSessionDebugContents().replaceAll("\n", "<br>");
    }

}