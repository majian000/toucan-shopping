package com.toucan.shopping.modules.common.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

public class VerifyCodeUtil {
    // 去掉了1,0,i,o几个容易混淆的字符，以及占用太宽的字符W
    public static final String VERIFY_CODES = "23456789ABCDEFGHJKLMNPQRSTUVXYZ";
    private static Random random = new Random();

    // ========== 内嵌位图字体（5宽 × 7高）==========
    // 每个字符用7个byte表示，每个byte的低5位代表一行，bit4=最左像素, bit0=最右像素
    // 完全不依赖操作系统字体，在 Windows / Linux / Docker 容器中均可正常渲染
    private static final Map<Character, byte[]> BITMAP_FONT = new HashMap<>();

    static {
        // @formatter:off
        BITMAP_FONT.put('2', new byte[]{0b11110, 0b10001, 0b00001, 0b00010, 0b00100, 0b01000, 0b11111});
        BITMAP_FONT.put('3', new byte[]{0b11110, 0b10001, 0b00001, 0b00110, 0b00001, 0b10001, 0b11110});
        BITMAP_FONT.put('4', new byte[]{0b10010, 0b10010, 0b10010, 0b11111, 0b00010, 0b00010, 0b00010});
        BITMAP_FONT.put('5', new byte[]{0b11111, 0b10000, 0b11110, 0b00001, 0b00001, 0b10001, 0b11110});
        BITMAP_FONT.put('6', new byte[]{0b11110, 0b10001, 0b10000, 0b11110, 0b10001, 0b10001, 0b11110});
        BITMAP_FONT.put('7', new byte[]{0b11111, 0b00001, 0b00010, 0b00100, 0b01000, 0b01000, 0b01000});
        BITMAP_FONT.put('8', new byte[]{0b11110, 0b10001, 0b10001, 0b11110, 0b10001, 0b10001, 0b11110});
        BITMAP_FONT.put('9', new byte[]{0b11110, 0b10001, 0b10001, 0b11111, 0b00001, 0b00001, 0b11110});
        BITMAP_FONT.put('A', new byte[]{0b00100, 0b01010, 0b10001, 0b11111, 0b10001, 0b10001, 0b10001});
        BITMAP_FONT.put('B', new byte[]{0b11110, 0b10001, 0b10001, 0b11110, 0b10001, 0b10001, 0b11110});
        BITMAP_FONT.put('C', new byte[]{0b01110, 0b10001, 0b10000, 0b10000, 0b10000, 0b10001, 0b01110});
        BITMAP_FONT.put('D', new byte[]{0b11100, 0b10010, 0b10001, 0b10001, 0b10001, 0b10010, 0b11100});
        BITMAP_FONT.put('E', new byte[]{0b11111, 0b10000, 0b10000, 0b11110, 0b10000, 0b10000, 0b11111});
        BITMAP_FONT.put('F', new byte[]{0b11111, 0b10000, 0b10000, 0b11110, 0b10000, 0b10000, 0b10000});
        BITMAP_FONT.put('G', new byte[]{0b01110, 0b10001, 0b10000, 0b10111, 0b10001, 0b10001, 0b01111});
        BITMAP_FONT.put('H', new byte[]{0b10001, 0b10001, 0b10001, 0b11111, 0b10001, 0b10001, 0b10001});
        BITMAP_FONT.put('J', new byte[]{0b00111, 0b00010, 0b00010, 0b00010, 0b00010, 0b10010, 0b01100});
        BITMAP_FONT.put('K', new byte[]{0b10001, 0b10010, 0b10100, 0b11000, 0b10100, 0b10010, 0b10001});
        BITMAP_FONT.put('L', new byte[]{0b10000, 0b10000, 0b10000, 0b10000, 0b10000, 0b10000, 0b11111});
        BITMAP_FONT.put('M', new byte[]{0b10001, 0b11011, 0b10101, 0b10101, 0b10001, 0b10001, 0b10001});
        BITMAP_FONT.put('N', new byte[]{0b10001, 0b11001, 0b10101, 0b10011, 0b10001, 0b10001, 0b10001});
        BITMAP_FONT.put('P', new byte[]{0b11110, 0b10001, 0b10001, 0b11110, 0b10000, 0b10000, 0b10000});
        BITMAP_FONT.put('Q', new byte[]{0b01110, 0b10001, 0b10001, 0b10001, 0b10101, 0b10010, 0b01101});
        BITMAP_FONT.put('R', new byte[]{0b11110, 0b10001, 0b10001, 0b11110, 0b10100, 0b10010, 0b10001});
        BITMAP_FONT.put('S', new byte[]{0b01111, 0b10000, 0b10000, 0b11110, 0b00001, 0b00001, 0b11110});
        BITMAP_FONT.put('T', new byte[]{0b11111, 0b00100, 0b00100, 0b00100, 0b00100, 0b00100, 0b00100});
        BITMAP_FONT.put('U', new byte[]{0b10001, 0b10001, 0b10001, 0b10001, 0b10001, 0b10001, 0b11110});
        BITMAP_FONT.put('V', new byte[]{0b10001, 0b10001, 0b10001, 0b10001, 0b10001, 0b01010, 0b00100});
        BITMAP_FONT.put('X', new byte[]{0b10001, 0b10001, 0b01010, 0b00100, 0b01010, 0b10001, 0b10001});
        BITMAP_FONT.put('Y', new byte[]{0b10001, 0b10001, 0b01010, 0b00100, 0b00100, 0b00100, 0b00100});
        BITMAP_FONT.put('Z', new byte[]{0b11111, 0b00001, 0b00010, 0b00100, 0b01000, 0b10000, 0b11111});
        // @formatter:on
    }

