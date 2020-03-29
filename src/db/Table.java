package db;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


public class Table {

    private Database db;
    private String name;
    private String[] columns;
    private String[] columnType;
    private static PrintWriter out;
    private String columnName;
    
    protected Table() {}

    /*
        public int insert(String...values);
        public int insertString(String...values);
        public int insertInt(int...values);
        public int insertString(String[] columns, String...values);
        public int insertInt(String[] columns, int...values);

        public int updateColumn(String condition,String[] columnName,String...val);
        public int updateColumn(String condition,String[] columnName,int...val);
        public int updateColumn(String condition,String columnName, String value);
        public int updateColumn(String condition,String columnName, int value);
        public int updateColumn(String condition,String columnName, float value);

        public boolean containsString(String columnName, String val);
        public boolean containsInt(String columnName, int val)
    public boolean containsString(String condition,String columnName, String val)
    public boolean containsInt(String condition,String columnName,int val)
    public ResultSet get(String columnName)
    public ResultSet get(String condition,String columnName)
    public Integer getInt(String condition,String columnName)
    public String getString(String condition,String columnName)
    public void setName(String name)
    public void setDb(Database db)
    public void setColumns()
    public static void setOut(PrintWriter out)

    */

    public int insert(String...values){
        String sql = "INSERT INTO "+name+" ";
        if(columns.length != values.length)
             throw new IllegalStateException();
        
        sql += "("+columns[0];
        for(int i = 1; i < columns.length; i++)
            sql += ","+columns[i];
        
        String type = columnType[0];
        if(type.equals("VARCHAR"))
            sql += ") VALUES('"+values[0]+"'";
        else 
            sql+=") VALUES("+Integer.parseInt(values[0])+"";
        for(int i = 1; i< columns.length;i++) {
            type = columnType[i];
            if(type.equals("VARCHAR"))
                sql += ",'"+values[i]+"'";
            else
                sql+=","+values[i];
        }
        sql += ")";

        return db.executeUpdate(sql);
    }

    public int insertString(String...values) {
        return insertString(columns,values);
    }

    public int insertInt(int...values) {
        return insertInt(columns, values);
    }

    public int insertString(String[] columns, String...values) {
        String sql = "INSERT INTO "+name+" ";

        if(columns.length != values.length) throw new IllegalStateException();
        sql += "("+columns[0];
        for(int i = 1; i < columns.length; i++)
            sql += ","+columns[i];
        sql += ") VALUES('"+values[0]+"'";
        for(int i = 1; i< columns.length;i++)
            sql += ",'"+values[i]+"'";
        sql += ")";
        
        return db.executeUpdate(sql);
    } 
    
    public int insertInt(String[] columns, int...values) {
        String sql = "INSERT INTO "+name+" ";

        if(columns.length != values.length) throw new IllegalStateException();
        sql += "("+columns[0];
        for(int i = 1; i < columns.length; i++)
            sql += ","+columns[i];
        sql += ") VALUES("+values[0];
        for(int i = 1; i< columns.length;i++)
            sql += ","+values[i];
        sql += ")";

        return db.executeUpdate(sql);
    }

    public int updateColumn(String condition,String[] columnName,String...val) {
        String sql = "UPDATE "+name+" SET ";
        if(columnName == null || columnName.length != val.length) return 0;
        else  {
            sql += columnName[0] + "='"+val[0]+"'";
            for(int i = 1;i<columnName.length;i++) 
                sql += ","+columnName[i]+"='"+val[i]+"'";
        }
        if(condition != null) sql += "WHERE "+condition;
        return db.executeUpdate(sql);
    }
    
    public int updateColumn(String condition,String[] columnName,int...val) {
        String sql = "UPDATE "+name+" SET ";
        if(columnName == null || columnName.length != val.length) return 0;
        else  {
            sql += columnName[0] + "="+val[0]+"";
            for(int i = 1;i<columnName.length;i++) 
                sql += ","+columnName[i]+"="+val[i];
        }
        if(condition != null) sql += "WHERE "+condition;
        return db.executeUpdate(sql);
    }

