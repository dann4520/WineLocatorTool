package winelocator.service;

import winelocator.domain.Product;
import winelocator.domain.Spreadsheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

@Service
public class ProductService {
    Logger logger = LoggerFactory.getLogger(ProductService.class);
    private List<Product> products = new ArrayList<>();
    Product product;

    public List<Product> getProducts(){
        return products;
    }

    public boolean removeProduct(String name){
        boolean found = false;
        ListIterator<Product> listIterator = products.listIterator();
        while(listIterator.hasNext()){
            if(listIterator.next().getName().equals(name)){
                listIterator.remove();
                found = true;
            }
        }
        return found;
    }

    public boolean productExists(String name){
        boolean found = false;
        ListIterator<Product> listIterator = products.listIterator();
        while(listIterator.hasNext()){
            if(listIterator.next().getName().equals(name)){
                found = true;
            }
        }
        return found;
    }

    public boolean updateProduct(String name, Product product){
        boolean found = false;
        ListIterator<Product> listIterator = products.listIterator();
        while(listIterator.hasNext()){
            if(listIterator.next().getName().equals(name)){
                listIterator.set(product);
                found = true;
            }
        }
        return found;
    }

    public boolean addProduct(String name){
        boolean added = false;
        product = getProductByName(name);
        if(product == null) {
            Product newProduct = new Product();
            newProduct.setSpreadsheets(new ArrayList<>());
            newProduct.setVersion(0);
            newProduct.setLastModified(new Date());
            newProduct.setName(name);
            products.add(newProduct);
            added = true;
        }
        return added;
    }
    public boolean editProduct(String oldName, String newName){
        boolean found = false;
        product = getProductByName(oldName);
        if(product != null) {
            product.setName(newName);
            found = true;
        }
        return found;
    }
    public void addSpreadsheet(String productName, Spreadsheet spreadsheet){
        Product product = getProductByName(productName);
        List<Spreadsheet> spreadsheets = product.getSpreadsheets();
        spreadsheets.add(spreadsheet);
        product.setSpreadsheets(spreadsheets);
        product.setLastModified(new Date());
        product.setVersion(product.getVersion()+1);
        updateProduct(product.getName(), product);
    }

    public List<String> getProductNames(){
        List<String> names = new ArrayList<>();
        for(Product p: products){
            names.add(p.getName());
        }
        logger.info("products: " + names);
        return names;
    }

    public Product getProductByName(String name){
        Product product = null;
        for(Product p: products){
            if(p.getName().equals(name)){
                product = p;
            }
        }
        return product;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
