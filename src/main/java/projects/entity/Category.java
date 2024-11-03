package projects.entity;
// Setting up category ID and name a la Material.java to make String of the two return
public class Category {
  private Integer categoryId;
  private String categoryName;
  
  public Integer getCategoryId() { 
	  return categoryId;
  }
  
  public void setCategoryId(Integer categoryId) {
	  this.categoryId = categoryId;
  }
  
  public String getCategoryName() {
	  return categoryName;
  }
  
  public void setCategoryName(String categoryName) {
	  this.categoryName = categoryName;
  }
  
  @Override
  public String toString() {
	  return "ID=" + categoryId + ", categoryName=" + categoryName;
  }
}
