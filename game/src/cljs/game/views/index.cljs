(ns game.views.index
  (:require [re-frame.core :as re]
            [game.views.frame :as frame]
            [game.subs :as subs])
  )


(defn home-panel
  []
  [:div "hi"])

(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    :frame      [frame/main]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re/subscribe [::subs/active-panel])]
     [show-panel @active-panel]
     ))
