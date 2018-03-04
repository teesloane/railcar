(ns game.views.components
  (:require [re-frame.core :as re]
            [reagent.core :as r]
            [game.subs :as subs]
            [game.events :as events]))

(defn display
  []
  (let [text @(re/subscribe [::subs/gst])]
    [:div.display text]))


(defn prompt
  "Prompt console."
  []

  (let [prompt-val (r/atom "")
        handle-change #(reset! prompt-val (-> % .-target .-value))
        handle-key-press #(when (= (.-key %) "Enter")
                            (reset! prompt-val "")
                            (re/dispatch [::events/enter-prompt]))]
    (fn []
      [:div.prompt
       [:div  {:style {:margin-top "-1px" }} ">" ]
       [:input {:placeholder "Enter a  command..."
                :value @prompt-val
                :on-change  handle-change
                :on-key-press  handle-key-press}
        ]])))
