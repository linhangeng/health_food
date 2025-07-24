package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.SysFile;
import com.example.model.dto.SysFileDTO;
import com.example.model.vo.SysFileVO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @ClassName VideoService
 * @Author sheng.lin
 * @Date 2025/7/2
 * @Version 1.0
 * @修改记录
 **/
public interface SysFileService extends IService<SysFile> {

    /**
     * 上传
     */
    Boolean upload(MultipartFile file);

    /**
     * 删除
     */
    Boolean delete(List<Integer> fileIdList);

    /**
     * 查询
     */
    List<SysFileVO> search(SysFileDTO sysFileDto);

    /**
     * 导出
     */
    void export(HttpServletResponse httpServletResponse);


    Boolean saveSysFile(SysFileDTO sysFileDto);

    /**
     * 添加测试数据
     */
    Boolean saveTestData();

}
