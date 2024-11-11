package com.tms.sportlight.util;

import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class ImageUtil {

    @Value("${file.img.extension}")
    private String IMG_EXT;
    @Value("${file.thumb.size.width}")
    private int THUMB_WIDTH;
    @Value("${file.thumb.size.height}")
    private int THUMB_HEIGHT;
    @Value("${file.icon.size.width}")
    private int ICON_WIDTH;
    @Value("${file.icon.size.height}")
    private int ICON_HEIGHT;

    /**
     * sourceImageFile에 대한 썸네일 이미지 파일 생성
     *
     * @param sourceImageFile 원 이미지 파일
     * @return 썸네일 이미지 byte array
     */
    public byte[] createThumbImg(MultipartFile sourceImageFile) {
        return drawImage(sourceImageFile, THUMB_WIDTH, THUMB_HEIGHT);
    }

    /**
     * sourceImageFile에 대한 아이콘 이미지 파일 생성
     *
     * @param sourceImageFile 원 이미지 파일
     * @return 아이콘 이미지 byte array
     */
    public byte[] createIconImg(MultipartFile sourceImageFile) {
        return drawImage(sourceImageFile, ICON_WIDTH, ICON_HEIGHT);
    }

    private byte[] drawImage(MultipartFile sourceFile, int width, int height) {
        BufferedImage bufferedThumbImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphic = bufferedThumbImage.createGraphics();
        byte[] thumbByteArray = null;
        try (InputStream inputStream = sourceFile.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            BufferedImage bufferedOrigImage = ImageIO.read(inputStream);
            graphic.drawImage(bufferedOrigImage, 0, 0, width, height, null);
            ImageIO.write(bufferedThumbImage, IMG_EXT, outputStream);
            thumbByteArray = outputStream.toByteArray();
        } catch (IOException e) {
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return thumbByteArray;
    }
}
