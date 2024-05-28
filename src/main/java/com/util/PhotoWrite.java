package com.util;

import java.sql.*;
import java.io.*;

class PhotoWrite {

    public static void main(String argv[]) {
        Connection con = null;
        PreparedStatement pstmt = null;
        InputStream fin = null;
        String url = "jdbc:mysql://localhost:3306/cinema?serverTimezone=Asia/Taipei";
        String userid = "root";
        String passwd = "123456";
        String photos = "src/main/resources/static/front_end/merchStore/image"; //測試用圖片已置於【專案錄徑】底下的【resources/DB_photos1】目錄內
        String update = "update merch set merch_img =? where merch_id=?";

        int count = 1;
        try {
            con = DriverManager.getConnection(url, userid, passwd);
            pstmt = con.prepareStatement(update);
            File[] photoFiles = new File(photos).listFiles();
            for (File f : photoFiles) {
                fin = new FileInputStream(f);
                pstmt = con.prepareStatement(update);
                pstmt.setBinaryStream(1, fin, (int) f.length());
                pstmt.setInt(2, count);
                pstmt.executeUpdate();
                count++;
                System.out.print(" update the database...");
                System.out.println(f.toString());
                fin.close();  // 每次文件操作后关闭
                pstmt.close();  // 每次操作后关闭
            }

//            fin.close();
//            pstmt.close();
            System.out.println("加入圖片-更新成功.........");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
//                con.close();
                if (con != null) con.close();  // 确保连接在最终块中关闭
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
