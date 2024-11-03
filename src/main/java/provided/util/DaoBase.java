package provided.util;
// Importing Time packages for converting time to local, changing case between Java and SQL, etc.
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public abstract class DaoBase {
	// Directing MySQL to start a transaction once indicated 
	protected void startTransaction(Connection conn) throws SQLException {
		conn.setAutoCommit(false);
	}
	// Scribing transactions into database  
	protected void commitTransaction(Connection conn) throws SQLException {
		conn.commit();
	}
	// Rolls back commits if errors
	protected void rollbackTransaction(Connection conn) throws SQLException {
		conn.rollback();
	}
	// parameter Index used as an order to fill in data signified by question marks in prepared statements 
	protected void setParameter(PreparedStatement stmt,
			int parameterIndex, Object value, Class<?> classType)
					throws SQLException {
		int sqlType = convertJavaClassToSqlType(classType);
		
		if(Objects.isNull(value)) {
			stmt.setNull(parameterIndex, sqlType);
		}
		else {
			switch(sqlType) {
			case Types.DECIMAL:
				stmt.setBigDecimal(parameterIndex, (BigDecimal)value);
		        break;
			
			case Types.DOUBLE:
				stmt.setDouble(parameterIndex, (Double)value);
		        break;
		        
			case Types.INTEGER:
		          stmt.setInt(parameterIndex, (Integer)value);
		          break;
		          
			case Types.OTHER:
		          stmt.setObject(parameterIndex, value);
		          break;
		          
			case Types.VARCHAR:
		          stmt.setString(parameterIndex, (String)value);
		          break;
		    // If data type not found, then message will display saying so and advise.
			default:
				throw new DaoException("Unrecognized type parameter" + classType + 
						"Please try a different data type.");
		}
	}
}
	// Converting Java class to an SQL data type (as with String & VARCHAR)
	private int convertJavaClassToSqlType(Class<?> classType) {
		if(Integer.class.equals(classType)) {
			return Types.INTEGER;
		}
		
		if(String.class.equals(classType)) {
		      return Types.VARCHAR;
		    }
		if(Double.class.equals(classType)) {
	        return Types.DOUBLE;
	      }

	    if(BigDecimal.class.equals(classType)) {
	        return Types.DECIMAL;
	      }

	    if(LocalTime.class.equals(classType)) {
	        return Types.OTHER;
	    }
	    // Error explained if not recognized
	    throw new DaoException("Unsupported class type: " + classType.getName());
	}
// Getting child row sequence by adding one to previous id integer using SELECT COUNT(*) FROM	
	protected Integer getSequence(Connection conn, Integer id, String tableName,
		      String idName) throws SQLException {
	    String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + idName + " = ?";

	    try(PreparedStatement stmt = conn.prepareStatement(sql)) {
	      setParameter(stmt, 1, id, Integer.class);
// Returns 1 if ID not recognized
	      try(ResultSet rs = stmt.executeQuery()) {
	        if(rs.next()) {
	          return rs.getInt(1) + 1;
	        }

	        return 1;
	      }
	    }
	  }
// Protected for encapsulation and returns the newest table's first value to get the primary key
	protected Integer getLastInsertId(Connection conn, String table) throws SQLException {
	    String sql = String.format("SELECT LAST_INSERT_ID() FROM %s", table);

	    try(Statement stmt = conn.createStatement()) {
	      try(ResultSet rs = stmt.executeQuery(sql)) {
	        if(rs.next()) {
	          return rs.getInt(1);
	        }

	        throw new SQLException("Unable to retrieve the primary key value. No result set sadly.");
	      }
	    }
	  }
	
	protected <T> T extract(ResultSet rs, Class<T> classType) {
	    try {
	      // Creates an object from the ResultSet
	      Constructor<T> con = classType.getConstructor();
	      T obj = con.newInstance();

	      // Turns column names from Java camel case to SQL snake case
	      for(Field field : classType.getDeclaredFields()) {
	        String colName = camelCaseToSnakeCase(field.getName());
	        Class<?> fieldType = field.getType();

	        field.setAccessible(true);
	        Object fieldValue = null;

	        try {
	          fieldValue = rs.getObject(colName);
	        }
	        catch(SQLException e) {
	
	        }
	        
	        if(Objects.nonNull(fieldValue)) {
	            if(fieldValue instanceof Time && fieldType.equals(LocalTime.class)) {
	              fieldValue = ((Time)fieldValue).toLocalTime();
	            }
	            else if(fieldValue instanceof Timestamp && fieldType.equals(LocalDateTime.class)) {
	              fieldValue = ((Timestamp)fieldValue).toLocalDateTime();
	            }

	            field.set(obj, fieldValue);
	          }
	        }

	        return obj;

	      }
	catch(Exception e) {
	    	  throw new DaoException("Unable to create object of type " + classType.getName(), e);
	      }
	    }
	// Changing row names from Java camel to SQL snake as with columns using append if upper initial
	private String camelCaseToSnakeCase(String identifier) {
	      StringBuilder nameBuilder = new StringBuilder();

	      for(char ch : identifier.toCharArray()) {
	        if(Character.isUpperCase(ch)) {
	          nameBuilder.append('_').append(Character.toLowerCase(ch));
	        }
	        else {
	          nameBuilder.append(ch);
	        }
	      }
	      
	      return nameBuilder.toString();
	    }
// RuntimeException wrapper 
	@SuppressWarnings("serial")
	static class DaoException extends RuntimeException {

	      public DaoException(String message, Throwable cause) {
	        super(message, cause);
	      }
	      public DaoException(String message) {
	          super(message);
	        }
	}
}