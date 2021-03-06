///*
// * ===========================================================
// * GTNA : Graph-Theoretic Network Analyzer
// * ===========================================================
// * 
// * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
// * and Contributors
// * 
// * Project Info:  http://www.p2p.tu-darmstadt.de/research/gtna/
// * 
// * GTNA is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// * 
// * GTNA is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// * 
// * You should have received a copy of the GNU General Public License
// * along with this program. If not, see <http://www.gnu.org/licenses/>.
// * 
// * ---------------------------------------
// * TwoPhaseGreedyRegistrationMultiR.java
// * ---------------------------------------
// * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
// * and Contributors 
// * 
// * Original Author: Benjamin Schiller;
// * Contributors:    -;
// * 
// * Changes since 2011-05-17
// * ---------------------------------------
// */
//package gtna.trash.routing.twoPhase;
//
//
//// TODO reimplement TwoPhaseGreedyRegistrationMultiR
//public class TwoPhaseGreedyRegistrationMultiR {
//	// public class TwoPhaseGreedyRegistrationMultiR extends TwoPhaseGreedy
//	// implements
//	// RoutingAlgorithm {
//	// private int copies;
//	//
//	// private int ttl;
//	//
//	// private int duplicates;
//	//
//	// public TwoPhaseGreedyRegistrationMultiR(int copies, int ttl, int
//	// duplicates) {
//	// super("TWO_PHASE_GREEDY_REGISTRATION_MULTI_R", new String[] { "COPIES",
//	// "TTL",
//	// "DUPLICATES" }, new String[] { "" + copies, "" + ttl,
//	// "" + duplicates });
//	// this.copies = copies;
//	// this.ttl = ttl;
//	// this.duplicates = duplicates;
//	// }
//	//
//	// public boolean applicable(Node[] nodes) {
//	// return nodes[0] instanceof RingNodeMultiR;
//	// }
//	//
//	// public Route randomRoute(Node[] nodes, Node src, Random rand) {
//	// int realities = ((RingNodeMultiR) nodes[0]).getIDs().length;
//	// RingNodeMultiR s = (RingNodeMultiR) src;
//	// int index = rand.nextInt(nodes.length);
//	// while (index == s.index()) {
//	// index = rand.nextInt(nodes.length);
//	// }
//	// RingNodeMultiR destNode = (RingNodeMultiR) nodes[index];
//	// Route route = new IDRouteImpl(destNode.getIDs()[0]);
//	// for (int r = 0; r < realities; r++) {
//	// Identifier dest = destNode.getIDs()[r];
//	// for (int d = 0; d <= this.duplicates; d++) {
//	// double pos = (((RingIDMultiR) dest).pos + ((double) d / (double)
//	// (this.duplicates + 1))) % 1.0;
//	// Identifier newDest = new RingIDMultiR(pos, r);
//	// route = this.phase1(s, s, newDest, rand, route);
//	// if (route.success()) {
//	// return route;
//	// }
//	// }
//	// }
//	// return route;
//	// // Route route = this.phase1(s, s, dest, rand, new IDRouteImpl(dest));
//	// // for (int i = 1; i <= this.duplicates; i++) {
//	// // if (!route.success()) {
//	// // double pos = (((RingIDMultiR) dest).pos + ((double) i / (double)
//	// // (this.duplicates + 1))) % 1.0;
//	// // Identifier newDest = new RingIDMultiR(pos, r);
//	// // route = this.phase1(s, s, newDest, rand, route);
//	// // }
//	// // }
//	// // return route;
//	// }
//	//
//	// public void init(Node[] nodes) {
//	// int realities = ((RingNodeMultiR) nodes[0]).getIDs().length;
//	// for (int i = 0; i < nodes.length; i++) {
//	// RingNodeMultiR n = (RingNodeMultiR) nodes[i];
//	// for (int j = 0; j < realities; j++) {
//	// this.registerAll(n, n.getIDs()[j], n.index());
//	// }
//	// }
//	// }
//	//
//	// private void registerAll(RingNodeMultiR start, RingIDMultiR id, int
//	// nodeID) {
//	// this.register(start, id, nodeID);
//	// for (int i = 1; i <= this.duplicates; i++) {
//	// double pos = (id.pos + ((double) i / (double) (this.duplicates + 1))) %
//	// 1.0;
//	// this.register(start, new RingIDMultiR(pos, id.reality), nodeID);
//	// }
//	// }
//	//
//	// private void register(RingNodeMultiR start, RingIDMultiR id, int nodeID)
//	// {
//	// if (this.copies == 1) {
//	// this.registerPhase1(start, id, nodeID);
//	// } else {
//	// Random rand = new Random(System.currentTimeMillis());
//	// for (int i = 0; i < this.copies; i++) {
//	// RingNodeMultiR start2 = this.randomWalk(start, this.ttl, rand);
//	// this.registerPhase1(start2, id, nodeID);
//	// }
//	// }
//	// }
//	//
//	// private RingNodeMultiR randomWalk(RingNodeMultiR current, int ttl,
//	// Random rand) {
//	// if (ttl == 0) {
//	// return current;
//	// }
//	// RingNodeMultiR next = (RingNodeMultiR) current.out()[rand
//	// .nextInt(current.out().length)];
//	// return this.randomWalk(next, ttl - 1, rand);
//	// }
//	//
//	// private void registerPhase1(RingNodeMultiR current, RingIDMultiR id,
//	// int nodeID) {
//	// Node[] out = current.out();
//	// Node max = null;
//	// int maxD = current.out().length + current.in().length;
//	// for (int i = 0; i < out.length; i++) {
//	// if (maxD < out[i].out().length + out[i].in().length) {
//	// max = out[i];
//	// maxD = out[i].out().length + out[i].in().length;
//	// }
//	// }
//	// if (max == null) {
//	// this.registerPhase2(current, id, nodeID);
//	// } else {
//	// this.registerPhase1((RingNodeMultiR) max, id, nodeID);
//	// }
//	// }
//	//
//	// private void registerPhase2(RingNodeMultiR n, RingIDMultiR id, int
//	// nodeID) {
//	// Node[] out = n.out();
//	// Node min = null;
//	// double minD = n.dist(id);
//	// for (int i = 0; i < out.length; i++) {
//	// if (((IDNode) out[i]).dist(id) < minD) {
//	// min = out[i];
//	// minD = ((IDNode) out[i]).dist(id);
//	// }
//	// }
//	// if (min == null) {
//	// n.register(id);
//	// } else {
//	// this.registerPhase2((RingNodeMultiR) min, id, nodeID);
//	// }
//	// }
//}
