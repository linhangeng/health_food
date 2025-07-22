package com.example.controller;

import com.example.model.dto.SysFileDTO;
import com.example.model.vo.SysFileVO;
import com.example.protocol.ApiServiceResponse;
import com.example.service.SysFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @ClassName VideoController
 * @Author sheng.lin
 * @Date 2025/7/2
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@RestController
@RequestMapping("/file")
@Tag(name = "file", description = "视频音频文件管理")
public class FileController {

    @Resource
    SysFileService sysFileService;

    @PostMapping("/upload")
    @Operation(summary = "上传视频音频文件")
    public ApiServiceResponse<Boolean> upload(@RequestPart("file") MultipartFile file) {
        return new ApiServiceResponse<>(sysFileService.upload(file));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除视频音频文件")
    public ApiServiceResponse<Boolean> delete(@RequestParam("fileIdList") List<Integer> fileIdList) {
        return new ApiServiceResponse<>(sysFileService.delete(fileIdList));
    }

    @PostMapping("/search")
    @Operation(summary = "查询视频音频文件")
    public ApiServiceResponse<List<SysFileVO>> search(@RequestBody SysFileDTO sysFileDto) {
        return new ApiServiceResponse<>(sysFileService.search(sysFileDto));
    }


    @GetMapping("/export")
    @Operation(summary = "文件数据导出")
    public void export(HttpServletResponse httpServletResponse){
        sysFileService.export(httpServletResponse);
    }

    @GetMapping("/saveTestData")
    @Operation(summary = "文件数据导出")
    public ApiServiceResponse<Boolean> saveTestData(){
        return new ApiServiceResponse<>(sysFileService.saveTestData());
    }


    @GetMapping("/hello")
    @Operation(summary = "文件数据导出")
    public String hello(){
        return "hello";
    }
}
