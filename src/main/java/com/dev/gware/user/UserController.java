package com.dev.gware.user;

import com.dev.gware.common.response.CommonResponse;
import com.dev.gware.user.dto.response.GetUserRes;
import com.dev.gware.user.dto.request.UpdateUserReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public CommonResponse test() {
        return new CommonResponse(true, 200, "토큰 잘확인 됨");
    }

    @GetMapping("/{usrKey}")
    public CommonResponse<GetUserRes> getUser(@PathVariable Long usrKey) {
        return new CommonResponse<>(true, 200, "회원 조회 성공", userService.findByKey(usrKey));
    }

    @PutMapping
    public CommonResponse editUser(@RequestBody UpdateUserReq updateUserReq) {
        userService.updateUser(updateUserReq);
        return new CommonResponse(true, 200, "회원 정보 수정 성공");
    }
}
