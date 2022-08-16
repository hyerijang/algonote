package com.jhr.algoNote.api.controller;

import static java.util.stream.Collectors.toList;

import com.jhr.algoNote.domain.Problem;
import com.jhr.algoNote.domain.Review;
import com.jhr.algoNote.domain.tag.ProblemTag;
import com.jhr.algoNote.domain.tag.Tag;
import com.jhr.algoNote.repository.ProblemRepository;
import com.jhr.algoNote.service.ProblemService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * xToOne(ManyToOne, OneToOne) Problem -> Member Problem -> Contnet
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/problems")
public class ProblemApiController {

    private final ProblemService problemService;
    private final ProblemRepository problemRepository;

    @GetMapping
    public Result problems(@RequestParam(value = "offset", defaultValue = "0") int offset,
        @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Problem> problems = problemRepository.findAllWithFetchJoin(offset,limit);
        List<ProblemDto> result = problems.stream().map(p -> new ProblemDto(p)).collect(toList());
        return new Result(result);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {

        T data;
    }

    @Getter
    static class ProblemDto {

        private Long problemId;
        private String problemTitle;
        private String problemSite;
        private String problemUrl;
        private String name;
        private String problemContent;

        //TODO: 1대다 관계 고려하여 페이징

//        private List<ProblemTagDto> problemTags = new ArrayList<>();
//        private List<ReviewDto> reviews = new ArrayList<>();

        public ProblemDto(Problem problem) {
            this.problemId = problem.getId();
            this.problemTitle = problem.getTitle();
            this.problemSite = problem.getSite();
            this.problemUrl = problem.getUrl();
            this.name = problem.getMember().getName();
            this.problemContent = problem.getContent().getText();



        }
    }

    @Getter
    @AllArgsConstructor
    static class ProblemTagDto {
        private String tagName;
    }

    @Getter
    @AllArgsConstructor
    static class ReviewDto {
        private String Title;
    }

}
