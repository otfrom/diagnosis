(ns diagnosis.core)



(comment

  (let [foo [{:diagnosis_code "345" :p1 "HIGH CPU" :p2 "HIGH IO"}
             {:diagnosis_code "345" :p1 "HIGH CPU" :p2 "MAGIC SMOKE ESCAPE"}
             {:diagnosis_code "345" :p1 "HIGH CPU" :p2 "NONE"}
             {:diagnosis_code "999" :p1 "FELL OVER" :p2 "NONE"}]
        counts (reduce (fn [acc {:keys [diagnosis_code p1 p2]}]
                         (-> acc
                             (update-in [diagnosis_code p1] (fnil inc 0))
                             (update-in [diagnosis_code p2] (fnil inc 0))))
                       {}
                       foo)
        columns (->> counts
                     (mapcat (comp keys second))
                     (into #{})
                     sort
                     vec)
        getters (map (fn [c] (fn [m] (-> m second (get c 0)))) columns)]
    (into (vector (vec (cons "DIAGNOSIS" columns)))
          (map
           (fn [c]
             (->> (cons (first c)
                        ((apply juxt getters) c))
                  (into [])))
           counts)))

  ;; output
  ;; [["DIAGNOSIS" "FELL OVER" "HIGH CPU" "HIGH IO" "MAGIC SMOKE ESCAPE" "NONE"] ["999" 1 0 0 0 1] ["345" 0 3 1 1 1]]

  )
