(ns game1.core
  (:use [game1.entities :as entities]
        [game1.components :as comp]
        [game1.graphics :as graphics]
        [game1.game.enititydef :as entitydef]
        [game1.input :as input]
        [game1.movement :as movement])
  (:require [penumbra.app :as app]))

(def options
  {:init graphics/init
   :reshape graphics/reshape
   :display graphics/display-proxy
   :update movement/do-movement
   :mouse-move input/mouse-move
   :mouse-drag input/mouse-drag
   :key-press input/key-press
   :key-release input/key-release})

(entitydef/basic-game-entities)

;(.start (new Thread #((app/start options {}))))
(app/start options {:camera-entity :player1
                    :mouse {:x-cord 0 :y-cord 0}
                    :keys #{}
                    :move {:forward false
                           :backward false
                           :left false
                           :right false}})