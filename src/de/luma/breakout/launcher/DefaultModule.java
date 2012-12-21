package de.luma.breakout.launcher;

import com.google.inject.AbstractModule;

import de.luma.breakout.controller.GameController;
import de.luma.breakout.controller.IGameController;

public class DefaultModule extends AbstractModule {

	/*
	 * (non-Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		bind(IGameController.class).to(GameController.class);
	}

}
