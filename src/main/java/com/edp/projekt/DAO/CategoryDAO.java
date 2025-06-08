package com.edp.projekt.DAO;

import com.edp.projekt.db.Categories;
import com.edp.projekt.db.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryDAO {
    private static final Logger logger = Logger.getLogger(CategoryDAO.class.getName());

    public static Categories getById(int id){
        String sql = "SELECT * FROM categories WHERE id = ?";
        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Categories category = new Categories();
                category.setId(id);
                category.setName(rs.getString("name"));
                return category;
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in getById: " + e.getMessage(), e);
        }
        return null;
    }

    public static ArrayList<Categories> getAll(){
        String sql = "SELECT * FROM categories";
        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ArrayList<Categories> categories = new ArrayList<>();
            while(rs.next()){
                Categories category = new Categories();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                categories.add(category);
            }
            return categories;
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in getAll: " + e.getMessage(), e);
        }
        return null;
    }

    public static ArrayList<Categories> getAllByType(String type){
        String sql = "SELECT * FROM categories WHERE type = ?";
        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();
            ArrayList<Categories> categories = new ArrayList<>();
            while(rs.next()){
                Categories category = new Categories();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                categories.add(category);
            }
            return categories;
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in getAll: " + e.getMessage(), e);
        }
        return null;
    }

    public static int getCategoryId(String name) {
        String sql = "SELECT * FROM categories WHERE name = ?";
        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt("id");
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in getCategoryById: " + e.getMessage(), e);
        }
        return -1;
    }
}
