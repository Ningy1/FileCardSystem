import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class FileCardsDB {
	
	SimpleIntegerProperty id;
	SimpleStringProperty sideA;
	SimpleStringProperty sideB;
	SimpleStringProperty category;
	SimpleStringProperty subcategory;
	
	public FileCardsDB(Integer id, String sideA, String sideB, String category, String subcategory)
	{
		this.id = new SimpleIntegerProperty(id);
		this.sideA = new SimpleStringProperty(sideA);
		this.sideB = new SimpleStringProperty(sideB);
		this.category = new SimpleStringProperty(category);
		this.subcategory = new SimpleStringProperty(subcategory);
	}
	
	public Integer getId() 
	{
        return id.get();
    }

    public void setId(Integer id) 
    {
        this.id.set(id);
    }
    
    public String getSideA()
    {
    	return sideA.get();
    }
    public void setSideA(String sideA)
    {
    	this.sideA.set(sideA);
    }
    
    public String getSideB()
    {
    	return sideB.get();
    }
    public void setSideB(String sideB)
    {
    	this.sideB.set(sideB);
    }
    
    public String getCategory()
    {
    	return category.get();
    }
    public void setCategory(String category)
    {
    	this.category.set(category);
    }
    
    public String getSubcategory()
    {
    	return subcategory.get();
    }
    public void setSubcategory(String subcategory)
    {
    	this.subcategory.set(subcategory);
    }   
}
