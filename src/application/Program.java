package application;

import dao.DaoFactory;
import dao.SellerDAO;
import entities.Seller;

public class Program {
    public static void main(String[] args) {
        SellerDAO sellerDAO = DaoFactory.createSellerDao();

        System.out.println("=== TEST 1: Seller findByID===");
        
        Seller seller = sellerDAO.findById(3);

        System.out.println(seller);

    }
}
