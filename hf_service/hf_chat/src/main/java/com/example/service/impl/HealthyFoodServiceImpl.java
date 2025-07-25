package com.example.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.example.enums.SysFileStatusEnum;
import com.example.enums.UploadSource;
import com.example.exception.BizEnums;
import com.example.exception.WrappedException;
import com.example.feign.FileFeign;
import com.example.model.dto.ChatDTO;
import com.example.model.dto.HealthyFoodSearchDTO;
import com.example.model.dto.SysFileDTO;
import com.example.properties.OssProperties;
import com.example.service.ChatService;
import com.example.service.HealthyFoodService;
import com.example.service.ConvertAPIService;
import com.example.util.MarkdownToPdfOrMdConverterUtil;
import com.example.service.OssService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName HealthyFoodServiceImpl
 * @Author sheng.lin
 * @Date 2025/7/24
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@Service
@Slf4j
public class HealthyFoodServiceImpl implements HealthyFoodService {

    @Resource
    ChatService chatService;
    @Resource
    OssService ossService;
    @Resource
    OssProperties ossProperties;
    @Resource
    FileFeign fileFeign;

    private final Map<String, String> dishVideoMap  = new HashMap<>();


    @PostConstruct
    private void init(){
        // todo
        dishVideoMap.put("茄子豆角", "https://media.w3.org/2010/05/sintel/trailer.mp4");
        dishVideoMap.put("番茄炒鸡蛋", "https://media.w3.org/2010/05/sintel/trailer.mp4");
    }

    /**
     * 查询美食制作方法
     *
     * @param healthyFoodSearchDto
     * @Author sheng.lin
     * @Date 2025/7/24
     * @return: java.lang.String
     * @Version 1.0
     * @修改记录
     */
    @Override
    public String search(HealthyFoodSearchDTO healthyFoodSearchDto) {
        String content = healthyFoodSearchDto.getContent();
        ChatResponse chatResponse = chatService.chat(ChatDTO.builder()
                .sessionId(UUID.randomUUID().toString())
                .content(content).build());
        SysFileDTO sysFileDTO = new SysFileDTO();
        try {
            // 获取回答
            String answer = chatResponse.getResult().getOutput().getText();
            // 获取菜名
            String dishName = "";
            if (healthyFoodSearchDto.getIsMakeVideo()) {
                dishName = extractDishName(content);
                if (ObjectUtil.isNotEmpty(dishName)) {
                    // 加入制作视频
                    answer = addMakeVideo(answer, dishName);
                }
            }
            sysFileDTO = builSysFileDTO(answer,dishName);
        } catch (Exception e) {
            sysFileDTO.setFileStatus(SysFileStatusEnum.FAILED.getCode());
            throw new WrappedException(BizEnums.CUSTOM_ERROR, "上传失败");
        } finally {
            fileFeign.saveSysFile(sysFileDTO);
        }
        return sysFileDTO.getFileUrl();
    }


    /**
     * 追加提示语
     * @param content
     * @Author sheng.lin
     * @Date 2025/7/25
     * @return: java.lang.String
     * @Version  1.0
     * @修改记录
     */
    private String replenish(String content){
        StringBuilder stringBuilder = new StringBuilder(content);
        stringBuilder.append("要求：1.要求内容详细；2.每个步骤分割线分开;3.标准Markdown格式返回");
        return stringBuilder.toString();
    }


    /**
     * 添加制作视频
     *
     * @param result
     * @Author sheng.lin
     * @Date 2025/7/24
     * @return: void
     * @Version 1.0
     * @修改记录
     */
    private String addMakeVideo(String result, String dishName) {
        log.info("当前处理的菜品：{}", dishName);
        // 1. 根据菜品名获取视频链接（实际场景可从数据库/API查询）
        String videoUrl = dishVideoMap.get(dishName);
        StringBuilder videoModule = new StringBuilder("\n\n### 【制作视频参考】\n");
        if (videoUrl != null && !videoUrl.trim().isEmpty()) {
            // 使用标准的Markdown超链接格式，而不是图片语法
            videoModule.append("[点击观看制作视频](").append(videoUrl).append(")");
        } else {
            videoModule.append("暂无该菜品的制作视频参考，敬请期待～");
        }
        log.info("拼接之后的回答->{}", result + videoModule.toString());
        return result + videoModule.toString();
    }


    /**
     * build SysFileDTO
     *
     * @param result
     * @Author sheng.lin
     * @Date 2025/7/24
     * @return: com.example.model.dto.SysFileDTO
     * @Version 1.0
     * @修改记录
     */
    private SysFileDTO builSysFileDTO(String answer, String fileName) {
        SysFileDTO sysFileDTO = new SysFileDTO();
        // 生成远程文件名称
        String objectName = UUID.randomUUID() + ".md";
        sysFileDTO.setUploadSource(UploadSource.CONVERT_API.getCode());
        sysFileDTO.setUploadName(objectName);
        sysFileDTO.setFileName(fileName);
        // 生成可访问的md文件，在将md文件转为pdf
        sysFileDTO.setFileUrl(ConvertAPIService.convertToPdf(ossService.uploadFileByInputStream(
                MarkdownToPdfOrMdConverterUtil.markdownToFile(answer), ossProperties.getTempBucketName(), objectName, true), fileName));
        sysFileDTO.setFileStatus(SysFileStatusEnum.UPLOADED.getCode());
        return sysFileDTO;
    }


    /**
     * 获取菜名
     *
     * @param content
     * @Author sheng.lin
     * @Date 2025/7/24
     * @return: java.lang.String
     * @Version 1.0
     * @修改记录
     */
    public String extractDishName(String content) {
        // 定义正则表达式：匹配【和】之间的任意字符（非贪婪模式）
        Pattern pattern = Pattern.compile("【(.*?)】");
        Matcher matcher = pattern.matcher(content);
        // 提取第一个匹配结果
        if (matcher.find()) {
            return matcher.group(1); // 返回括号内的内容（不包含【】）
        }
        return null; // 未找到匹配
    }
}
