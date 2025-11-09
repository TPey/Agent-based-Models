---

# Agent-based modeling: Persistence in lottery gambling (Euromillions FDJ France)

## Table of Contents

1. [Background and motivation](#context-and-motivation)
2. [Purpose of the model](#purpose-of-the-model)
3. [Protocole ODD](#protocole-odd)

   1. [Overview](#overview)
   2. [Design Concepts](#design-concepts)
   3. [Details](#etails)
4. [Conclusion](#Conclusion)

---

## Background and motivation
> I have been playing the lottery for a long time and I lose more often than I win, but I continue to play.
> Talking with my friends and family, I noticed that many others do the same.
> This behavior seemed irrational, even absurd, to me.
> So I decided to take advantage of the courses on **agent-based models (ABM)** to try to **understand this phenomenon**.

---

## Purpose of the model

**Goal :** Understanding why players continue to play the lottery **despite repeated losses**, by integrating individual behaviors and social influence.

### Main  hypothesis

1. Each player has:

   * a limited **budget**,
   * a subjective **belief** in their luck,
   * a **propensity for risk**,
   * a **psychological resistance** to stopping the game.
2. Players interact socially and influence each other.
3. Rare wins, cognitive biases, and social influence explain why gambling persists.

---

## ODD Protocol

### Overview

**Specific objective:** To study the combined effect of losses, rare gains, and social interactions on player behavior.

**Entities, variables, and scales:**

| Element                  | Description                                                                            |
| ------------------------ | -------------------------------------------------------------------------------------- |
| **Agents (turtles)**     | Individual players with budgets and psychological traits                               |
| **Globale Variable**   | `jackpot`, `ticket-price`, `base-win-prob`, `influence-strength`, `social-radius`      |
| **Agents variable** | `budget`, `belief`, `plays?`, `risk-propensity`, `stubbornness`, `last-win-tick`       |
| **Scales**             | Discrete time (`tick`), population ~1000 players, social influence within a given radius |

**Process and scheduling:**

1. Each player calculates their propensity to play by combining belief, risk, and social influence.
2. Possible purchase of a ticket if the propensity exceeds a threshold.
3. Random drawing of the jackpot for players who have purchased a ticket.
4. Update budget and belief based on win or loss.
5. Check for potential game termination (budget or belief too low).
6. Collect overall statistics: average belief, percentage playing, tickets sold, and winners.

---

### Design Concepts

* **Emergence:** Percentage of active players, average belief, and ticket sales emerge from individual decisions and social influence.
* **Adaptation:** Players adjust their belief after a win or loss.
* **Objectives:** Maximize perceived gain or potential profit.
* **Perception:** Players perceive the average belief of their neighbors within a given social radius.
* **Interaction:** Social influence modifying the propensity to play.
* **Stochasticity:** Random draws, cognitive biases, and decision to stop modulated by stubbornness.
* **Collectives:** No formal groups, but social clusters emerge from local influence.
* **Observation:** Percentage of active players, average belief, total number of tickets sold, and number of winners.

---

### Details

**Initialisation :**

* 1,000 players with random initial budgets and beliefs.
* Random risk propensity and resistance to stopping.
* All players start playing.
* Global parameters set: jackpot, ticket price, thresholds, and social influence.

**Submodels:**

1. **Purchase decision:**
   [
   decision_score = belief × (1 + 0.5 × risk_propensity) + influence_strength × neighbors_belief
   ]
   If `decision_score > play_threshold`, purchase the ticket.

2. **Purchase and draw:**

* Budget reduced by `ticket_price`.
* Draw with probability `base-win-prob` of winning the jackpot.

3. **Belief update:**

* Win → `belief` increases (`recovery_after_win`).
* Loss → `belief` decreases (`belief_decrease`) or increases slightly if the “gambler's fallacy” effect applies.

4. **Game termination:**

* If `budget < ticket_price` or `belief < stop-belief-threshold`.
* Probability of termination modulated by `stubbornness`.

5. **Statistics collection:**

* `avg-belief`, `pct-playing`, `total-tickets-sold`, `total-winners`.

---

## Conclusion
Once this ODD protocol has been implemented and simulated, it will enable the persistence of lottery-playing behavior to be reproduced, incorporating individual characteristics, cognitive biases, and social influence.
The model would show how individual irrational behaviors can generate collective dynamics that may or may not be observable, providing a comprehensive view of the dynamics of gambling in the population.

The next step would be to model this protocol in NetLogo in order to have an analytical tool capable of:

* Understanding and visualizing the dynamics of gambling behavior
* Testing policies or interventions to influence player behavior.

---
# Author 
* [Peyanan TRAORE](ptraore97@gmail.com)

---
# Repository
* [Agent-Based Model](https://github.com/TPey/Agent-based-Models)
# License
* github Free
