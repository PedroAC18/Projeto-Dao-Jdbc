package dao.impl;

import dao.SellerDAO;
import db.DB;
import db.DbException;
import db.DbIntegrityException;
import entities.Department;
import entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDAO {

    private Connection conn;

    public SellerDaoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement(
                    "INSERT INTO seller\n" +
                    "(Name, Email, BirthDate, BaseSalary, DepartmentId) \n" +
                    "VALUES \n" +
                    "(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1,obj.getName());
            st.setString(2,obj.getEmail());
            st.setDate(3,new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4,obj.getBaseSalary());
            st.setInt(5,obj.getDepartment().getId());

            int rowsAffected = st.executeUpdate();

            if(rowsAffected>0){
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);//associando o id ao novo objeto instanciado
                }
                DB.closeResultSet(rs);
            }else{
                throw new DbException("Erro inesperado");
            }
        }
        catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Seller obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement(
                    "UPDATE seller \n" +
                    "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? \n" +
                    "WHERE Id = ?");
            st.setString(1,obj.getName());
            st.setString(2,obj.getEmail());
            st.setDate(3,new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4,obj.getBaseSalary());
            st.setInt(5,obj.getDepartment().getId());
            st.setInt(6, obj.getId());

            st.executeUpdate();
        }
        catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }

    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "DELETE FROM seller \n" +
                    "WHERE Id = ?");

            st.setInt(1,id);
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName \n" +
                            "FROM seller INNER JOIN department \n" +
                            "ON seller.DepartmentId = department.Id \n" +
                            "WHERE seller.Id = ?"
            );
            st.setInt(1, id);
            rs = st.executeQuery();//traz os dados em formato de tabela
            //necessário criar o objeto Seller e o Department dele
            if(rs.next()){
                Department dep = instantiateDepartment(rs);
                Seller obj = instantitateSeller(rs,dep);
                return obj;
            }
            return null;//se retornar nulo, é porque não havia seller

        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    @Override
    public List<Seller> findAll() {

        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("SELECT seller.*,department.Name as DepName \n" +
                    "FROM seller INNER JOIN department \n" +
                    "ON seller.DepartmentId = department.Id\n" +
                    "ORDER BY Name");

            rs = st.executeQuery();//traz os dados em formato de tabela

            List<Seller> listSeller = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();//garantir que valores não se repitam

            //necessário criar o objeto Seller e o Department dele
            while(rs.next()){//pode haver mais de um valor, então usa-se o while

                Department dep = map.get(rs.getInt("DepartmentId"));//ver se o dep já existe
                if(dep == null){
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);//salva para que em próximas consultas já esteja indicado a existência
                }
                Seller obj = instantitateSeller(rs,dep);
                listSeller.add(obj);
            }
            return listSeller;//se retornar nulo, é porque não havia seller

        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("SELECT seller.*,department.Name as DepName \n" +
                    "FROM seller INNER JOIN department \n" +
                    "ON seller.DepartmentId = department.Id\n" +
                    "WHERE DepartmentId = ?\n" +
                    "ORDER BY Name");
            st.setInt(1, department.getId());
            rs = st.executeQuery();//traz os dados em formato de tabela

            List<Seller> listSeller = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();//garantir que valores não se repitam

            //necessário criar o objeto Seller e o Department dele
            while(rs.next()){//pode haver mais de um valor, então usa-se o while

                Department dep = map.get(rs.getInt("DepartmentId"));//ver se o dep já existe
                if(dep == null){
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);//salva para que em próximas consultas já esteja indicado a existência
                }
                Seller obj = instantitateSeller(rs,dep);
                listSeller.add(obj);
            }
            return listSeller;//se retornar nulo, é porque não havia seller

        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException{
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    private Seller instantitateSeller(ResultSet rs, Department dep) throws SQLException{

        Seller aux = new Seller();
        aux.setId(rs.getInt("Id"));
        aux.setName(rs.getString("Name"));
        aux.setEmail(rs.getString("Email"));
        aux.setBaseSalary(rs.getDouble("BaseSalary"));
        aux.setBirthDate(rs.getDate("BirthDate"));
        aux.setDepartment(dep);

        return aux;
    }
}
