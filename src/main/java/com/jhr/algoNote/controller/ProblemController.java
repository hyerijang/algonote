package com.jhr.algoNote.controller;


import com.jhr.algoNote.config.auth.LoginUser;
import com.jhr.algoNote.config.auth.dto.SessionUser;
import com.jhr.algoNote.domain.Member;
import com.jhr.algoNote.domain.Problem;
import com.jhr.algoNote.domain.Site;
import com.jhr.algoNote.dto.ProblemCreateRequest;
import com.jhr.algoNote.dto.ProblemUpdateRequest;
import com.jhr.algoNote.repository.ProblemSearch;
import com.jhr.algoNote.service.MemberService;
import com.jhr.algoNote.service.ProblemService;
import com.jhr.algoNote.service.TagService;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/problems")
public class ProblemController {

    private final HttpSession httpSession;
    private final ProblemService problemService;
    private final MemberService memberService;
    private final TagService tagService;

    //URI
    private final String CREAT = "/new";
    private final String EDIT = "/{id}/edit";

    @GetMapping(CREAT)
    public String createForm(Model model) {
        model.addAttribute("form", new ProblemForm());
        //사이트 정보
        model.addAttribute("sites", Site.values());
        return "problems/createProblemForm";
    }

    @PostMapping(CREAT)
    public String creat(@Valid ProblemForm problemForm, @LoginUser SessionUser user) {
        Member member = memberService.findByEmail(user.getEmail());
        ProblemCreateRequest problemCreateRequest = ProblemCreateRequest.builder()
            .title(problemForm.getTitle())
            .contentText(problemForm.getContentText())
            .url(problemForm.getUrl())
            .tagText(problemForm.getTagText())
            .site(problemForm.getSite())
            .build();
        Long problemId = problemService.registerWithDto(member.getId(), problemCreateRequest);
        log.info("registered problem id = {}", problemId);

        return "redirect:/";
    }

    /**
     * 로그인한 유저가 등록한 문제 조회
     *
     * @param model
     * @return
     */
    @GetMapping
    public String list(Model model, @LoginUser SessionUser user) {

        Member member = memberService.findByEmail(user.getEmail());

        ProblemSearch problemSearch = ProblemSearch.builder()
            .memberId(member.getId())
            .build();

        List<Problem> problems = problemService.search(problemSearch);

        log.info("problems size ={}", problems.size());

        model.addAttribute("problems", problems);
        return "problems/problemList";
    }

    // TODO : 태그 조회 추가

    @GetMapping(EDIT)
    public String updateProblemForm(@PathVariable Long id, Model model) {
        Problem problem = problemService.findOne(id);
        ProblemForm form = new ProblemForm();
        //문제태그정보 text로 변환
        String tagText = problemService.getTagText(problem.getProblemTags());
        form.setId(problem.getId());
        form.setTitle(problem.getTitle());
        form.setUrl(problem.getUrl());
        form.setContentText(problem.getContent().getText());
        form.setTagText(tagText);
        form.setSite(problem.getSite());
        model.addAttribute("form", form);
        //사이트 정보
        model.addAttribute("sites", Site.values());
        return "problems/updateProblemForm";
    }

    @PostMapping(EDIT)
    public String edit(@ModelAttribute ProblemForm problemForm, @LoginUser SessionUser user) {

        Member member = memberService.findByEmail(user.getEmail());

        ProblemUpdateRequest dto = ProblemUpdateRequest.builder()
            .title(problemForm.getTitle())
            .contentText(problemForm.getContentText())
            .url(problemForm.getUrl())
            .tagText(problemForm.getTagText())
            .site(problemForm.getSite())
            .id(problemForm.getId())
            .build();

        problemService.edit(member.getId(), dto);

        return "redirect:/";
    }


}
