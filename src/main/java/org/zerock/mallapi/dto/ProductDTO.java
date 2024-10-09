package org.zerock.mallapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    // 1. 상품 등록 - multipartfile
    // 2. 상품 조회 - DB 파일 저장 X
    //             - 파일 이름만 조회할 것

    private Long pno;

    private String pname;

    private int price;

    private String pdesc;

    // 삭제 플래그 / 삭제 컬럼
    private boolean delFlag;

    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>(); // 진짜 파일

    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>(); // 올라간 파일이름들

}
