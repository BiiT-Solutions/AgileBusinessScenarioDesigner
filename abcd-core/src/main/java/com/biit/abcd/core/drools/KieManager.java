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

@SuppressWarnings("rawtypes")
public class KieManager {

	private List<DroolsGlobalVariable> globalVariables;
	private List<Object> facts;
	private KieServices ks;

	public KieManager(){
		globalVariables = new ArrayList<DroolsGlobalVariable>();
		facts = new ArrayList<Object>();
	}

	public List<DroolsGlobalVariable> getGlobalVariables() {
		return globalVariables;
	}

	public void setGlobalVariables(List<DroolsGlobalVariable> globalVariables) {
		this.globalVariables = globalVariables;
	}

	public List<Object> getFacts() {
		return facts;
	}

	public void setFacts(List<Object> facts) {
		this.facts = facts;
	}

	public void buildSessionRules(String rules){
		ks = KieServices.Factory.get();
		KieFileSystem kfs = ks.newKieFileSystem();
		createRules(kfs, rules);
		build(ks, kfs);
	}

	public void execute(){
		startKie(globalVariables, facts);
	}

	/**
	 * Method in charge of initializing the kie session, set the rules, variables and facts and fire the rules
	 * @param rules
	 * @param globalVars
	 * @param facts
	 */
	public void startKie(List<DroolsGlobalVariable> globalVars, List<Object> facts){
		KieRepository kr = ks.getRepository();
		KieContainer kContainer = ks.newKieContainer(kr.getDefaultReleaseId());
		KieSession kSession = kContainer.newKieSession();
		setGlobalVariables(kSession, globalVars);
		insertFacts(kSession, facts);
		kSession.fireAllRules();
	}

	private void createRules(KieFileSystem kfs, String rules){
		kfs.write("src/main/resources/kiemodulemodel/form.drl", rules);
	}

	private void build(KieServices ks, KieFileSystem kfs){
		// Build and deploy the new information
		KieBuilder kb = ks.newKieBuilder(kfs);
		kb.buildAll(); // kieModule is automatically deployed to KieRepository if successfully built.
		if (kb.getResults().hasMessages(Level.ERROR)) {
			throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
		}
	}

	// Insert global variables in the drools session
	private void setGlobalVariables(KieSession kSession, List<DroolsGlobalVariable> globalVars){
		for(DroolsGlobalVariable dgb : globalVars) {
			kSession.setGlobal(dgb.getName(), dgb.getValue());
		}
	}

	// Insert any number of facts in the drools session
	private void insertFacts(KieSession kSession, List<Object> facts){
		for(Object fact : facts){
			kSession.insert(fact);
		}
	};
}
