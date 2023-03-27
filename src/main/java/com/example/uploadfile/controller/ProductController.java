package com.example.uploadfile.controller;

import com.example.uploadfile.model.Product;
import com.example.uploadfile.model.ProductForm;
import com.example.uploadfile.service.IProductService;
import com.example.uploadfile.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService productService;


    @Autowired
    private Environment environment;



    @GetMapping("/")
    public ModelAndView index()
    {
        ModelAndView modelAndView = new ModelAndView();
        List<Product> products = productService.findAll();
        modelAndView.addObject("products", products);
        modelAndView.setViewName("/index");
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable("id") int id)
    {
        Product product = productService.findById(id);
        ProductForm productForm = new ProductForm(product.getId(), product.getName(), product.getDescription());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("productForm", productForm);
        modelAndView.addObject("status", 0);
        modelAndView.setViewName("/edit");
        return modelAndView;
    }


    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("productForm", new ProductForm());
        modelAndView.addObject("status", 1);
        modelAndView.setViewName("/edit");
        return modelAndView;
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("productForm") ProductForm productForm, HttpServletRequest request)
    {
        // lấy hình ảnh từ phía client lên server
        MultipartFile multipartFile = productForm.getImage();

        if (multipartFile != null)
        {
            // lay ten file sau khi upload luu vao database
            String nameFile = multipartFile.getOriginalFilename();

            Product product = productService.findById(productForm.getId());

            if (product == null)
            {
                product = new Product(productForm.getName(), productForm.getDescription(), nameFile);
                productService.save(product);
            }
            else
            {
                product.setName(productForm.getName());
                product.setDescription(productForm.getDescription());
                product.setImage(nameFile);
                productService.update(product.getId(), product);
            }
            try
            {
                //day la duong dan de luu file len server
                String pathFile = environment.getProperty("uploadFileLocation");
                FileCopyUtils.copy(multipartFile.getBytes(), new File(pathFile+nameFile));
            }
            catch (IOException e)
            {
                System.out.println("Save Error File");
            }
            return "redirect:/product/";
        }
        return "/edit";
    }
}