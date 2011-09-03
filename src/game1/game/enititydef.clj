(ns game1.game.enititydef
  (:use [game1.entities]
       [game1.components]))

(defn basic-game-entities []

(add-entity :player1 (-> {}
                      (create-type :player)
                      (create-location [0 0 0] [0 0 0])))

(add-entity :triangle (-> {}
                        (create-type :triangle)
                        (create-location [-10 0 -6] [0 0 0])
                        (create-renderable [[0 1 0]
                                            [-1 -1 0]
                                            [1 -1 0]])))

(add-entity :mybox (-> {}
                     (create-type :box)
                     (create-location [0 -2 -10] [0 0 0])
                     (create-renderable 2)
                     (create-animation {:rotate [0 25 0]})))

)