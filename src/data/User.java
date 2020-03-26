package data;

import java.util.Base64;

import db.Database;
import db.Table;

public class User {

    private String username;
    private String email;
    private static Database usersDB;
    private static Table usersTable;

    public static void setDb(Database db){
        usersDB = db;
        usersTable = usersDB.getTable("employeeDat");
    }

    public User(String username) {
        this.username = username;
        if(usersDB == null) {
            usersDB = Database.getDatabase("employee");
            usersTable = usersDB.getTable("employeeDat");
        }
    }

    public User(String username, String email) {
        this(username);
        this.email = email;
    }

    public String getUserName() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public boolean registerUser(String password) {
        if(usersTable.containsString("name", username)) return false;
        Password securePassword = Encrypt.hashPassword(password);
        usersTable.insertString(username,email,securePassword.getHash(),securePassword.getSalt());
        return true;
    }

    public boolean authenticate(String password) {

        if(!usersTable.containsString("name", username)) return false;

        String userHashedPass = usersTable.getString("name='"+username+"'", "password");
        String salt = usersTable.getString("name='"+username+"'", "salt");

        Password enteredPassword = Encrypt.hashPassword(password,Base64.getDecoder().decode(salt));
        
        return enteredPassword.getHash().equals(userHashedPass);
    }

}