    /** 位图字体固有宽度（列数） */
    private static final int BITMAP_COLS = 5;
    /** 位图字体固有高度（行数） */
    private static final int BITMAP_ROWS = 7;


    /**
     * 使用系统默认字符源生成验证码
     *
     * @param verifySize 验证码长度
     * @return
     */
    public static String generateVerifyCode(int verifySize) {
        return generateVerifyCode(verifySize, VERIFY_CODES);
    }

    /**
     * 使用指定源生成验证码
     *
     * @param verifySize 验证码长度
     * @param sources    验证码字符源
     * @return
     */
    public static String generateVerifyCode(int verifySize, String sources) {
        if (sources == null || sources.length() == 0) {
            sources = VERIFY_CODES;
        }
        int codesLen = sources.length();
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder verifyCode = new StringBuilder(verifySize);
        for (int i = 0; i < verifySize; i++) {
            verifyCode.append(sources.charAt(rand.nextInt(codesLen - 1)));
        }
        return verifyCode.toString();
    }

    /**
     * 生成随机验证码文件,并返回验证码值
     *
     * @param w
     * @param h
     * @param outputFile
     * @param verifySize
     * @return
     * @throws IOException
     */
    public static String outputVerifyImage(int w, int h, File outputFile, int verifySize) throws IOException {
        String verifyCode = generateVerifyCode(verifySize);
        outputImage(w, h, outputFile, verifyCode);
        return verifyCode;
    }

    /**
     * 输出随机验证码图片流,并返回验证码值
     *
     * @param w
     * @param h
     * @param os
     * @param verifySize
     * @return
     * @throws IOException
     */
    public static String outputVerifyImage(int w, int h, OutputStream os, int verifySize) throws IOException {
        String verifyCode = generateVerifyCode(verifySize);
        outputImage(w, h, os, verifyCode);
        return verifyCode;
    }

