package com.example.gatherplan.common.unit;

import com.example.gatherplan.common.enums.LocationType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
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
    @Comment("형식 구분 값 (상세주소/행정구역/직접입력)")
    @NotNull
    private LocationType locationType;

    @Comment("상세주소/행정구역/직접입력 명")
    @NotBlank
    private String fullAddress;

    @Comment("장소 이름 (상세주소 전용 필드)")
    private String placeName;

    @Comment("장소 상세 정보 링크 (상세주소 전용 필드)")
    private String placeUrl;

}
