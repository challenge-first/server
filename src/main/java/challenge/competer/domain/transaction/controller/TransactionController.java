package challenge.competer.domain.transaction.controller;

import challenge.competer.domain.transaction.dto.RequestTransactionDto;
import challenge.competer.domain.transaction.dto.ResponseTransactionDto;
import challenge.competer.domain.transaction.service.TransactionService;
import challenge.competer.global.auth.LoginMember;
import challenge.competer.global.auth.MemberDetails;
import challenge.competer.global.response.ResponseDataDto;
import challenge.competer.global.response.ResponseMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/products/{productId}")
    public ResponseEntity<ResponseDataDto> payment(@PathVariable Long productId,
                                                      @RequestBody RequestTransactionDto requestDto,
                                                      @LoginMember MemberDetails memberDetails) {

        ResponseMessageDto responseMessage = transactionService.payment(productId, requestDto, memberDetails);
        ResponseDataDto<ResponseMessageDto> responseData = new ResponseDataDto<>(responseMessage);
        return ResponseEntity.status(responseData.getData().getStatusCode()).body(responseData);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ResponseDataDto> getTransaction(@PathVariable Long productId,
                                                          @LoginMember MemberDetails memberDetails) {
        ResponseDataDto<ResponseTransactionDto> response = transactionService.getTransaction(productId, memberDetails);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
