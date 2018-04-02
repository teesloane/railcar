(ns game.views.frame
  "Represents the game's frame; everything within is part of the game."
  (:require [game.views.components :as comp]
            [game.events :as e]
            [game.subs :as subs]
            [re-frame.core :as re]))

(defn splash-screen
  []
  [:div
   [:div.h2.caps.t-white.pb1 "RAILCAR"]
   [:div.h6.caps.t-grey.headphones-reco "[headphones recommended]"]
   [:div.h4.caps.t-white.pt4.start-btn
    {:on-click #(re/dispatch [::e/start-game])}
    "start"]])

(defn main
  []
  (let [started? @(re/subscribe [::subs/started?])]
    [:div.flex.flex-column.frame
     (if-not started?
       [splash-screen]
       [:section.flex.flex-column.border {:style {:width "700px" :height "500px"}}
        [comp/display]
        [comp/prompt]])]))
