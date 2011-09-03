(ns game1.movement
  (:use [game1.entities :as entities]
        [game1.components :as comp]
        [clojure.contrib.generic.math-functions]))

(def piOver180 (/ 3.14159265 180))

(defn heading-deltas [heading]
  [(* (sin (* heading piOver180)) 0.05) (* (cos (* heading piOver180)) 0.05)])

(defn move [headingmod location]
  (update-in location [:loc]
    #(let [heading (+ (* (nth (:ori location) 1) -1) headingmod)]
       (let [[dx dz] (heading-deltas heading)]
         (map + % [dx 0 dz])))))

(defn ismoving [movetype]
  (not (not-any? identity (vals movetype))))

(defn get-head-mod [movetype]
  (let [f (:forward movetype)
        b (:backward movetype)
        l (:left movetype)
        r (:right movetype)]
    (if (and f l) 45
      (if (and f r) -45
        (if (and b l) 135
          (if (and b r) -135
            (if (and l (not r)) 90
              (if (and r (not l)) -90
                (if (and b (not f)) 180
                  0)))))))))

(defn rotate-entity [e delta]
  (let [ori (:ori (comp/get-location e))
        rotate (map * (:rotate (comp/get-animation e)) (repeat 3 delta))]
     (update-in e [:location] (fn [l] (update-in l [:ori] #(map + % rotate))))))

(defn do-movement [[delta time] state]
  (let [name (:camera-entity state) movetype (:move state)]
    (if (ismoving movetype)
      (let [headingmod (get-head-mod movetype)]
        (entities/update-entity name #(comp/move-location % (partial move headingmod))))))
  (entities/map-entity
    #(contains? % :animation)
    #(rotate-entity % delta))
  state)