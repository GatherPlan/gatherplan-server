package com.example.gatherplan.common.unit;

import com.example.gatherplan.common.enums.LocationType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.hibernate.annotations.Comment;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Address {
    @Enumerated(EnumType.STRING)
    @Comment("형식 구분 값 (상세주소/행정구역)")
    private LocationType locationType;
    @Comment("상세주소/행정구역 명")
    private String fullAddress;
    @Comment("장소 이름")
    private String placeName;
    @Comment("장소 상세 정보 링크)")
    private String placeUrl;

}
