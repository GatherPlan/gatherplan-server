package com.example.gatherplan.common.unit;

import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.annotations.Comment;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Address {
    @Comment("상세주소/행정구역 명")
    private String fullAddress;
    @Comment("장소 이름 (행정구역일 시 Null)")
    private String placeName;
    @Comment("장소 상세 정보 링크 (행정구역일 시 Null)")
    private String placeUrl;

}
