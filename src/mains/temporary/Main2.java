package mains.temporary;

import models.extended.Town;

public class Main2 {

	public static void main(String[] args) {

		Town town = new Town();
		town.buildFromMesh("Tests/test - maquette toul");
		town.writeCityGML("test.xml");
	}
}