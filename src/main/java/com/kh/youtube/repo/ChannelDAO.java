package com.kh.youtube.repo;

import com.kh.youtube.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChannelDAO extends JpaRepository<Channel, Integer> {
    
    // 특정 멤버의 모든 채널 조회
    // SELECT * FROM channel WHERE id=?

    @Query(value = "SELECT * FROM channel WHERE id= :id", nativeQuery = true) // ?대신에 id가 들어가야되서 :id
    //List<Channel> findByMemberId(String id);
    List<Channel> findByMemberId(@Param("id") String id);

}
