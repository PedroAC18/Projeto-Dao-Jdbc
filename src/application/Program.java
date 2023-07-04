package application;

import dao.DaoFactory;
import dao.SellerDAO;
import entities.Department;
import entities.Seller;

import java.util.Date;
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
        System.out.println();
        System.out.println("=== TEST 4: Seller insert===");
        Seller newSeller = new Seller(null, "Sara", "sara@gmail.com", new Date(), 5000.0, dep);
        sellerDAO.insert(newSeller);
        System.out.println("ID novo seller" + newSeller.getId());

        System.out.println();
        System.out.println("=== TEST 5: Seller insert===");
        seller = sellerDAO.findById(1);
        seller.setName("João");
        sellerDAO.update(seller);
        System.out.println("Update done");

        System.out.println();
        System.out.println("=== TEST 6: Seller delete===");
        sellerDAO.deleteById(5);
        System.out.println("Seller deleted");

    }
}
