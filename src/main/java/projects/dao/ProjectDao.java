package projects.dao;
// Imports allow for more accurate hours recordings & preparedstatements
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import projects.entity.Project;
import projects.exception.DbException;
import provided.util.DaoBase;

// Initializing constants to be referenced multiple times
@SuppressWarnings("unused")
public class ProjectDao extends DaoBase {
private static final String CATEGORY_TABLE = "category";
private static final String MATERIAL_TABLE = "material";
private static final String PROJECT_TABLE = "project";
private static final String PROJECT_CATEGORY_TABLE = "project_category";
private static final String STEP_TABLE = "step";

public Project insertProject(Project project) {
	String sql = "" 
	+ "INSERT INTO " + PROJECT_TABLE + " " 
	+ "(project_name, estimated_hours, actual_hours, difficulty, notes) "
	+ "VALUES "
	+ "(?, ?, ?, ?, ?)";
// Creating a connection to add to populate tables
try(Connection conn = DbConnection.getConnection()) {
startTransaction(conn);

try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	setParameter(stmt, 1, project.getProjectName(), String.class);
	setParameter(stmt, 2, project.getEstimatedHours(), BigDecimal.class);
	setParameter(stmt, 3, project.getActualHours(), BigDecimal.class);
	setParameter(stmt, 4, project.getDifficulty(), Integer.class);
	setParameter(stmt, 5, project.getNotes(), String.class);
	
	stmt.executeUpdate();
	Integer projectId = getLastInsertId(conn, PROJECT_TABLE);
	commitTransaction(conn);
	
	project.setProjectId(projectId);
	return project;
	}
// Exception 
	catch(Exception e) {
		rollbackTransaction(conn);
		throw new DbException(e);
	}
}
// Exception to see if a connection was unsuccessful
catch(SQLException e) {
	throw new DbException(e);
	}
 }

}
