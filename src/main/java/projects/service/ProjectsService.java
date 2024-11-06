package projects.service;
import java.util.*;

//Separating DAO for layer organization (accessing projects)
import projects.dao.ProjectDao;
import projects.entity.Project;
import projects.exception.DbException;

public class ProjectsService {
	private ProjectDao projectDao = new ProjectDao(); //Private to encapsulate
	
	public Project addProject(Project project) {
		return projectDao.insertProject(project);
	}
// Retrieving all database projects through a List
	public List<Project> fetchAllProjects() {
		return projectDao.fetchAllProjects();
	}
// Retrieving a project by ID and providing an error message if not found
	public Project fetchProjectById(Integer projectId) {
		return projectDao.fetchProjectById(projectId).orElseThrow(() 
			-> new NoSuchElementException("Project with project ID=" 
			+ projectId + " does not exist."));
	}
	
	// Calls service to change project details
	public void modifyProjectDetails(Project project) {
		if(!projectDao.modifyProjectDetails(project)) {
			throw new DbException("Project with ID="
					+ project.getProjectId() + "does not exist.");
	}
	}
	// Deletes project of inputted matching ID if found
	public void deleteProject(Integer projectId) {
		if(!projectDao.deleteProject(projectId)) {
			throw new DbException("Project with ID=" + projectId + "does not exist.");
		}
	}
}