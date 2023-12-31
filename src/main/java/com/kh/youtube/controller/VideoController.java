package com.kh.youtube.controller;

import com.kh.youtube.domain.*;
import com.kh.youtube.domain.QVideo;
import com.kh.youtube.service.VideoCommentService;
import com.kh.youtube.service.VideoService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/*")
@Log4j2 //log찍기 위해서
@CrossOrigin(origins={"*"}, maxAge = 6000) //리액트랑 연결 시키기 위해서
public class VideoController {

    @Value("${youtube.upload.path}") // application.properties에 있는 변수
    private String uploadPath;

    @Autowired
    private VideoService videoService;

    @Autowired
    private VideoCommentService comment;


    // 영상 전체 조회 : GET - http://localhost:8080/api/video
    @GetMapping("/video")
    //@RequestParam 숨겨있을뿐 명시해줘야되서 파라미터를 넣지 않는경우(?page=1을 안하는 경우/즉 http://localhost:8080/api/video)에 페이지를 1로 맞춘다라는 의미
    public ResponseEntity<List<Video>> VideoList(@RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="category", required = false) Integer category) { // int와 달리 Integer로 지정하면 null값도 받을수 있음

        // 정렬
        Sort sort = Sort.by("videoCode").descending();

        // 한 페이지의 10개 
        Pageable pageable = PageRequest.of(page-1, 20, sort); //page-1한이유 처음 페이지를 1로 맞추기 위해

        // 동적 쿼리를 위한 QueryDSL을 사용한 코드들 추가

        // 1. Q도메인 클래스를 가져와야 한다. -> build.gradle로 만드는거
        QVideo qVideo = QVideo.video;

        // 2. BooleanBuilder는 where문에 들어가는 조건들을 넣어주는 컨테이너
        BooleanBuilder builder = new BooleanBuilder();

        if(category!=null) {
            // 3. 원하는 조건은 필드값과 같이 결합해서 생성한다.
            BooleanExpression expression = qVideo.category.categoryCode.eq(category);

            // 4. 만들어진 조건은 where문에 and나 or 같은 키워드와 결합한다.
            builder.and(expression);
        }

        Page<Video> result = videoService.showAll(pageable, builder);

        log.info("Total Pages : " + result.getTotalPages()); // 총 몇 페이지
        log.info("Total Count : " + result.getTotalElements()); // 전체 개수
        log.info("Page Number : " + result.getNumber()); //현재 페이지 번호
        log.info("Page Size : " + result.getSize()); // 페이지당 데이터 개수
        log.info("Next Page : " + result.hasNext()); // 다음 페이지가 있는지 존재 여부
        log.info("First Page : " + result.isFirst()); //시작 페이지 여부

        //return ResponseEntity.status(HttpStatus.OK).build();
        return ResponseEntity.status(HttpStatus.OK).body(result.getContent()); //getContent return 타입이 List<Video>
    }

    // 영상 추가 : POST - http://localhost:8080/api/video
    @PostMapping("/video")
    //기본값이 아닌 값들은 required = false로 지정하는 방법(여러개일 경우 각각의 것들에다가 지정해주면됨)
    public ResponseEntity<Video> createVideo(MultipartFile video, MultipartFile image, String title, @RequestParam(name="desc", required = false) String desc, String categoryCode) {
        log.info("video : " + video);
        log.info("image : " + image);
        log.info("title : " + title);
        log.info("desc : " + desc);
        log.info("categoryCode : " + categoryCode);
        // video_title, video_desc, video_url, video_photo, category_code

        // 업로드 처리
        // 비디오의 실제 파일 이름
        String originalVideo = video.getOriginalFilename();
        log.info("original : " + originalVideo);
        String realVideo = originalVideo.substring(originalVideo.lastIndexOf("\\")+1);
        log.info("realVideo : " + realVideo);

        // 이미지의 실제 이름
        String originalImage = image.getOriginalFilename();
        String realImage = originalImage.substring(originalImage.lastIndexOf("\\")+1);

        // UUID 무작위로 파일명 붙여주는거
        String uuid = UUID.randomUUID().toString();

        // 실제로 저장할 파일 명 (위치 포함)
        String saveVideo = uploadPath + File.separator + uuid + "_" + realVideo;
        Path pathVideo = Paths.get(saveVideo);

        String saveImage = uploadPath + File.separator + uuid + "_" + realImage;
        Path pathImage = Paths.get(saveImage);
        try {
            video.transferTo(pathVideo);
            image.transferTo(pathImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 파일 업로드가 끝났으니 경로 (saveImage), (saveVideo), title, desc, categoryCode
        Video vo = new Video();
        vo.setVideoTitle(title);
        vo.setVideoDesc(desc);
        vo.setVideoUrl(uuid + "_" + realVideo);
        vo.setVideoPhoto(uuid + "_" + realImage);
        Category category = new Category();
        category.setCategoryCode(Integer.parseInt(categoryCode));
        vo.setCategory(category);
        Channel channel = new Channel();
        channel.setChannelCode(24);
        vo.setChannel(channel);
        Member member = new Member();
        member.setId("user1");
        vo.setMember(member);

        //return ResponseEntity.status(HttpStatus.OK).build();
        return ResponseEntity.status(HttpStatus.OK).body(videoService.create(vo));
    }

    // 영상 수정 : PUT - http://localhost:8080/api/video
    @PutMapping("/video")
    public ResponseEntity<Video> updateVideo(@RequestBody Video vo) {
        return ResponseEntity.status(HttpStatus.OK).body(videoService.update(vo));
    }

    // 영상 삭제 : DELETE - http://localhost:8080/api/video/1
    @DeleteMapping("/video/{id}")
    public ResponseEntity<Video> deleteVideo(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(videoService.delete(id));
    }

    // 영상 1개 조회 : GET - http://localhost:8080/api/video/1
    @GetMapping("/video/{id}")
    public ResponseEntity<Video> showVideo(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(videoService.show(id));
    }

    // 영상 1개에 따른 댓글 전체 조회 : GET - http://localhost:8080/api/video/1/comment
    @GetMapping("/video/{id}/comment")
    public ResponseEntity<List<VideoComment>> VideoCommentList(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(comment.findByVideoCode(id));
    }

    // 좋아요 추가 : POST - http://localhost:8080/api/video/like
//    @PostMapping("/video/like")
//    public ResponseEntity<Video> VideoLike(@RequestBody VideoLike vo) {
//        return ResponseEntity.status(HttpStatus.OK).body(videolike.create(vo));
//    }

    // 좋아요 취소 : DELETE - http://localhost:8080/api/video/like/1
    // 댓글 추가 : POST - http://localhost:8080/api/video/comment
    // 댓글 수정 : PUT - http://localhost:8080/api/video/comment
    // 댓글 삭제 : DELETE - http://localhost:8080/api/video/comment/1
    // 댓글 좋아요 추가 : POST - http://localhost:8080/api/video/comment/like
    // 댓글 좋아요 취소 : DELETE - http://localhost:8080/api/video/commentlike/1

}
























