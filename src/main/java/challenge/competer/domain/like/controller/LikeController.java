package challenge.competer.domain.like.controller;

import challenge.competer.domain.like.service.LikeService;
import challenge.competer.global.auth.LoginMember;
import challenge.competer.global.auth.MemberDetails;
import challenge.competer.global.response.ResponseDataDto;
import challenge.competer.global.response.ResponseMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/products/{productId}")
    public ResponseEntity<ResponseDataDto> toggleLike(@LoginMember MemberDetails memberDetails, @PathVariable Long productId) {
        ResponseMessageDto responseMessageDto = likeService.toggleLike(memberDetails, productId);
        ResponseDataDto responseDataDto = new ResponseDataDto(responseMessageDto);

        return ResponseEntity.status(OK).body(responseDataDto);
    }

}
