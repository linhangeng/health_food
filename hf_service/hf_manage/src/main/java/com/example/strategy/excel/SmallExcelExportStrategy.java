package com.example.strategy.excel;

import cn.hutool.core.util.ObjectUtil;
import com.example.domain.SysFile;
import com.example.model.vo.SysFileVO;
import com.example.util.EasyExcelUtil;
import com.example.util.LocalDateTimeUtils;
import org.springframework.beans.BeanUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linhangeng
 * @version 1.0
 * @description: TODO
 * @date 2025/7/20 15:00
 */
public class SmallExcelExportStrategy<T> implements ExcelExportStrategy<T> {


    /**
     * 具体实现
     * @param
     * @Author sheng.lin
     * @Date 2025/7/20
     * @return: void
     * @Version  1.0
     * @修改记录
     */
    @Override
    public void execute(ExcelContext excelContext) {
        List<SysFile> sysFiles = excelContext.getSysFileService().list();
        List<SysFileVO> sysFileVOS = new ArrayList<>();
        sysFiles.forEach(e->{
            SysFileVO sysFileVO = new SysFileVO();
            BeanUtils.copyProperties(e, sysFileVO);
            if (ObjectUtil.isNotEmpty(e.getCreateTime())) {
                sysFileVO.setCreateTime(LocalDateTimeUtils.format(LocalDateTimeUtils.fromMillis(e.getCreateTime()),LocalDateTimeUtils.YMD_HMS));
            }
            sysFileVOS.add(sysFileVO);
        });
        EasyExcelUtil.exportSmall(excelContext.getHttpServletResponse(), sysFileVOS, SysFileVO.class, URLEncoder.encode("文件数据", StandardCharsets.UTF_8), "文件信息");
    }
}
