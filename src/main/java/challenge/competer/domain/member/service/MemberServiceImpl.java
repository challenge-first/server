package challenge.competer.domain.member.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import challenge.competer.domain.member.dto.ResponsePointDto;
import challenge.competer.domain.member.exception.PasswordMismatchException;
import challenge.competer.domain.member.dto.RequestMemberLoginDto;
import challenge.competer.domain.member.dto.RequestMemberPointDto;
import challenge.competer.domain.member.dto.ResponseTokenDto;
import challenge.competer.domain.member.entity.Member;
import challenge.competer.domain.member.exception.UserNotFoundException;
import challenge.competer.domain.member.repository.MemberRepository;
import challenge.competer.global.jwt.JwtProvider;
import challenge.competer.global.auth.MemberDetails;
import challenge.competer.global.response.ResponseMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Override
    public ResponseTokenDto login(RequestMemberLoginDto requestDto) {
        Member member = memberRepository.findByUsername(requestDto.getUsername())
            .orElseThrow(() -> new UserNotFoundException("회원을 찾을 수 없습니다."));

        if (!requestDto.getPassword().equals(member.getPassword())) {
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtProvider.createAccessToken(member.getId(),member.getRole());
        return new ResponseTokenDto(jwtProvider.getHEADER_NAME(), accessToken);
    }

    @Transactional
    @Override
    public ResponseMessageDto addPoint(RequestMemberPointDto requestDto, MemberDetails memberDetails) {
        Member member = memberRepository.findById(memberDetails.getId())
            .orElseThrow(() -> new UserNotFoundException("회원을 찾을 수 없습니다."));
        member.addPoint(requestDto.getPoint());
    return new ResponseMessageDto("충전 완료", HttpStatus.OK.value(), HttpStatus.OK.toString());
    }

    @Override
    public ResponsePointDto getPoint(MemberDetails memberDetails) {
        return new ResponsePointDto(memberDetails.getPoint());
    }
}
