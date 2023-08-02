package challenge.competer.domain.like.service;

import challenge.competer.domain.like.entity.Like;
import challenge.competer.domain.like.repository.LikeRepository;
import challenge.competer.global.auth.MemberDetails;
import challenge.competer.global.response.ResponseMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    @Override
    public ResponseMessageDto toggleLike(MemberDetails memberDetails, Long productId) {
        String stateMessage = "좋아요 취소";
        Long memberId = memberDetails.getId();
        Optional<Like> findLike = likeRepository.findByMemberIdAndProductId(memberId, productId);

        if (findLike.isEmpty()) {
            stateMessage = "좋아요 완료";
            Like like = createLike(productId, memberId);
            likeRepository.save(like);
        }

        findLike.ifPresent(likeRepository::delete);

        return new ResponseMessageDto(stateMessage, 200, "OK");
    }

    private Like createLike(Long productId, Long memberId) {
        return Like.builder()
                .productId(productId)
                .memberId(memberId)
                .build();
    }
}
