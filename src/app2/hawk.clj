(ns app2.hawk
(:require [hawk.core :as hawk]
          [aviary.system :refer [defsystem* using] :as system]
                    [aviary.serve :refer [serve]]
                    [aviary.watch :refer [watch] :as w]
                    [aviary.figwheel :as fw]
                    [aviary.filesystem :as fs]
                    [weasel.repl.websocket :as weasel])
(:require [app2.server :refer [export-pages]]))

(defn start-weasel [& opts]
  (cemerick.piggieback/cljs-repl
    (apply weasel.repl.websocket/repl-env opts)))

(defn init
  []
  (export-pages)
  (start-weasel {:ip "0.0.0.0" :port 9001})
  (hawk/watch! [{:paths ["src/app2/templates" "resources/templates"]
               :filter hawk/modified?
               :handler (fn [ctx e]
                          (export-pages)
                          ctx)}]))
