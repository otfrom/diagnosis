(ns diagnosis.core)



(comment

  (let [foo [{:diagnosis_code "345" :p1 "HIGH CPU" :p2 "HIGH IO"}
             {:diagnosis_code "345" :p1 "HIGH CPU" :p2 "MAGIC SMOKE ESCAPE"}
             {:diagnosis_code "345" :p1 "HIGH CPU" :p2 "NONE"}
             {:diagnosis_code "999" :p1 "FELL OVER" :p2 "NONE"}]
        counts (reduce (fn [acc new]
                         (-> acc
                             (update-in ((juxt :diagnosis_code :p1) new) (fnil inc 0))
                             (update-in ((juxt :diagnosis_code :p2) new) (fnil inc 0))))
                       {}
                       foo)
        columns (->> counts
                     (mapcat (comp keys second))
                     (into #{})
                     sort
                     vec)
        getters (map (fn [c] (fn [m] (-> m second (get c 0)))) columns)]
    (into (vector (cons "DIAGNOSIS" columns))
          (map
           (fn [c]
             (cons (first c)
                   ((apply juxt getters) c)))
           counts)))

  )
