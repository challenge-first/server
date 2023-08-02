package challenge.competer.domain.like.service;

import challenge.competer.global.auth.MemberDetails;
import challenge.competer.global.response.ResponseMessageDto;

public interface LikeService {

    ResponseMessageDto toggleLike(MemberDetails memberDetails, Long productId);

}
