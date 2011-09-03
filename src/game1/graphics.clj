(ns game1.graphics
  (:use [penumbra opengl]
        [penumbra.opengl.core :only [gl-import]]
        [game1.entities :as entities]
        [game1.components :as comp]
        [clojure.contrib.generic.math-functions])
  (:require [penumbra.app :as app]
            [penumbra.text :as text]))

(def app-width 640)
(def app-height 480)

(gl-import glClearDepth clear-depth)

(defn init [state]
  (app/title! "Michal Test")
  (app/vsync! false)
  (app/display-mode! app-width app-height)
  (shade-model :smooth)
  (clear-color 0 0 0 0.5)
  (clear-depth 1)
  (enable :depth-test)
  (enable :lighting)
  (depth-test :lequal)
  (hint :perspective-correction-hint :nicest)
  (assoc state :fullscreen false))

(defn reshape [[x y width height] state]
  (viewport 0 0 app-width app-height)
  (frustum-view 45 (/ (double app-width) app-height) 0.1 100)
  (load-identity)
  state)

(defn handle-input-mouse [[delta time] state]
  (text/write-to-screen (str "Mouse= X:" (:x-cord (:mouse state)) ",Y:" (:y-cord (:mouse state))) 0 60))

(defn handle-input-key [[delta time] state]
  (if (:keys state) (text/write-to-screen (str "Keys= " (:keys state)) 0 30)))

(defn handle-input [[delta time] state]
  (handle-input-mouse [delta time] state)
  (handle-input-key [delta time] state))

(defn handle-camera [[delta time] state]
  (let [location (comp/get-location (entities/get-entity (:camera-entity state)))]
    (let [[x y z] (:loc location)]
      (rotate (first (:ori location)) 1 0 0)
      (rotate (nth (:ori location) 1) 0 1 0))
    (apply translate (:loc location))))

(defn draw-entity-triangle [[delta time] state e]
  (push-matrix
    (apply translate (:loc (comp/get-location e)))
    (draw-triangles (dorun (map #(apply vertex %) (comp/get-renderable e))))))

(defn point-tran-z [p] (map + p [0 0 -1]))

(defn draw-entity-box [[delta time] state e]
  (push-matrix
    (let [location (comp/get-location e)]
      (apply translate (:loc location))
      (rotate (first (:ori location)) 1 0 0)
      (rotate (nth (:ori location) 1) 0 1 0)
      (rotate (nth (:ori location) 2) 0 0 1))
    ;(apply rotate (:ori (comp/get-location e)))
    (let [d (comp/get-renderable e) r (/ d 2)]
      (let [v1 [(* r -1) r r]
            v2 [r r r]
            v3 [r (* r -1) r]
            v4 [(* r -1) (* r -1) r]
            v5 [(* r -1) r (* r -1)]
            v6 [r r (* r -1)]
            v7 [r (* r -1) (* r -1)]
            v8 [(* r -1) (* r -1) (* r -1)]]
        (normal 0 0 d)
        (draw-quads (dorun (map #(apply vertex %) [v1 v2 v3 v4])))

        (normal 0 d 0)
        (draw-quads (dorun (map #(apply vertex %) [v1 v2 v6 v5])))

        (normal d 0 0)
        (draw-quads (dorun (map #(apply vertex %) [v2 v3 v7 v6])))

        (normal (* d -1) 0 0)
        (draw-quads (dorun (map #(apply vertex %) [v1 v4 v8 v5])))

        (normal 0 (* d -1) 0)
        (draw-quads (dorun (map #(apply vertex %) [v4 v3 v7 v8])))

        (normal 0 0 (* d -1))
        (draw-quads (dorun (map #(apply vertex %) [v5 v6 v7 v8])))
        ))))

(defn draw-entity [[delta time] state e]
  (case (comp/get-type e)
    :triangle (draw-entity-triangle [delta time] state e)
    :box (draw-entity-box [delta time] state e)
    nil))

(defn display [[delta time] state]
  (text/write-to-screen (format "%d fps" (int (/ 1 delta))) 0 0)

  (handle-camera [delta time] state)
  (handle-input [delta time] state)

  (push-matrix
    (push-matrix (light 0 :position [5 3 10 0]))
    (enable :light0))
  (entities/with-each-entity #(contains? % :renderable) #(draw-entity [delta time] state %))
  (app/repaint!))



(defn display-proxy [& args] (apply display args))