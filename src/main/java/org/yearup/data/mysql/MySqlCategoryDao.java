package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {
        // get all categories
        String sql="SELECT category_id, name, description FROM categories ORDER BY category_id";
        List<Category> categories=new ArrayList<>();
        try(Connection connection=getConnection()){
            PreparedStatement ps=connection.prepareStatement(sql);
            ResultSet row=ps.executeQuery();
            while(row.next()){
                Category category=mapRow(row);
                categories.add(category);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId)
    {
        // get category by id
        String sql="SELECT category_id, name, description FROM categories WHERE category_id=?";
        Category category=null;
        try(Connection connection=getConnection()){
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setInt(1,categoryId);
            ResultSet row=ps.executeQuery();
            while(row.next()){
                category=mapRow(row);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return category;
    }

    @Override
    public Category create(Category category)
    {
        // create a new category
        String sql="INSERT INTO categories(name, description) VALUES(?,?)";
        try (Connection connection = getConnection()){
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1,category.getName());
            ps.setString(2,category.getDescription());
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the generated keys
                ResultSet generatedKeys = ps.getGeneratedKeys();

                if (generatedKeys.next()) {
                    // Retrieve the auto-incremented ID
                    int categoryID = generatedKeys.getInt(1);

                    // get the newly inserted category
                    return getById(categoryID);
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        // update category
        String sql="UPDATE categories SET name=?, description=? WHERE category_id=?";
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,category.getName());
            statement.setString(2,category.getDescription());
            statement.setInt(3,categoryId);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
        String sql = "DELETE FROM categories " +
                " WHERE category_id = ?;";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
