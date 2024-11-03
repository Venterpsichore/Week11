package projects;
//Scanner allows for user input to select from options
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectsService; 

import projects.dao.DbConnection;
//Driver Manager connecting the code to the database URL
public class ProjectsApp {
	 public static void main(String[] args) {new ProjectsApp().processUserSelections();
	 
	 }
	 // Creating a scanner for user input
	 private Scanner scanner = new Scanner(System.in);
	 private ProjectsService projectService = new ProjectsService();
	 
	 // @formatter:off
	 private List<String> operations = List.of(
			 "1) Add a project"
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
	 // Converting the input to a BigDecimal taking null and exception into account
	 private BigDecimal getDecimalInput(String prompt) {
		 String input = getStringInput(prompt);

		    if(Objects.isNull(input)) {
		      return null;
		    }

		    try {
		      
		      return new BigDecimal(input).setScale(2);
		    }
		    catch(NumberFormatException e) {
		      throw new DbException(input + " is not a valid decimal number.");
		    }
		  }
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
	 
	 private String getStringInput(String prompt) {
		    System.out.print(prompt + ": ");
		    String input = scanner.nextLine();

		    return input.isBlank() ? null : input.trim();
		  }
	 // Prints menu options
	 private void printOperations() {
		    System.out.println("\nThese are the available selection options. Press the Enter key to quit:");

		    operations.forEach(line -> System.out.println("  " + line));

	 }
}
