package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.config.OssProperties;
import com.example.domain.SysFile;
import com.example.enums.SysFileStatusEnum;
import com.example.enums.UploadSource;
import com.example.exception.BizEnums;
import com.example.exception.WrappedException;
import com.example.mapper.SysFileMapper;
import com.example.model.dto.SysFileDTO;
import com.example.model.vo.SysFileVO;
import com.example.service.SysFileService;
import com.example.util.OssUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @ClassName VideoServiceImpl
 * @Author sheng.lin
 * @Date 2025/7/2
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@Service
@Slf4j
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements SysFileService {

    private static final String VIDEO_PATH = "video/";

    @Resource
    OssUtil ossUtil;
    @Resource
    OssProperties ossProperties;


    /**
     * 上传视频/音频文件
     *
     * @param file
     * @Author sheng.lin
     * @Date 2025/7/3
     * @return: java.lang.Boolean
     * @Version 1.0
     * @修改记录
     */
    @Override
    public Boolean upload(MultipartFile file) {
        // 入库
        SysFile sysFile = new SysFile();
        sysFile.setUploadSource(UploadSource.ALIBABA_OSS.getCode());
        sysFile.setBucketName(ossProperties.getBucketName());
        try {
            String extension = getExtension(file.getOriginalFilename());
            String objectName = VIDEO_PATH + UUID.randomUUID().toString().replaceAll("-", "") + "." + extension;
            String path = ossUtil.uploadFileByMul(file, ossProperties.getBucketName(), objectName, true);
            sysFile.setFileName(file.getOriginalFilename());
            sysFile.setFileSize((int) file.getSize());
            sysFile.setFileStatus(SysFileStatusEnum.UPLOADED.getCode());
            sysFile.setFileUrl(path);
            sysFile.setCreateTime(System.currentTimeMillis());
            return true;
        } catch (Exception e) {
            sysFile.setFileStatus(SysFileStatusEnum.FAILED.getCode());
            throw new WrappedException(BizEnums.CUSTOM_ERROR, "上传文件失败");
        } finally {
            save(sysFile);
        }
    }


    /**
     * 删除音频视频文件
     *
     * @param fileIdList
     * @Author sheng.lin
     * @Date 2025/7/4
     * @return: java.lang.Boolean
     * @Version 1.0
     * @修改记录
     */
    @Override
    public Boolean delete(List<Integer> fileIdList) {
        QueryWrapper<SysFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("rec_id", fileIdList);
        List<SysFile> list = list(queryWrapper);
        if (ObjectUtil.isNotEmpty(list)) {
            list.forEach(e -> {
                e.setFileStatus(SysFileStatusEnum.DELETED.getCode());
            });
            return updateBatchById(list);
        }
        return false;
    }


    /**
     * 查询音频视频文件
     *
     * @param
     * @Author sheng.lin
     * @Date 2025/7/4
     * @return: java.util.List<java.lang.String>
     * @Version 1.0
     * @修改记录
     */
    @Override
    public List<SysFileVO> search(SysFileDTO sysFileDto) {
        QueryWrapper<SysFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(ObjectUtil.isNotEmpty(sysFileDto.getFileName()),
                "file_name", sysFileDto.getFileName());
        queryWrapper.eq(ObjectUtil.isNotEmpty(sysFileDto.getFileStatus()),
                "file_status", sysFileDto.getFileStatus());
        queryWrapper.eq(ObjectUtil.isNotEmpty(sysFileDto.getUploadSource()),
                "upload_source", sysFileDto.getUploadSource());
        queryWrapper.eq(ObjectUtil.isNotEmpty(sysFileDto.getBucketName()),
                "bucket_name", sysFileDto.getBucketName());
        List<SysFile> list = list(queryWrapper);
        return list.stream()
                .map(sysFile -> {
                    SysFileVO sysFileVO = new SysFileVO();
                    BeanUtils.copyProperties(sysFile, sysFileVO);
                    return sysFileVO;
                })
                .collect(Collectors.toList());
    }


    /**
     * 获取后缀名
     *
     * @param fileName
     * @Author sheng.lin
     * @Date 2025/7/3
     * @return: java.lang.String
     * @Version 1.0
     * @修改记录
     */
    private String getExtension(String fileName) {
        String extension = "";
        int dotIndex = fileName.lastIndexOf(".");
        // 如果找不到 '.'，说明没有扩展名
        if (dotIndex != -1) {
            extension = fileName.substring(dotIndex + 1); // 或者返回 null，取决于你的需求
        }
        return extension;
    }


}
