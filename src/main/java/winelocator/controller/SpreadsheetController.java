package winelocator.controller;

import winelocator.dao.MongoDao;
import winelocator.domain.Data;
import winelocator.domain.Product;
import winelocator.domain.Spreadsheet;
import winelocator.service.DataService;
import winelocator.service.ProductService;
import winelocator.util.SpreadsheetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class SpreadsheetController {
    @Autowired
    ProductService productService;

    @Autowired
    DataService dataService;

    @Autowired
    SpreadsheetUtil spreadsheetUtil;

    @Autowired
    MongoDao mongoDao;

    Spreadsheet spreadsheet;
    Logger logger = LoggerFactory.getLogger(SpreadsheetController.class);

    @PostMapping("/admin/upload_spreadsheet")
    public String upload_spreadsheet(@RequestPart("file")MultipartFile file, @RequestParam("product_hidden_field")String product,
                                     @RequestParam("notes")String notes, RedirectAttributes redirectAttributes){
        List<Data> data;
        Product productObj;
        try {
            spreadsheet = new Spreadsheet();
            spreadsheet.setFile(file.getBytes());
            spreadsheet.setFilename(file.getOriginalFilename());
            spreadsheet.setMime(file.getContentType());
            spreadsheet.setNotes(notes);
            spreadsheet.setVersion(productService.getProductByName(product).getVersion()+1);
            spreadsheet.setDate(new Date());
            productService.addSpreadsheet(product, spreadsheet);

            logger.info("Creating data objects from " + file.getOriginalFilename() + "...");
            data = dataService.getDataFromXlsxLines(
                    spreadsheetUtil.getLinesFromXlsx(file.getInputStream()), file.getOriginalFilename(),
                    productService.getProductByName(product).getVersion(), product);
            dataService.getData().addAll(data);
            mongoDao.insertData(dataService.getData());

            // TODO: 10/10/2018 Why is this here?
            productObj = productService.getProductByName(product);
            mongoDao.removeProduct(productObj);
            mongoDao.insertProduct(productObj);

            redirectAttributes.addFlashAttribute("message", file.getOriginalFilename() +
                    " spreadsheet added to product " + product)
                    .addFlashAttribute("class", "alert alert-success");
        } catch (IOException e) {
            logger.error(e.getMessage());
            redirectAttributes.addFlashAttribute("message", "Spreadsheat upload failed :(")
                    .addFlashAttribute("class", "alert alert-warning");
        }

        return "redirect:/admin/spreadsheet";
    }

    @GetMapping("/admin/spreadsheet")
    public ModelAndView spreadsheet(HttpSession session){
        ModelAndView mav = new ModelAndView();
//        logger.info("products: " + productService.getProductNames().toString());
        Product product = productService.getProductByName((String)session.getAttribute("product"));
        productService.setProducts(mongoDao.getProducts());
        if(product != null) {
            mav.addObject("spreadsheets", product.getSpreadsheets());
            logger.info("spreadsheets (product): " + product.getSpreadsheets().toString());
        }
        mav.addObject("products", productService.getProductNames());
        logger.info(mav.getModel().toString());
        mav.setViewName("spreadsheet");
        if(!productService.productExists((String)session.getAttribute("product"))){
            session.removeAttribute("product");
        }
        return mav;
    }

    @PostMapping("/admin/set_product")
    public ResponseEntity set_spreadsheet(@RequestParam("product")String p, HttpSession session){
        session.setAttribute("product", p);
        return new ResponseEntity(HttpStatus.OK);
    }

}
