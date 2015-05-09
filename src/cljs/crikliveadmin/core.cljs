(ns crikliveadmin.core
    (:require [reagent.core :as reagent :refer [atom]]
              [crikliveadmin.live.core :as live]
              [re-frame.core :refer [dispatch-sync]]
              [crikliveadmin.database])
    (:import goog.History))

(enable-console-print!)

;; -------------------------
;; Initialize app
(defn mount-root []
    (reagent/render [live/live-page] (.getElementById js/document "app")))

(defn ^:export init! []
    (dispatch-sync [:initialise-database])
    (mount-root))
