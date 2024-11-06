package projects.entity;
// Needed to implement Big Decimal methods
import java.math.BigDecimal;
// Declaring Integers, Strings, and BigDecimal for calculation
public class Material {
private Integer materialId;
private Integer projectId;
private String materialName; // String used to make a more specific name
private Integer numRequired;
private BigDecimal cost;
// Getting meterialId for return
public Integer getMaterialId() {
	return materialId; 
	}
// Instantiating using this. to avoid conflict(s)
public void setMaterialId(Integer materialId) {
	this.materialId = materialId; 
	}
// Mirroring the process for ProjectId, materialName, numRequired, & cost
public Integer getProjectId() {
	return projectId;
	}

public void setProjectId(Integer projectId) {
	this.projectId = projectId;
	}

public String getMaterialName() { 
	return materialName; 
	}

public void setMaterialName(String materialName) {
	this.materialName = materialName;
	}

public Integer getNumRequired() {
	return numRequired;
	}

public void setNumRequired(Integer numRequired) {
	this.numRequired = numRequired;
	}

public BigDecimal getCost() {
	return cost;
	}

public void setCost(BigDecimal cost) { 
	this.cost = cost;
	}
/* Tags to indicate superclass overrides this subclass method and that this code will be affected. 
Will also error out if the structure isn't correct.  */
@Override
public String toString() {
	return "ID=" + materialId + ", materialName=" + materialName + ", numRequired=" + numRequired + ", cost=" + cost;
}
}