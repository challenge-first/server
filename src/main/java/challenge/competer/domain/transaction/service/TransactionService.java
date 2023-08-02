package challenge.competer.domain.transaction.service;

import challenge.competer.domain.transaction.dto.RequestTransactionDto;
import challenge.competer.domain.transaction.dto.ResponseTransactionDto;
import challenge.competer.global.auth.MemberDetails;
import challenge.competer.global.response.ResponseDataDto;
import challenge.competer.global.response.ResponseMessageDto;

public interface TransactionService {
    ResponseMessageDto payment(Long productId, RequestTransactionDto requestDto, MemberDetails memberDetails);
    ResponseDataDto<ResponseTransactionDto> getTransaction(Long productId, MemberDetails memberDetails);
}
