package data;

import java.util.Base64;

import db.Database;
import db.Table;

public class User {

    private static final String[] FIELDS = new String[] {"name","email","password","salt"};

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
            usersDB = Database.getDatabase("supplyline");
            usersTable = usersDB.getTable("employeeData");
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
        if(usersTable.contains("name", username)) return false;
        Password securePassword = Encrypt.hashPassword(password);
        usersTable.insert(FIELDS,username,email,securePassword.getHash(),securePassword.getSalt());
        return true;
    }

    public boolean authenticate(String password) {

        if(!usersTable.contains("name", username)) return false;

        String userHashedPass = usersTable.getString("password","name=?",username);
        String salt = usersTable.getString("salt","name=?",username);

        Password enteredPassword = Encrypt.hashPassword(password,Base64.getDecoder().decode(salt));
        
        return enteredPassword.getHash().equals(userHashedPass);
    }

}