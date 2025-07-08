package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.SysFile;
import com.example.model.dto.SysFileDTO;
import com.example.model.vo.SysFileVO;
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

    Boolean upload(MultipartFile file);


    Boolean delete(List<Integer> fileIdList);

    List<SysFileVO> search(SysFileDTO sysFileDto);
}
