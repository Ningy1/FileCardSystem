import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class FileCardsDB {
	Integer idSideA;
	Integer idSideB;
	String sideA;
	String sideB;
	
	
	public FileCardsDB(Integer idSideA, Integer idSideB, String sideA, String sideB)
	{
		this.idSideA = idSideA;
		this.idSideB = idSideB;
		this.sideA = sideA;
		this.sideB = sideB;
	}
	public Integer getIdSideA()
    {
    	return idSideA;
    }
    public void setIdSideA(Integer idSideA)
    {
    	this.idSideA = idSideA;
    }
    public Integer getIdSideB()
    {
    	return idSideB;
    }
    public void setIdSideB(Integer idSideB)
    {
    	this.idSideB = idSideB;
    }
    public String getSideA()
    {
    	return sideA;
    }
    public void setSideA(String sideA)
    {
    	this.sideA = sideA;
    }
    
    public String getSideB()
    {
    	return sideB;
    }
    public void setSideB(String sideB)
    {
    	this.sideB = sideB;
    }
}
