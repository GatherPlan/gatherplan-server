package com.example.gatherplan.common.unit;

import com.example.gatherplan.common.enums.LocationType;
import jakarta.annotation.Nullable;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private LocationType locationType;

    @Comment("상세주소/행정구역 명")
    @NotNull
    private String fullAddress;

    @Comment("장소 이름")
    @Nullable
    private String placeName;

    @Comment("장소 상세 정보 링크)")
    @Nullable
    private String placeUrl;

}
