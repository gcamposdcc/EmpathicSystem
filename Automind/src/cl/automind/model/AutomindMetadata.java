package cl.automind.model;

import cl.automind.sagde.model.SagdeMetadata;

public class AutomindMetadata {
	private SagdeMetadata sagdeMetadata;
	private SessionInfo sessionContext;
	private AlertSystemInfo alertSystemInfo;
	public void setSessionContext(SessionInfo sessionContext) {
		this.sessionContext = sessionContext;
	}
	public SessionInfo getSessionContext() {
		return sessionContext;
	}
	public void setSagdeMetadata(SagdeMetadata sagdeMetadata) {
		this.sagdeMetadata = sagdeMetadata;
	}
	public SagdeMetadata getSagdeMetadata() {
		return sagdeMetadata;
	}
	public void setAlertSystemInfo(AlertSystemInfo alertSystemInfo) {
		this.alertSystemInfo = alertSystemInfo;
	}
	public AlertSystemInfo getAlertSystemInfo() {
		return alertSystemInfo;
	}
}
