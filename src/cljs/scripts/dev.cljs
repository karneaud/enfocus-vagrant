(ns ^:figwheel-always scripts.dev
  (:require [figwheel.client :as fig]
            [scripts.client :as client]
  ))

(defn refresh []
  (.reload js/window.location true))
(fig/add-message-watch
 :html-watcher
 (fn [{:keys [msg-name files] :as msg}]
    ; (let [type (:type
    ; (nth '({:file "out/scripts/dev.js", :namespace "scripts.dev", :type :namespace}) 0))]
    ;  (cond
    ;    (= type :namespace) (client/get-questions)
    ;    :else
       (refresh)
    ;    ))
      )
    )
