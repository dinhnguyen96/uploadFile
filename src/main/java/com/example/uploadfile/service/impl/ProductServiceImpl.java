package com.example.uploadfile.service.impl;

import com.example.uploadfile.model.Product;
import com.example.uploadfile.service.IProductService;

import java.util.ArrayList;
import java.util.List;

public class ProductServiceImpl implements IProductService
{
    private List<Product> products = new ArrayList<>();

    @Override
    public List<Product> findAll() {
        return products;
    }

    @Override
    public void save(Product product)
    {
        product.setId(products.size()+1);
        products.add(product);
    }

    @Override
    public Product findById(int id)
    {
        if (products.size() > 0)
        {
            return products.get(id);
        }
        return null;

    }

    @Override
    public void update(int id, Product product) {
        for (Product p : products) {
            if (p.getId() == id) {
                p = product;
                break;
            }
        }
    }

    @Override
    public void remove(int id) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == id) {
                products.remove(i);
                break;
            }
        }
    }

}
