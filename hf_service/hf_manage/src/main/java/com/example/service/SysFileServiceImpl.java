package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.properties.OssProperties;
import com.example.domain.SysFile;
import com.example.enums.SysFileStatusEnum;
import com.example.enums.UploadSource;
import com.example.exception.BizEnums;
import com.example.exception.WrappedException;
import com.example.mapper.SysFileMapper;
import com.example.model.dto.SysFileDTO;
import com.example.strategy.excel.*;
import com.example.model.vo.SysFileVO;
import com.example.util.LocalDateTimeUtils;
import com.example.util.OssUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
    @Resource
    SysFileMapper sysFileMapper;

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
        checkIsSameFileName(file.getOriginalFilename());
        // 入库
        SysFile sysFile = new SysFile();
        sysFile.setUploadSource(UploadSource.ALIBABA_OSS.getCode());
        sysFile.setBucketName(ossProperties.getBucketName());
        try {
            String extension = getExtension(file.getOriginalFilename());
            String objectName = UUID.randomUUID().toString().replaceAll("-", "") + "." + extension;
            String path = ossUtil.uploadFileByMul(file, ossProperties.getBucketName(), objectName, true);
            sysFile.setFileName(file.getOriginalFilename());
            sysFile.setFileSize((int) file.getSize());
            sysFile.setUploadName(objectName);
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
        if (ObjectUtil.isEmpty(list)) {
            return false;
        }
        list.forEach(e -> {
            e.setFileStatus(SysFileStatusEnum.DELETED.getCode());
        });
        // 库删除
        updateBatchById(list);
        // oss删除,判断是否是同一个桶
        if (checkIsSameBucket(list)) {
            ossUtil.batchDelFile(list.stream().map(SysFile::getUploadName).toList(), list.get(0).getBucketName());
        }
        return true;
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
     * 文件数据导出
     *
     * @param
     * @Author sheng.lin
     * @Date 2025/7/20
     * @return: java.lang.Boolean
     * @Version 1.0
     * @修改记录
     */
    @Override
    public void export(HttpServletResponse httpServletResponse) {
        long count = count();
        if (count <= 0) {
            return;
        }

        // 开始导出
        ExcelExportStrategy<SysFileVO> strategy = ExcelExportStrategyFactory.getStrategy(count);
        ExcelContext<SysFileVO> excelContext = new ExcelContext<>();
        excelContext.setFileName(URLEncoder.encode("文件数据", StandardCharsets.UTF_8));
        excelContext.setSheetName("文件信息");
        excelContext.setClazz(SysFileVO.class);
        excelContext.setHttpServletResponse(httpServletResponse);
        if (strategy instanceof SmallExcelExportStrategy) {
            log.info("进入小数据量导出");
            // 小于1w
            List<SysFile> sysFiles = list();
            List<SysFileVO> sysFileVOS = new ArrayList<>();
            sysFiles.forEach(e -> {
                SysFileVO sysFileVO = new SysFileVO();
                BeanUtils.copyProperties(e, sysFileVO);
                if (ObjectUtil.isNotEmpty(e.getCreateTime())) {
                    sysFileVO.setCreateTime(LocalDateTimeUtils.format(LocalDateTimeUtils.fromMillis(e.getCreateTime()), LocalDateTimeUtils.YMD_HMS));
                }
                sysFileVOS.add(sysFileVO);
            });
            excelContext.setDataList(sysFileVOS);
        } else if (strategy instanceof MediumExcelExportStrategy) {
            // 大于1w小于5w
            log.info("进入中数据量导出");
            excelContext.setPageSize(5000);
            excelContext.setFetchFunc((pageNum, pageSize) -> {
                QueryWrapper<SysFile> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("file_status",SysFileStatusEnum.UPLOADED.getCode());
                List<SysFile> records = sysFileMapper
                        .selectPage(new Page<>(pageNum, pageSize), queryWrapper)
                        .getRecords();
                return records.stream().map(file -> {
                    SysFileVO vo = new SysFileVO();
                    BeanUtils.copyProperties(file, vo);
                    return vo;
                }).collect(Collectors.toList());
            });
        } else if (strategy instanceof LargeExcelExportStrategy) {
            // 大于5w
            log.info("进入大数据量导出");
        }
        strategy.execute(excelContext);
    }

    /**
     *
     * @param sysFileDto
     * @Author sheng.lin
     * @Date 2025/7/24
     * @return: java.lang.Boolean
     * @Version  1.0
     * @修改记录
     */
    @Override
    public Boolean saveSysFile(SysFileDTO sysFileDto) {
        SysFile sysFile = new SysFile();
        BeanUtils.copyProperties(sysFileDto,sysFile);
        return save(sysFile);
    }


    /**
     * 添加测试数据
     * @param
     * @Author sheng.lin
     * @Date 2025/7/21
     * @return: java.lang.Boolean
     * @Version  1.0
     * @修改记录
     */
    @Override
    public Boolean saveTestData() {
        List<SysFile> sysFileList = new ArrayList<>();
        for (int i = 0; i < 50000; i++) {
            SysFile sysFile = new SysFile();
            sysFile.setUploadSource(UploadSource.ALIBABA_OSS.getCode());
            sysFile.setBucketName(ossProperties.getBucketName());
            sysFile.setFileStatus(SysFileStatusEnum.UPLOADED.getCode());
            sysFile.setFileName("林"+i);
            sysFile.setFileUrl("url"+i);
            sysFile.setCreateTime(LocalDateTimeUtils.toMillis(LocalDateTimeUtils.now()));
            sysFile.setUploadName(VIDEO_PATH+i);
            sysFileList.add(sysFile);
        }
        return saveBatch(sysFileList);
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


    /**
     * 检查文件是否重名
     *
     * @param fileName
     * @Author sheng.lin
     * @Date 2025/7/16
     * @return: void
     * @Version 1.0
     * @修改记录
     */
    private void checkIsSameFileName(String fileName) {
        QueryWrapper<SysFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_name", fileName);
        long count = count(queryWrapper);
        if (count != 0) {
            throw new WrappedException(BizEnums.CUSTOM_ERROR, "文件名已存在");
        }
    }


    /**
     * @param sysFileList
     * @Author sheng.lin
     * @Date 2025/7/16
     * @return: java.lang.Boolean
     * @Version 1.0
     * @修改记录
     */
    private Boolean checkIsSameBucket(List<SysFile> sysFileList) {
        Set<String> bucketNameSet = sysFileList.stream().map(SysFile::getBucketName).collect(Collectors.toSet());
        return ObjectUtil.equal(bucketNameSet.size(), 1);
    }


}
