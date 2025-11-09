;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Modèle ABM : comportements de joueurs à la loterie (Euromillions)
;; Auteur : TPey
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

globals [
  ;; paramètres de simulation
  number-of-players
  initial-budget
  initial-belief
  initial-belief-0
  play-threshold
  max-ticks
  recovery-after-win
  gambler-fraction
  gambler-increase
  belief-decrease
  stop-belief-threshold
  ticket-price
  base-win-prob
  influence-strength
  social-radius

  ;; variables d’état
  jackpot
  avg-belief
  pct-playing
  total-tickets-sold
  total-winners
]

turtles-own [
  budget
  belief
  plays?
  risk-propensity
  stubbornness
  last-win-tick
]

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; SETUP
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
to setup
  clear-all

  ;; paramètres de simulation
  set number-of-players 1000
  set initial-budget 50
  set initial-belief 0.05
  set initial-belief-0 0.05
  set play-threshold 0.2
  set max-ticks 520
  set recovery-after-win 0.3
  set gambler-fraction 0.05
  set gambler-increase 0.02
  set belief-decrease 0.01
  set stop-belief-threshold 0.02
  set ticket-price 2.5
  set base-win-prob 1e-7
  set influence-strength 0.3
  set social-radius 5
  set jackpot 10000000

  ;; variables globales d’état
  set total-tickets-sold 0
  set total-winners 0

  ;; création des joueurs
  create-turtles number-of-players [
    setxy random-xcor random-ycor
    set color blue
    set size 1.2
    set budget (initial-budget) + random (initial-budget)
    set belief (initial-belief) + random-float (initial-belief-0)
    if belief > 1 [ set belief 1 ]
    if belief < 0 [ set belief 0 ]
    set plays? true
    set risk-propensity random-float 1
    set stubbornness random-float 1
    set last-win-tick -9999
  ]
  
  clear-all-plots
  reset-ticks
end

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; GO
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
to go
  if ticks >= max-ticks [ stop ]

  set total-tickets-sold 0

  ask turtles [
    if plays? and (budget >= ticket-price) [
      let decision-score belief * (1 + 0.5 * risk-propensity)
      let neighbors-belief mean [belief] of turtles in-radius social-radius
      if neighbors-belief != nobody [
        set decision-score decision-score + influence-strength * neighbors-belief
      ]
      if decision-score > play-threshold [
        buy-ticket
      ]
    ]
  ]

  set avg-belief mean [belief] of turtles
  set pct-playing (100 * count turtles with [plays?] / count turtles)
  
  ;; mettre à jour les plots: non defini dans cette version
;;   set-current-plot "pct-playing"
;;   plot pct-playing

;;   set-current-plot "avg-belief"
;;   plot avg-belief


  tick
end

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Achat et tirage
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
to buy-ticket
  set total-tickets-sold total-tickets-sold + 1
  set budget budget - ticket-price

  ;; tirage : le joueur gagne ?
  if random-float 1 < base-win-prob [
    ;; GAIN
    set budget budget + jackpot
    set last-win-tick ticks
    set color green
    set total-winners total-winners + 1
    set belief min (list 1 (belief + recovery-after-win))
  ]
  ;; sinon, pas de gain → on gère la perte
  if random-float 1 >= base-win-prob [
    ;;probabilité d’effet "gambler's fallacy"
    if random-float 1 < gambler-fraction [
      set belief min (list 1 (belief + gambler-increase))
    ]
    ;; sinon, découragement classique
    if random-float 1 >= gambler-fraction [
      set belief max (list 0 (belief - belief-decrease))
    ]
  ]

  ;; vérifie si le joueur doit arrêter
  if (budget < ticket-price) or (belief < stop-belief-threshold) [
    if random-float 1 > stubbornness [
      set plays? false
      set color gray
    ]
  ]
end


