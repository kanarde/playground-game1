(ns game1.input
  (:use [game1.entities :as entities]
        [game1.components :as comp]
        [clojure.contrib.generic.math-functions])
  (:require [penumbra.app :as app]))

(defn handle-fullscreen [state key]
  (if (= :f1 key)
    (let [fstate (not (:fullscreen state))] (app/fullscreen! fstate) (assoc state :fullscreen fstate))
    state))

(defn is-pressed [state key]
  (contains? (:keys state) key))

(defn gen-move-state [state key]
  (assoc state :move {:forward (is-pressed state "w")
   :backward (is-pressed state "s")
   :left (is-pressed state "a")
   :right (is-pressed state "d")}))

(defn key-press [key state]
  (if (= :escape key) (app/stop!))
  (-> state
    (update-in [:keys] #(conj % key))
    (gen-move-state key)
    (handle-fullscreen key)))

(defn key-release [key state]
  (-> state
    (update-in [:keys] #(disj % key))
    (gen-move-state key)))

(defn adjust-ori [[dx dy] location]
  (update-in location [:ori] #(map + % [dy dx 0])))

(defn mouse-move [[dx dy] [x y] state] state)

(defn mouse-drag [[dx dy] _ button state]
  (entities/update-entity (:camera-entity state) #(comp/move-location % (partial adjust-ori [dx dy])))
  (let [mouse (:mouse state)]
    (let [x (:x-cord mouse) y (:y-cord mouse)]
      (let [newx (if (>= (abs x) 360) 0 (+ x dy))
            newy (if (>= (abs y) 360) 0 (+ y dx))]
        (assoc state :mouse (assoc mouse :x-cord newx :y-cord newy))))))

