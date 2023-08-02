package challenge.competer.domain.event.controller;

import challenge.competer.domain.event.dto.ResponseEventDto;
import challenge.competer.domain.event.service.EventService;
import challenge.competer.global.auth.LoginMember;
import challenge.competer.global.auth.MemberDetails;
import challenge.competer.global.response.ResponseDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<ResponseDataDto> getOpenEvent(@LoginMember MemberDetails MemberDetails) {
        ResponseDataDto<ResponseEventDto> responseDataDto = eventService.getOpenEvent();

        return ResponseEntity.status(OK).body(responseDataDto);
    }

}
