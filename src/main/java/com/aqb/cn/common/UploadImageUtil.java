package com.aqb.cn.common;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2017/8/5/0005.
 */

public class UploadImageUtil {

    /**
     * 上传文件
     *
     * @param file           上传的文件
     * @param uploadPath     上传路径相对路径
     * @param realUploadPath 原图实际路径
     * @return
     */
    public static String uploadImage(CommonsMultipartFile file, String uploadPath, String realUploadPath) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = file.getInputStream();
            String des = realUploadPath + "/" + file.getOriginalFilename();
            os = new FileOutputStream(des);
            byte[] buffer = new byte[1024];
            int len = 0;

            while ((len = is.read(buffer)) > 0) {
                os.write(buffer, 0, len);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return uploadPath + "/" + file.getOriginalFilename();
    }


    //生成缩略图
    public static String thumbnailUploadImage(CommonsMultipartFile file, int width, int height, String uploadPath, String realUploadPath) {
        String des = realUploadPath + "/thum" + file.getOriginalFilename();//缩略图实际存储路径
        try {
            Thumbnails.of(file.getInputStream()).size(width, height).toFile(des);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uploadPath + "/thum" + file.getOriginalFilename();
    }
}