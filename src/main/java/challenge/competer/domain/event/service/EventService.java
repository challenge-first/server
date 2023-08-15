package challenge.competer.domain.event.service;

import challenge.competer.domain.event.dto.ResponseEventDto;
import challenge.competer.global.auth.MemberDetails;
import challenge.competer.global.response.ResponseDataDto;
import challenge.competer.global.response.ResponseMessageDto;

public interface EventService {

    ResponseDataDto<ResponseEventDto> getOpenEvent();

    ResponseDataDto<ResponseMessageDto> createCoupon(Long evenId, MemberDetails memberDetails);
}
