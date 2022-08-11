package com.jhr.algoNote.api.controller;


import com.jhr.algoNote.api.dto.CreateMemberRequest;
import com.jhr.algoNote.api.dto.CreateMemberResponse;
import com.jhr.algoNote.domain.Member;
import com.jhr.algoNote.domain.Role;
import com.jhr.algoNote.service.MemberService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class MemberApiController {


    private final MemberService memberService;

    //URI
    private final String CREATE = "/new";

    @PostMapping(CREATE)
    public CreateMemberResponse create(
        @RequestBody @Valid CreateMemberRequest createMemberRequest) {

        Member member = Member.builder()
            .name(createMemberRequest.getName())
            .email(createMemberRequest.getEmail())
            .picture(createMemberRequest.getPicture())
            .role(Role.USER) // 기본권한 : ADMIN
            .build();

        memberService.join(member);

        return new CreateMemberResponse(member.getId());
    }


}
