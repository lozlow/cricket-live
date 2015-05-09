(ns crikliveadmin.live.scoring
    (:require [reagent.core :as reagent :refer [atom]]
              [re-frame.core :refer [subscribe dispatch]]
              [crikliveadmin.live.teams :as teams]))

(defn scoring-player-entry
    [player-info team]
    [:div.Team-Sheet__player-info.pure-u-5-5 (:name player-info)])

(defn swap-teams-button
    []
    [:button {:on-click #(dispatch [:toggle-batting-team])}
        "Switch batting team"])

(defn scoring-panel
    []
    (let [scoring-team (subscribe [:currently-batting])]
        (fn []
            [teams/team-panel @scoring-team scoring-player-entry])))

(defn render
    []
    [:div [:h1 "Scoring"]
          [swap-teams-button]
          [scoring-panel]])
