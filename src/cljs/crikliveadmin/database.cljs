(ns crikliveadmin.database
    (:require [re-frame.core :refer [register-handler]]))

(def default
    {:active :teams
     :team-list {:home {:name "Essex"
                        :players [{:pos 1 :name "Bob Willis" :score 312 :balls 510}
                                  {:pos 2 :name "Geoffrey Boycott" :captain true}
                                  {:pos 3 :name "Alistair Cook"}]}
                 :away {:name "Surrey"
                        :players [{:pos 1 :name "Michael Vaughan" :captain true}
                                  {:pos 2 :name "Kevin Pietersen"}]}}
     :currently-batting :home})

(register-handler
    :initialise-database
    (fn [_ _]
        default))
