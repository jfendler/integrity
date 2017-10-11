/*******************************************************************************
 * Copyright (c) 2017 Rene Schneider, GEBIT Solutions GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package de.gebit.integrity.docgen.html;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.gebit.integrity.dsl.SuiteDefinition;
import htmlflow.HtmlView;
import htmlflow.elements.HtmlA;
import htmlflow.elements.HtmlBody;
import htmlflow.elements.HtmlDiv;

/**
 * This view represents a tree view of packages.
 *
 * @author Rene Schneider - initial API and implementation
 *
 */
public class PackageTreeView extends HtmlView<Collection<String>> {

	/**
	 * Constructor.
	 * 
	 * @param somePackages
	 */
	public PackageTreeView(Map<String, Collection<SuiteDefinition>> someSuitesByPackages) {
		head().linkCss("resources/css/main.css").title("Package Tree");

		HtmlBody<?> tempBody = body();
		HtmlDiv<?> tempMainDiv = tempBody.div().classAttr("packagetree");

		for (PackageTreeNode tempRoot : buildPackageTrees(someSuitesByPackages)) {
			addPackageTree(tempRoot, tempMainDiv);
		}
	}

	protected void addPackageTree(PackageTreeNode aNode, HtmlDiv<?> aContainer) {
		addPackageTree(aNode, aContainer, null, new Boolean[1000]);
	}

	protected void addPackageTree(PackageTreeNode aNode, HtmlDiv<?> aContainer, Boolean aLastChildFlag,
			Boolean[] aDepthMap) {
		HtmlDiv<?> tempTreeRow = aContainer.div();
		int tempDepth = 0;
		while (aDepthMap[tempDepth] != null) {
			HtmlDiv<?> tempTreeLine = tempTreeRow.div();

			if (aDepthMap[tempDepth]) {
				if (aDepthMap[tempDepth + 1] == null) {
					if (aLastChildFlag) {
						tempTreeLine.classAttr("nav_line nav_lineright");
						aDepthMap[tempDepth] = false;
					} else {
						tempTreeLine.classAttr("nav_line nav_linedownright");
					}
				} else {
					tempTreeLine.classAttr("nav_line nav_linedown");
				}
			} else {
				tempTreeLine.classAttr("nav_line");
			}

			tempDepth++;
		}

		if (aNode.containsSuites()) {
			HtmlA<?> tempLink = new HtmlA<>("packages/" + aNode.getQualifiedName() + ".html");
			tempLink.text(aNode.getName());
			tempTreeRow.addChild(tempLink);
		} else {
			tempTreeRow.text(aNode.getName());
		}

		aDepthMap[tempDepth] = true;
		List<PackageTreeNode> tempChildren = aNode.getChildren();
		for (int i = 0; i < tempChildren.size(); i++) {
			PackageTreeNode tempChild = tempChildren.get(i);

			boolean tempIsLastChild = (i + 1 == tempChildren.size());
			addPackageTree(tempChild, aContainer, tempIsLastChild, aDepthMap);
		}
		aDepthMap[tempDepth] = null;
	}

	/**
	 * Takes a list of fully qualified package names and constructs a number of trees that fully represent the mentioned
	 * packages.
	 * 
	 * @param somePackages
	 * @return the package trees, with each node in the list being a tree root
	 */
	protected List<PackageTreeNode> buildPackageTrees(Map<String, Collection<SuiteDefinition>> someSuitesByPackages) {
		PackageTreeNode tempRoot = new PackageTreeNode("", null);

		for (Entry<String, Collection<SuiteDefinition>> tempEntry : someSuitesByPackages.entrySet()) {
			PackageTreeNode tempCurrentPackage = tempRoot;

			String[] tempParts = tempEntry.getKey().split("\\.");
			for (String tempPart : tempParts) {
				PackageTreeNode tempNewPackage = tempCurrentPackage.getChild(tempPart);
				if (tempNewPackage == null) {
					tempNewPackage = new PackageTreeNode(tempPart, tempCurrentPackage);
				}
				tempCurrentPackage = tempNewPackage;
			}

			if (tempEntry.getValue().size() > 0) {
				tempCurrentPackage.setContainsSuites();
			}
		}

		List<PackageTreeNode> tempResult = tempRoot.getChildren();
		tempResult.forEach(PackageTreeNode::decoupleFromParent);
		return tempResult;
	}

	/**
	 * A node in the package tree.
	 *
	 *
	 * @author Rene Schneider - initial API and implementation
	 *
	 */
	protected static class PackageTreeNode implements Comparable<PackageTreeNode> {

		/**
		 * Package name.
		 */
		private String name;

		/**
		 * The children of this package.
		 */
		private Map<String, PackageTreeNode> children = new HashMap<String, PackageTreeView.PackageTreeNode>();

		/**
		 * The parent (or null if it's the root).
		 */
		private PackageTreeNode parent;

		/**
		 * Whether there are any suites in this package.
		 */
		private boolean containsSuites;

		/**
		 * Constructs a new instance.
		 * 
		 * @param aName
		 * @param aParent
		 */
		public PackageTreeNode(String aName, PackageTreeNode aParent) {
			name = aName;
			parent = aParent;
			if (aParent != null) {
				aParent.children.put(aName, this);
			}
		}

		public String getName() {
			return name;
		}

		/**
		 * Returns the children in a sorted-by-name fashion.
		 * 
		 * @return
		 */
		public List<PackageTreeNode> getChildren() {
			List<PackageTreeNode> tempList = new ArrayList<PackageTreeView.PackageTreeNode>(children.values());
			Collections.sort(tempList);

			return tempList;
		}

		/**
		 * Decouples this node from the parent.
		 */
		public void decoupleFromParent() {
			if (parent != null) {
				parent.children.remove(getName());
				parent = null;
			}
		}

		/**
		 * Returns a particular child, identified by its name.
		 * 
		 * @param aChildName
		 * @return
		 */
		public PackageTreeNode getChild(String aChildName) {
			return children.get(aChildName);
		}

		public PackageTreeNode getParent() {
			return parent;
		}

		/**
		 * Whether this package contains suites.
		 * 
		 * @return
		 */
		public boolean containsSuites() {
			return containsSuites;
		}

		/**
		 * Marks this package as containing suites.
		 * 
		 * @return
		 */
		public void setContainsSuites() {
			containsSuites = true;
		}

		/**
		 * Returns the fully qualified name.
		 * 
		 * @return
		 */
		public String getQualifiedName() {
			if (parent != null) {
				return getParent().getQualifiedName() + "." + getName();
			} else {
				return getName();
			}
		}

		@Override
		public int compareTo(PackageTreeNode anOther) {
			return name.compareTo(anOther.name);
		}

	}
}
