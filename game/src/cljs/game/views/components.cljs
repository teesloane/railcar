(ns game.views.components
  (:require [re-frame.core :as re]
            [reagent.core :as r]
            [game.subs :as subs]
            [game.events :as e]
            [game.util :refer [<sub >evt]]))



(defn display
  "Displays the story."
  []
  (let [history          (<sub [::subs/history])
        curr-text        (<sub [::subs/current-text])
        possible-prompts (<sub [::subs/possible-prompts])]

    [:div.display
     (for [s history]
       [:div.history-text {:key (random-uuid)} s])
     [:div.prompt-text curr-text]]))


(defn prompt
  "Prompt console."
  []
  (let [prompt        (<sub [::subs/prompt])
        handle-change #(>evt [::e/change-prompt (-> % .-target .-value)])
        handle-enter  #(when (= (.-key %) "Enter")
                         (>evt [::e/enter-prompt prompt]))]
    [:div.prompt
     [:div  {:style {:margin-top "-1px" }} ">"]
     [:input {:placeholder "Enter a command..."
              :value prompt
              :on-change handle-change
              :on-key-press  handle-enter}]]))
