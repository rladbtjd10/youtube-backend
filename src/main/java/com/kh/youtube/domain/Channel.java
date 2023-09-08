package com.kh.youtube.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert //오라클SQL문에 디폴드값이 들어가면 넣어줘야됨
public class Channel {

    @Id
    @Column(name="channel_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "channelSequence")
    @SequenceGenerator(name="channelSequence", sequenceName = "SEQ_CHANNEL", allocationSize = 1)
    private int channelCode;

    @Column(name="channel_name")
    private String channelName;

    @Column(name="channel_photo")
    private String channelPhoto;

    @Column(name="channel_desc")
    private String channelDesc;

    @Column(name="channel_date")
    private Date channelDate;

    @ManyToOne // Channel 엔티티과 Member 엔티티를 다대일 관계로 설정 //한 멤버가 여러 채널을 가지고 있음
    @JoinColumn(name="id")
    private Member member;


}
