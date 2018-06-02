import javax.sql.RowSetEvent;
import javax.sql.RowSetListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class PictureRowSetListener implements RowSetListener{
	
	public BooleanProperty booleanProperty = new SimpleBooleanProperty(false);

	@Override
	public void cursorMoved(RowSetEvent event) {
		//not used
	}

	@Override
	public void rowChanged(RowSetEvent event) {
		
		booleanProperty.set(true);
	}

	@Override
	public void rowSetChanged(RowSetEvent event) {
		//not used
	}

}