    public int updateColumn(String condition,String columnName, String value) {
        String sql = "UPDATE "+name+ " SET "+columnName+"='"+value+"' WHERE "+condition;
        return db.executeUpdate(sql);
    }

    public int updateColumn(String condition,String columnName, int value) {
        String sql = "UPDATE "+name+ " SET "+columnName+"="+value+" WHERE "+condition;
        return db.executeUpdate(sql);
    }
    
    public int updateColumn(String condition,String columnName, float value) {
        String sql = "UPDATE "+name+ " SET "+columnName+"="+value+" WHERE "+condition;
        return db.executeUpdate(sql);
    }


    public boolean containsString(String columnName, String val) {
        ResultSet rs = get(columnName);
        return containsString(rs, columnName, val);
    }

    public boolean containsInt(String columnName, int val) {
        ResultSet rs = get(columnName);
        return containsInt(rs, columnName, val);
    }

    public boolean containsString(String condition,String columnName, String val) {
        ResultSet rs = get(condition, columnName);
        if(rs == null) return false;
        return containsString(rs, columnName, val);
    }

    public boolean containsInt(String condition,String columnName,int val) {
        ResultSet rs = get(condition,columnName);
        if(rs == null) return false;
        return containsInt(rs,columnName,val);
    }

    public ResultSet get(String columnName) {
        return get(null, columnName);
    }

    public ResultSet get(String condition,String columnName) {
        if(db == null) return null;
        return condition != null && !condition.equals("")? 
                db.executeQuery("SELECT "+columnName+" FROM "+name+" WHERE "+condition) :
                db.executeQuery("SELECT "+columnName+" FROM "+name);
    }

    public Integer getInt(String condition,String columnName) {
        ResultSet rs = get(condition,columnName);
        try {
            rs.last();
            int count = rs.getRow();
            if(count > 1 || count < 1)
                return null;
            rs.beforeFirst();
            rs.next();
            return rs.getInt(columnName);
        } catch (SQLException e) {
            e.printStackTrace(out);
        }
        return null;
    }
    
    public String getString(String condition,String columnName) {
        ResultSet rs = get(condition,columnName);
        try {
            rs.last();
            int count = rs.getRow();
            if(count > 1 || count < 1)
                return null;
            rs.beforeFirst();
            rs.next();
            return rs.getString(columnName);
        } catch (SQLException e) {
            e.printStackTrace(out);
        }
        return null;
    }

    public void setName(String name) {
        this.name = name;
    } 

    public void setDb(Database db) {
        this.db = db;
    }

    public void setColumns() {
        try {
            ResultSet rs = db.executeQuery("SELECT * FROM "+name);
            if(rs == null)
             throw new IllegalStateException("could'nt initialise table "+name);
            ResultSetMetaData rsm = rs.getMetaData();
            this.columns = new String[rsm.getColumnCount()];
            this.columnType = new String[rsm.getColumnCount()];

            for(int i = 1; i <= rsm.getColumnCount();i++) {
                this.columns[i - 1] = rsm.getColumnLabel(i);
                this.columnType[i-1] = rsm.getColumnTypeName(i);
            }

        } catch (SQLException e) {
           e.printStackTrace();
        }
    }

    public static void setOut(PrintWriter out) {
        Table.out = out;
    }


    private boolean containsInt(ResultSet rs,String columnName,int val) {
        try {
            while (rs.next()) {
                if (rs.getInt(columnName) == val)
                    return true;
            }
        } catch (SQLException e) {
            e.printStackTrace(out);
        }
        return false;
    }

    private synchronized boolean containsString(ResultSet rs,String columnName, String val) {
        try {
            while (rs.next()) {
                if (rs.getString(columnName).equals(val))
                    return true;
            }
        } catch (SQLException e) {
            e.printStackTrace(out);
        }
        return false;
    }

    private synchronized boolean containsFloat(ResultSet rs, String columnName, float val) {
        this.columnName = columnName;
		try {
            while (rs.next()) {
                if (rs.getFloat(columnName) == val)
                    return true;
            }
        } catch (SQLException e) {
            e.printStackTrace(out);
        }
        return false;
    }
}