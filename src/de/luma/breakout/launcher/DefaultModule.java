package de.luma.breakout.launcher;

import com.google.inject.AbstractModule;

import de.luma.breakout.controller.GameController;
import de.luma.breakout.controller.IGameController;

/**
 * DefaultModule for dependecy injection
 * @author mabausch
 *
 */
public class DefaultModule extends AbstractModule {

	/**
	 * Mapps Interfaces to concret classes
	 */
	@Override
	protected void configure() {
		bind(IGameController.class).to(GameController.class);
	}

}
