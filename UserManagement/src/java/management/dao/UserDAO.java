/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package management.dao;


import java.sql.*;
import java.util.*;
import management.model.User;

/**
 *
 * @author ANUBHAV
 */
//this class provides CRUD operation on database for the table users in the database management 
public class UserDAO {
    private String jdbcURL="jdbc:mysql://localhost:3306/management"; 
    private String jdbcUsername="root";
    private String jdbcPassword="sirftum";
    
    private static final String INSERT_USERS_SQL="insert into users"+"(name,email,country) values"+" (?,?,?);";
    private static final String SELECT_USER_BY_ID="select id,name,email,country from users where id=?";
    private static final String SELECT_ALL_USERS="select * from users";
    private static final String DELETE_USERS_SQL="delete from users where id=?;";
    private static final String UPDATE_USERS_SQL="update users set name=?,email=?,country=? where id=?;";
    
    protected Connection getConnection()
    {
        Connection connection =null;
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection=DriverManager.getConnection(jdbcURL,jdbcUsername,jdbcPassword);
           
        }
        catch(SQLException e)
        {
           e.printStackTrace();
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return connection;
    }
                
    //create or insert user
    public void insertUser(User user) throws SQLException
    {
        System.out.println(INSERT_USERS_SQL);
                
        try(Connection connection =getConnection(); PreparedStatement preparedStatement=connection.prepareStatement(INSERT_USERS_SQL))
        {
            preparedStatement.setString(1,user.getName());
            preparedStatement.setString(2,user.getEmail());
            preparedStatement.setString(3,user.getCountry());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public User selectUser(int id)
    {
        User user=null;
        try(Connection connection =getConnection(); PreparedStatement preparedStatement=connection.prepareStatement(SELECT_USER_BY_ID);)
        {
            preparedStatement.setInt(1,id);
            System.out.println(preparedStatement);
            
            ResultSet rs=preparedStatement.executeQuery();
            while(rs.next())
            {
                String name=rs.getString("name");
                String email=rs.getString("email");
                String country=rs.getString("country");
                user =new User(id,name,email,country);
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return user;
    }
    
    public List<User> selectAllUsers()
    {
        List<User > users=new ArrayList<>();
        try(Connection connection =getConnection(); PreparedStatement preparedStatement=connection.prepareStatement(SELECT_ALL_USERS);)
        {
            System.out.println(preparedStatement);
            
            ResultSet rs=preparedStatement.executeQuery();
            while(rs.next())
            {
                int id =rs.getInt("id");
                String name=rs.getString("name");
                String email=rs.getString("email");
                String country=rs.getString("country");
                users.add(new User(id,name,email,country));
            }
            
        }
          catch(SQLException e)
        {
            e.printStackTrace();
        }
        return users;
        
    }
    
    public boolean deleteUser(int id)throws SQLException
    {
        boolean rowDeleted;
           try(Connection connection =getConnection(); PreparedStatement statement=connection.prepareStatement(DELETE_USERS_SQL);)
        {
            statement.setInt(1, id);
            rowDeleted=statement.executeUpdate()>0;
        }   
    
         return rowDeleted;
    }
    
    public boolean updateUser(User user) throws SQLException
    {
        boolean rowUpdated;
        try(Connection connection =getConnection(); PreparedStatement statement=connection.prepareStatement(UPDATE_USERS_SQL);)
        {
            statement.setString(1, user.getName());
             statement.setString(2, user.getEmail());
              statement.setString(3, user.getCountry());
               statement.setInt(4, user.getId());
               
               rowUpdated=statement.executeUpdate()>0;
        }
        return rowUpdated;
    }
           
    private void printSQLException(SQLException ex)
    {
        for(Throwable e: ex)
        {
            if(e instanceof SQLException)
            {
                e.printStackTrace(System.err);
                System.err.println("SQLState : "+((SQLException) e).getSQLState());
                System.err.println("Error Code : "+((SQLException) e).getErrorCode());
                System.err.println("Message : "+e.getMessage());
                Throwable t=ex.getCause();
                while(t!=null)
                {
                    System.out.println("Clause : "+t);
                    t=t.getCause();
                }
            }
        }
    }
            
    //update user
    //select user by id
    //select users
    //delete user
    
}
