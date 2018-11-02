package winelocator.controller;

import winelocator.dao.MongoDao;
import winelocator.domain.Product;
import winelocator.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductController {
    Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    ProductService productService;

    @Autowired
    MongoDao mongoDao;

    @GetMapping("/admin/product")
    public ModelAndView index(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("products", mongoDao.getProducts());
        mav.setViewName("product");
        return mav;
    }

    @PostMapping("/admin/add_product")
    public String add_product(@RequestParam("name")String name, RedirectAttributes redirectAttributes){
        if(!name.equals("") && productService.getProductByName(name) == null && productService.addProduct(name)) {
            mongoDao.insertProduct(productService.getProductByName(name));
            redirectAttributes.addFlashAttribute("message",
                    "Product: " + name + " added :)")
                    .addFlashAttribute("class", "alert alert-success");
        } else {
            redirectAttributes.addFlashAttribute("message",
                    "Unable to add product :(")
                    .addFlashAttribute("class", "alert alert-warning");
        }
        return "redirect:/admin/product";
    }

    @PostMapping("/admin/edit_product")
    public String edit_product(@RequestParam("original_name")String original_name,
                               @RequestParam("new_name")String new_name, RedirectAttributes redirectAttributes){
        if(!original_name.equals("") && !new_name.equals("") && productService.getProductByName(original_name) != null) {
            mongoDao.removeProduct(productService.getProductByName(original_name));
            productService.editProduct(original_name, new_name);
            mongoDao.insertProduct(productService.getProductByName(new_name));
            redirectAttributes.addFlashAttribute("message",
                    "Product: " + original_name + " now called " + new_name + ".")
                    .addFlashAttribute("class", "alert alert-success");
        } else {
            redirectAttributes.addFlashAttribute("message",
                    "Product update unsuccessful :(")
            .addFlashAttribute("class", "alert alert-warning");
        }

        return "redirect:/admin/product";
    }

    @PostMapping("/admin/delete_product")
    public String delete_product(@RequestParam("name")String name, RedirectAttributes redirectAttributes){
        Product product = productService.getProductByName(name);
        if(productService.removeProduct(name)) {
            mongoDao.removeProduct(product);
            redirectAttributes.addFlashAttribute("message",
                    "Product: " + name + " deleted.")
                    .addFlashAttribute("class", "alert alert-success");
        } else {
            redirectAttributes.addFlashAttribute("message",
                    "Product deletion unsuccessful :(")
                    .addFlashAttribute("class", "alert alert-warning");
        }
        return "redirect:/admin/product";
    }

}
