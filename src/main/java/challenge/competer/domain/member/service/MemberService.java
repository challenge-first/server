package challenge.competer.domain.member.service;

import challenge.competer.domain.member.dto.RequestMemberLoginDto;
import challenge.competer.domain.member.dto.RequestMemberPointDto;
import challenge.competer.domain.member.dto.ResponsePointDto;
import challenge.competer.domain.member.dto.ResponseTokenDto;
import challenge.competer.global.auth.MemberDetails;
import challenge.competer.global.response.ResponseMessageDto;

public interface MemberService {

    ResponseTokenDto login(RequestMemberLoginDto requestDto);

    ResponseMessageDto addPoint(RequestMemberPointDto requestDto, MemberDetails memberDetails);

    ResponsePointDto getPoint(MemberDetails member);
}
