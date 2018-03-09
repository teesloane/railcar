(ns game.views.components
  (:require [re-frame.core :as re]
            [reagent.core :as r]
            [game.subs :as subs]
            [game.events :as e]
            [game.util :refer [<sub >evt]]))





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

(defn prompt-options
  "Display a list of prompts the user can type in."
  [prompts]
  [:ul.prompt-opt
     (for [p prompts]
        [:li {:key (random-uuid)} (name p)])])

(defn display
  "Displays the story."
  []
  (let [history          (<sub [::subs/history])
        curr-text        (<sub [::subs/current-text])
        prompt-opts      (<sub [::subs/possible-prompts])
        prompt-opts?     (not (empty? prompt-opts))]

    (println prompt-opts? prompt-opts)
    [:div.display
     (for [s history] [:div.history-text {:key (random-uuid)} s])
     [:div.prompt-text curr-text]

     (when prompt-opts [prompt-options prompt-opts])]))



