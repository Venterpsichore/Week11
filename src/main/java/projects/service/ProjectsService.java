package projects.service;
import java.util.*;

//Separating DAO for layer organization (accessing projects)
import projects.dao.ProjectDao;
import projects.entity.Project;

public class ProjectsService {
	private ProjectDao projectDao = new ProjectDao();
	
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
}