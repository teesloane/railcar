(ns game.views.components
  (:require [re-frame.core :as re]
            [reagent.core :as r]
            [game.subs :as subs]
            [game.events :as events]))

(defn display
  []
  (let [story @(re/subscribe [::subs/gst])
        prompt @(re/subscribe [::subs/prompt])
        #_text #_(-> story :actions prompt)]
    [:div.display
     [:div prompt]
     [:div (:text story)]
     #_[:div text]]))

(defn prompt
  "Prompt console."
  []

  (let [prompt-val @(re/subscribe [::subs/prompt])
        ; handle-change #(reset! prompt-val (-> % .-target .-value))
        handle-key-press #(when (= (.-key %) "Enter")
                            (re/dispatch [::events/enter-prompt prompt-val])
                            (reset! prompt-val ""))]
    (fn []
      [:div.prompt
       [:div  {:style {:margin-top "-1px" }} ">"]
       [:input {:placeholder "Enter a  command..."
                :value prompt-val
                :on-change  #(re/dispatch [::events/change-prompt (-> % .-target .-value)])
                :on-key-press  handle-key-press}]])))
