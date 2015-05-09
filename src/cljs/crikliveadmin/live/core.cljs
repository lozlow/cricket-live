(ns crikliveadmin.live.core
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :refer [subscribe dispatch register-sub register-handler]]
              [crikliveadmin.live.header-bar :as header-bar]))

;; Subscriptions

(register-sub
    :active-panel
    (fn [db _]
        (reaction (:active @db))))

(register-sub
    :team-info
    (fn [db [_ list]]
        (reaction (conj (get (:team-list @db) list) {:team list}))))

(register-sub
    :currently-batting
    (fn [db _]
        (reaction (:currently-batting @db))))

;; Handlers

(register-handler
    :change-active-panel
    (fn [db [_ active-item]]
        (assoc db :active active-item)))

;; View

(defn render-active-panel
    []
    (let [active-item (subscribe [:active-panel])
          render-fn (:render-fn (get header-bar/items @active-item))]
        (render-fn)))

(defn live-page
    []
    [:div
        (header-bar/nav-bar)
        [:div.active-panel (render-active-panel)]])
