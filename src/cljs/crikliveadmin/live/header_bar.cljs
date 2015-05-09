(ns crikliveadmin.live.header-bar
    (:require [re-frame.core :refer [subscribe dispatch]]
              [crikliveadmin.live.teams :as teams]
              [crikliveadmin.live.scoring :as scoring]
              [crikliveadmin.live.commentary :as commentary]
              [crikliveadmin.live.debrief :as debrief]))

(def items
    {:teams {:title "Teams" :render-fn teams/render}
     :scoring {:title "Scoring" :render-fn scoring/render}
     :commentary {:title "Commentary" :render-fn commentary/render}
     :debrief {:title "Debrief" :render-fn debrief/render}})

(defn nav-bar-item
    [item]
    (let [active-item (subscribe [:active-panel])
          text (:title (val item))]
        (if (= (key item) @active-item)
            [:div.Header-Bar__item.Header-Bar__item--active.pure-u-1-4 text]
            [:div.Header-Bar__item.pure-u-1-4 {:on-click #(dispatch [:change-active-panel (key item)])} text])))

(defn nav-bar
    []
    (into [:div.Header-Bar.pure-g] (map nav-bar-item items)))
