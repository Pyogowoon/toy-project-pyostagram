package com.pyo.pyostagram.web.api;


import com.pyo.pyostagram.config.auth.PrincipalDetails;
import com.pyo.pyostagram.domain.user.User;
import com.pyo.pyostagram.handler.ex.CustomValidationApiException;
import com.pyo.pyostagram.handler.ex.CustomValidationException;
import com.pyo.pyostagram.service.SubscribeService;
import com.pyo.pyostagram.service.UserService;
import com.pyo.pyostagram.web.dto.CMRespDto;
import com.pyo.pyostagram.web.dto.subscribe.SubscribeDto;
import com.pyo.pyostagram.web.dto.user.UserUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserApiController {


    private final UserService userService;
    //DATA를 응답하는 애들은 api

    private final SubscribeService subscribeService;

    @PutMapping("/api/user/{id}")
    public CMRespDto<?> update(
            @PathVariable int id,
            @Valid UserUpdateDto userUpdateDto,
            BindingResult bindingResult, // 꼭@Valid가 적힌 다음파라메터에 적어야함
            @AuthenticationPrincipal PrincipalDetails principalDetails) {


            User userEntity = userService.회원수정(id, userUpdateDto.toEntity());
            principalDetails.setUser(userEntity);
            return new CMRespDto<>(1, "회원수정완료", userEntity);
            //응답시에 userEntity의 모든 getter 함수가 호출되고 JSON으로 파싱하여 응답한다.
            //이것 역시 또 List<Image>의 무한반복 문제.


    }
    @GetMapping("api/user/{pageUserId}/subscribe")
    public ResponseEntity<?> subscribeList(@PathVariable int pageUserId, @AuthenticationPrincipal PrincipalDetails principalDetails){

        List<SubscribeDto> subscribeDto = subscribeService.구독리스트( principalDetails.getUser().getId(), pageUserId);

        return new ResponseEntity<>(new CMRespDto<>(1,"구독자 정보 리스트 가져오기 성공",subscribeDto), HttpStatus.OK);
    }

    @PutMapping("/api/user/{principalId}/profileImageUrl")
    public ResponseEntity<?> profileImageUrlUpdate(@PathVariable int principalId, MultipartFile profileImageFile,
       @AuthenticationPrincipal PrincipalDetails principalDetails){

       User userEntity =  userService.회원프로필사진변경(principalId , profileImageFile);
       principalDetails.setUser(userEntity); // 세션 변경

        return new ResponseEntity<>(new CMRespDto<>(1,"이미지 저장성공",null), HttpStatus.OK);
    }
}
