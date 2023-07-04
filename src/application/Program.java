package application;

import dao.DaoFactory;
import dao.SellerDAO;
import entities.Department;
import entities.Seller;

import java.util.List;

public class Program {
    public static void main(String[] args) {
        SellerDAO sellerDAO = DaoFactory.createSellerDao();

        System.out.println("=== TEST 1: Seller findByID===");
        Seller seller = sellerDAO.findById(3);
        System.out.println(seller);
        System.out.println();
        System.out.println("=== TEST 2: Seller findByDepartment===");
        Department dep = new Department(2,null);
        List<Seller> list = sellerDAO.findByDepartment(dep);
        for(Seller obj: list){
            System.out.println(obj);
        }
        System.out.println();
        System.out.println("=== TEST 3: Seller findALL===");
        list = sellerDAO.findAll();
        for(Seller obj: list){
            System.out.println(obj);
        }

    }
}
