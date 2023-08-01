package challenge.competer.domain.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import challenge.competer.domain.member.dto.RequestMemberLoginDto;
import challenge.competer.domain.member.dto.RequestMemberPointDto;
import challenge.competer.domain.member.dto.ResponsePointDto;
import challenge.competer.domain.member.dto.ResponseTokenDto;
import challenge.competer.domain.member.service.MemberService;
import challenge.competer.global.auth.LoginMember;
import challenge.competer.global.auth.MemberDetails;
import challenge.competer.global.response.ResponseMessageDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<ResponseMessageDto> login(@RequestBody RequestMemberLoginDto requestDto) {
        ResponseTokenDto loginResponse = memberService.login(requestDto);
        ResponseMessageDto responseBody = new ResponseMessageDto("로그인 완료", HttpStatus.OK.value(), HttpStatus.OK.toString());

        return ResponseEntity.status(HttpStatus.OK)
            .header(loginResponse.getHeaderName(),loginResponse.getAccessTokenValue())
            .body(responseBody);
    }

    @PostMapping("/point")
    public ResponseEntity<ResponseMessageDto> addPoint(@RequestBody RequestMemberPointDto requestDto, @LoginMember MemberDetails memberDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.addPoint(requestDto, memberDetails));
    }

    @GetMapping("/point")
    public ResponseEntity<ResponsePointDto> getPoint(@LoginMember MemberDetails memberDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getPoint(memberDetails));
    }
}
