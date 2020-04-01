package db;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;

public class Table {

    private Database db;
    private String name;
    private String[] columns;
    private String[] columnType;

    protected Table(String name, Database db) {
        this.name = name;
        this.db = db;
        this.setColumns();
    }

    public int insert(Object... values) {
        if (columns.length != values.length)
            throw new IllegalStateException("sql error::values.lenth != columns.length");

        PreparedStatement statement = db.createPreparedStatement(getInsertStatement());
        setStatementParams(statement, values);
        return db.executeUpdate(statement);
    }

    public int insert(String[] columnNames, Object...values) {
        if(columnNames.length != values.length)
            throw new IllegalStateException("values length != columnNames length");

        PreparedStatement statement = db.createPreparedStatement(getInsertStatement(columnNames));
        setStatementParams(statement, values);
        return db.executeUpdate(statement);
    }

    public int updateColumns(String condition, String[] columnNames, Object...values) {
        if(columnNames.length != values.length)
            throw new IllegalStateException("column names does not match values");

        PreparedStatement statement = db.createPreparedStatement( getUpdateStatement(condition, columnNames) );
        setStatementParams(statement, values);
        return db.executeUpdate(statement);
    }

    public int updateColumns(String condition, String columnName, Object value) {
        return updateColumns(condition, new String[] { columnName }, value);
    }

    public ResultSet get(String columnName) {
        return get(columnName, null);
    }

    public ResultSet get(String columnName, String condition) {
        return get(columnName, condition, (Object) null);
    }

    public ResultSet get(String[] columnNames, String condition, Object...conditionParams) {
        PreparedStatement statement = db.createPreparedStatement(getQueryStatement(condition, columnNames));
        if(conditionParams != null && conditionParams.length != 0 && condition != null && !condition.isEmpty())
            setStatementParams(statement, conditionParams);
        return db.executeQuery(statement);
    }

    public ResultSet get(String columnName, String condition, Object...conditionParams) {
        PreparedStatement statement = db.createPreparedStatement(getQueryStatement(condition, columnName));
        if(conditionParams != null && conditionParams.length != 0 && condition != null && !condition.isEmpty())
            setStatementParams(statement, conditionParams);
        return db.executeQuery(statement);
    }


    public boolean contains(String columnName, Object value) { return  getObject(columnName, columnName + "=?", value) != null; }

    public String   getString (String columnName, String condition) { return (String) getObject(columnName, condition); }
    public byte     getByte   (String columnName, String condition) { return (byte)   getObject(columnName, condition); }
    public short    getShort  (String columnName, String condition) { return (short)  getObject(columnName, condition); }
    public int      getInt    (String columnName, String condition) { return (int)    getObject(columnName, condition); }
    public long     getLong   (String columnName, String condition) { return (long)   getObject(columnName, condition); }
    public float    getFloat  (String columnName, String condition) { return (float)  getObject(columnName, condition); }
    public double   getDouble (String columnName, String condition) { return (double) getObject(columnName, condition); }
    public boolean  getBoolean(String columnName, String condition) { return (boolean)getObject(columnName, condition); }
    public Date     getDate   (String columnName, String condition) { return (Date)   getObject(columnName, condition); }
    public Time     getTime   (String columnName, String condition) { return (Time)   getObject(columnName, condition); }

    public String   getString (String columnName, String condition, Object...cParams) { return (String) getObject(columnName, condition, cParams); }
    public byte     getByte   (String columnName, String condition, Object...cParams) { return (byte)   getObject(columnName, condition, cParams); }
    public short    getShort  (String columnName, String condition, Object...cParams) { return (short)  getObject(columnName, condition, cParams); }
    public int      getInt    (String columnName, String condition, Object...cParams) { return (int)    getObject(columnName, condition, cParams); }
    public long     getLong   (String columnName, String condition, Object...cParams) { return (long)   getObject(columnName, condition, cParams); }
    public float    getFloat  (String columnName, String condition, Object...cParams) { return (float)  getObject(columnName, condition, cParams); }
    public double   getDouble (String columnName, String condition, Object...cParams) { return (double) getObject(columnName, condition, cParams); }
    public boolean  getBoolean(String columnName, String condition, Object...cParams) { return (boolean)getObject(columnName, condition, cParams); }
    public Date     getDate   (String columnName, String condition, Object...cParams) { return (Date)   getObject(columnName, condition, cParams); }
    public Time     getTime   (String columnName, String condition, Object...cParams) { return (Time)   getObject(columnName, condition, cParams); }

