package fr.dabsunter.darkour.util;

import fr.dabsunter.darkour.api.parkour.Parkour;

public class IncompleteParkourException extends RuntimeException {
	private final Parkour parkour;
	private final Parkour.MissingParameter parameter;

	public IncompleteParkourException(Parkour parkour, Parkour.MissingParameter parameter) {
		super("Parkour has missing parameter: " + parameter);
		this.parkour = parkour;
		this.parameter = parameter;
	}

	public Parkour getParkour() {
		return parkour;
	}

	public Parkour.MissingParameter getMissingParameter() {
		return parameter;
	}
}
