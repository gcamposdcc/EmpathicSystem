package cl.automind.sagde.model;

/**
 * <p>Encapsulates the metadata of a educational activity
 * according to the Sagde specification.</p>
 * <p>According to this specification an activity can be
 * categorized according to 5 elements:
 * <ul>
 * <li><b>Sector:</b> Educational sector (Math, Biology, etc)</li>
 * <li><b>Axis:</b> Educational axis (Randomness, number theory). Each Axis is associated to an specific Sector.</li>
 * <li><b>Cmo:</b> (Contenido mínimo obligatorio - Minimal mandatory content).
 * Represents a specific content in an Axis. This content is also linked to an specific Level</li>
 * <li><b>Ko:</b> Knowledge object. A further specification of a Cmo. Each Ko is Associated to an specific Cmo.</li>
 * <li><b>Level:</b> The educational level (1st, 2nd, 3rd grade, etc)</li>
 * </ul></p>
 * @author Guillermo
 */
public class SagdeMetadata{

	private int idSector;
	private int idAxis;
	private int idCmo;
	private int idKo;
	private int idLevel;

	/**
	 * Builds an instance with all ids setted to 1.
	 */
	public SagdeMetadata(){
		this.idSector = 1;
		this.idAxis = 1;
		this.idCmo = 1;
		this.idKo = 1;
	}
	/**
	 * Creates an instance with the specified ids.
	 * @param sector The Id for the specific Sector
	 * @param axis The Id for the specific Axis
	 * @param cmo The Id for the specific Cmo
	 * @param ko The Id for the specific Ko
	 * @param level The Id for the specific Level
	 */
	public SagdeMetadata(int sector, int axis, int cmo, int ko, int level){
		this.idSector = sector;
		this.idAxis = axis;
		this.idCmo = cmo;
		this.idKo = ko;
		this.setIdLevel(level);
	}
	public void setIds(int sector, int axis, int cmo, int ko, int level){
		this.idSector = sector;
		this.idAxis = axis;
		this.idCmo = cmo;
		this.idKo = ko;
		this.setIdLevel(level);
	}

	public int getIdSector(){
		return this.idSector;
	}
	public void setIdSector(int idSector){
		this.idSector = idSector;
	}
	public int getIdAxis(){
		return this.idAxis;
	}
	public void setIdAxis(int idAxis){
		this.idAxis = idAxis;
	}
	public int getIdCmo(){
		return this.idCmo;
	}
	public void setIdCmo(int idCmo){
		this.idCmo = idCmo;
	}
	public int getIdKo(){
		return this.idKo;
	}
	public void setIdLevel(int idLevel) {
		this.idLevel = idLevel;
	}
	public int getIdLevel() {
		return idLevel;
	}
	public void setIdKo(int idKo){
		this.idKo = idKo;
	}
	public Object getValue(String field){
		if(field.equals("idSector")) return getIdSector();
		if(field.equals("idAxis")) return getIdAxis();
		if(field.equals("idCmo")) return getIdCmo();
		if(field.equals("idKo")) return getIdKo();
		return null;
	}

}
