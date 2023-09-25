package com.kh.youtube.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder //기존에있는 데이터들을 하나하나 입력할수 잇음
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private String token;
    private String id;
    private String password;
    private String name;
}
