package projects.service;
//Separating DAO for layer organization
import projects.dao.ProjectDao;
import projects.entity.Project;

public class ProjectsService {
	private ProjectDao projectDao = new ProjectDao();
	
	public Project addProject(Project project) {
		return projectDao.insertProject(project);
	}
}
