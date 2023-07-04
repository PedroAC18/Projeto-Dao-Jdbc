package dao;

import dao.impl.DepartmentDaoJDBC;
import dao.impl.SellerDaoJDBC;
import db.DB;

public class DaoFactory {
    public static SellerDAO createSellerDao(){
        return new SellerDaoJDBC(DB.getConnection());//faz a conexão com o BD do MySQL
    }

    public static DepartmentDAO createDepartmentDao() {
        return new DepartmentDaoJDBC(DB.getConnection());
    }
}
