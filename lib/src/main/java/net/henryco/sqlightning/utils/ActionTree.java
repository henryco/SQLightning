package net.henryco.sqlightning.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HenryCo on 18/05/17
 */

public final class ActionTree {

	private List<ActionTree> actionNodes = new ArrayList<>();
	private Runnable action;

	public ActionTree() {}
	public ActionTree(Runnable action) {
		this();
		setAction(action);
	}

	public void action() {
		action.run();
		for (ActionTree node: actionNodes) node.action();
	}

	public ActionTree setAction(Runnable action) {
		this.action = action;
		return this;
	}

	public ActionTree addActionNode(ActionTree node) {
		actionNodes.add(node);
		return this;
	}

	public ActionTree removeActionNode(ActionTree node) {
		actionNodes.remove(node);
		return this;
	}

}
