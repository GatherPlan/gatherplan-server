package com.example.gatherplan.external.vo;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalClientResp {
    private DataPortalResponse response;

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DataPortalResponse {
        private DataPortalHeader header;
        private DataPortalBody body;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DataPortalHeader {
        private String resultCode;
        private String resultMsg;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DataPortalBody {
        private ItemInfo items;
        private long numOfRows;
        private long pageNo;
        private long totalCount;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ItemInfo {
        private List<Item> item;

        @Getter
        @Builder
        @AllArgsConstructor(access = AccessLevel.PROTECTED)
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        public static class Item {
            // 행사 명
            private String title;
            // 주소 명
            private String addr1;
            // 장소 명
            private String addr2;
            // 행사 시작 일
            private String eventstartdate;
            // 행사 종료 일
            private String eventenddate;
            // 첫번째 이미지
            private String firstimage;
            // 두번째 이미지
            private String firstimage2;
            // 지역 코드
            private String areacode;
            // 시/군/구 코드
            private String sigungucode;
            // 전화 번호
            private String tel;
        }
    }
}