package edu.ufl.cise.cs1.controllers;
import game.controllers.AttackerController;
import game.models.*;
import java.awt.*;
import java.util.List;

public final class StudentAttackerController implements AttackerController
{
	public void init(Game game) { }

	public void shutdown(Game game) { }

	public int update(Game game,long timeDue) {
		int action = 0;
		int nearDist;

		//Initializing Defender/Nodes/Attacker
		Defender nearGhost;
		Node nearPill;
		Attacker pakuMan = game.getAttacker();

		//The lists needed
		List<Defender> ghosts = game.getDefenders();
		List<Node> pills = game.getPowerPillList();
		List<Node> points = game.getPillList();

		// Nearest defender distance and object
		nearGhost = (Defender) pakuMan.getTargetActor(ghosts, true);
		nearDist = nearGhost.getLocation().getPathDistance(pakuMan.getLocation());
		// Ensures no error when power pills dont exist
		if (pills.size() > 0) {
			nearPill = pakuMan.getTargetNode(pills, true);
		}
		else{
			nearPill = null;
		}
		// Ensures that when the nearest ghost is super far that the pakuMan doesn't run away from normal pills.
		if (nearDist < 98) {
			// Essentially the default state
			action = pakuMan.getNextDir(nearGhost.getLocation(), false);
		} else {
			action = pakuMan.getNextDir(pakuMan.getTargetNode(points, true), true);
		}
		// Ensures pakuMan won't run directly into a defender
		if (pakuMan.getPossibleDirs(false).size() == 1) {
			if (nearDist > 9 ) {
				action = pakuMan.getPossibleDirs(false).get(0);
			}
		}
		// If ghost is vulnerable then run into it
		if (nearGhost.isVulnerable()) {
			action = pakuMan.getNextDir(nearGhost.getLocation(), true);
		}
		// Checks if powerPills exist
		if (pills.size() > 0) {
			// Checks if this is the first pill
			if (pills.size() < 4) {
				// If not first pill, run if the distance to the nearest ghost is slightly less than the distance to the power Pill
				if (0.98*nearDist>nearPill.getPathDistance(pakuMan.getLocation())){
					action = pakuMan.getNextDir(nearPill, true);
				}
			}
			// Move side to side if at a power pill
			if (nearPill.getPathDistance(pakuMan.getLocation()) < 3 && nearDist > 5) {
				if (nearPill.getPathDistance(pakuMan.getLocation()) == 2) {
					action = pakuMan.getNextDir(nearPill, true);
				}
				if (nearPill.getPathDistance(pakuMan.getLocation()) == 1) {
					action = pakuMan.getNextDir(nearPill, false);
				}
			} else {
				if (nearPill.getPathDistance(pakuMan.getLocation()) < 3) {
					action = pakuMan.getNextDir(nearPill, true);
				}
			}
			}
		return action;
	}
}