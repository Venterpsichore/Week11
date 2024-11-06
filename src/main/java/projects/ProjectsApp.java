package projects;
//Scanner allows for user input to select from options
import java.math.BigDecimal;
import java.util.*;
import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectsService; 

import projects.dao.DbConnection;
// Driver Manager connecting the code to the database URL
public class ProjectsApp {
	 public static void main(String[] args) {new ProjectsApp().processUserSelections();
	 
	 }
	 // Creating a scanner for user input
	 private Scanner scanner = new Scanner(System.in);
	 private ProjectsService projectService = new ProjectsService();
	 private Project curProject;
	 
	 // @formatter:off
	 private List<String> operations = List.of( // UI options are given through numbers
			 "1) Add a project",
			 "2) List projects",
			 "3) Select a project",
			 "4) Update project details",
			 "5) Delete a project"
	 );
	 // @formatter:on
	 private void processUserSelections() {
		 boolean done = false;
		 
		 while(!done) {
			 try {
				 int selection = getUserSelection();
				 // Sets up selection options with an option for quitting
				 switch(selection) {
				 case -1:
					 done = exitMenu();
					 break;
					 
				 case 1:
					 createProject();
					 break;
					 
				 case 2:
					 listProjects();
					 break;
					 
				 case 3:
					 selectProject();
					 break;
					 
				 case 4:
					 updateProjectDetails();
					 break;
					 
				 case 5:
					 deleteProject();
					 break;
				 // Gives the response if input isn't recognized	 
			     default:
			    	 System.out.println("\n" + selection + "is not valid. Try a new input.");
			    	 break;
				 }
			 }
			 catch(Exception e) {
			        System.out.println("\nError: " + e + " Try again, please.");
		 }
	 }
	 }
	 // Allowing new project option to be built
	 private void createProject() {
		 String projectName = getStringInput("Enter project name");
		 BigDecimal estimatedHours = getDecimalInput("Enter estimated hours");
		 BigDecimal actualHours = getDecimalInput("Enter actual hours");
		 Integer difficulty = getIntInput("Enter project difficulty from 1 to 5");
		 String notes = getStringInput("Enter the project's notes");

		 Project project = new Project();

		 project.setProjectName(projectName);
		 project.setEstimatedHours(estimatedHours);
		 project.setActualHours(actualHours);
		 project.setDifficulty(difficulty);
		 project.setNotes(notes);
         // Confirming the new project w/ a message
		 Project dbProject = projectService.addProject(project);
		 System.out.println("You have successfully created project: " + dbProject);
		  }
	 // Listing out all projects by ID and name separated by a colon
	 private void listProjects() {
		 List<Project> projects = projectService.fetchAllProjects();
		 
		 System.out.println("\nProjects:");
		 
		 projects.forEach(project -> 
		 System.out.println("	" + project.getProjectId() + ": " + project.getProjectName()));
	 }
	// Choosing a project by its correspondent ID
		 private void selectProject() { // Setting up method 
			 listProjects(); // Listing the projects
			 Integer projectId = getIntInput("Enter a project ID to select a project");
			 
			 curProject = null; //
			 // Assigning the gotten projectID as cuProject in this private method
			 curProject = projectService.fetchProjectById(projectId);
		 }
		 
 	 
	 private void updateProjectDetails() {
		 if(Objects.isNull(curProject)) {
			 System.out.println("\nPlease select your project");
			 return;
		 }
		 // Allows for the overwrite of projectName, estimatedHours, actualHours, difficulty & notes
		 String projectName = 
				 getStringInput("Enter project name [" + curProject.getProjectName() + "]");
		 
		 BigDecimal estimatedHours =
				 getDecimalInput("Enter estimated hours [" + curProject.getEstimatedHours() + "]");
		 
		 BigDecimal actualHours = 
				 getDecimalInput("Enter actual hours [" + curProject.getActualHours() + "]");
		 
		 Integer difficulty =
				 getIntInput("Enter project difficulty from 1 to 5 [" + curProject.getDifficulty() + "]");
		 
		 String notes =
				 getStringInput("Enter project notes [" + curProject.getNotes() + "]");
		 // Project was made to work with in private access
		 Project project = new Project();
		 // Sets details with inputted overwrites
		 project.setProjectId(curProject.getProjectId());
		 project.setProjectName(Objects.isNull(projectName) ? curProject.getProjectName() : projectName);
		 
		 project.setEstimatedHours(
				 Objects.isNull(estimatedHours) ? curProject.getEstimatedHours() : estimatedHours);
		 
		 project.setActualHours(Objects.isNull(actualHours) ? curProject.getActualHours() : actualHours);
		 project.setDifficulty(Objects.isNull(difficulty) ? curProject.getDifficulty() : difficulty);
		 project.setNotes(Objects.isNull(notes) ? curProject.getNotes() : notes);
		 // Updates the project w/ the sets
		 projectService.modifyProjectDetails(project);
		 // Refreshes curProject with the projectId
		 curProject = projectService.fetchProjectById(curProject.getProjectId());
		 
		 
	 }
	 /* Lists the projects and goads user to choose a project ID matching the project 
	 one would like to delete.  */
	 private void deleteProject() {
		 listProjects();
		 
		 Integer projectId = getIntInput("Please, enter project ID to delete");
		 
		 // Calls service layer to delete the project matching inputted ID
		 projectService.deleteProject(projectId); 
		 System.out.println("Project" + projectId + " was deleted successfully.");
		 
		/* Nulls the current project if the inputted ID matches the current project's ID
		 * so that the project cannot 
		 */
		 if(Objects.nonNull(curProject) && curProject.getProjectId().equals(projectId)) {
			 curProject = null; 
		 }
	 }
	 
