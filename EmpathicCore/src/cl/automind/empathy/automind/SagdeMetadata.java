package cl.automind.empathy.automind;


public class SagdeMetadata{
	
	private final int idSector;
	private final int idAxis;
	private final int idCMO;
	private final int idKO;
	
	public SagdeMetadata(int sector, int axis, int cmo, int ko){
		this.idSector = sector;
		this.idAxis = axis;
		this.idCMO = cmo;
		this.idKO = ko;
	}
	public int getIdSector(){
		return this.idSector;
	}
	public int getIdAxis(){
		return this.idAxis;
	}
	public int getIdCmo(){
		return this.idCMO;
	}
	public int getIdKo(){
		return this.idKO;
	}
	public Object getValue(String field){
		if(field.equals("idSector")) return getIdSector();
		if(field.equals("idAxis")) return getIdAxis();
		if(field.equals("idCmo")) return getIdCmo();
		if(field.equals("idKo")) return getIdKo();
		return null;
	}
}
