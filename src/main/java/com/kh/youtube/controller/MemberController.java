package com.kh.youtube.controller;

import com.kh.youtube.domain.Channel;
import com.kh.youtube.domain.Member;
import com.kh.youtube.service.ChannelService;
import com.kh.youtube.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/*")
public class MemberController {

    @Autowired
    private MemberService service;

    @Autowired
    private ChannelService channelservice;

    @GetMapping("/user")
    public ResponseEntity<List<Member>> showAll() {
        return ResponseEntity.status(HttpStatus.OK).body(service.showAll());
    }

    @GetMapping("/user/{id}") //내프로필보기 같은 곳에쓰임
    public ResponseEntity<Member> show(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.show(id));
    }

    @PostMapping("/user")
    public ResponseEntity<Member> create(@RequestBody Member member) {
        return ResponseEntity.status(HttpStatus.OK).body(service.create(member));
    }

    @PutMapping("/user")
    public ResponseEntity<Member> update(@RequestBody Member member) {
        Member result = service.update(member);
        if(result!=null) {
            return ResponseEntity.status(HttpStatus.OK).body(service.update(member));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); //build()는 body null로 보내는거랑 같음
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Member> delete(@PathVariable String id) {
        log.info("삭제~");
        return ResponseEntity.status(HttpStatus.OK).body(service.delete(id));
    }

    // SELECT * FROM channel WHERE id=?
    // http://localhost:8080/user/channel?id=user1
    @GetMapping("/user/channel")
    public ResponseEntity<List<Channel>> showMember(@RequestParam String id) {
        return ResponseEntity.status(HttpStatus.OK).body(channelservice.showMember(id));
    }

}























