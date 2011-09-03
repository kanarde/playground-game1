(ns game1.components)

(defn get-component [entity component]
  (get entity component))

(defn has-component [entity component]
  (contains? entity component))

(defn update-component [entity component f]
  (update-in entity [component] #(f %)))

(defn create-component [entity component value]
  (update-component entity component (constantly value)))

; Type Component
(defn get-type [entity]
  (get-component entity :type))

(defn create-type [entity type]
  (create-component entity :type type))

(defn update-type [entity type]
  (update-component entity :type (constantly type)))

; Renderable Component
(defn get-renderable [entity]
  (get-component entity :renderable))

(defn create-renderable [entity data]
  (create-component entity :renderable data))

(defn update-renderable [entity data]
  (update-component entity (constantly data)))

; Location Component
(defn get-location [entity]
  (get-component entity :location))

(defn create-location [entity loc orientation]
  (create-component entity :location {:loc loc :ori orientation}))

(defn update-location
  ([entity loc]
    (update-component entity :location #(assoc % :loc loc)))
  ([entity loc ori]
    (update-component entity :location #(-> %
                                          (assoc :loc loc)
                                          (assoc :ori ori)))))

(defn update-location-ori [entity ori]
  (update-component entity :location #(assoc % :ori ori)))

(defn move-location [entity movef]
  (update-component entity :location movef))

; Animation Component
(defn create-animation [entity movements]
  (create-component entity :animation movements))

(defn get-animation [entity]
  (get-component entity :animation))

; Movable Component
(defn create-movable [entity]
  (create-component entity :movable 1))

(defn get-movable [entity]
  (get-component entity :movable))