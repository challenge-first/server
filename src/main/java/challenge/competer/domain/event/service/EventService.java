package challenge.competer.domain.event.service;

import challenge.competer.domain.event.dto.ResponseEventDto;
import challenge.competer.global.response.ResponseDataDto;

public interface EventService {

    ResponseDataDto<ResponseEventDto> getOpenEvent();

}
