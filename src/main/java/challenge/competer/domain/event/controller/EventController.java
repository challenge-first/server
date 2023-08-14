package challenge.competer.domain.event.controller;

import challenge.competer.domain.event.dto.ResponseEventDto;
import challenge.competer.domain.event.service.EventService;
import challenge.competer.global.auth.LoginMember;
import challenge.competer.global.auth.MemberDetails;
import challenge.competer.global.response.ResponseDataDto;
import challenge.competer.global.response.ResponseMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{eventId}/coupons")
    public ResponseEntity<ResponseDataDto> createCoupon(@PathVariable Long eventId, @LoginMember MemberDetails memberDetails) {
        ResponseDataDto<ResponseMessageDto> responseDataDto = eventService.createCoupon(eventId, memberDetails);

        return ResponseEntity.status(OK).body(responseDataDto);
    }
}
