package es.unican.meteo.tap.model;


import es.predictia.util.Metadata;
import es.unican.meteo.tap.constants.Variables;

public class RoleDataset extends Role{

	private String datasetURL;
	private boolean isPrivate;
	private String label;

	public RoleDataset(){
		super();
		Metadata meta = new Metadata(super.getMetadata());
		this.datasetURL = meta.getVariable(Variables.URL.toString());
		this.isPrivate = Boolean.getBoolean(meta.getVariable(Variables.IS_PRIVATE.toString()));
		this.label = meta.getVariable(Variables.LABEL.toString());
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDatasetURL() {
		Metadata meta = new Metadata(super.getMetadata());
		meta.getVariable(Variables.URL.toString());
		return datasetURL;
	}

	public void setDatasetURL(String datasetURL) {
		Metadata meta = new Metadata(super.getMetadata());
		meta.setVariable(Variables.URL.toString(), datasetURL);
		this.datasetURL = datasetURL;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivacy(boolean isPrivate) {
		Metadata meta = new Metadata(super.getMetadata());
		meta.setVariable(Variables.IS_PRIVATE.toString(), String.valueOf(isPrivate));
		this.isPrivate = isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	
	public boolean getIsPrivate(){
		return isPrivate;
	}
}
