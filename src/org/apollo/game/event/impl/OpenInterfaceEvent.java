package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.InterfaceSet;

/**
 * An event which opens an interface.
 * @author Graham
 */
public final class OpenInterfaceEvent extends Event {

	/**
	 * The interface id.
	 */
	private final int id;
	
	/**
	 * The 'root' interface id.
	 */
	private final int rootInterface;
	
	/**
	 * The layer it is on, also known as (overlay).
	 */
	private final int layer;
	
	/**
	 * The component the interface is using.
	 */
	private final int childComponent;

	/**
	 * Creates the event with the specified interface id.
	 * @param id The interface id.
	 */
	public OpenInterfaceEvent(int layer, int rootInterface, int id,  int childComponent) {
		this.layer = layer;
		this.rootInterface = rootInterface;
		this.id = id;
		this.childComponent = childComponent;
	}
	
	public OpenInterfaceEvent(int layer, int id,  int childComponent) {
		this.layer = layer;
		this.rootInterface = InterfaceSet.DEFAULT_ROOT_INTERFACE;
		this.id = id;
		this.childComponent = childComponent;
	}
	
	public OpenInterfaceEvent( int id,  int childComponent) {
		this.layer = InterfaceSet.DEFAULT_LAYER_SETTING;
		this.rootInterface = InterfaceSet.DEFAULT_ROOT_INTERFACE;
		this.id = id;
		this.childComponent = childComponent;
	}

	/**
	 * Gets the interface id.
	 * @return The interface id.
	 */
	public int getId() {
		return id;
	}

	public int getRootInterface() {
		return rootInterface;
	}

	public int getLayer() {
		return layer;
	}

	public int getChildComponent() {
		return childComponent;
	}
}
