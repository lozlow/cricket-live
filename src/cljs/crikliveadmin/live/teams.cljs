(ns crikliveadmin.live.teams
    (:require [reagent.core :as reagent :refer [atom]]
              [re-frame.core :refer [subscribe dispatch register-handler]]))

(defn apply-class
    [classname elem]
    (let [[tag {:keys [class] :as props}] elem]
        [tag (assoc props :class (str class " " classname)) (drop 2 elem)]))

(def grab-focus
    (with-meta identity
        {:component-did-mount #(.focus (reagent/dom-node %))}))

(defn player-entry-editing
    [player-info team on-save]
    (let [val (atom (:name player-info))
          save #(do (dispatch [:update-player {:key (:pos player-info) :name @val :team team}])
                    (if on-save (on-save)))]
        (fn [player-info]
            [:div.Team-Sheet__player-info.Team-Sheet__player-info--editing.pure-u-11-24
                [grab-focus
                    [:input {:type "text"
                             :value @val
                             :on-blur save
                             :on-change #(reset! val (-> % .-target .-value))
                             :on-key-down #(case (.-which %)
                                                13 (save)
                                                27 (save)
                                                nil)}]]])))

(defn player-entry
    [player-info team]
    (let [editing (atom false)]
        (fn [player-info team]
            (let [captain? (:captain player-info)]
                (cond
                    @editing [player-entry-editing player-info team #(reset! editing false)]
                    captain? [:div.Team-Sheet__player-info.Team-Sheet__player-info--captain.pure-u-11-24
                                [:span {:on-click #(reset! editing true)}
                                    (str (:name player-info) " (c)")]]
                    :else [:div.Team-Sheet__player-info.pure-u-11-24
                                [:span {:on-click #(reset! editing true)}
                                    (:name player-info)]])))))

(defn team-panel
    [team entry-renderer]
    (let [team-info (subscribe [:team-info team])
          team (:team @team-info)]
        [:div.Team-Sheet
            [:h3.Team-Sheet__team-name (:name @team-info)]
            [:div.pure-g
                (for [player (:players @team-info)]
                    ^{:key (:pos player)} [entry-renderer player team])]]))

(defn render
    []
    [:div
        [:h1 "Teams"]
        (team-panel :home player-entry)
        (team-panel :away player-entry)])

;; Handlers

(register-handler
    :update-player
    (fn [db [_ {:keys [key name team]}]]
        (update-in db [:team-list team :players (- key 1)] assoc :name name)))

(register-handler
    :toggle-batting-team
    (fn [db _]
        (let [current-team (:currently-batting db)
              transition {:home :away
                          :away :home}]
            (assoc db :currently-batting (get transition current-team)))))
