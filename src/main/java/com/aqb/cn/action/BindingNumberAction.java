package com.aqb.cn.action;

import com.aqb.cn.bean.BindingNumber;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.service.BindingNumberService;
import com.aqb.cn.utils.Request;
import com.aqb.cn.utils.UUIDCreator;
import com.aqb.cn.utils.getSession.MySessionContext;
import com.aqb.cn.utils.zxing.ImageHandleHelper;
import com.aqb.cn.utils.zxing.QRCodeUtil;
import com.aqb.cn.utils.zxing.TestImage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/5.
 */
@Controller
public class BindingNumberAction {

    private final Log logger = LogFactory.getLog(CofAction.class);

    @Autowired
    private BindingNumberService bindingNumberService;

    /**
     * 导入设备号
     * @param multipartFile
     * @return state : 0 -- 成功, 1--失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/bindingNumber/importBindingNumber", method = RequestMethod.POST)
    public Object importBindingNumber(HttpServletRequest request,@RequestParam("file")MultipartFile multipartFile){
        int status= Status.ERROR;
        String message = "";
        List<BindingNumber> bindingNumbers = new ArrayList<>();
        try {
            //创建一个临时文件
            File file = File.createTempFile("test01","xlsx");

            multipartFile.transferTo(file);
            // 构造 Workbook 对象，excleFile 是传入文件路径(获得Excel工作区)
            Workbook book = null;
            try {
                // Excel 2007获取方法
                book = new XSSFWorkbook(new FileInputStream(file));
            } catch (Exception ex) {
                // Excel 2003获取方法
                book = new HSSFWorkbook(new FileInputStream(file));
            }

            // 读取表格的第一个sheet页
            Sheet sheet = book.getSheetAt(0);
            // 定义 row、cell
            Row row;
            String cell;
            // 总共有多少行,从0开始
            int totalRows = sheet.getLastRowNum();
            //获取第二行的第二个、第三个单元格
//            Row row1 = sheet.getRow(0);
//            Row row2 = sheet.getRow(0);
            String batchNumber = sheet.getRow(1).getCell(1).toString();
            String batchDescribe = sheet.getRow(1).getCell(2).toString();
            Date date = new Date();
            // 循环输出表格中的内容,首先循环取出行,再根据行循环取出列
            for (int i = 3; i <= totalRows; i++) {//从第四行开始
                row = sheet.getRow(i);
                // 处理空行
                if(row == null){
                    continue;
                }
                BindingNumber bindingNumber = new BindingNumber();
                bindingNumber.setBatchNumber(batchNumber);
                bindingNumber.setBatchDescribe(batchDescribe);

                if(row.getCell(0) != null){
                    cell = row.getCell(0).toString();
                    int a=0;
                    for(BindingNumber b : bindingNumbers){//排除重复的设备号
                        if(b.getDeviceNumber().equals(cell)){
                            a=1;
                            break;
                        }
                    }
                    if(a == 1){
                        continue;
                    }
                    bindingNumber.setDeviceNumber(cell);
                }

                bindingNumber.setId(UUIDCreator.getUUID());
                bindingNumber.setStatus(0);
                bindingNumber.setFoundDate(date);

                bindingNumbers.add(bindingNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int key = bindingNumberService.addList(bindingNumbers);
        if (key != 0) {
            message = "导入成功，成功添加"+key+"条";
//            HttpSession session = request.getSession();
//            session.setAttribute(SESSION_FINANCINGCOMPANYS, financingCompanies);
            return new Response(Status.SUCCESS, message);
        }
        message = "添加融资企业失败";
        return new Response(status, message);
    }


    /**
     * 生成二维码
     */
    @ResponseBody
    @RequestMapping(value = "/api/bindingNumber/generateQRcode", method = RequestMethod.POST)
    public Object generateQRcode(HttpServletRequest request) {
        List<BindingNumber> bindingNumbers = bindingNumberService.queryWhole();
        for(BindingNumber bindingNumber : bindingNumbers){
            String batchNumber = bindingNumber.getBatchNumber();//版本号
            String batchDescribe = bindingNumber.getBatchDescribe();//版本描述
            String deviceNumber = bindingNumber.getDeviceNumber();//设备号
            //生成二维码
            try {
                QRCodeUtil.encode(deviceNumber, "E:/test/2.jpg", "E:/test/QRCode1", true);
            }catch (Exception e){
                e.printStackTrace();
            }
            //将版本号、设备号等信息，写入图片
            TestImage.exportImg1(batchNumber,batchDescribe,deviceNumber);
            //拼接图片
            String[] files = new String[2];//生成的文件路径
            files[0] = "E:/test/QRCode1/"+deviceNumber+".jpg";
            files[1] = "E:/test/QRCode2/"+deviceNumber+".jpg";
            int type = 2;//1  横向拼接， 2 纵向拼接
            String targetFile = "E:/test/QRCode/"+deviceNumber+".jpg";//拼接后的图片
            ImageHandleHelper.mergeImage(files,type,targetFile);

        }

        return new Response(Status.SUCCESS, "成功");
    }

}
