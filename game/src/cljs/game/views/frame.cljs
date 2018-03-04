(ns game.views.frame
  "Represents the game's frame; everything within is part of the game."
  (:require [game.views.components :as comp]))


(defn main
  []
  [:div.flex.flex-column.frame
   [:section.flex.flex-column.border {:style {:width "700px" :height "500px"}}
    [comp/display]
    [comp/prompt]
    ]])
