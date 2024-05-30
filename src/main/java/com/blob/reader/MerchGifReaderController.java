package com.blob.reader;

import com.service.MerchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Controller
@RequestMapping("/merch")
public class MerchGifReaderController {

    @Autowired
    MerchService merchService;

    /*
     * This method will serve as listOneEmp.html , listAllEmp.html handler.
     */
    @GetMapping("DBGifReader")
    public void dBGifReader(@RequestParam("merchId") String merchId, HttpServletRequest req, HttpServletResponse res)
            throws IOException {
//        res.setContentType("image/gif");
//        ServletOutputStream out = res.getOutputStream();
//
//        try {
////			EmpService empSvc = new EmpService();
//            out.write(merchService.getbyMerchId(Integer.valueOf(merchId)).getMerchImg());
//        } catch (Exception e) {
//            byte[] buf = java.util.Base64.getDecoder().decode("front_end/merchStore/images/merch.jpg");
//            out.write(buf);
//        }
    }
}
