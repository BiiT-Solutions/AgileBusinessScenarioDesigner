package com.biit.abcd.persistence.entity;

import java.util.Set;

import com.biit.form.IBaseFormView;

public interface IFormView extends IBaseFormView {

	public void setStatus(FormWorkStatus status);

	public FormWorkStatus getStatus();

	public Set<Integer> getLinkedFormVersions();

	public Long getLinkedFormOrganizationId();

	public String getLinkedFormLabel();

	public boolean isLastVersion();
}
