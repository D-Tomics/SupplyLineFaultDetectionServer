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

    /**
     * this inserts given values into a new row of the table
     * The no of values must match the no of columns in the table and their types
     * @param values values to be inserted
     * @return  1 if insertion is successfull
     *          0 if insertion failed
     */
    public int insert(Object... values) {
        if (columns.length != values.length)
            throw new IllegalStateException("sql error::values.lenth != columns.length");

        PreparedStatement statement = db.createPreparedStatement(getInsertStatement());
        setStatementParams(statement, values);
        return db.executeUpdate(statement);
    }

    /**
     * This inserts values into a new row of table but only to the columns specified by columnNames
     * @param columnNames columns of the new row to which values are to be inserted
     * @param values the values to be inserted
     * @return 1 if insertion is successfull
     *         0 if insertion fails
     */
    public int insert(String[] columnNames, Object...values) {
        if(columnNames.length != values.length)
            throw new IllegalStateException("values length != columnNames length");

        PreparedStatement statement = db.createPreparedStatement(getInsertStatement(columnNames));
        setStatementParams(statement, values);
        return db.executeUpdate(statement);
    }

    /**
     * this method updates values of columns specified by columnNames and 
     * that passes a condition
     * 
     * @param condition this can be a parameterised or not parameterised sql condition if parameterised
     * the values must be specified at the end of values
     * 
     * @param columnNames these are the names of columns whose values are to be updated
     * 
     * @param values this contains the new values to be placed in the columns that passes condition
     * also if condition is parameterised sql statement then at the end of values it should contain parameters
     * of condition
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
     * or (2) 0 for SQL statements that return nothing
     */
    public int updateColumns(String condition, String[] columnNames, Object...values) {
        PreparedStatement statement = db.createPreparedStatement( getUpdateStatement(condition, columnNames) );
        setStatementParams(statement, values);
        return db.executeUpdate(statement);
    }

    /**
     * this method updates value of a sigle column specified by a columnName and 
     * that passes a condition
     * 
     * @param condition this can be a parameterised or not parameterised sql condition if parameterised
     * the values must be specified at the end of values
     * 
     * @param columnName this is the name of column whose value is to be updated
     * 
     * @param values this is the new value of the column. This vararg is of length 1 iff condition 
     * is not parameterised else it also contains parameters of condition statement
     * 
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
     * or (2) 0 for SQL statements that return nothing
     */
    public int updateColumns(String condition, String columnName, Object...values) {
        return updateColumns(condition, new String[] { columnName }, values);
    }

    /**
     * this executes a sql select query on a single column and returns a <code>ResutlSet</code> object that contains all 
     * data of the column
     * @param columnName this is the name of column to obtained
     * @return  a <code>ResultSet</code> object that contains the column;
     *          <code>null</code> when column name does not exist
     */
    public ResultSet get(String columnName) {
        return get(columnName, null);
    }

    /**
     * this executes a sql select query on a single column with a condition and returns a <code>ResutlSet</code> object that contains all 
     * data of the column. If condition is parameterised then use {@link Table#get(String,String,Object...)} method.
     * @param columnName this is the name of column to obtained
     * @param condition this is the condition to be checked
     * @return  a <code>ResultSet</code> object that contains the column;
     *          <code>null</code> when column name does not exist
     */
    public ResultSet get(String columnName, String condition) {
        return get(columnName, condition, (Object) null);
    }

    /**
     * this returns a <code>ResultSey</cod> object that contains all the cells that passes a condition and are in the columns specified by
     * coulumn names
     * @param columnNames this is a <code>String</code> array that contains the column names that is to be queried
     * @param condition this is the condition that is checked when querying. This can be a parameterised statement.
     * @param conditionParams this contains values of parameters that need to be passed into the condition
     * @return  a <code>ResultSet</code> object that contains the all columns
     *         <code>null</code> when either any of column name in column names does not exist
     */
    public ResultSet get(String[] columnNames, String condition, Object...conditionParams) {
        PreparedStatement statement = db.createPreparedStatement(getQueryStatement(condition, columnNames));
        if(conditionParams != null && conditionParams.length != 0 && condition != null && !condition.isEmpty())
            setStatementParams(statement, conditionParams);
        return db.executeQuery(statement);
    }

    /**
     * this executes a sql select query on a single column with a condition and returns a <code>ResutlSet</code> object that contains all 
     * data of the column 
     * @param columnName this is the name of column to obtained
     * @param condition this is the condition to be checked .This can be parameterised
     * @param conditionParams if condition is a parameterised statement then its params must be specified here
     * @return a <code>ResultSet</code> object that contains the all columns
     *         <code>null</code> when either any of column name in column names does not exist
     */
    public ResultSet get(String columnName, String condition, Object...conditionParams) {
        PreparedStatement statement = db.createPreparedStatement(getQueryStatement(condition, columnName));
        if(conditionParams != null && conditionParams.length != 0 && condition != null && !condition.isEmpty())
            setStatementParams(statement, conditionParams);
        return db.executeQuery(statement);
    }


    /**
     * checks if a value exist in specified column name
     * @param columnName name of the column to be checked
     * @param value value to be checked in column
     * @return  true if value exist in the column
     *          false if not
     */
    public boolean contains(String columnName, Object value) { return  getObject(columnName, columnName + "=?", value) != null; }

    /* these functions are to be used when the condition returns a single cell of a table. If not then these functions returns null*/
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
            if(count > 1 || count < 1) {//returned result contains more than one Object or does not contain any element"
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
            ResultSet rs = db.executeQuery("SELECT * FROM " + name + " WHERE 1=2"); // condition 1 = 2 returns null from table. But enough to get metadata of the table
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