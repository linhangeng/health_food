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
import com.example.util.ConvertAPIUtil;
import com.example.util.MarkdownToPdfOrMdConverterUtil;
import com.example.util.OssUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
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
    OssUtil ossUtil;
    @Resource
    OssProperties ossProperties;
    @Resource
    FileFeign fileFeign;

    private final Map<String, String> dishVideoMap  = new HashMap<>();


    @PostConstruct
    private void init(){
        dishVideoMap.put("茄子豆角", "https://example.com/videos/eggplant-beans.mp4");
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
        // 提问内容
        String content = healthyFoodSearchDto.getContent();
        ChatResponse chatResponse = chatService.chat(ChatDTO.builder()
                .sessionId(UUID.randomUUID().toString())
                .content(content).build());
        SysFileDTO sysFileDTO = new SysFileDTO();
        try {
            // 获取内容
            String result = chatResponse.getResult().getOutput().getText();
            log.info("AI回答的内容 -> {}", result);
            if (healthyFoodSearchDto.getIsMakeVideo()) {
                String dishName = extractDishName(content);
                if (ObjectUtil.isNotEmpty(dishName)) {
                    // 加入制作视频
                    result= addMakeVideo(result, dishName);
                }
            }
            sysFileDTO = builSysFileDTO(result);
        } catch (Exception e) {
            sysFileDTO.setFileStatus(SysFileStatusEnum.FAILED.getCode());
            throw new WrappedException(BizEnums.CUSTOM_ERROR, "上传失败");
        } finally {
            fileFeign.saveSysFile(sysFileDTO);
        }
        return sysFileDTO.getFileUrl();
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
//        // 2. 构建 MD 格式的【制作视频参考】模块
//        StringBuilder videoModule = new StringBuilder();
//        // 用两个换行符分隔上一个模块（符合 MD 段落分隔规范）
//        videoModule.append("\n\n### 【制作视频参考】\n");
//        if (videoUrl != null && !videoUrl.trim().isEmpty()) {
//            // 符合 MD 图片链接语法：![alt文本](链接)
//            videoModule.append("![制作视频参考图](").append(videoUrl).append(")");
//        } else {
//            // 无视频时，用 MD 普通文本说明
//            videoModule.append("暂无该菜品的制作视频参考，敬请期待～");
//        }
        // 3. 拼接原内容和新模块，返回完整 MD
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
    private SysFileDTO builSysFileDTO(String result) {
        SysFileDTO sysFileDTO = new SysFileDTO();
        // 生成远程文件名称
        String objectName = UUID.randomUUID() + ".md";
        sysFileDTO.setBucketName(ossProperties.getTempBucketName());
        sysFileDTO.setUploadName(objectName);
        sysFileDTO.setFileName(objectName);
        sysFileDTO.setUploadSource(UploadSource.CONVERT_API.getCode());
        // step1 : 生成可访问的md文件，在将md文件转为pdf
        sysFileDTO.setFileUrl(ConvertAPIUtil.convertToPdf(ossUtil.uploadFileByInputStream(
                MarkdownToPdfOrMdConverterUtil.markdownToFile(result),
                ossProperties.getTempBucketName(), objectName, true)));
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
