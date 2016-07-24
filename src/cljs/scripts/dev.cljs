(ns ^:figwheel-always scripts.dev
  (:require [figwheel.client :as fig]
            [scripts.client]
  ))

(defn refresh []
  (.reload js/window.location true))

(fig/add-message-watch
 :html-watcher
 (fn [{:keys [msg-name] :as msg}]
   ;; (.log js/console "stuff " msg-name :html-files-changed)
   ;; (when (= msg-name :html-files-changed)
     (refresh)
     (println "Figwheel: file(s) changed. Reloaded page.")))
     ;; )
