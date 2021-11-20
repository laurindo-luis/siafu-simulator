/*
 * Copyright NEC Europe Ltd. 2006-2007
 *
 * This file is part of the context simulator called Siafu.
 *
 * Siafu is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * Siafu is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.nec.nle.siafu.safepet;

import static de.nec.nle.siafu.safepet.Constants.POPULATION_OF_POINTS;
import static de.nec.nle.siafu.safepet.Constants.Fields.ACTIVITY;

import java.util.*;

import de.nec.nle.siafu.behaviormodels.BaseAgentModel;
import de.nec.nle.siafu.exceptions.PlaceTypeUndefinedException;
import de.nec.nle.siafu.model.Agent;
import de.nec.nle.siafu.model.Place;
import de.nec.nle.siafu.model.World;
import de.nec.nle.siafu.safepet.Constants.Activity;
import de.nec.nle.siafu.types.EasyTime;

/**
 * This Agent Model defines the behavior of users in this test simulation.
 * Essentially, most users will wander around in a zombie like state, except
 * for Pietro and Teresa, who will stay put, and the postman, who will spend a
 * simulation life time running between the two ends of the map.
 *
 * @author Miquel Martin
 *
 */
public class AgentModel extends BaseAgentModel {

	/**
	 * The time at which the psotman moves fastest.
	 */
	private static final int POSTMAN_PEAK = 12;

	/**
	 * A random number generator.
	 */
	private static final Random RAND = new Random();

	/**
	 * The top speed at which agents will move.
	 */
	private static final int TOP_SPEED = 10;

	/**
	 * A special user that plays a courrier of the Czar.
	 */
	private Agent dogOne;
	private Agent dogTwo;
	private Agent catOne;

	/**
	 * The current time.
	 */
	private EasyTime now;

	/** Place one in the simulation. */
	private Place placeHome;
	private Place placeHomeTwo;
	private Place placeHomeThree;
	private Place placeOneZumbi;
	private Place placeTwoZumbi;
	private Place placeThreeZumbi;
	private Place placeFourZumbi;
	private Place placeFiveZumbi;
	private Place placeSixZumbi;
	private Place placeSevenZumbi;

	private List<Place> places;

	/** Place two in the simulation. */

	/**
	 * Constructor for the agent model.
	 *
	 * @param world the simulation's world
	 */
	public AgentModel(final World world) {
		super(world);
	}

	/**
	 * Create a bunch of agents that will wander around aimlessly. Tweak them
	 * for testing purposes as needed. Two agents, Pietro and Teresa, are
	 * singled out and left under the control of the user. A third agent,
	 * Postman, is set to run errands between the two places int he map.
	 *
	 * @return the created agents
	 */
	public ArrayList<Agent> createAgents() {
		world.stopSpinning(true);

		try {
			placeHome = world.getPlacesOfType("Home").iterator().next();
			placeHomeTwo = world.getPlacesOfType("HomeTwo").iterator().next();
			placeHomeThree = world.getPlacesOfType("HomeThree").iterator().next();
			placeOneZumbi = world.getPlacesOfType("PlaceOneZumbi").iterator().next();
			placeTwoZumbi = world.getPlacesOfType("PlaceTwoZumbi").iterator().next();
			placeThreeZumbi = world.getPlacesOfType("PlaceThreeZumbi").iterator().next();
			placeFourZumbi = world.getPlacesOfType("PlaceFourZumbi").iterator().next();
			placeFiveZumbi = world.getPlacesOfType("PlaceFiveZumbi").iterator().next();
			placeSixZumbi = world.getPlacesOfType("PlaceSixZumbi").iterator().next();
			placeSevenZumbi = world.getPlacesOfType("PlaceSevenZumbi").iterator().next();

			places =  Arrays.asList(placeOneZumbi, placeTwoZumbi,
					placeThreeZumbi, placeFourZumbi, placeFiveZumbi,
					placeSixZumbi, placeSevenZumbi);
		} catch (PlaceTypeUndefinedException e) {
			throw new RuntimeException(e);
		}

		int totalPetNumbers = POPULATION_OF_POINTS * places.size();

		System.out.println("Creating " + totalPetNumbers + " pets zumbis.");
		ArrayList<Agent> population = AgentGenerator.createRandomPopulation(POPULATION_OF_POINTS,
				places, world);

		/** Creating controlled pets home one */
		System.out.println("Home one");

		dogOne = AgentGenerator.createConttroledAgent(placeHome.getPos(),
				"Dog", String.format("dog-%d", totalPetNumbers+1), world);
		population.add(dogOne);
		System.out.println(dogOne.getName());

		dogTwo = AgentGenerator.createConttroledAgent(placeHome.getPos(),
				"Dog", String.format("dog-%d", totalPetNumbers+2), world);
		population.add(dogTwo);
		System.out.println(dogTwo.getName());

		catOne = AgentGenerator.createConttroledAgent(placeHome.getPos(),
				"cat", String.format("cat-%d", totalPetNumbers+3), world);
		population.add(catOne);
		System.out.println(catOne.getName());

		/** Creating controlled pets home two */
		System.out.println("Home two");


		return population;
	}

