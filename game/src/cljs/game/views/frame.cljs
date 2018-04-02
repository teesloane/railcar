(ns game.views.frame
  "Represents the game's frame; everything within is part of the game."
  (:require [game.views.components :as comp]
            [game.events :as e]
            [game.subs :as subs]
            [reagent.core :as r]
            [re-frame.core :as re]))

;; https://stackoverflow.com/questions/18575006/connect-canvas-with-lines
(defn railcar-canvas
  []
  (let [canvas      (js/document.getElementById "rails")
        ctx         (.getContext canvas "2d")
        car-factory #(.rect ctx % 10 50 20)
        connector   #(do (.beginPath ctx)
                         (.moveTo ctx 20 20)
                         (.lineTo ctx 50 50)
                         (.stroke ctx))
        stroke      #(.stroke ctx)]

    (doseq [x [0 1 2 3 4]]
      (do
        (car-factory (* x 60))
        (stroke)))
))

(defn splash-screen
  []
  ;; (railcar-canvas)
  (r/create-class
   {:display-name "spash-screen"

    ;; :component-did-mount
    ;; #(railcar-canvas)

    :reagent-render
    (fn []
      [:div
       [:div.h2.caps.t-white.pb1 "RAILCAR"]
       [:div.h6.caps.t-grey.headphones-reco "[headphones recommended]"]
       [:div.h4.caps.t-white.mt4.start-btn
        {:on-click #(re/dispatch [::e/start-game])}
        "start"]
       #_[:canvas#rails.mt3 {:width "400px" :height "50px"}]])}))

(defn main
  []
  (let [started? @(re/subscribe [::subs/started?])]
    [:div.flex.flex-column.frame
     (if-not started?
       [splash-screen]
       [:section.flex.flex-column.border {:style {:width "700px" :height "500px"}}
        [comp/display]
        [comp/prompt]])]))
