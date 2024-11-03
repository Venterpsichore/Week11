package projects.entity;
// Set constants to be used by similar to Material.java and a String of the ID and its text return
public class Step {
	private Integer stepId;
	private Integer projectId;
	private String stepText;
	private Integer stepOrder;
	
	public Integer getStepId() {
		return stepId;
	}
	
	public void stepId(Integer stepId) {
		this.stepId = stepId;
	}
	
	public Integer getProjectId() {
		return projectId;
	}
	
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	
	public String getStepText() {
		return stepText;
	}
	
	public void setStepText(String stepText) {
		this.stepText = stepText;
	}
	
	public Integer getStepOrder() {
		return stepOrder;
	}
	
	public void setStepOrder(Integer stepOrder) {
		this.stepOrder = stepOrder;
	}
	
	@Override
	public String toString() {
		return "ID=" + stepId + ", stepText=" + stepText;
	}
}
