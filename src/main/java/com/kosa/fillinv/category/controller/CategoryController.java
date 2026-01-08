package com.kosa.fillinv.category.controller;

import com.kosa.fillinv.category.dto.CategoryResponseDto;
import com.kosa.fillinv.category.service.CategoryService;
import com.kosa.fillinv.member.dto.MemberApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public MemberApiResponse<List<CategoryResponseDto>> getAllCategories() {
        return MemberApiResponse.ok(categoryService.getAllCategories());
    }
}
