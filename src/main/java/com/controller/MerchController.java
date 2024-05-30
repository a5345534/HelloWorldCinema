package com.controller;


import com.entity.Emp;
import com.entity.Merch;
import com.service.MerchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/merch")
@Component
public class MerchController {

    @Autowired
    MerchService merchService;

    @GetMapping("/merchId/{id}")
    public String showProductPage(@PathVariable Integer id, Model model) {
        Merch merch = merchService.getbyMerchId(id);
        model.addAttribute("merch", merch);
        return "front_end/merchStore/TestSingleMerch"; // 返回單獨商品頁面的HTML文件名
    }

    @GetMapping("/front")
    public String front() {
        return "front_end/merchStore/TestMerchStore";
    }


//    public String front() {
//        return "front_end/merchStore/TestMerchStore";
//    }

    @GetMapping("/frontsingle")
    public String frontsingle() {
        return "front_end/merchStore/TestSingleMerch";
    }

    @PostMapping("/toggleMerchStatus")
    public String toggleMerchStatus(@Valid Merch merchVo) {

        Merch merch = merchService.getbyMerchId(merchVo.getMerchId());
        String text = "上架";
        if (merch.getMerchStatus().equals("上架")) {
            text = "下架";
        }
        merch.setMerchStatus(text);
        merchService.updateMerch(merch);
        return "redirect:/merch/listAllMerch";
    }


    @PostMapping("/addMerch")
    public String addMerch(Model model) {

        Merch merch = new Merch();
        model.addAttribute(merch);
        return "back_end/merchStore/addMerch";
    }

    @GetMapping("/addMerch")
    public String getaddMerch(Model model) {

        Merch merch = new Merch();
        model.addAttribute(merch);
        return "back_end/merchStore/addMerch";
    }


    @PostMapping("/insertMerch")
    public String insert(@Valid Merch merch, BindingResult result, Model model, @RequestParam("merchImg") MultipartFile[] parts) throws IOException {
/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        // 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
        result = removeFieldError(merch, result, "merchImg");

        if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
            model.addAttribute("errorMessage", "照片: 請上傳照片");
        } else {
            for (MultipartFile multipartFile : parts) {
                byte[] buf = multipartFile.getBytes();
                String b64 = Base64.getEncoder().encodeToString(buf);
                merch.setMerchImg(b64);
            }
        }
        if (result.hasErrors() || parts[0].isEmpty()) {
            return "back_end/merchStore/addMerch";
        }

        merchService.addMerch(merch);
        List<Merch> list = merchService.getAll();
        model.addAttribute("merchListDate", list);
        model.addAttribute("success", "修改成功");
        return "back_end/merchStore/listAllMerch";

    }

    @PostMapping("/getbyMerchId")
    public String getbyMerchId(@RequestParam("merchId") Integer merchId, Model model) {
        Merch merch = merchService.getbyMerchId(merchId);
        model.addAttribute("Merch", merch);
        return "front_end/merchStore/TestSingleMerch";

    }

    @PostMapping("getOne_For_Update")
    public String getOne_For_Update(@RequestParam("merchId") String merchId, ModelMap model) {

        // EmpService empSvc = new EmpService();
        Merch merch = merchService.getbyMerchId(Integer.valueOf(merchId));
        model.addAttribute("merch", merch);
        return "back_end/merchStore/updateMerch"; // 查詢完成後轉交update_emp_input.html
    }

    @PostMapping("/updateMerch")
    public String updateMerch(@Valid Merch merch, BindingResult result, Model model, @RequestParam("merchImg") MultipartFile[] parts) throws IOException {

        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        // 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
        result = removeFieldError(merch, result, "upFiles");

        if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
            // EmpService empSvc = new EmpService();
//            byte[] merchImg = merchService.getbyMerchId(merch.getMerchId()).getMerchImg();
//            merch.setMerchImg(merchImg);
        } else {
            for (MultipartFile multipartFile : parts) {
                byte[] merchImg = multipartFile.getBytes();
                String s = Base64.getEncoder().encodeToString(merchImg);
                merch.setMerchImg(s);
            }
        }
        if (result.hasErrors()) {
            return "back-end/merchStore/update_merch_input";
        }

        merchService.updateMerch(merch);
        model.addAttribute("success", "修改成功");
        merch = merchService.getbyMerchId(Integer.valueOf(merch.getMerchId()));
        return "back_end/merchStore/listAllMerch";

    }

    public BindingResult removeFieldError(Merch merch, BindingResult result, String removedFieldname) {
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
                .collect(Collectors.toList());
        result = new BeanPropertyBindingResult(merch, "merch");
        for (FieldError fieldError : errorsListToKeep) {
            result.addError(fieldError);
        }
        return result;
    }

    @PostMapping("/toUpdateMerch")
    public String getupdateMerch(@ModelAttribute("merchId") Merch merch, Model model) {
        merch = merchService.getbyMerchId(merch.getMerchId());
        model.addAttribute("merch", merch);
        return "back_end/merchStore/updateMerch";
    }

    @PostMapping("doUpdateMerch")
    public String update(@ModelAttribute Merch merch, @RequestParam("base64Img") String base64Img, Model model) {
        // 將 base64Img 賦值給 merch 實體
        if (base64Img != null && !base64Img.isEmpty()) {
            merch.setMerchImg(base64Img);
        }
        merchService.updateMerch(merch);
        model.addAttribute("merch", merch);
        return "back_end/merchStore/updateMerch"; // 修改成功後轉交listOneEmp.html
    }

    @PostMapping("/deleteMerch")
    public String deleteMerch(@RequestParam("merchId") Integer merchId, Model model) {
        merchService.deleteMerch(merchId);
//        List<Merch> list = merchService.getAll();
//        model.addAttribute("merchListDate", list);
//        model.addAttribute("success", "刪除成功");
        return "redirect:/merch/listAllMerch";


    }

    @GetMapping("/listAllMerch")
    public String listAllMerch(Model model) {
        List<Merch> merchList = merchService.getAll();
        model.addAttribute("merchListData", merchList);
        return "back_end/merchStore/listAllMerch";
    }

    @ModelAttribute("merchListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
    protected List<Merch> referenceListData(Model model, HttpServletRequest request) {
        String scheme = request.getScheme();             // http
        String serverName = request.getServerName();     // localhost
        int serverPort = request.getServerPort();        // 8080
        String contextPath = request.getContextPath();   // /merch
        String rootPath = scheme + "://" + serverName + ":" + serverPort;


        List<Merch> list = merchService.getAll();
//        list.forEach(item -> {
//            boolean b = item.getMerchImg().startsWith("\uFEFF");
//            item.setMerchImg(item.getMerchImg().substring(1));
//        });
        return list;
    }

    @PostMapping("/getbyMerchStatus")
    public String MerchStatus(Model model) {
        List<Merch> merchlist = merchService.getbyMerchStatus("上架");
        model.addAttribute("merchStatusList", merchlist);
        return "front_end/merchStore/TestMerchStore";
    }

    @ModelAttribute("merchListStatus")
    public List<Merch> listStatus(Model model) {
        List<Merch> list = merchService.getbyMerchStatus("上架");
        return list;
    }
}