    /**
     * 生成指定验证码图像文件
     *
     * @param w
     * @param h
     * @param outputFile
     * @param code
     * @throws IOException
     */
    public static void outputImage(int w, int h, File outputFile, String code) throws IOException {
        if (outputFile == null) {
            return;
        }
        File dir = outputFile.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            outputFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(outputFile);
            outputImage(w, h, fos, code);
            fos.close();
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 输出指定验证码图片流
     *
     * @param w      图片宽度
     * @param h      图片高度
     * @param os     输出流
     * @param code   验证码字符串
     * @throws IOException
     */
    public static void outputImage(int w, int h, OutputStream os, String code) throws IOException {
        int verifySize = code.length();
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Random rand = new Random();
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color[] colors = new Color[5];
        Color[] colorSpaces = new Color[]{Color.WHITE, Color.CYAN,
                Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,
                Color.PINK, Color.YELLOW};
        float[] fractions = new float[colors.length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = colorSpaces[rand.nextInt(colorSpaces.length)];
            fractions[i] = rand.nextFloat();
        }
        Arrays.sort(fractions);

        g2.setColor(Color.GRAY);// 设置边框色
        g2.fillRect(0, 0, w, h);

        Color c = getRandColor(200, 250);
        g2.setColor(c);// 设置背景色
        g2.fillRect(0, 2, w, h - 4);

        //绘制干扰线
        Random random = new Random();
        g2.setColor(getRandColor(160, 200));// 设置线条的颜色
        for (int i = 0; i < 20; i++) {
            int x = random.nextInt(w - 1);
            int y = random.nextInt(h - 1);
            int xl = random.nextInt(6) + 1;
            int yl = random.nextInt(12) + 1;
            g2.drawLine(x, y, x + xl + 40, y + yl + 20);
        }

        // 添加噪点
        float yawpRate = 0.05f;// 噪声率
        int area = (int) (yawpRate * w * h);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(w);
            int y = random.nextInt(h);
            int rgb = getRandomIntColor();
            image.setRGB(x, y, rgb);
        }

        shear(g2, w, h, c);// 使图片扭曲

        // ========== 使用内嵌位图字体绘制验证码（不依赖操作系统字体）==========
        int textColor = getRandColor(100, 160).getRGB();
        // 计算每个位图像素的物理尺寸，使字符适配图片区域
        int dotSize = Math.min((w - 10) / (verifySize * BITMAP_COLS), (h - 4) / BITMAP_ROWS);
        int charPixelWidth = BITMAP_COLS * dotSize;
        int charPixelHeight = BITMAP_ROWS * dotSize;
        // 每个字符分配的水平区域宽度
        int charAreaWidth = (w - 10) / verifySize;
        // 垂直居中
        int baseY = (h - charPixelHeight) / 2;

        char[] chars = code.toCharArray();
        for (int i = 0; i < verifySize; i++) {
            byte[] bitmap = BITMAP_FONT.get(chars[i]);
            if (bitmap == null) {
                continue; // 跳过未定义的字符
            }
            // 字符在图像中的起始X坐标（在分配区域内水平居中）
            int charStartX = charAreaWidth * i + 5 + (charAreaWidth - charPixelWidth) / 2;

            // 逐行逐列绘制位图像素
            for (int row = 0; row < BITMAP_ROWS; row++) {
                int rowBits = bitmap[row] & 0xFF;
                for (int col = 0; col < BITMAP_COLS; col++) {
                    // bit4 对应 col=0（最左），bit0 对应 col=4（最右）
                    if ((rowBits & (1 << (BITMAP_COLS - 1 - col))) != 0) {
                        int px = charStartX + col * dotSize;
                        int py = baseY + row * dotSize;
                        // 填充 dotSize × dotSize 的像素块
                        for (int dx = 0; dx < dotSize; dx++) {
                            for (int dy = 0; dy < dotSize; dy++) {
                                int ix = px + dx;
                                int iy = py + dy;
                                if (ix >= 0 && ix < w && iy >= 0 && iy < h) {
                                    image.setRGB(ix, iy, textColor);
                                }
                            }
                        }
                    }
                }
            }
        }

        g2.dispose();
        ImageIO.write(image, "jpg", os);
    }

    private static Color getRandColor(int fc, int bc) {
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    private static int getRandomIntColor() {
        int[] rgb = getRandomRgb();
        int color = 0;
        for (int c : rgb) {
            color = color << 8;
            color = color | c;
        }
        return color;
    }

    private static int[] getRandomRgb() {
        int[] rgb = new int[3];
        for (int i = 0; i < 3; i++) {
            rgb[i] = random.nextInt(255);
        }
        return rgb;
    }

    private static void shear(Graphics g, int w1, int h1, Color color) {
        shearX(g, w1, h1, color);
        shearY(g, w1, h1, color);
    }

    private static void shearX(Graphics g, int w1, int h1, Color color) {

        int period = random.nextInt(2);

        boolean borderGap = true;
        int frames = 1;
        int phase = random.nextInt(2);

        for (int i = 0; i < h1; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                    + (6.2831853071795862D * (double) phase)
                    / (double) frames);
            g.copyArea(0, i, w1, 1, (int) d, 0);
            if (borderGap) {
                g.setColor(color);
                g.drawLine((int) d, i, 0, i);
                g.drawLine((int) d + w1, i, w1, i);
            }
        }

    }

    private static void shearY(Graphics g, int w1, int h1, Color color) {

        int period = random.nextInt(40) + 10; // 50;

        boolean borderGap = true;
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < w1; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                    + (6.2831853071795862D * (double) phase)
                    / (double) frames);
            g.copyArea(i, 0, 1, h1, 0, (int) d);
            if (borderGap) {
                g.setColor(color);
                g.drawLine(i, (int) d, i, 0);
                g.drawLine(i, (int) d + h1, i, h1);
            }

        }

    }
}
