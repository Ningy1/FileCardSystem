import java.io.Serializable;

public class FileCardsDB implements Serializable{
	Integer idSideA;
	Integer idSideB;
	String sideA;
	String sideB;
	String cat;
	String subCatA;
	String subCatB;
	
	
	public FileCardsDB(Integer idSideA, Integer idSideB, String sideA, String sideB, 
			String cat, String subCatA, String subCatB)
	{
		this.idSideA = idSideA;
		this.idSideB = idSideB;
		this.sideA = sideA;
		this.sideB = sideB;
		this.cat = cat;
		this.subCatA = subCatA;
		this.subCatB = subCatB;
	}
	public FileCardsDB()
	{
		idSideA = null;
		idSideB = null;
		sideA = null;
		sideB = null;
		cat = null;
		subCatA = null;
		subCatB = null;
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
    public String getCat()
    {
    	return cat;
    }
    public void setCat(String cat)
    {
    	this.cat = cat;
    }
    public String getSubCatA()
    {
    	return subCatA;
    }
    public void setSubCatA(String subCatA)
    {
    	this.subCatA = subCatA;
    }
    public String getSubCatB()
    {
    	return subCatB;
    }
    public void setSubCatB(String subCatB)
    {
    	this.subCatB = subCatB;
    }
}