	 // Converting the input to a BigDecimal taking null and exception into account
	 private BigDecimal getDecimalInput(String prompt) {
		 String input = getStringInput(prompt);

		    if(Objects.isNull(input)) {
		      return null;
		    }

		    try {
		      
		      return new BigDecimal(input).setScale(2); // Designates String to only have two decimal places
		    }
		    catch(NumberFormatException e) {
		      throw new DbException(input + " is not a valid decimal number.");
		    }
		  }
	 // Method that indicates printing exit to client before returning true
	 private boolean exitMenu() {
		    System.out.println("Exiting the menu.");
		    return true;
		  }
	 private int getUserSelection() {
		    printOperations();

		    Integer input = getIntInput("Enter a menu selection");
		    // Null results in exit
		    return Objects.isNull(input) ? -1 : input;
		  }
	 // Gets input of a number (integer) to try to match it with a prescribed option
	 private Integer getIntInput(String prompt) {
		    String input = getStringInput(prompt);

		    if(Objects.isNull(input)) {
		      return null;
		    }

		    try {
		      return Integer.valueOf(input);
		    }
		    catch(NumberFormatException e) {
		      throw new DbException(input + " is not a valid number.");
		    }
		  }
	 // Gets String to allow for update of project details
	 private String getStringInput(String prompt) {
		    System.out.print(prompt + ": ");
		    String input = scanner.nextLine();
		    
		    /* If blank, input returns null. If not, returns trimmed (spaces taken out preceding 
		    and following) String input */
		    return input.isBlank() ? null : input.trim(); // 
		  }
	 // Prints menu options
	 private void printOperations() {
		    System.out.println("\nThese are the available selection options. Press the Enter key to quit:");

		    operations.forEach(line -> System.out.println("  " + line));
		    // Prints message if could not find project.
		    if(Objects.isNull(curProject)) {
		    	System.out.println("\nYou are not working with a project.");
		    }
		    // Prints project found.
		    else {
		    	System.out.println("\nYou are working with this project: " + curProject);
		    }
	 }
}
