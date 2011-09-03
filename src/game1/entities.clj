(ns game1.entities)

(def registry (agent {}))

(defn add-entity [name value]
  (send registry #(assoc % name value)))

(defn update-entity [name f]
  (send registry #(update-in % [name] (fn [e] (f e)))))

(defn update-entity-component [name component value]
  (send registry #(update-in % [name] (fn [v] (assoc v component value)))))

(defn remove-entity [name]
  (send registry #(dissoc % name)))

(defn get-entity [name]
  (get @registry name))

(defn with-each-entity [matcher f]
  (doseq [e (vals @registry)]
    (if (matcher e) (f e))))

(defn map-entity [matcher f]
  (doseq [kv (filter #(matcher (val %)) @registry)]
    (update-entity (key kv) f)))
