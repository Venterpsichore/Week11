package projects.dao;
// Imports allow for more accurate hours recordings & preparedstatements
import java.math.BigDecimal;
import java.sql.*;

import projects.entity.*;
import projects.exception.DbException;
import provided.util.DaoBase;

import java.util.*;

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
// Exception causing rollback 
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
// Fetching projects initiating a connection and executing query
public  List<Project> fetchAllProjects() {
	String sql = "SELECT * FROM " + PROJECT_TABLE + " ORDER BY project_name";
	
	try(Connection conn = DbConnection.getConnection()) {
		startTransaction(conn);
		
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			try(ResultSet rs = stmt.executeQuery()) { 
				List<Project> projects = new LinkedList<>();
				
				while (rs.next()) {
					projects.add(extract(rs, Project.class));
					
				}
				
				return projects;
			}
		}
		catch (Exception e) {
			rollbackTransaction(conn);
			throw new DbException(e);
		}
	}
	catch(SQLException e) {
		throw new DbException(e);
	}
}
// Synchronizing project and child rows retrieval using projectId w/ ? parameter placeholder
public Optional<Project> fetchProjectById(Integer projectId) {
	String sql = "SELECT * FROM " + PROJECT_TABLE + " WHERE project_id = ?";
	
	try(Connection conn = DbConnection.getConnection()) {
		startTransaction(conn);
		
		try {
			Project project = null;
		
			try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			setParameter(stmt, 1, projectId, Integer.class);
				
				try(ResultSet rs = stmt.executeQuery()) {
					if(rs.next()) {
					project = extract(rs, Project.class);
				}
			  }
			}
			// Retrieving rows associated with the project
			if(Objects.nonNull(project)) {
				project.getMaterials().addAll(fetchMaterialsForProject(conn, projectId));
				project.getSteps().addAll(fetchStepsForProject(conn, projectId));
				project.getCategories().addAll(fetchCategoriesForProject(conn, projectId));
			}
			// Allows possible null. If not null, wraps Optional to safeguard against crashes
			commitTransaction(conn);
			return Optional.ofNullable(project);
		}
		// Both exception types distinguishes the difference in source of error
		catch(Exception e) {
			rollbackTransaction(conn);
			throw new DbException(e);
		}
	}
	catch(SQLException e) {
		throw new DbException(e);
 }
}

private List<Category> fetchCategoriesForProject(Connection conn,
		Integer projectId) throws SQLException {
	//formatter:off
	String sql = "" 
			+ "SELECT c.* FROM " + CATEGORY_TABLE + " c "
			+ "JOIN " + PROJECT_CATEGORY_TABLE + " pc USING (category_id) "
			+ "WHERE project_id = ?";
	//formatter:on
	
	try(PreparedStatement stmt = conn.prepareStatement(sql)) {
		setParameter(stmt, 1, projectId, Integer.class);
		
		try(ResultSet rs = stmt.executeQuery()) {
			List<Category> categories = new LinkedList<>();
			
			while(rs.next()) {
				categories.add(extract(rs, Category.class));
			}
			return categories;
			}
		}
	}
private List<Step> fetchStepsForProject(Connection conn, Integer projectId) throws SQLException {
	String sql = "SELECT * FROM " + STEP_TABLE + " WHERE project_id = ?";
	
	try(PreparedStatement stmt = conn.prepareStatement(sql)) {
		setParameter(stmt, 1, projectId, Integer.class);
		
		try(ResultSet rs = stmt.executeQuery()) {
			List<Step> steps = new LinkedList<>();
			
			while(rs.next()) {
				steps.add(extract(rs, Step.class));
			}
			
			return steps;	
		}
	}
}
private List<Material> fetchMaterialsForProject(Connection conn, Integer projectId)
throws SQLException {
	String sql = "SELECT * FROM " + MATERIAL_TABLE + " WHERE project_id = ?";
	
	try(PreparedStatement stmt = conn.prepareStatement(sql)) {
		setParameter(stmt, 1, projectId, Integer.class);
		
		try(ResultSet rs = stmt.executeQuery()) {
			List<Material> materials = new LinkedList<>();
			
			while(rs.next()) {
				materials.add(extract(rs, Material.class));
			}
			
			return materials;
		}
	}
}}