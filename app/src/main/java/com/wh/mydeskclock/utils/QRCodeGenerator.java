package com.wh.mydeskclock.utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.HashMap;
import java.util.Map;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class QRCodeGenerator {
    private Map<EncodeHintType, String> hints;
    private Bitmap bitmap = null;
    private String str;
    private int WIDTH,HEIGHT;

    /**
     * @describe QRCode生成器 构造函数
     * @args str String 生成二维码的字符串
     *       WIDTH 生成的二维码宽度
     *       HEIGHT 生成的二维码高度
     * @return Void null
     *
     * */
    public QRCodeGenerator(String str, int WIDTH, int HEIGHT){
        this.str = (str.length()>300)?"str is too long":str;
        this.WIDTH=WIDTH;
        this.HEIGHT=HEIGHT;
        this.hints = new HashMap<>();
        this.hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
    }

    /**
     * @describe 生成二维码
     * @return Bitmap 返回生成的二维码图像
     * */
    public Bitmap getQRCode() {
        try {
            BitMatrix Result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);//通过字符串创建二维矩阵
            int width = Result.getWidth();
            int height = Result.getHeight();

            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = Result.get(x, y) ? BLACK : WHITE;//根据二维矩阵数据创建数组
                }
            }
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);//创建位图
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);//将数组加载到位图中
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
