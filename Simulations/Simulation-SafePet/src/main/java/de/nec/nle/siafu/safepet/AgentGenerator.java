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

import java.util.*;

import de.nec.nle.siafu.exceptions.PlaceNotFoundException;
import de.nec.nle.siafu.model.Agent;
import de.nec.nle.siafu.model.Place;
import de.nec.nle.siafu.model.Position;
import de.nec.nle.siafu.model.World;
import de.nec.nle.siafu.types.Publishable;
import de.nec.nle.siafu.types.Text;

import static de.nec.nle.siafu.safepet.Constants.Fields.ACTIVITY;

/**
 * Utility class to generate an agent with randomized parameters.
 * 
 * @author Miquel Martin
 * 
 */
final class AgentGenerator {

	private static int idPet = 1;

	/**
	 * A random number generator.
	 */
	private static Random rand = new Random();

	/** Prevent the class from being instantiated. */
	private AgentGenerator() {
	}

	/**
	 * Create a random agent.
	 * 
	 * @param world the world to create it in
	 * @return the new agent
	 */
	public static Agent createRandomAgent(final World world, Place place, String namePet, String typePet) {
		Agent a = new Agent(place.getPos(), typePet, world);
		a.set(ACTIVITY, Constants.Activity.WALKING);
		a.setName(namePet);
		a.setSpeed(1);
		a.setVisible(true);
		return a;
	}

	/**
	 * Create a number of random agents.
	 * 
	 * @param amount the amount of agents to create
	 * @param world the world where the agents will dwell
	 * @return an ArrayList with the created agents
	 */
	public static ArrayList<Agent> createRandomPopulation(
			final int amount, List<Place> places, final World world) {
		ArrayList<Agent> population = new ArrayList<>();

		places.forEach(place -> {
			String typePet = "";
			String namePlace = place.getName().split("-")[0];

			if(namePlace.equals("PlaceOneZumbi"))
				typePet = "Dog";
			else if(namePlace.equals("PlaceTwoZumbi"))
				typePet = "cat";
			else if(namePlace.equals("PlaceThreeZumbi"))
				typePet = "Horse";
			else if(namePlace.equals("PlaceFourZumbi"))
				typePet = "Dog";
			else if(namePlace.equals("PlaceFiveZumbi"))
				typePet = "cat";
			else if(namePlace.equals("PlaceSixZumbi"))
				typePet = "Horse";
			else if(namePlace.equals("PlaceSevenZumbi"))
				typePet = "Dog";

			for(int i = 0; i < amount; i++) {
				String namePet = String.format("%s-%d", typePet.toLowerCase(), idPet++);
				population.add(createRandomAgent(world, place, namePet, typePet));
			}

			System.out.println("Place = "+namePlace);
		});

		return population;
	}

	/**
	 * Create a random info field for the agents. In this case, the field's
	 * empty.
	 * 
	 * @param world the world the agent lives in
	 * @return the info field
	 */
	public static Map<String, Publishable> createRandomInfo(
			final World world) {
		Map<String, Publishable> info = new HashMap<String, Publishable>();
		return info;
	}

	public static Agent createConttroledAgent(Position start, String typeAgent, String nameAgent, World world) {
		Agent agent = new Agent(start, typeAgent, world);
		agent.setName(nameAgent);
		agent.setPos(start);
		agent.setSpeed(1);
		agent.getControl();
		agent.set(ACTIVITY, Constants.Activity.WALKING);
		agent.setVisible(true);
		return agent;
	}
}
