(ns crikliveadmin.live.scoring
    (:require [reagent.core :as reagent :refer [atom]]
              [re-frame.core :refer [subscribe dispatch register-handler]]
              [re-frame.undo :as undo]
              [crikliveadmin.live.teams :as teams]))

;; Logic

(def batting-events
    {:dot 0
     :1 1
     :2 2
     :3 3
     :4 4
     :6 6
     :wicket 0})

(def bowler-extras
    [:wide :no-ball])

(def extra-events
    {:wide 1
     :no-ball 1
     :leg-byes 0
     :byes 0})

(def dismissals
    {:bowled [nil :caught :stumped]
     :run-out [nil]
     :hit-wicket [nil]
     :timed-out [nil]
     :hurt [nil]
     :retired [nil]
     :handled-ball [nil]
     :hit-twice [nil]})

;; Views

(defn batting-player-entry
    [{:keys [name pos score balls] :as player-info}]
    [:div.Team-Sheet__player-info.pure-u-5-5 {:draggable true}
        [:span.Team-Sheet__player-name name]
        [:input.Team-Sheet__player-score--input {:type :number
                                                 :value score
                                                 :on-change #(dispatch [:update-player-score pos (-> % .-target .-value)])}]])

(defn bowling-player-entry
    [player-info]
    [:div.Team-Sheet__player-info.pure-u-5-5
        [:span.Team-Sheet__player-name (:name player-info)]])

(defn swap-teams-button
    []
    [:button {:on-click #(do (undo/store-now! "Switching team")
                             (dispatch [:toggle-batting-team]))}
        "Switch batting team"])

(defn batting-panel
    []
    (let [batting-team (subscribe [:currently-batting])]
        (fn []
            [:div
                [:h3 "Batting"]
                [teams/team-panel @batting-team batting-player-entry]])))

(defn bowling-panel
    []
    (let [bowling-team (subscribe [:currently-bowling])]
        (fn []
            [:div
                [:h3 "Bowling"]
                [teams/team-panel @bowling-team bowling-player-entry]])))

(defn render
    []
    [:div
        [:h1 "Scoring"]
        [swap-teams-button]
        [batting-panel]
        [bowling-panel]])

(defn player-in-pos
    [player-list pos]
    (for [player player-list
          :when (= (:pos player) pos)]
        player))

;; Handlers

(register-handler
    :toggle-batting-team
    (fn [db _]
        (let [current-team (:currently-batting db)
              transition {:home :away
                          :away :home}]
            (assoc db :currently-batting (get transition current-team)))))

(register-handler
    :update-player-score
    (fn [db [_ pos score]]
        (let [team (:currently-batting db)]
            (update-in db [:team-list team] assoc :players (map #(if (= (:pos %) pos)
                                                                (assoc % :score score)
                                                                %)
                                                            (:players (get (:team-list db) team)))))))
