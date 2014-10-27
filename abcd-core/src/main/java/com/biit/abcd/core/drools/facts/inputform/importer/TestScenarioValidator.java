package com.biit.abcd.core.drools.facts.inputform.importer;


public class TestScenarioValidator {

//	private static TreeObject findChild(TreeObject treeObject, TestScenarioObject testScenarioObject) {
//		for (TreeObject child : treeObject.getChildren()) {
//			System.out.println("TREE OBJECT: " + child.getUniqueNameReadable());
//			System.out.println("TEST OBJECT: " + testScenarioObject.getName());
//			if (child.getUniqueNameReadable().equals(testScenarioObject.getName())) {
//				System.out.println("RETURN");
//				return child;
//			}
//		}
//		return null;
//	}
//
//	/**
//	 * Checks if testScenarioObject is a subset of the treeObject structure. To
//	 * be a subset all elements of testScenarioObject and their children need to
//	 * be present in current structure.
//	 * 
//	 * @throws ChildrenNotFoundException
//	 * 
//	 */
//	public static boolean isSubset(TreeObject treeObject, TestScenarioObject testScenarioObject)
//			throws ChildrenNotFoundException {
//		for (TreeObject child : testScenarioObject.getChildren()) {
//			// In the case the child is a repeated group
//			if (child instanceof TestScenarioRepeatedGroup) {
//				System.out.println("REPEATED GROUP");
//				for (TreeObject repeatedGroupChild : child.getChildren()) {
//					TreeObject thisCorrespondence = findChild(treeObject, (TestScenarioObject) repeatedGroupChild);
//					if (thisCorrespondence == null
//							|| (!isSubset(thisCorrespondence, (TestScenarioObject) repeatedGroupChild))) {
//						return false;
//					}
//				}
//			} else {
//				// Find a child of this group with the same name as the
//				// children
//				TreeObject thisCorrespondence = findChild(treeObject, (TestScenarioObject) child);
//				if (thisCorrespondence == null || (!isSubset(thisCorrespondence, (TestScenarioObject) child))) {
//					// If child is not found is not a subset.
//					// If child is not a subset of thisCorrespondence is not a
//					// subset
//					return false;
//				}
//			}
//		}
//		// If all the elements have not failed then is a subset.
//		return true;
//	}
}