	/**
	 * Make all the normal agents wander around, and the postman, run errands
	 * from one place to another. His speed depends on the time, slowing down
	 * at night.
	 *
	 * @param agents the list of agents
	 */
	public void doIteration(final Collection<Agent> agents) {
		Calendar time = world.getTime();
		now = new EasyTime(time.get(Calendar.HOUR_OF_DAY), time
				.get(Calendar.MINUTE));
		handleDogOne();
		handleDogTwo();
		handleCatOne();
		for (Agent a : agents) {
			if (!a.isOnAuto()) {
				continue; // This guy's being managed by the user interface
			}
			if (a.equals(dogOne) || a.equals(dogTwo) || a.equals(catOne)) {
				continue;
			}
			handleAgent(a);
		}
	}

	/**
	 * Move the postman from one place to the next, increasing the speed the
	 * closer to noon it is.
	 *
	 */
	private void handleDogOne() {
		dogOne.setSpeed(POSTMAN_PEAK
				- Math.abs(POSTMAN_PEAK - now.getHour()));
		if (dogOne.isAtDestination()) {
			if (dogOne.getPos().equals(placeHome.getPos())) {
				dogOne.setDestination(placeThreeZumbi);
			} else if(dogOne.getPos().equals(placeThreeZumbi.getPos())) {
				dogOne.setDestination(placeOneZumbi);
			} else if(dogOne.getPos().equals(placeOneZumbi.getPos())) {
				dogOne.setDestination(placeHome);
			}
		}
	}

	private void handleDogTwo() {
		dogTwo.setSpeed(POSTMAN_PEAK
				- Math.abs(POSTMAN_PEAK - now.getHour()));
		if (dogTwo.isAtDestination()) {
			if (dogTwo.getPos().equals(placeHome.getPos())) {
				//Sair depois de 3 segundos
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						dogTwo.setDestination(placeTwoZumbi);
					}
				}, 2000);
			} else if(dogTwo.getPos().equals(placeTwoZumbi.getPos())) {
				dogTwo.setDestination(placeSevenZumbi);
			} else if(dogTwo.getPos().equals(placeSevenZumbi.getPos())) {
				dogTwo.setDestination(placeHome);
			}
		}
	}

	private void handleCatOne() {
		catOne.setSpeed(POSTMAN_PEAK
				- Math.abs(POSTMAN_PEAK - now.getHour()));
		if (catOne.isAtDestination()) {
			if (catOne.getPos().equals(placeHome.getPos())) {
				//Sair depois de 4 segundos
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						catOne.setDestination(placeSevenZumbi);
					}
				}, 3000);
			} else if(catOne.getPos().equals(placeSevenZumbi.getPos())) {
				catOne.setDestination(placeFourZumbi);
			} else if(catOne.getPos().equals(placeFourZumbi.getPos())) {
				catOne.setDestination(placeHome);
			}
		}
	}

	/**
	 * Keep the agent wandering around zombie style.
	 *
	 * @param a the agent to zombiefy
	 */
	private void handleAgent(final Agent a) {
		switch ((Activity) a.get(ACTIVITY)) {
			case WAITING:
				break;

			case WALKING:
				a.wander();
				break;
			default:
				throw new RuntimeException("Unable to handle activity "
						+ (Activity) a.get(ACTIVITY));
		}

	}
}