    public Object getObject( String columnName, String condition,Object...conditionParams) {
        ResultSet rs = get(columnName, condition, conditionParams);
        if(rs == null) return null;
        try {
            rs.last();
            int count = rs.getRow();
            if(count > 1 || count < 1) {
                //System.err.println("returned result contains more than one Object or does not contain any element");
                return null;
            }
            rs.beforeFirst();
            rs.next();
            return rs.getObject(columnName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] getColumnLabels() { return columns; }
    public String[] getColumnTypes() { return columnType; }

    private void setColumns() {
        try {
            ResultSet rs = db.executeQuery("SELECT * FROM " + name + " WHERE 1=2");
            if (rs == null)
                throw new IllegalStateException("could'nt initialise table " + name);

            ResultSetMetaData rsm = rs.getMetaData();
            this.columns = new String[rsm.getColumnCount()];
            this.columnType = new String[rsm.getColumnCount()];

            for (int i = 1; i <= rsm.getColumnCount(); i++) {
                this.columns[i - 1] = rsm.getColumnLabel(i);
                this.columnType[i - 1] = rsm.getColumnTypeName(i);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setStatementParams(PreparedStatement statement, Object... values) {
        int index = 1;
        try {
            for (Object value : values) {
                     if (value instanceof String)   statement.setString(    index++, (String)   value);
                else if (value instanceof Byte)     statement.setByte(      index++, (byte)     value);
                else if (value instanceof Short)    statement.setShort(     index++, (short)    value);
                else if (value instanceof Integer)  statement.setInt(       index++, (int)      value);
                else if (value instanceof Long)     statement.setLong(      index++, (long)     value);
                else if (value instanceof Float)    statement.setFloat(     index++, (float)    value);
                else if (value instanceof Double)   statement.setDouble(    index++, (double)   value);
                else if (value instanceof Boolean)  statement.setBoolean(   index++, (boolean)  value);
                else if (value instanceof Date)     statement.setDate(      index++, (Date)     value);
                else if (value instanceof Time)     statement.setTime(      index++, (Time)     value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getInsertStatement(String...columnLabels) {
        String[] cLabels = columnLabels;
        if(cLabels == null || cLabels != null && cLabels.length == 0) cLabels = columns;

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(name);
        if(columnLabels != null && columnLabels.length != 0) {
            sql.append("( ").append(columnLabels[0]);
            for(int i = 1; i < columnLabels.length; i++) sql.append(",").append(columnLabels[i]);
            sql.append(")");
        }
        
        sql.append(" VALUES ( ? ");
        for(int i = 1; i < cLabels.length; i++) sql.append(", ?");
        sql.append(")");

        return sql.toString();
    }

    private String getUpdateStatement(String condition, String...columnLabels) {
        String[] cLabels = columnLabels;
        if(cLabels == null || cLabels != null && cLabels.length == 0) cLabels = columns;

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(name).append(" SET ").append(cLabels[0]).append("=?");
        for(int  i = 1; i < cLabels.length; i++) sql.append(",").append(cLabels[i]).append("=?");
        if(condition != null && !condition.isEmpty()) sql.append(" WHERE ").append(condition);
        return sql.toString();
    }

    public String getQueryStatement(String condition, String...columnLabels) {

        String[] cLabel =columnLabels;
        if(cLabel == null || cLabel != null && cLabel.length == 0)
            cLabel = new String[] {"*"};

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(cLabel[0]);
        for(int i = 1; i < cLabel.length; i++) sql.append(",").append(cLabel[i]);
        sql.append(" FROM ").append(name);

        if(condition != null && !condition.isEmpty()) sql.append(" WHERE ").append(condition);
        return sql.toString();
    }

}