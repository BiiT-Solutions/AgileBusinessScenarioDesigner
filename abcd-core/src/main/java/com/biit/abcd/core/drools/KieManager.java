package com.biit.abcd.core.drools;

import java.util.ArrayList;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message.Level;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.biit.abcd.core.drools.rules.GroupRuleFired;
import com.biit.orbeon.form.ISubmittedForm;

public class KieManager {

	private List<DroolsGlobalVariable> globalVariables;
	private List<ISubmittedForm> facts;
	private KieServices ks;

	public KieManager() {
		this.globalVariables = new ArrayList<DroolsGlobalVariable>();
		this.facts = new ArrayList<ISubmittedForm>();
	}

	public List<DroolsGlobalVariable> getGlobalVariables() {
		return this.globalVariables;
	}

	public void setGlobalVariables(List<DroolsGlobalVariable> globalVariables) {
		if (globalVariables != null) {
			this.globalVariables = globalVariables;
		}
	}

	public List<ISubmittedForm> getFacts() {
		return this.facts;
	}

	public void setFacts(List<ISubmittedForm> list) {
		this.facts = list;
	}

	public void buildSessionRules(String rules) {
		this.ks = KieServices.Factory.get();
		KieFileSystem kfs = this.ks.newKieFileSystem();
		createRules(kfs, rules);
		build(this.ks, kfs);
	}

	public void execute() {
		this.startKie(this.globalVariables, this.facts);
	}

	/**
	 * Method in charge of initializing the kie session, set the rules, variables and facts and fire the rules
	 * 
	 * @param rules
	 * @param globalVars
	 * @param facts
	 */
	public void startKie(List<DroolsGlobalVariable> globalVars, List<ISubmittedForm> facts) {
		KieRepository kr = ks.getRepository();
		KieContainer kContainer = ks.newKieContainer(kr.getDefaultReleaseId());
		KieSession kSession = kContainer.newKieSession();
		this.setGlobalVariables(kSession, globalVars);
		this.insertFacts(kSession, facts);
		kSession.fireAllRules();
	}

	private void createRules(KieFileSystem kfs, String rules) {
		// Needs a virtual path to store the file and retrieve it
		// Can't load the string directly
		kfs.write("src/main/resources/kiemodulemodel/form.drl", rules);
	}

	private void build(KieServices ks, KieFileSystem kfs) {
		// Build and deploy the new information
		KieBuilder kb = ks.newKieBuilder(kfs);
		kb.buildAll(); // kieModule is automatically deployed to KieRepository if successfully built.
		if (kb.getResults().hasMessages(Level.ERROR)) {
			throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
		}
	}

	// Insert global variables in the drools session
	private void setGlobalVariables(KieSession kSession, List<DroolsGlobalVariable> globalVars) {
		for (DroolsGlobalVariable dgb : globalVars) {
			kSession.setGlobal(dgb.getName(), dgb.getValue());
		}
	}

	// Insert any number of facts in the drools session
	private void insertFacts(KieSession kSession, List<ISubmittedForm> facts) {
		kSession.insert(new GroupRuleFired());
		for (ISubmittedForm fact : facts) {
			kSession.insert(fact);
		}
	};
}
