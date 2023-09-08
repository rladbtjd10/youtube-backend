package com.kh.youtube.controller;

import com.kh.youtube.domain.Channel;
import com.kh.youtube.service.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/*")
public class ChannelController {

    @Autowired
    private ChannelService service;


    @GetMapping("/channel")
    public ResponseEntity<List<Channel>> showAll() {
        return ResponseEntity.status(HttpStatus.OK).body(service.showAll());
    }

    @GetMapping("/channel/{id}")
    public ResponseEntity<Channel> show(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.show(id));
    }



    @PostMapping("/channel")
    public ResponseEntity<Channel> create(@RequestBody Channel channel) {
        return ResponseEntity.status(HttpStatus.OK).body(service.create(channel));
    }

    @PutMapping("/channel")
    public ResponseEntity<Channel> update(@RequestBody Channel channel) {
        Channel result = service.update(channel);
        if(result!=null){
            return  ResponseEntity.status(HttpStatus.OK).body(service.update(channel));
        }
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/channel/{id}")
    public ResponseEntity<Channel> delete(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.delete(id));
    }






}
